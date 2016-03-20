package com.turbointernational.metadata.security;

import com.turbointernational.metadata.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by dmytro.trunykov on 3/20/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles("integration")
@Transactional
public class MetadataAuthenticationProviderTest {

    @Autowired
    private AuthenticationProvider metadataAuthenticationProvider;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/auth_provider/ldap_user.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    public void testAuthenticate() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken("LDAP", "9)Fkp6%gaBk");
        Authentication authenticate = metadataAuthenticationProvider.authenticate(authentication);
        Assert.assertTrue("LDAP authentication failed.", authenticate.isAuthenticated());
    }

}