package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class KitTypeDao extends AbstractDao<KitType> {
    public KitTypeDao() {
        super(KitType.class);
    }
}
