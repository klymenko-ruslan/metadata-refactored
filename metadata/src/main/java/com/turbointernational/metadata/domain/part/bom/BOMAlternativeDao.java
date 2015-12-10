package com.turbointernational.metadata.domain.part.bom;

import com.turbointernational.metadata.domain.AbstractDao;
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
