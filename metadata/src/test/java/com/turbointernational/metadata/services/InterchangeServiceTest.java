package com.turbointernational.metadata.services;

import com.turbointernational.metadata.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles("integration")
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
})
public class InterchangeServiceTest {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    private InterchangeService interchangeService;

    @Test
    public void testLeaveInterchangeableGroup() {
        //interchangeService.leaveInterchangeableGroup(1L);
        int n = JdbcTestUtils.countRowsInTable(new JdbcTemplate(dataSource), "part_type");
        Assert.assertEquals(21, n);
    }

}
