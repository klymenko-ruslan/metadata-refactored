package com.turbointernational.metadata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.dao.UserDao;
import com.turbointernational.metadata.entity.User;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public boolean isEmailUnique(Long userId, String email) {
        User user = userDao.findUserByEmail(email);
        return isUserAttrUnique(userId, user);
    }

    public boolean isUsernameUnique(Long userId, String username) {
        User user = userDao.findUserByUsername(username);
        return isUserAttrUnique(userId, user);
    }

    private boolean isUserAttrUnique(Long userId, User user) {
         if (userId == null) {   // a new (unregistered) user
            return user == null;
        } else {   // existed user (userId != null)
            return user == null || userId.equals(user.getId());
        }
    }

}
