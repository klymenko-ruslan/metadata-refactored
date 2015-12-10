package com.turbointernational.metadata.domain.part.types.kit;

import com.turbointernational.metadata.domain.AbstractDao;
import java.util.List;
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

    List<KitComponent> findByKitId(long kitId) {
        return getEntityManager()
                .createQuery("SELECT kc FROM KitComponent kc JOIN kc.part p WHERE kc.kit.id = :kitId", KitComponent.class)
                .setParameter("kitId", kitId)
                .getResultList();
    }
}
