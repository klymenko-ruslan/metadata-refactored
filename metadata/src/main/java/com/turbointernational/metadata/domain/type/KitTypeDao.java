package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class KitTypeDao extends GenericDao<KitType> {
    public KitTypeDao() {
        super(KitType.class);
    }
}
