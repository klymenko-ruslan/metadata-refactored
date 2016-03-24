package com.turbointernational.metadata.security;

import com.turbointernational.metadata.domain.security.AuthProvider;
import com.turbointernational.metadata.domain.security.AuthProviderLdap;
import com.turbointernational.metadata.domain.security.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import java.util.Set;

/**
 * Specialized authentication provider.
 * <p>
 * This provider can authenticate a user against local database or LDAP server depending
 * on field 'authProvider' in an user instance.
 * <p>
 * LDAP authentication assumes only verification of a provided password. User's permissions
 * are loaded form the database.
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
            if (userAuthProvider == null) { // authenticate against local database
                DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
                daoAuthProvider.setUserDetailsService(userDetailsService);
                daoAuthProvider.setPasswordEncoder(new BCryptPasswordEncoder());
                authProvider = daoAuthProvider;
            } else if (userAuthProvider.getTyp() == AuthProvider.AuthProviderTypeEnum.LDAP) {
                authProvider = new MetadataLdapAuthenticationProvider(user);
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

class MetadataLdapAuthenticationProvider implements AuthenticationProvider {

    private final static Logger log = LoggerFactory.getLogger(MetadataLdapAuthenticationProvider.class);

    private final static String PROTOCOL_LDAP = "ldap";
    private final static String PROTOCOL_LDAPS = "ldaps";

    private final User user;
    private final X509TrustManager x509TrustManager;

    MetadataLdapAuthenticationProvider(User user) {
        this.user = user;
        this.x509TrustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
    }

    private Hashtable<String, String> prepareLdapEnv(String providerUrl, String name, String password) {
        Hashtable<String, String> ldapEnv = new Hashtable<>(5);
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL, providerUrl);
        ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        ldapEnv.put(Context.SECURITY_PRINCIPAL, name);
        ldapEnv.put(Context.SECURITY_CREDENTIALS, password);
        return ldapEnv;
    }

    private String getProviderUrl(AuthProviderLdap userAuthProviderLdap) {
        String protocol;
        AuthProviderLdap.ProtocolEnum protocolEnum = userAuthProviderLdap.getProtocol();
        switch (protocolEnum) {
            case LDAP:
                protocol = PROTOCOL_LDAP;
                break;
            case LDAPS:
                protocol = PROTOCOL_LDAPS;
                break;
            case LDAPS_SOFT:
                protocol = PROTOCOL_LDAPS;
                break;
            default:
                throw new IllegalArgumentException("Unsupported protocol: " + protocolEnum);
        }
        String providerUrl = protocol + "://" + userAuthProviderLdap.getHost() + ":" + userAuthProviderLdap.getPort();
        return providerUrl;
    }

    private String getFullLogonName(String logonName, AuthProviderLdap userAuthProviderLdap) {
        String domain = userAuthProviderLdap.getDomain();
        if (StringUtils.isNotBlank(domain)) {
            return logonName + "@" + domain.trim();
        } else {
            return logonName;
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication ldapAuthResult;
        AuthProviderLdap userAuthProviderLdap = (AuthProviderLdap) user.getAuthProvider();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String name = token.getName();
        String fullLogonName = getFullLogonName(name, userAuthProviderLdap);
        String providerUrl = getProviderUrl(userAuthProviderLdap);
        String password = token.getCredentials().toString();
        Hashtable<String, String> ldapEnv = prepareLdapEnv(providerUrl, fullLogonName, password);
        try {
            if (userAuthProviderLdap.getProtocol() == AuthProviderLdap.ProtocolEnum.LDAPS_SOFT) {
                try {
                    SSLContext sslCtxDefault = SSLContext.getDefault();
                    try {
                        SSLContext ctx = SSLContext.getInstance("TLS");
                        ctx.init(null, new TrustManager[]{x509TrustManager}, null);
                        SSLContext.setDefault(ctx);
                        new InitialDirContext(ldapEnv); // it throws NamingException when credentials are invalid
                    } finally {
                        SSLContext.setDefault(sslCtxDefault);
                    }
                } catch (GeneralSecurityException e) {
                    log.error("Replacing of a SSL trust manager failed.", e);
                    return authentication; // failure
                }
            } else {
                new InitialDirContext(ldapEnv); // it throws NamingException when credentials are invalid
            }
            // The authentication is successful.
            Set<SimpleGrantedAuthority> roles = user.getAuthorities();
            ldapAuthResult = new UsernamePasswordAuthenticationToken(user, password, roles);
        } catch (NamingException e) {
            if (log.isDebugEnabled()) {
                log.debug("Authentication of an user '" + fullLogonName + "' failed.", e);
            } else {
                log.info("Authentication of an user '{}' failed: {}", fullLogonName, e.getMessage());
            }
            ldapAuthResult = authentication; // isAuthenticated() == false
        }
        return ldapAuthResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

}