package com.turbointernational.metadata.domain.security;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class GroupDao extends AbstractDao<Group> {

    public GroupDao() {
        super(Group.class);
    }
    
}
