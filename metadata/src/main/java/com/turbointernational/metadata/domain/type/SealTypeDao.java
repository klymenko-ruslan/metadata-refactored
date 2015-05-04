package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class SealTypeDao extends GenericDao<SealType> {

    public SealTypeDao() {
        super(SealType.class);
    }
}
