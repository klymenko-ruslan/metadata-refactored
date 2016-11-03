package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.Group;
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
