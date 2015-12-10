package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class CoolTypeDao extends AbstractDao<CoolType> {
    public CoolTypeDao() {
        super(CoolType.class);
    }
}
