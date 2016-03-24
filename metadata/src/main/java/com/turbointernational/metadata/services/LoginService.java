package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    GroupDao groupDao;
    
    @Autowired
    RoleDao roleDao;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = userDao.findUserByUsername(username);
        if (user == null) {
            // If there are users on the system, this is just a failed login
            if (userDao.count() > 0) {
                throw new UsernameNotFoundException("No users with username: " + username);
            }
            // If we don't have any users, create the admin account
            user = createFirstUser();
        }
        // Setup the user's granted authorities
        for (Role role : roleDao.findByUserId(user.getId())) {
            user.getAuthorities().add(new SimpleGrantedAuthority(role.getName()));
        }
        return user;
    }
            
    public User createFirstUser() {
        User newUser = new User();
        newUser.setName("Administrator");
        newUser.setEmail("admin@localhost");
        newUser.setEnabled(true);
        newUser.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));

        // Save the new user
        userDao.persist(newUser);
        
        // Save the new group
        Group adminGroup = groupDao.findAll(0, 1).get(0);
        adminGroup.getUsers().add(newUser);
        groupDao.merge(adminGroup);
        
        return newUser;
    }
}
