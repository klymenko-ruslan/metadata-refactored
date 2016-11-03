package com.turbointernational.metadata.dao;

import java.util.List;

import com.turbointernational.metadata.entity.BOMItem;
import org.springframework.stereotype.Repository;

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

}
