package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.BOMAlternativeHeader;
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
