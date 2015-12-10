package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class SealTypeDao extends AbstractDao<SealType> {

    public SealTypeDao() {
        super(SealType.class);
    }
}
