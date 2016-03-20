package com.turbointernational.metadata.security;

import com.turbointernational.metadata.domain.security.AuthProvider;
import com.turbointernational.metadata.domain.security.AuthProviderLdap;
import com.turbointernational.metadata.domain.security.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Set;

/**
 * ldapsearch -h ldap.turbointernational.com -p 389 -D 'LDAP' -b 'cn=Users, dc=TurboInternational, dc=local' -x -w '9)Fkp6%gaBk' -z 5 sAMAccountName
 * <p>
 * Created by dmytro.trunykov@zorallabs.com on 3/17/16.
 */
public class MetadataAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    MetadataAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication retVal = authentication;
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String name = token.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
        if (userDetails != null) {
            AuthenticationProvider authProvider;
            User user = (User) userDetails;
            AuthProvider userAuthProvider = user.getAuthProvider();
            if (userAuthProvider != null && userAuthProvider.getTyp() == AuthProvider.AuthProviderTypeEnum.LDAP) {
                authProvider = new AuthenticationProvider() {
                    @Override
                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                        Authentication ldapAuthResult;
                        AuthProviderLdap userAuthProviderLdap = (AuthProviderLdap) userAuthProvider;
                        String providerUrl = "ldap://" + userAuthProviderLdap.getHost() + ":" + userAuthProviderLdap.getPort();
                        String password = token.getCredentials().toString();
                        Hashtable<String, String> ldapEnv = new Hashtable<>(5);
                        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                        ldapEnv.put(Context.PROVIDER_URL, providerUrl);
                        ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
                        ldapEnv.put(Context.SECURITY_PRINCIPAL, name);
                        ldapEnv.put(Context.SECURITY_CREDENTIALS, password);
                        try {
                            new InitialDirContext(ldapEnv); // it throws NamingException when credentials are invalid
                            // The authentication is successful.
                            Set<SimpleGrantedAuthority> roles = user.getAuthorities();
                            ldapAuthResult = new UsernamePasswordAuthenticationToken(user, password, roles);
                        } catch(NamingException e) {
                            ldapAuthResult = authentication; // isAuthenticated() == false
                        }
                        return ldapAuthResult;
                    }

                    @Override
                    public boolean supports(Class<?> authentication) {
                        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
                    }
                };
           } else if (userAuthProvider == null) {
                DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
                daoAuthProvider.setUserDetailsService(userDetailsService);
                daoAuthProvider.setPasswordEncoder(new BCryptPasswordEncoder());
                authProvider = daoAuthProvider;
            } else {
                throw new IllegalArgumentException("The user has unknown type of auth provider: " + userAuthProvider.getTyp());
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
