package com.turbointernational.metadata.domain.security;

import com.turbointernational.metadata.domain.GenericDao;
import java.util.List;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class UserDao extends GenericDao<User> {

    public UserDao() {
        super(User.class);
    }
    
    public List<User> findActiveUsers() {
        return em.createQuery("SELECT o FROM User o WHERE o.enabled = true", User.class).getResultList();
    }
    
    public User findUserByEmail(String email) {
        if (StringUtils.isNotBlank(email)) {
            List<User> users = em
                    .createQuery("SELECT u FROM User u WHERE u.email = ?")
                    .setParameter(1, email)
                    .getResultList();
            
            if (!users.isEmpty()) {
                return users.get(0);
            }
        }
        
        return null;
    }
    
    public User findByPasswordResetToken(String token) {
        Query q = em.createQuery("SELECT u FROM User u WHERE u.passwordResetToken = ?", User.class);
        q.setParameter(1, token);
        return (User) q.getSingleResult();
    }
    
}
