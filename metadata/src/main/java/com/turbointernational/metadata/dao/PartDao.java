package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.BOMAncestor;
import com.turbointernational.metadata.entity.PartType;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.web.dto.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.turbointernational.metadata.entity.PartType.PTID_TURBO;

/**
 *
 * @author jrodriguez
 */
@Repository
public class PartDao extends AbstractDao<Part> {
    
    private static final Logger log = LoggerFactory.getLogger(PartDao.class);

    @Autowired
    private JdbcTemplate db;

    public PartDao() {
        super(Part.class);
    }

    /**
     * Get list of parts ordered by 'id'.
     *
     * Ordering is important when parts are processed by batches (as in Magmi CSV export).
     * Unordered (sub)list can contain duplications or skip some rows.
     *
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<Part> findAllOrderedById(int firstResult, int maxResults) {
        return em.createNamedQuery("findAllPartsOrderedById", Part.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    public List<ProductImage> findProductImages(Collection<Long> productIds) {
        return em.createNamedQuery("findProductImagesForPart", ProductImage.class)
                .setParameter("productIds", productIds).getResultList();
    }

    public Part findByPartNumber(String partNumber) throws NoResultException {
        try {
            return em.createNamedQuery("findByPartNumber", Part.class)
                    .setParameter("partNumber", partNumber)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public Part findByPartNumberAndManufacturer(Long manufacturerId, String partNumber) {
        try {
            return em.createNamedQuery("findByPartNumberAndManufacturer", Part.class)
                    .setParameter("manufacturerId", manufacturerId)
                    .setParameter("partNumber", partNumber)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public List<Turbo> listTurbosLinkedToGasketKit(Long id) {
        try {
            return em.createQuery("FROM Turbo AS t WHERE t.partType.id=" + PTID_TURBO +
                    " AND t.gasketKit.id=:gasketKitId", Turbo.class).setParameter("gasketKitId", id).getResultList();
        } catch(NoResultException e) {
            return new ArrayList<>();
        }
    }

    public Page<BOMAncestor> ancestors(Long partId, int offest, int limit) throws Exception {
        List<BOMAncestor> ancestors = db.query(
                "select p.id, p.manfr_part_num, pt.name, m.name, ba.distance, ba.type " +
                        "from part as p " +
                        "join manfr m on m.id = p.manfr_id " +
                        "join part_type pt on pt.id = p.part_type_id " +
                        "join (" +
                        "    select distinct part_id, ancestor_part_id, distance, type " +
                        "    from vbom_ancestor " +
                        "    where part_id=? and distance > 0 " +
                        ") as ba on ba.ancestor_part_id = p.id " +
                        "order by ba.distance, ba.type, p.manfr_part_num " +
                        "limit ?, ?",
                (rs, rowNum) -> {
                    long ancestorPartId = rs.getLong(1);
                    String ancestorManufacturerPartNumber = rs.getString(2);
                    String manufacturerName = rs.getString(3);
                    String ancestorPartType = rs.getString(4);
                    int distance = rs.getInt(5);
                    String relationType = rs.getString(6);
                    BOMAncestor ancestor = new BOMAncestor(ancestorPartId, ancestorPartType, manufacturerName,
                            ancestorManufacturerPartNumber, relationType,distance);
                    return ancestor;
                }, partId, offest, limit);
        Long total = db.queryForObject(
                "select count(*) " +
                        "from part as p " +
                        "join manfr m on m.id = p.manfr_id " +
                        "join part_type pt on pt.id = p.part_type_id " +
                        "join (" +
                        "    select distinct part_id, ancestor_part_id, distance, type " +
                        "    from vbom_ancestor " +
                        "    where part_id=? and distance > 0 " +
                        ") as ba on ba.ancestor_part_id = p.id " +
                        "order by ba.distance, ba.type, p.manfr_part_num", Long.class, partId
        );
        return new Page<>(total, ancestors);
    }

}
