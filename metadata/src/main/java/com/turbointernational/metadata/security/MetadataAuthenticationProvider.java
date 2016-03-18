package com.turbointernational.metadata.security;

import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.services.LoginService;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.support.SimpleDirContextAuthenticationStrategy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;

/**
 * ldapsearch -h ldap.turbointernational.com -p 389 -D 'LDAP' -b 'cn=Users, dc=TurboInternational, dc=local' -x -w '9)Fkp6%gaBk' -z 5 sAMAccountName
 *
 * Created by dmytro.trunykov@zorallabs.com on 3/17/16.
 */
public class MetadataAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    MetadataAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication retVal = authentication;
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String name = token.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
        if (userDetails != null) {
            User user = (User) userDetails;
            AuthenticationProvider authProvider;
            if ("LDAP".equals(name)) {
                LdapContextSource ldapContextSource = new LdapContextSource();
                ldapContextSource.setAuthenticationStrategy(new SimpleDirContextAuthenticationStrategy());
                ldapContextSource.setBase("cn=Users, dc=TurboInternational, dc=local");
                ldapContextSource.setUserDn(name);
                String password = token.getCredentials().toString();
                ldapContextSource.setPassword(password);
                ldapContextSource.setUrl("ldap.turbointernational.com:389");
                /*
                BindAuthenticator ldapAuthenticator = new BindAuthenticator(ldapContextSource);
                ldapAuthenticator.setUserDnPatterns(new String[] {"cn={0}"});
                */
                LdapAuthenticationProvider ldapAuthProvider = new LdapAuthenticationProvider(/*ldapAuthenticator*/ null);
                authProvider = ldapAuthProvider;
            } else {
                DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
                daoAuthProvider.setUserDetailsService(userDetailsService);
                daoAuthProvider.setPasswordEncoder(new BCryptPasswordEncoder());
                authProvider = daoAuthProvider;
            }
            retVal = authProvider.authenticate(authentication);
        }
        return retVal;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
