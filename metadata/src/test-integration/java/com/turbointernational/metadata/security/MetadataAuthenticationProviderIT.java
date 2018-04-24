package com.turbointernational.metadata.security;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dmytro.trunykov on 3/20/16.
 */
/*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles("integration")
@Transactional
*/

//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
@Transactional
@SqlConfig(
        dataSource = "dataSource",
        transactionManager = "transactionManagerMetadata"
)
public class MetadataAuthenticationProviderIT {

    private final static String DB_USER = "dbtest";
    private final static String DB_PASSWORD = "zoraltemp";

    private final static String LDAP_USER = "ldaptest";
    private final static String LDAP_PASSWORD = "b%nGAhS10f&JixJ";

    @Autowired
    private AuthenticationProvider metadataAuthenticationProvider;

    //@formatter:off
    /**
     * Test authentication with valid credentials against local database.
     * <p>
     *   Test case:
     * </p>
     * <ol>
     *   <li>Prepare in a database an user authenticated by database.</li>
     *   <li>Call method {@link MetadataAuthenticationProvider#authenticate(Authentication)} with valid credentials.</li>
     *   <li>Make sure that:
     *     <ul>
     *       <li>authentication passed</li>
     *     </ul>
     *   </li>
     * </ol>
     */
    //@formatter:on
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/auth_provider/db_user.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testAuthenticateLocalDB() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(DB_USER, DB_PASSWORD);
        Authentication authenticate = metadataAuthenticationProvider.authenticate(authentication);
        Assert.assertTrue("DB authentication failed.", authenticate.isAuthenticated());
    }

    //@formatter:off
    /**
     * Test authentication with INVALID credentials against local database.
     * <p>
     *   Test case:
     * </p>
     * <ol>
     *   <li>Prepare in a database an user authenticated by database.</li>
     *   <li>Call method {@link MetadataAuthenticationProvider#authenticate(Authentication)} with invalid credentials.</li>
     *   <li>Make sure that:
     *     <ul>
     *       <li>authentication failed</li>
     *     </ul>
     *   </li>
     * </ol>
     */
    //@formatter:on
    @Ignore
    @Test(expected = BadCredentialsException.class)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/auth_provider/db_user.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @Transactional(noRollbackFor = BadCredentialsException.class, transactionManager = "transactionManagerMetadata")
    public void testAuthenticateLocalDBFailure() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(DB_USER, DB_PASSWORD + "1"); // invalid password
        metadataAuthenticationProvider.authenticate(authentication);
    }

    //@formatter:off
    /**
     * Test plain (not secured) LDAP authentication with valid credentials.
     * <p>
     *   Test case:
     * </p>
     * <ol>
     *   <li>Prepare in a database authentication LDAP provider and an user authenticated by that provider.</li>
     *   <li>Call method {@link MetadataAuthenticationProvider#authenticate(Authentication)} with valid credentials.</li>
     *   <li>Make sure that:
     *     <ul>
     *       <li>authentication passed</li>
     *     </ul>
     *   </li>
     * </ol>
     */
    //@formatter:on
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/auth_provider/ldap_user.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testAuthenticateLDAP() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(LDAP_USER, LDAP_PASSWORD);
        Authentication authenticate = metadataAuthenticationProvider.authenticate(authentication);
        Assert.assertTrue("LDAP authentication failed.", authenticate.isAuthenticated());
    }

    //@formatter:off
    /**
     * Test plain (not secured) LDAP authentication with INVALID credentials.
     * <p>
     *   Test case:
     * </p>
     * <ol>
     *   <li>Prepare in a database authentication LDAP provider and an user authenticated by that provider.</li>
     *   <li>Call method {@link MetadataAuthenticationProvider#authenticate(Authentication)} with invalid credentials.</li>
     *   <li>Make sure that:
     *     <ul>
     *       <li>authentication NOT passed</li>
     *     </ul>
     *   </li>
     * </ol>
     */
    //@formatter:on
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/auth_provider/ldap_user.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testAuthenticateLDAPFailure() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(LDAP_USER, LDAP_PASSWORD + "1"); // invalid password
        Authentication authenticate = metadataAuthenticationProvider.authenticate(authentication);
        Assert.assertFalse("LDAP authentication passed.", authenticate.isAuthenticated());
    }

    //@formatter:off
    /**
     * Test LDAPS authentication with valid credentials.
     * In this case the LDAPS authentication process doesn't validate server's certificate.
     * <p>
     *   Test case:
     * </p>
     * <ol>
     *   <li>Prepare in a database authentication LDAPS_SOFT provider and an user authenticated by that provider.</li>
     *   <li>Call method {@link MetadataAuthenticationProvider#authenticate(Authentication)} with valid credentials.</li>
     *   <li>Make sure that:
     *     <ul>
     *       <li>authentication passed</li>
     *     </ul>
     *   </li>
     * </ol>
     */
    //@formatter:on
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/auth_provider/ldaps_soft_user.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testAuthenticateLDAPS_SOFT() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(LDAP_USER, LDAP_PASSWORD);
        // Authentication authentication = new UsernamePasswordAuthenticationToken("creddick", "greenlink99!");
        Authentication authenticate = metadataAuthenticationProvider.authenticate(authentication);
        Assert.assertTrue("LDAPS_SOFT authentication failed.", authenticate.isAuthenticated());
    }

}