package com.turbointernational.metadata.dao;

import static com.turbointernational.metadata.entity.PartType.PTID_ACTUATOR;
import static com.turbointernational.metadata.entity.PartType.PTID_BACKPLATE;
import static com.turbointernational.metadata.entity.PartType.PTID_BACKPLATE_SEALPLATE;
import static com.turbointernational.metadata.entity.PartType.PTID_BEARING_HOUSING;
import static com.turbointernational.metadata.entity.PartType.PTID_BOLT_SCREW;
import static com.turbointernational.metadata.entity.PartType.PTID_CARBON_SEAL;
import static com.turbointernational.metadata.entity.PartType.PTID_CARTRIDGE;
import static com.turbointernational.metadata.entity.PartType.PTID_CLAMP;
import static com.turbointernational.metadata.entity.PartType.PTID_COMPRESSOR_COVER;
import static com.turbointernational.metadata.entity.PartType.PTID_COMPRESSOR_WHEEL;
import static com.turbointernational.metadata.entity.PartType.PTID_FAST_WEARING_COMPONENT;
import static com.turbointernational.metadata.entity.PartType.PTID_FITTING;
import static com.turbointernational.metadata.entity.PartType.PTID_GASKET;
import static com.turbointernational.metadata.entity.PartType.PTID_GASKET_KIT;
import static com.turbointernational.metadata.entity.PartType.PTID_HEATSHIELD_SHROUD;
import static com.turbointernational.metadata.entity.PartType.PTID_JOURNAL_BEARING;
import static com.turbointernational.metadata.entity.PartType.PTID_JOURNAL_BEARING_SPACER;
import static com.turbointernational.metadata.entity.PartType.PTID_KIT;
import static com.turbointernational.metadata.entity.PartType.PTID_MAJOR_COMPONENT;
import static com.turbointernational.metadata.entity.PartType.PTID_MINOR_COMPONENT;
import static com.turbointernational.metadata.entity.PartType.PTID_MISC;
import static com.turbointernational.metadata.entity.PartType.PTID_MISCELLANEOUS_MINOR_COMPONENTS;
import static com.turbointernational.metadata.entity.PartType.PTID_NOZZLE_RING;
import static com.turbointernational.metadata.entity.PartType.PTID_NUT;
import static com.turbointernational.metadata.entity.PartType.PTID_OIL_DEFLECTOR;
import static com.turbointernational.metadata.entity.PartType.PTID_O_RING;
import static com.turbointernational.metadata.entity.PartType.PTID_PART;
import static com.turbointernational.metadata.entity.PartType.PTID_PIN;
import static com.turbointernational.metadata.entity.PartType.PTID_PISTON_RING;
import static com.turbointernational.metadata.entity.PartType.PTID_PLUG;
import static com.turbointernational.metadata.entity.PartType.PTID_RETAINING_RING;
import static com.turbointernational.metadata.entity.PartType.PTID_SEAL_PLATE;
import static com.turbointernational.metadata.entity.PartType.PTID_SHROUD;
import static com.turbointernational.metadata.entity.PartType.PTID_SPRING;
import static com.turbointernational.metadata.entity.PartType.PTID_THRUST_BEARING;
import static com.turbointernational.metadata.entity.PartType.PTID_THRUST_COLLAR;
import static com.turbointernational.metadata.entity.PartType.PTID_THRUST_PARTS;
import static com.turbointernational.metadata.entity.PartType.PTID_THRUST_SPACER;
import static com.turbointernational.metadata.entity.PartType.PTID_THRUST_WASHER;
import static com.turbointernational.metadata.entity.PartType.PTID_TURBINE_HOUSING;
import static com.turbointernational.metadata.entity.PartType.PTID_TURBINE_WHEEL;
import static com.turbointernational.metadata.entity.PartType.PTID_TURBO;
import static com.turbointernational.metadata.entity.PartType.PTID_WASHER;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
            return em.createQuery(
                    "FROM Turbo AS t WHERE t.partType.id=" + PTID_TURBO + " AND t.gasketKit.id=:gasketKitId",
                    Turbo.class).setParameter("gasketKitId", id).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Delete a record in a child table of the part.
     * 
     * The child table depends on the specified part type (e.g. kit, turbo, catridge, etc.).
     * 
     * @param partId
     * @param partTypeId
     */
    public void deletePartExt(long partId, long partTypeId) {
        String sql;
        if (partTypeId == PTID_ACTUATOR) {
            sql = "delete from actuator where part_id=?";
        } else if (partTypeId == PTID_BACKPLATE) {
            sql = "delete from backplate where part_id=?";
        } else if (partTypeId == PTID_BACKPLATE_SEALPLATE) {
            sql = "delete from backplate_sealplate where part_id=?";
        } else if (partTypeId == PTID_BEARING_HOUSING) {
            sql = "delete from bearing_housing where part_id=?";
        } else if (partTypeId == PTID_BOLT_SCREW) {
            sql = "delete from bolt_screw where part_id=?";
        } else if (partTypeId == PTID_CARBON_SEAL) {
            sql = "delete from carbon_seal where part_id=?";
        } else if (partTypeId == PTID_CARTRIDGE) {
            sql = "delete from cartridge where part_id=?";
        } else if (partTypeId == PTID_CLAMP) {
            sql = "delete from clamp where part_id=?";
        } else if (partTypeId == PTID_COMPRESSOR_COVER) {
            sql = "delete from compressor_cover where part_id=?";
        } else if (partTypeId == PTID_COMPRESSOR_WHEEL) {
            sql = "delete from compressor_wheel where part_id=?";
        } else if (partTypeId == PTID_FAST_WEARING_COMPONENT) {
            sql = "delete from fast_wearing_component where part_id=?";
        } else if (partTypeId == PTID_FITTING) {
            sql = "delete from fitting where part_id=?";
        } else if (partTypeId == PTID_GASKET) {
            sql = "delete from gasket where part_id=?";
        } else if (partTypeId == PTID_GASKET_KIT) {
            sql = "delete from gasket_kit where part_id=?";
        } else if (partTypeId == PTID_HEATSHIELD_SHROUD) {
            sql = "delete from heatshield where part_id=?";
        } else if (partTypeId == PTID_JOURNAL_BEARING) {
            sql = "delete from journal_bearing where part_id=?";
        } else if (partTypeId == PTID_JOURNAL_BEARING_SPACER) {
            sql = "delete from journal_bearing_spacer where part_id=?";
        } else if (partTypeId == PTID_KIT) {
            sql = "delete from kit where part_id=?";
        } else if (partTypeId == PTID_MAJOR_COMPONENT) {
            sql = "delete from major_component where part_id=?";
        } else if (partTypeId == PTID_MINOR_COMPONENT) {
            sql = "delete from minor_component where part_id=?";
        } else if (partTypeId == PTID_MISC) {
            sql = "delete from misc where part_id=?";
        } else if (partTypeId == PTID_MISCELLANEOUS_MINOR_COMPONENTS) {
            sql = "delete from misc_minor_component where part_id=?";
        } else if (partTypeId == PTID_NOZZLE_RING) {
            sql = "delete from nozzle_ring where part_id=?";
        } else if (partTypeId == PTID_NUT) {
            sql = "delete from nut where part_id=?";
        } else if (partTypeId == PTID_O_RING) {
            sql = "delete from o_ring where part_id=?";
        } else if (partTypeId == PTID_OIL_DEFLECTOR) {
            sql = "delete from oil_deflector where part_id=?";
        } else if (partTypeId == PTID_PART) {
            sql = "delete from p where part_id=?";
        } else if (partTypeId == PTID_PIN) {
            sql = "delete from pin where part_id=?";
        } else if (partTypeId == PTID_PISTON_RING) {
            sql = "delete from piston_ring where part_id=?";
        } else if (partTypeId == PTID_PLUG) {
            sql = "delete from plug where part_id=?";
        } else if (partTypeId == PTID_RETAINING_RING) {
            sql = "delete from retaining_ring where part_id=?";
        } else if (partTypeId == PTID_SEAL_PLATE) {
            sql = "delete from seal_plate where part_id=?";
        } else if (partTypeId == PTID_SHROUD) {
            sql = "delete from shroud where part_id=?";
        } else if (partTypeId == PTID_SPRING) {
            sql = "delete from spring where part_id=?";
        } else if (partTypeId == PTID_THRUST_BEARING) {
            sql = "delete from thrust_bearing where part_id=?";
        } else if (partTypeId == PTID_THRUST_COLLAR) {
            sql = "delete from thrust_collar where part_id=?";
        } else if (partTypeId == PTID_THRUST_PARTS) {
            sql = "delete from thrust_part where part_id=?";
        } else if (partTypeId == PTID_THRUST_SPACER) {
            sql = "delete from thrust_spacer where part_id=?";
        } else if (partTypeId == PTID_THRUST_WASHER) {
            sql = "delete from thrust_washer where part_id=?";
        } else if (partTypeId == PTID_TURBINE_HOUSING) {
            sql = "delete from turbine_housing where part_id=?";
        } else if (partTypeId == PTID_TURBINE_WHEEL) {
            sql = "delete from turbine_wheel where part_id=?";
        } else if (partTypeId == PTID_TURBO) {
            sql = "delete from turbo where part_id=?";
        } else if (partTypeId == PTID_WASHER) {
            sql = "delete from washer where part_id=?";
        } else {
            throw new AssertionError("Unsupported part type: " + partTypeId);
        }
        jdbcTemplate.update(sql, partId);
    }

    public void insertPartExt(long partId, long partTypeId) {
        String sql;
        if (partTypeId == PTID_ACTUATOR) {
            sql = "insert into actuator(part_id) values(?)";
        } else if (partTypeId == PTID_BACKPLATE) {
            sql = "insert into backplate(part_id) values(?)";
        } else if (partTypeId == PTID_BACKPLATE_SEALPLATE) {
            sql = "insert into backplate_sealplate(part_id) values(?)";
        } else if (partTypeId == PTID_BEARING_HOUSING) {
            sql = "insert into bearing_housing(part_id) values(?)";
        } else if (partTypeId == PTID_BOLT_SCREW) {
            sql = "insert into bolt_screw(part_id) values(?)";
        } else if (partTypeId == PTID_CARBON_SEAL) {
            sql = "insert into carbon_seal(part_id) values(?)";
        } else if (partTypeId == PTID_CARTRIDGE) {
            sql = "insert into cartridge(part_id) values(?)";
        } else if (partTypeId == PTID_CLAMP) {
            sql = "insert into clamp(part_id) values(?)";
        } else if (partTypeId == PTID_COMPRESSOR_COVER) {
            sql = "insert into compressor_cover(part_id) values(?)";
        } else if (partTypeId == PTID_COMPRESSOR_WHEEL) {
            sql = "insert into compressor_wheel(part_id) values(?)";
        } else if (partTypeId == PTID_FAST_WEARING_COMPONENT) {
            sql = "insert into fast_wearing_component(part_id) values(?)";
        } else if (partTypeId == PTID_FITTING) {
            sql = "insert into fitting(part_id) values(?)";
        } else if (partTypeId == PTID_GASKET) {
            sql = "insert into gasket(part_id) values(?)";
        } else if (partTypeId == PTID_GASKET_KIT) {
            sql = "insert into gasket_kit(part_id) values(?)";
        } else if (partTypeId == PTID_HEATSHIELD_SHROUD) {
            sql = "insert into heatshield(part_id) values(?)";
        } else if (partTypeId == PTID_JOURNAL_BEARING) {
            sql = "insert into journal_bearing(part_id) values(?)";
        } else if (partTypeId == PTID_JOURNAL_BEARING_SPACER) {
            sql = "insert into journal_bearing_spacer(part_id) values(?)";
        } else if (partTypeId == PTID_KIT) {
            throw new AssertionError("Internal error. This is a special case for the part type KIT [" + partId + "].");
        } else if (partTypeId == PTID_MAJOR_COMPONENT) {
            sql = "insert into major_component(part_id) values(?)";
        } else if (partTypeId == PTID_MINOR_COMPONENT) {
            sql = "insert into minor_component(part_id) values(?)";
        } else if (partTypeId == PTID_MISC) {
            sql = "insert into misc(part_id) values(?)";
        } else if (partTypeId == PTID_MISCELLANEOUS_MINOR_COMPONENTS) {
            sql = "insert into misc_minor_component(part_id) values(?)";
        } else if (partTypeId == PTID_NOZZLE_RING) {
            sql = "insert into nozzle_ring(part_id) values(?)";
        } else if (partTypeId == PTID_NUT) {
            sql = "insert into nut(part_id) values(?)";
        } else if (partTypeId == PTID_O_RING) {
            sql = "insert into o_ring(part_id) values(?)";
        } else if (partTypeId == PTID_OIL_DEFLECTOR) {
            sql = "insert into oil_deflector(part_id) values(?)";
        } else if (partTypeId == PTID_PART) {
            sql = "insert into p(part_id) values(?)";
        } else if (partTypeId == PTID_PIN) {
            sql = "insert into pin(part_id) values(?)";
        } else if (partTypeId == PTID_PISTON_RING) {
            sql = "insert into piston_ring(part_id) values(?)";
        } else if (partTypeId == PTID_PLUG) {
            sql = "insert into plug(part_id) values(?)";
        } else if (partTypeId == PTID_RETAINING_RING) {
            sql = "insert into retaining_ring(part_id) values(?)";
        } else if (partTypeId == PTID_SEAL_PLATE) {
            sql = "insert into seal_plate(part_id) values(?)";
        } else if (partTypeId == PTID_SHROUD) {
            sql = "insert into shroud(part_id) values(?)";
        } else if (partTypeId == PTID_SPRING) {
            sql = "insert into spring(part_id) values(?)";
        } else if (partTypeId == PTID_THRUST_BEARING) {
            sql = "insert into thrust_bearing(part_id) values(?)";
        } else if (partTypeId == PTID_THRUST_COLLAR) {
            sql = "insert into thrust_collar(part_id) values(?)";
        } else if (partTypeId == PTID_THRUST_PARTS) {
            sql = "insert into thrust_part(part_id) values(?)";
        } else if (partTypeId == PTID_THRUST_SPACER) {
            sql = "insert into thrust_spacer(part_id) values(?)";
        } else if (partTypeId == PTID_THRUST_WASHER) {
            sql = "insert into thrust_washer(part_id) values(?)";
        } else if (partTypeId == PTID_TURBINE_HOUSING) {
            sql = "insert into turbine_housing(part_id) values(?)";
        } else if (partTypeId == PTID_TURBINE_WHEEL) {
            sql = "insert into turbine_wheel(part_id) values(?)";
        } else if (partTypeId == PTID_TURBO) {
            throw new AssertionError(
                    "Internal error. This is a special case for the part type TURBO [" + partId + "].");
        } else if (partTypeId == PTID_WASHER) {
            sql = "insert into washer(part_id) values(?)";
        } else {
            throw new AssertionError("Unsupported part type: " + partTypeId);
        }
        jdbcTemplate.update(sql, partId);
    }

    public void changePartType(long partId, long oldPartTypeId, long newPartTypeId, boolean specialCase) {
        if (oldPartTypeId == PTID_TURBO) {
            // Delete references on Applications.
            jdbcTemplate.update("delete from turbo_car_model_engine_year where part_id=?", partId);
        }
        deletePartExt(partId, oldPartTypeId);
        jdbcTemplate.update("update part set part_type_id=? where id=?", newPartTypeId, partId);
        if (!specialCase) {
            insertPartExt(partId, newPartTypeId);
        }
    }

    public void changePartTypeOnKit(long partId, long oldPartTypeId, long kitTypeId) {
        changePartType(partId, oldPartTypeId, PTID_KIT, true);
        jdbcTemplate.update("insert into kit(part_id, kit_type_id) values(?, ?)", partId, kitTypeId);
    }

    public void changePartTypeOnTurbo(long partId, long oldPartTypeId, long turboModelId) {
        changePartType(partId, oldPartTypeId, PTID_TURBO, true);
        jdbcTemplate.update("insert into turbo(part_id, turbo_model_id) values(?, ?)", partId, turboModelId);
    }

}
