package com.turbointernational.metadata.dao;

import static com.turbointernational.metadata.entity.PartType.PTID_TURBO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.turbointernational.mas90.entity.ArInvoiceHistoryDetail;
import com.turbointernational.mas90.entity.ArInvoiceHistoryDetail_;
import com.turbointernational.metadata.entity.BOMAncestor;
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
            Double tupQtyShipped = t.get(COL_ALIAS_QTYSHIPPED, Double.class);
            Long tupOrders = t.get(COL_ALIAS_ORDERS, Long.class);
            Double tupSaleAmount = t.get(COL_ALIAS_SALEAMOUNT, Double.class);
            return new AlsoBought(tupManufacturerPartNumber, tupQtyShipped.intValue(), tupOrders.intValue(),
                    tupSaleAmount);
        }

    };

    @PersistenceContext(unitName = "mas90")
    private EntityManager emMas90;

    @Autowired
    private JdbcTemplate db;

    public PartDao() {
        super(Part.class);
    }

    /**
     * Get list of parts ordered by 'id'.
     *
     * Ordering is important when parts are processed by batches (as in Magmi
     * CSV export). Unordered (sub)list can contain duplications or skip some
     * rows.
     *
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<Part> findAllOrderedById(int firstResult, int maxResults) {
        return em.createNamedQuery("findAllPartsOrderedById", Part.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
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

    public Page<AlsoBought> filterAlsoBough(Long partId, String manufacturerPartNumber, Integer qtyShipped,
            Double saleAmount, Integer orders, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        /* @formatter:off
           select ITEMCODE, sum(QUANTITYSHIPPED) as qty_shipped, count(distinct id.invoiceno) as orders, sum(id.EXTENSIONAMT) as amt
           from AR_INVOICEHISTORYDETAIL as id
           where
               id.INVOICENO in (select invoiceno from AR_INVOICEHISTORYDETAIL where ITEMCODE = '8-F-0431') 
               and id.itemcode <> '8-F-0431'
           group by
               id.ITEMCODE
           order by
               sum(id.QUANTITYSHIPPED) desc;
           @formatter:on */
        CriteriaBuilder cb = emMas90.getCriteriaBuilder();
        CriteriaQuery<Tuple> cqt = cb.createTupleQuery();
        Root<ArInvoiceHistoryDetail> root = cqt.from(ArInvoiceHistoryDetail.class);
        Path<String> colManufacturerPartNumber = root.get(ArInvoiceHistoryDetail_.itemcode);
        Expression<Double> colSumQuatityShipped = cb.sum(root.get(ArInvoiceHistoryDetail_.quantityshipped));
        Expression<Long> colOrders = cb.countDistinct(root.get(ArInvoiceHistoryDetail_.invoiceno));
        Expression<Double> colSaleAmount = cb.sum(root.get(ArInvoiceHistoryDetail_.extensionamt));
        // @formatter:off
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

        List<Predicate> lstPredicates = new ArrayList<>(5);
        lstPredicates.add(root.get(ArInvoiceHistoryDetail_.invoiceno).in(subqueryInvoices));
        lstPredicates.add(cb.notEqual(root.get(ArInvoiceHistoryDetail_.itemcode), manufacturerPartNumber));
        Predicate[] arrPredicates = lstPredicates.toArray(new Predicate[lstPredicates.size()]);
        cqt.where(arrPredicates);
        cqt.groupBy(root.get(ArInvoiceHistoryDetail_.itemcode));
        cqt.orderBy(cb.desc(colSumQuatityShipped));
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
        cq.where(arrPredicates);
        TypedQuery<Long> cntQuery = emMas90.createQuery(cq);
        // String sql = cntQuery.unwrap(org.hibernate.Query.class).getQueryString();
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

    public Page<BOMAncestor> ancestors(Long partId, int offest, int limit) throws Exception {
        List<BOMAncestor> ancestors = db.query("select p.id, p.manfr_part_num, pt.name, m.name, ba.distance, ba.type "
                + "from part as p " + "join manfr m on m.id = p.manfr_id "
                + "join part_type pt on pt.id = p.part_type_id " + "join ("
                + "    select distinct part_id, ancestor_part_id, distance, type " + "    from vbom_ancestor "
                + "    where part_id=? and distance > 0 " + ") as ba on ba.ancestor_part_id = p.id "
                + "order by ba.distance, ba.type, p.manfr_part_num " + "limit ?, ?", (rs, rowNum) -> {
                    long ancestorPartId = rs.getLong(1);
                    String ancestorManufacturerPartNumber = rs.getString(2);
                    String manufacturerName = rs.getString(3);
                    String ancestorPartType = rs.getString(4);
                    int distance = rs.getInt(5);
                    String relationType = rs.getString(6);
                    BOMAncestor ancestor = new BOMAncestor(ancestorPartId, ancestorPartType, manufacturerName,
                            ancestorManufacturerPartNumber, relationType, distance);
                    return ancestor;
                }, partId, offest, limit);
        Long total = db.queryForObject("select count(*) " + "from part as p " + "join manfr m on m.id = p.manfr_id "
                + "join part_type pt on pt.id = p.part_type_id " + "join ("
                + "    select distinct part_id, ancestor_part_id, distance, type " + "    from vbom_ancestor "
                + "    where part_id=? and distance > 0 " + ") as ba on ba.ancestor_part_id = p.id "
                + "order by ba.distance, ba.type, p.manfr_part_num", Long.class, partId);
        return new Page<>(total, ancestors);
    }

}
