package com.turbointernational.metadata.dao;

import static com.turbointernational.metadata.entity.PartType.PartTypeEnum.KIT;
import static com.turbointernational.metadata.entity.PartType.PartTypeEnum.TURBO;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
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
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.turbointernational.mas90.entity.ArInvoiceHistoryDetail;
import com.turbointernational.mas90.entity.ArInvoiceHistoryDetail_;
import com.turbointernational.metadata.entity.PartType;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.web.dto.AlsoBought;
import com.turbointernational.metadata.web.dto.Page;

/**
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

    @Autowired
    private DataSource dataSource = null;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public PartDao() {
        super(Part.class);
    }

    public <T extends Part> T getReferenceOnPart(Class<T> clazz, Long id) {
        return em.getReference(clazz, id);
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
            return em
                    .createQuery("FROM Turbo AS t WHERE t.partType.id=" + TURBO.id + " AND t.gasketKit.id=:gasketKitId",
                            Turbo.class)
                    .setParameter("gasketKitId", id).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void changePartType(long partId, PartType.PartTypeEnum oldPartType, PartType.PartTypeEnum newPartType,
            long turboModelId, long kitTypeId, boolean copyCritDims) {
        if (oldPartType == TURBO) {
            // Delete in the Turbo references on Applications.
            jdbcTemplate.update("delete from turbo_car_model_engine_year where part_id=?", partId);
        }
        // Delete any references on the part in the table 'kit_part_common_component'.
        jdbcTemplate.update("delete from kit_part_common_component where kit_id=? or part_id=?", partId, partId);
        List<CritDimVal> cdv = null;
        if (copyCritDims) {
            cdv = readCritDimVals(partId, oldPartType, newPartType);
        }
        // Delete a record in a child table of the part.
        jdbcTemplate.update("delete from " + oldPartType.table + " where part_id=?", partId);
        // Change part_type_id in the part.
        jdbcTemplate.update("update part set part_type_id=? where id=?", newPartType.id, partId);
        // Insert a record in a new child table of the part.
        if (newPartType == TURBO) {
            jdbcTemplate.update("insert into turbo(part_id, turbo_model_id) values(?, ?)", partId, turboModelId);
        } else if (newPartType == KIT) {
            jdbcTemplate.update("insert into kit(part_id, kit_type_id) values(?, ?)", partId, kitTypeId);
        } else {
            jdbcTemplate.update("insert into " + newPartType.table + "(part_id) values(?)", partId);
        }
        if (copyCritDims) {
            writeCritDimVals(partId, newPartType, cdv);
        }
    }

    class CritDimVal {

        private final String colName;

        private Object val;

        public CritDimVal(String colName) {
            super();
            this.colName = colName;
        }

        public String getColName() {
            return colName;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }

    }

    /**
     * Select list of critical dimensions columns common for both types.
     * 
     * @param partId
     * @param oldPartType
     * @param newPartType
     * @return
     */
    private List<CritDimVal> readCritDimVals(long partId, PartType.PartTypeEnum oldPartType,
            PartType.PartTypeEnum newPartType) {
        List<CritDimVal> cdm = jdbcTemplate.query(
                "select cd0.json_name " + "from crit_dim cd0 join crit_dim cd1 on cd0.json_name = cd1.json_name "
                        + "where cd0.data_type = cd1.data_type and cd0.enum_id <=> cd1.enum_id "
                        + "and cd0.part_type_id=? and cd1.part_type_id=?",
                new Object[] { oldPartType.id, newPartType.id }, (rs, rownum) -> {
                    String colName = rs.getString(1);
                    return new CritDimVal(colName);
                });
        String cols = critDimMeta2ColsQuery(cdm);
        String sql = "select " + cols + " from " + oldPartType.table + " where part_id=?";
        jdbcTemplate.query(sql, new Object[] { partId }, rs -> {
            for (CritDimVal cdv : cdm) {
                String colName = cdv.getColName();
                Object val = rs.getObject(colName);
                cdv.setVal(val);
            }
        });
        return cdm;
    }

    private String critDimMeta2ColsQuery(List<CritDimVal> cdm) {
        StringBuilder sb = new StringBuilder();
        cdm.forEach(cdv -> {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(cdv.getColName());
        });
        return sb.toString();
    }

    private void writeCritDimVals(long partId, PartType.PartTypeEnum newPartType, List<CritDimVal> cdm) {
        String cols = critDimMeta2ColsUpdate(cdm);
        String sql = "update " + newPartType.table + " set " + cols + " where part_id=?";
        jdbcTemplate.update(sql, ps -> {
            int i = 1;
            for (CritDimVal cdv : cdm) {
                ps.setObject(i++, cdv.getVal());
            }
            ps.setLong(i, partId);
        });
    }

    private String critDimMeta2ColsUpdate(List<CritDimVal> cdm) {
        StringBuilder sb = new StringBuilder();
        cdm.forEach(cdv -> {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(cdv.getColName());
            sb.append("=?");
        });
        return sb.toString();
    }

}
