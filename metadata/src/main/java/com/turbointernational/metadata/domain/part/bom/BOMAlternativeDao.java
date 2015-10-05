package com.turbointernational.metadata.domain.part.bom;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class BOMAlternativeDao extends GenericDao<BOMAlternative> {
    public BOMAlternativeDao() {
        super(BOMAlternative.class);
    }
}
