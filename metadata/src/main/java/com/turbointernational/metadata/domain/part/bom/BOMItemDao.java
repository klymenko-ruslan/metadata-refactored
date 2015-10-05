package com.turbointernational.metadata.domain.part.bom;

import com.turbointernational.metadata.domain.GenericDao;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class BOMItemDao extends GenericDao<BOMItem> {
    
    public BOMItemDao() {
        super(BOMItem.class);
    }

    public List<BOMItem> findByParentId(long id) {
        return getEntityManager()
                .createQuery("SELECT i FROM BOMItem i JOIN i.child c WHERE i.parent.id = :parentPartId", BOMItem.class)
                .setParameter("parentPartId", id)
                .getResultList();
    }
}
