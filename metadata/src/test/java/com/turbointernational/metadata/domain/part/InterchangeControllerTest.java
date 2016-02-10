package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.Application;
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

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
@WebIntegrationTest
@ActiveProfiles("integration")
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
public class InterchangeControllerTest {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Test
    public void testMerge() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String selectQuery = "SELECT * from PERSON WHERE PERSONID = 1";
        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(selectQuery);
        System.out.println(resultSet);
    }

}
