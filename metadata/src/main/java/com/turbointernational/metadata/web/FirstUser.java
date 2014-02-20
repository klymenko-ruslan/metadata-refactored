
package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.security.Group;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.security.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author jrodriguez
 */
@Controller
public class FirstUser {
    
    @Autowired(required=true)
    BCryptPasswordEncoder bcrypt;
    
    @RequestMapping("/metadata/firstUser/create")
    public void create() {
        if (User.countUsers() == 0) {

            // First login, create the user object
            User newUser = new User();
            newUser.setEmail("admin");
            newUser.setEnabled(true);
            newUser.setPasswordSalt(bcrypt.genSalt());
            newUser.setPassword(bcrypt.encodePassword("admin", newUser.getPasswordSalt()));

            // No other users, create the first user
            Group adminGroup = Group.findGroupEntries(0, 1).get(0);
            newUser.getGroups().add(adminGroup);
            adminGroup.getUsers().add(newUser);
            adminGroup.merge();

            // Save the new user
            newUser.persist();
        }
    }
}
