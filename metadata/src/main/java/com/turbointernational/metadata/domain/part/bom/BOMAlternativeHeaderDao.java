package com.turbointernational.metadata.domain.part.bom;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class BOMAlternativeHeaderDao extends GenericDao<BOMAlternativeHeader> {
    
    public BOMAlternativeHeaderDao() {
        super(BOMAlternativeHeader.class);
    }
}
