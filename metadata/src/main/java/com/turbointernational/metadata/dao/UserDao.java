package com.turbointernational.metadata.dao;

import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import com.turbointernational.metadata.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @author jrodriguez
 */
@Repository
public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super(User.class);
    }

    public List<User> findActiveUsers() {
        return em.createQuery("SELECT o FROM User o WHERE o.enabled = true", User.class).getResultList();
    }

    public User findUserByUsername(String username) {
        if (StringUtils.isNotBlank(username)) {
            try {
                return em.createNamedQuery("findUserByUsername", User.class)
                        .setParameter("username", username)
                        .getSingleResult();
            } catch(NoResultException | NonUniqueResultException e) {
                // Ignore, return null.
            }
        }
        return null;
    }

    public User findByPasswordResetToken(String token) {
        Query q = em.createQuery("SELECT u FROM User u WHERE u.passwordResetToken = :token", User.class);
        q.setParameter("token", token);
        return (User) q.getSingleResult();
    }

}
