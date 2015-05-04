package com.turbointernational.metadata.domain.part.types.kit;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class KitComponentDao extends GenericDao<KitComponent> {

    public KitComponentDao() {
        super(KitComponent.class);
    }
}
