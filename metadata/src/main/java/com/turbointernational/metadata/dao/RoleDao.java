package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author jrodriguez
 */
@Repository
public class RoleDao extends AbstractDao<Role> {

    public RoleDao() {
        super(Role.class);
    }
    
    public List<Role> findByUserId(long userId) {
        return getEntityManager().createQuery(
                  "SELECT DISTINCT r\n"
                + "FROM Group g\n"
                + "JOIN g.roles r\n"
                + "JOIN  g.users u\n"
                + "WHERE u.id = :userId", Role.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    
}
