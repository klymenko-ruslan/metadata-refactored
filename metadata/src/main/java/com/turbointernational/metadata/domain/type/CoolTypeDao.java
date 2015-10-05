package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class CoolTypeDao extends GenericDao<CoolType> {
    public CoolTypeDao() {
        super(CoolType.class);
    }
}
