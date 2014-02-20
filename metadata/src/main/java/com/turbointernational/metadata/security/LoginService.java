package com.turbointernational.metadata.security;

import com.turbointernational.metadata.domain.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author jrodriguez
 */
@Service("loginService")
@Configuration
public class LoginService implements UserDetailsService, AuthenticationProvider {
    
    @Autowired(required=true)
    BCryptPasswordEncoder bcrypt;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException {
        User user = User.findUserByEmail(email);
        if (user != null) {
            return user;
        }

        return null;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) 
      throws AuthenticationException {
        User user = (User) authentication.getPrincipal();
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
 
        if (bcrypt.isPasswordValid(user.getPassword(), password, user.getPasswordSalt())) {
            return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
        } else {
            throw new AuthenticationException("Unable to auth against third party systems") {};
        }
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(this)
//            .userDetailsService(this)
//            .passwordEncoder(bcrypt);
//    }
}
