package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.security.Group;
import com.turbointernational.metadata.domain.security.GroupDao;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.domain.security.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 *
 * @author jrodriguez
 */
@Service("loginService")
@Configuration
public class LoginService implements UserDetailsService {
    
    @Autowired(required=true)
    UserDao userDao;
    
    @Autowired(required=true)
    GroupDao groupDao;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException {
        User user =  userDao.findUserByEmail(email);
        
        if (user == null) {
            
            // If we don't have any users, create the admin account
            if (userDao.count() == 0) {
                return createFirstUser();
            }
            
            // Nope, just a failed login
            throw new UsernameNotFoundException("No users with email address: " + email);
        }
        
        return user;
    }
            
    public User createFirstUser() {
        User newUser = new User();
        newUser.setName("Administrator");
        newUser.setEmail("admin@localhost");
        newUser.setEnabled(true);
        newUser.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));

        // No other users, create the first user
        Group adminGroup = groupDao.findAll(0, 1).get(0);
        newUser.getGroups().add(adminGroup);
        adminGroup.getUsers().add(newUser);

        // Save the new user
        userDao.persist(newUser);
        groupDao.merge(adminGroup);
        
        return newUser;
    }
}
