package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class PartTypeDao extends AbstractDao<PartType> {
    
    public PartTypeDao() {
        super(PartType.class);
    }
}
