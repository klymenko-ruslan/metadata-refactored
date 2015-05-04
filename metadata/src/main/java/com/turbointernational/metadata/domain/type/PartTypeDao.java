package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class PartTypeDao extends GenericDao<PartType> {
    
    public PartTypeDao() {
        super(PartType.class);
    }
}
