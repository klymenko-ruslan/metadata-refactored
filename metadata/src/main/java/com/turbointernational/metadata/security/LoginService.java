package com.turbointernational.metadata.security;

import com.turbointernational.metadata.domain.security.User;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author jrodriguez
 */
@Service("loginService")
public class LoginService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException {
        User user = User.findUserByEmail(email);
        if (user != null) {
            return user;
        }

        return null;
    }
}
