package com.turbointernational.metadata.domain.part.bom;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class BOMAlternativeHeaderDao extends AbstractDao<BOMAlternativeHeader> {
    
    public BOMAlternativeHeaderDao() {
        super(BOMAlternativeHeader.class);
    }
}
