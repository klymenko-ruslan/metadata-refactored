package com.turbointernational.metadata.domain.security;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class GroupDao extends GenericDao<Group> {

    public GroupDao() {
        super(Group.class);
    }
    
}
