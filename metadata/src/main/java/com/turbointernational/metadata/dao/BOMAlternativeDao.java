package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.BOMAlternative;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class BOMAlternativeDao extends AbstractDao<BOMAlternative> {
    public BOMAlternativeDao() {
        super(BOMAlternative.class);
    }
}
