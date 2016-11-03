package com.turbointernational.metadata.dao;

import java.util.List;

import com.turbointernational.metadata.entity.part.types.kit.KitComponent;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class KitComponentDao extends AbstractDao<KitComponent> {

    public KitComponentDao() {
        super(KitComponent.class);
    }

    public List<KitComponent> findByKitId(long kitId) {
        return getEntityManager()
                .createQuery("SELECT kc FROM KitComponent kc JOIN kc.part p WHERE kc.kit.id = :kitId", KitComponent.class)
                .setParameter("kitId", kitId)
                .getResultList();
    }
}
