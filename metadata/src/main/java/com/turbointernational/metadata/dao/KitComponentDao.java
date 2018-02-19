package com.turbointernational.metadata.dao;

import static com.turbointernational.metadata.entity.PartType.PTID_KIT;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.Manufacturer_;
import com.turbointernational.metadata.entity.PartType_;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.Part_;
import com.turbointernational.metadata.entity.part.types.Kit;
import com.turbointernational.metadata.entity.part.types.Kit_;
import com.turbointernational.metadata.entity.part.types.kit.KitComponent;
import com.turbointernational.metadata.entity.part.types.kit.KitComponent_;
import com.turbointernational.metadata.web.dto.CommonComponent;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author jrodriguez
 */
@Repository
public class KitComponentDao extends AbstractDao<KitComponent> {

    @Autowired
    private DataSource dataSource = null;

    private JdbcTemplate jdbcTemplate;

    public KitComponentDao() {
        super(KitComponent.class);
    }

    @PostConstruct
    public void init() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Page<KitComponent> filter(Long kitId, Long partId, String sortProperty, String sortOrder, Integer offset,
            Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<KitComponent> ecq = cb.createQuery(KitComponent.class);
        Root<KitComponent> root = ecq.from(KitComponent.class);
        Join<KitComponent, Kit> kitJoin = null;
        Join<KitComponent, Part> partJoin = null;
        ecq.select(root);
        int numPredicates = 0;
        List<Predicate> lstPredicates = new ArrayList<>(2);
        if (kitId != null) {
            kitJoin = root.join(KitComponent_.kit);
            lstPredicates.add(cb.equal(kitJoin.get(Kit_.id), kitId));
            numPredicates++;
        }
        if (partId != null) {
            partJoin = root.join(KitComponent_.part);
            lstPredicates.add(cb.equal(partJoin.get(Part_.id), partId));
            numPredicates++;
        }
        Predicate[] arrPredicates = lstPredicates.toArray(new Predicate[numPredicates]);
        ecq.where(arrPredicates);
        if (sortOrder != null) {
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            Path<?> sortPath;
            if (sortProperty.equals("part.manufacturerPartNumber")) {
                if (partJoin == null) {
                    partJoin = root.join(KitComponent_.part);
                }
                sortPath = partJoin.get(Part_.manufacturerPartNumber);
            } else if (sortProperty.equals("part.partType.name")) {
                if (partJoin == null) {
                    partJoin = root.join(KitComponent_.part);
                }
                sortPath = partJoin.get(Part_.partType).get(PartType_.name);
            } else if (sortProperty.equals("part.manufacturer.name")) {
                if (partJoin == null) {
                    partJoin = root.join(KitComponent_.part);
                }
                sortPath = partJoin.get(Part_.manufacturer).get(Manufacturer_.name);
            } else if (sortProperty.equals("kit.manufacturerPartNumber")) {
                if (kitJoin == null) {
                    kitJoin = root.join(KitComponent_.kit);
                }
                sortPath = kitJoin.get(Kit_.manufacturerPartNumber);
            } else if (sortProperty.equals("kit.partType.name")) {
                if (kitJoin == null) {
                    kitJoin = root.join(KitComponent_.kit);
                }
                sortPath = kitJoin.get(Part_.partType).get(PartType_.name);
            } else if (sortProperty.equals("kit.manufacturer.name")) {
                if (kitJoin == null) {
                    kitJoin = root.join(KitComponent_.kit);
                }
                sortPath = kitJoin.get(Part_.manufacturer).get(Manufacturer_.name);
            } else if (sortProperty.equals("exclude")) {
                sortPath = root.get(KitComponent_.exclude);
            } else {
                throw new AssertionError("Unsupported sort property: " + sortProperty);
            }
            if (sortOrder.equalsIgnoreCase("asc")) {
                ecq.orderBy(cb.asc(sortPath));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                ecq.orderBy(cb.desc(sortPath));
            } else {
                throw new AssertionError("Unknown sort order: " + sortOrder);
            }
        }
        TypedQuery<KitComponent> q = em.createQuery(ecq);
        if (offset != null) {
            q.setFirstResult(offset);
        }
        if (limit != null) {
            q.setMaxResults(limit);
        }
        List<KitComponent> recs = q.getResultList();
        CriteriaQuery<Long> ccq = cb.createQuery(Long.class);
        Root<KitComponent> countRoot = ccq.from(KitComponent.class);
        if (kitId != null) {
            countRoot.join(KitComponent_.kit);
        }
        if (partId != null) {
            countRoot.join(KitComponent_.part);
        }
        ccq.select(cb.count(countRoot));
        ccq.where(arrPredicates);
        long total = em.createQuery(ccq).getSingleResult();
        return new Page<>(total, recs);
    }

    public List<CommonComponent> listCommonTurboTypes(Long partId /* Turbo */) {
        //@formatter:off
        List<CommonComponent> retVal = jdbcTemplate.query(
                "select" + 
                "    p.id as id, p.manfr_part_num as manfr_part_num, " + 
                "    p.part_type_id as part_type_id, pt.name as part_type_name, " + 
                "    p.manfr_id as manfr_id, m.name as manfr_name, " + 
                "    p.name as name, p.description as description, p.inactive as inactive, " + 
                "    k.kit_type_id as kit_type_id, kt.name as kit_type_name, " + 
                "    kpcc.id as kpccid, kpcc.exclude as exclude " + 
                "from " + 
                "    part as p " + 
                "    join part_turbo_type as ptt on ptt.part_id = p.id " + 
                "    join part_type as pt on p.part_type_id = pt.id " + 
                "    join manfr as m on p.manfr_id = m.id " + 
                "    join kit as k on p.id = k.part_id " + 
                "    join kit_type as kt on k.kit_type_id = kt.id " + 
                "    left outer join kit_part_common_component as kpcc on p.id = kpcc.kit_id " + 
                "where " + 
                "    p.part_type_id = " + PTID_KIT + " " + 
                "    and ptt.turbo_type_id in(" + 
                "      select tm.turbo_type_id" + 
                "      from turbo as t join turbo_model as tm on t.turbo_model_id = tm.id" + 
                "      where t.part_id = ?" + 
                "    )", 
                ps -> ps.setLong(1, partId),
                (rs, rowNum) -> {
                    Long id = rs.getLong("kpccid");
                    if (rs.wasNull()) {
                        id = null;
                    }
                    Boolean exclude = rs.getBoolean("exclude");
                    if (rs.wasNull()) {
                        exclude = null;
                    }
                    Long kitId = rs.getLong("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String partNumber = rs.getString("manfr_part_num");
                    Long partTypeId = rs.getLong("part_type_id");
                    String partTypeName = rs.getString("part_type_name");
                    com.turbointernational.metadata.web.dto.PartType partType = new com.turbointernational.metadata.web.dto.PartType(partTypeId, partTypeName);
                    Long manufacturerId = rs.getLong("manfr_id");
                    String manufacturerName = rs.getString("manfr_name");
                    com.turbointernational.metadata.web.dto.Manufacturer manufacturer = new com.turbointernational.metadata.web.dto.Manufacturer(manufacturerId, manufacturerName);
                    boolean inactive = rs.getBoolean("exclude");
                    Long kitTypeId = rs.getLong("kit_type_id");
                    String kitTypeName = rs.getString("kit_type_name");
                    com.turbointernational.metadata.web.dto.KitType kitType = new com.turbointernational.metadata.web.dto.KitType(kitTypeId, kitTypeName);
                    com.turbointernational.metadata.web.dto.Kit kit = new com.turbointernational.metadata.web.dto.Kit(kitId,  name,  description, partNumber, partType,
                            manufacturer, inactive, kitType);
                    return new CommonComponent(id, kit, null, exclude);
                });
        //@formatter:on
        return retVal;
    }

}
