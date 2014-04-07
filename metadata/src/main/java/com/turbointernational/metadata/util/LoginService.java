package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.security.Group;
import com.turbointernational.metadata.domain.security.User;
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
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException {
        User user =  User.findUserByEmail(email);
        
        if (user == null) {
            
            // If we don't have any users, create the admin account
            if (User.countUsers() == 0) {
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
        newUser.setEmail("admin");
        newUser.setEnabled(true);
        newUser.setPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));

        // No other users, create the first user
        Group adminGroup = Group.findGroupEntries(0, 1).get(0);
        newUser.getGroups().add(adminGroup);
        adminGroup.getUsers().add(newUser);

        // Save the new user
        newUser.persist();
        adminGroup.merge();
        
        return newUser;
    }
}
