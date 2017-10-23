package com.turbointernational.metadata.dao;

import static com.turbointernational.metadata.entity.PartType.PTID_TURBO;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Repository;

import com.turbointernational.mas90.entity.ArInvoiceHistoryDetail;
import com.turbointernational.mas90.entity.ArInvoiceHistoryDetail_;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.web.dto.AlsoBought;
import com.turbointernational.metadata.web.dto.Page;

/**
 *
 * @author jrodriguez
 */
@Repository
public class PartDao extends AbstractDao<Part> {

    private final static String COL_ALIAS_MANUFACTURERPARTNUMBER = "manufacturerPartNumber";
    private final static String COL_ALIAS_QTYSHIPPED = "qtyShipped";
    private final static String COL_ALIAS_ORDERS = "orders";
    private final static String COL_ALIAS_SALEAMOUNT = "saleAmount";

    private static Function<Tuple, AlsoBought> tupleToAlsoBoughtConverter = new Function<Tuple, AlsoBought>() {

        @Override
        public AlsoBought apply(Tuple t) {
            String tupManufacturerPartNumber = t.get(COL_ALIAS_MANUFACTURERPARTNUMBER, String.class);
            BigDecimal tupQtyShipped = t.get(COL_ALIAS_QTYSHIPPED, BigDecimal.class);
            Long tupOrders = t.get(COL_ALIAS_ORDERS, Long.class);
            BigDecimal tupSaleAmount = t.get(COL_ALIAS_SALEAMOUNT, BigDecimal.class);
            return new AlsoBought(tupManufacturerPartNumber, tupQtyShipped.intValue(), tupOrders.intValue(),
                    tupSaleAmount);
        }

    };

    @PersistenceContext(unitName = "mas90")
    private EntityManager emMas90;

    public PartDao() {
        super(Part.class);
    }

    /**
     * Get list of parts ordered by 'id'.
     *
     * Ordering is important when parts are processed by batches (as in Magmi CSV export). Unordered (sub)list can
     * contain duplications or skip some rows.
     *
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<Part> findAllOrderedById(int firstResult, int maxResults) {
        return em.createNamedQuery("findAllPartsOrderedById", Part.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public List<Part> findPartsByIds(Collection<Long> ids) {
        return em.createNamedQuery("findPartsByIds", Part.class).setParameter("ids", ids).getResultList();
    }

    public List<Part> findPartsByMnfrsAndNumbers(Long manufacturerId, List<String> manufacturersNumbers) {
        return em.createNamedQuery("findPartsByMnfrsAndNumbers", Part.class).setParameter("mnfrId", manufacturerId)
                .setParameter("mnfrPrtNmbrs", manufacturersNumbers).getResultList();
    }

    public List<ProductImage> findProductImages(Collection<Long> productIds) {
        return em.createNamedQuery("findProductImagesForPart", ProductImage.class)
                .setParameter("productIds", productIds).getResultList();
    }

    public Part findByPartNumber(String partNumber) throws NoResultException {
        try {
            return em.createNamedQuery("findByPartNumber", Part.class).setParameter("partNumber", partNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Part findByPartNumberAndManufacturer(Long manufacturerId, String partNumber) {
        try {
            return em.createNamedQuery("findByPartNumberAndManufacturer", Part.class)
                    .setParameter("manufacturerId", manufacturerId).setParameter("partNumber", partNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Page<AlsoBought> filterAlsoBough(String manufacturerPartNumber, String fltrManufacturerPartNumber,
            String fltrPartTypeValue, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        /* @formatter:off
           select ITEMCODE, sum(QUANTITYSHIPPED) as qty_shipped, count(distinct id.invoiceno) as orders, sum(round(id.extensionamt/coalesce(id.armc_234_entryrate, 1), 2)) as amt
           from AR_INVOICEHISTORYDETAIL as id
           where
               id.INVOICENO in (select invoiceno from AR_INVOICEHISTORYDETAIL where ITEMCODE = '8-F-0431')
               and id.itemcode <> '8-F-0431'
           group by
               id.ITEMCODE
           order by
               sum(id.QUANTITYSHIPPED) desc;
           @formatter:on */
        // Convert (if any) a part type value to a product line code.
        String productLineCode = null;
        if (fltrPartTypeValue != null) {
            try {
                productLineCode = emMas90.createNamedQuery("convertPartTypeValue2ProductLineCode", String.class)
                        .setParameter("partTypeValue", fltrPartTypeValue).getSingleResult();
            } catch (NoResultException e) {
                // ignore
            }
        }

        CriteriaBuilder cb = emMas90.getCriteriaBuilder();
        CriteriaQuery<Tuple> cqt = cb.createTupleQuery();
        Root<ArInvoiceHistoryDetail> root = cqt.from(ArInvoiceHistoryDetail.class);
        Path<String> colManufacturerPartNumber = root.get(ArInvoiceHistoryDetail_.itemcode);
        Expression<BigDecimal> colSumQuatityShipped = cb.sum(root.get(ArInvoiceHistoryDetail_.quantityshipped));
        Expression<Long> colOrders = cb.countDistinct(root.get(ArInvoiceHistoryDetail_.invoiceno));
        // @formatter:off
        // below is expression: sum(ROUND(id.EXTENSIONAMT / ISNULL(ih.ARMC_234_ENTRYRATE, 1), 2)) as as amt
        Expression<BigDecimal> colSaleAmount = cb.sum(
                cb.function("round", BigDecimal.class,
                        cb.quot(root.get(ArInvoiceHistoryDetail_.extensionamt),
                                cb.coalesce(root.get(ArInvoiceHistoryDetail_.armc234Entryrate), 1)),
                        cb.literal(2)
                )
        );
        cqt.select(
            cb.tuple(
                colManufacturerPartNumber.alias(COL_ALIAS_MANUFACTURERPARTNUMBER),
                colSumQuatityShipped.alias(COL_ALIAS_QTYSHIPPED),
                colOrders.alias(COL_ALIAS_ORDERS),
                colSaleAmount.alias(COL_ALIAS_SALEAMOUNT)
            )
        );
        // @formatter:on

        Subquery<String> subqueryInvoices = cqt.subquery(String.class);
        Root<ArInvoiceHistoryDetail> rootSubquery = subqueryInvoices.from(ArInvoiceHistoryDetail.class);
        subqueryInvoices.select(rootSubquery.get(ArInvoiceHistoryDetail_.invoiceno));
        subqueryInvoices.where(cb.equal(rootSubquery.get(ArInvoiceHistoryDetail_.itemcode), manufacturerPartNumber));

        List<Predicate> lstWherePredicates = new ArrayList<>(5);
        lstWherePredicates.add(root.get(ArInvoiceHistoryDetail_.invoiceno).in(subqueryInvoices));
        lstWherePredicates.add(cb.notEqual(root.get(ArInvoiceHistoryDetail_.itemcode), manufacturerPartNumber));
        if (isNotBlank(fltrManufacturerPartNumber)) {
            lstWherePredicates.add(cb.like(cb.lower(root.get(ArInvoiceHistoryDetail_.itemcode)),
                    "%" + fltrManufacturerPartNumber.toLowerCase() + "%"));
        }
        if (productLineCode != null) {
            lstWherePredicates.add(cb.equal(root.get(ArInvoiceHistoryDetail_.productLine), productLineCode));
        }
        Predicate[] arrWherePredicates = lstWherePredicates.toArray(new Predicate[lstWherePredicates.size()]);
        cqt.where(arrWherePredicates);
        cqt.groupBy(root.get(ArInvoiceHistoryDetail_.itemcode));
        Order order = null;
        if (sortOrder != null) {
            boolean asc = sortOrder.equalsIgnoreCase("asc");
            if (!asc && !sortOrder.equalsIgnoreCase("desc")) {
                throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
            }
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            if (sortProperty.equals(COL_ALIAS_MANUFACTURERPARTNUMBER)) {
                order = asc ? cb.asc(colManufacturerPartNumber) : cb.desc(colManufacturerPartNumber);
            } else if (sortProperty.equals(COL_ALIAS_QTYSHIPPED)) {
                order = asc ? cb.asc(colSumQuatityShipped) : cb.desc(colSumQuatityShipped);
            } else if (sortProperty.equals(COL_ALIAS_SALEAMOUNT)) {
                order = asc ? cb.asc(colSaleAmount) : cb.desc(colSaleAmount);
            } else if (sortProperty.equals(COL_ALIAS_ORDERS)) {
                order = asc ? cb.asc(colOrders) : cb.desc(colOrders);
            } else {
                throw new IllegalArgumentException("Invalid sort property: " + sortProperty);
            }
        }
        if (order != null) {
            cqt.orderBy(order);
        }
        TypedQuery<Tuple> tq = emMas90.createQuery(cqt);
        if (offset != null) {
            tq.setFirstResult(offset);
        }
        if (limit != null) {
            tq.setMaxResults(limit);
        }
        List<AlsoBought> recs = tq.getResultList().stream().map(tupleToAlsoBoughtConverter)
                .collect(Collectors.toList());

        // Calculate number of records.
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ArInvoiceHistoryDetail> countRoot = cq.from(ArInvoiceHistoryDetail.class);
        cq.select(cb.countDistinct(countRoot.get(ArInvoiceHistoryDetail_.itemcode)));
        cq.where(arrWherePredicates);
        TypedQuery<Long> cntQuery = emMas90.createQuery(cq);
        long total = cntQuery.getSingleResult();
        return new Page<>(total, recs);
    }

    public List<Turbo> listTurbosLinkedToGasketKit(Long id) {
        try {
            return em.createQuery(
                    "FROM Turbo AS t WHERE t.partType.id=" + PTID_TURBO + " AND t.gasketKit.id=:gasketKitId",
                    Turbo.class).setParameter("gasketKitId", id).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

}
