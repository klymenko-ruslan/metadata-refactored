package com.turbointernational.metadata.domain.part.bom;

import com.turbointernational.metadata.domain.AbstractDao;
import java.util.List;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

/**
 *
 * @author jrodriguez
 */
@Repository
public class BOMItemDao extends AbstractDao<BOMItem> {

    public BOMItemDao() {
        super(BOMItem.class);
    }

    public List<BOMItem> findByParentId(Long partId) {
        return em.createNamedQuery("findBomsOfPart", BOMItem.class)
                .setParameter("parentPartId", partId)
                .getResultList();
    }

    public List<BOMItem> findByParentAndTypeIds(Long partId, Long partTypeId) {
        return em.createNamedQuery("findBomsOfPartWithType", BOMItem.class)
                .setParameter("parentPartId", partId)
                .setParameter("partTypeId", partTypeId)
                .getResultList();
    }

    public List<BOMItem> findParentsForBom(Long partId) {
        return em.createNamedQuery("findBomParents", BOMItem.class)
                .setParameter("partId", partId)
                .getResultList();
    }

    public List<Long> bomChildren(Long partId) {
        return em.createNativeQuery("SELECT DISTINCT b2.child_part_id " +
                "FROM bom as b " +
                "JOIN bom_descendant AS bd ON b.id = bd.part_bom_id " +
                "JOIN bom AS b2 ON bd.descendant_bom_id = b2.id " +
                "WHERE b.parent_part_id=:partId")
                .setParameter("partId", partId).getResultList();
    }

}
