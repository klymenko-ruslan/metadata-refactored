package com.turbointernational.metadata.security;

import com.turbointernational.metadata.domain.security.Group;
import com.turbointernational.metadata.domain.security.User;
import javax.persistence.NoResultException;
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
        return User.findUserByEmail(email);
    }
    
    @Override
    public Authentication authenticate(Authentication authentication)  throws AuthenticationException {
        User user = (User) authentication.getPrincipal();
        String username = authentication.getName();
        String rawPass = authentication.getCredentials().toString();

        if (bcrypt.isPasswordValid(user.getPassword(), rawPass, user.getPasswordSalt())) {
            return new UsernamePasswordAuthenticationToken(username, rawPass, user.getAuthorities());
        } else {
            return null;
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
