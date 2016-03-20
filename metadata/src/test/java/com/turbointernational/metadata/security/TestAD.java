package com.turbointernational.metadata.security;

import org.junit.Test;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.Hashtable;

/**
 * Created by trunikov on 3/20/16.
 */
public class TestAD {

    @Test
    public void testActiveDirectoryAuth() {
        try {
            Hashtable<String, String> ldapEnv = new Hashtable<>(11);
            ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            ldapEnv.put(Context.PROVIDER_URL, "ldap://ldap.turbointernational.com:389");
            ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
            ldapEnv.put(Context.SECURITY_PRINCIPAL, "LDAP");
            ldapEnv.put(Context.SECURITY_CREDENTIALS, "9)Fkp6%gaBk");
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
                while(ids.hasMore()) {
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
        } catch (Exception e) {
            System.out.println(" Search error: " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
