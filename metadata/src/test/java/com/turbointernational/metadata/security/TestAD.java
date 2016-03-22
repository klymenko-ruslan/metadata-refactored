package com.turbointernational.metadata.security;

import org.junit.Test;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;

/**
 * Created by trunikov on 3/20/16.
 */
public class TestAD {

    private final static String SERVER_HOST = "ldap.turbointernational.com";
//    private final static String USER = "LDAP";
//    private final static String PASSWORD = "9)Fkp6%gaBk";
    private final static String USER = "max suen";
    private final static String PASSWORD = "turbotemp01!";

    @Test
    public void testLDAPAuth() throws NamingException {
        String url = "ldap://" + SERVER_HOST + ":389";
        Hashtable<String, String> ldapEnv = new Hashtable<>(11);
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL, url);
        ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        ldapEnv.put(Context.SECURITY_PRINCIPAL, USER);
        ldapEnv.put(Context.SECURITY_CREDENTIALS, PASSWORD);
        InitialDirContext ldapContext = new InitialDirContext(ldapEnv);
        // Create the search controls
        SearchControls searchCtls = new SearchControls();
        //Specify the attributes to return
        String returnedAtts[] = {"sn", "givenName", "samAccountName"};
        searchCtls.setReturningAttributes(returnedAtts);
        //Specify the search scope
        //searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        //searchCtls.setCountLimit(1);
        //specify the LDAP search filter
        //String searchFilter = "(&(objectClass=user))";
        String searchFilter = "sAMAccountName=IUSR_TURBOINT";

        //Specify the Base for the search
        String searchBase = "cn=Users, dc=TurboInternational, dc=local";
        //initialize counter to total the results
        int totalResults = 0;

        // Search for objects using the filter
        NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);

        //Loop through the search results
        while (answer.hasMoreElements()) {
            SearchResult sr = answer.next();
            totalResults++;
            System.out.println(">>>: " + sr);
            Attributes attrs = sr.getAttributes();
            NamingEnumeration<String> ids = attrs.getIDs();
            while (ids.hasMore()) {
                String id = ids.next();
                Attribute a = attrs.get(id);
                System.out.println(a);
            }
            System.out.println("============================================");
                /*
                System.out.println(">>>" + sr.getName());
                Attributes attrs = sr.getAttributes();
                System.out.println(">>>>>>" + attrs.get("samAccountName"));
                */
        }

        System.out.println("Total results: " + totalResults);
        ldapContext.close();

    }

    @Test
    public void testLDAPSAuth() throws NamingException, GeneralSecurityException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLContext.setDefault(ctx);

        Hashtable<String, String> ldapEnv = new Hashtable<>(11);
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL, "ldaps://" + SERVER_HOST + ":636");
        ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        ldapEnv.put(Context.SECURITY_PRINCIPAL, USER);
        ldapEnv.put(Context.SECURITY_CREDENTIALS, PASSWORD);
        InitialDirContext ldapContext = new InitialDirContext(ldapEnv);
        // Create the search controls
        SearchControls searchCtls = new SearchControls();
        //Specify the attributes to return
        String returnedAtts[] = {"sn", "givenName", "samAccountName"};
        searchCtls.setReturningAttributes(returnedAtts);
        //Specify the search scope
        //searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        //searchCtls.setCountLimit(1);
        //specify the LDAP search filter
        //String searchFilter = "(&(objectClass=user))";
        String searchFilter = "sAMAccountName=IUSR_TURBOINT";

        //Specify the Base for the search
        String searchBase = "cn=Users, dc=TurboInternational, dc=local";
        //initialize counter to total the results
        int totalResults = 0;

        // Search for objects using the filter
        NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);

        //Loop through the search results
        while (answer.hasMoreElements()) {
            SearchResult sr = answer.next();
            totalResults++;
            System.out.println(">>>: " + sr);
            Attributes attrs = sr.getAttributes();
            NamingEnumeration<String> ids = attrs.getIDs();
            while (ids.hasMore()) {
                String id = ids.next();
                Attribute a = attrs.get(id);
                System.out.println(a);
            }
            System.out.println("============================================");
                /*
                System.out.println(">>>" + sr.getName());
                Attributes attrs = sr.getAttributes();
                System.out.println(">>>>>>" + attrs.get("samAccountName"));
                */
        }

        System.out.println("Total results: " + totalResults);
        ldapContext.close();

    }

}
