package com.turbointernational.metadata.domain.security;

import com.turbointernational.metadata.domain.GenericDao;
import java.util.List;
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
