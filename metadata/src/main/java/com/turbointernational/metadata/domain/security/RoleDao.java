package com.turbointernational.metadata.domain.security;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class RoleDao extends GenericDao<Role> {

    public RoleDao() {
        super(Role.class);
    }
    
}
