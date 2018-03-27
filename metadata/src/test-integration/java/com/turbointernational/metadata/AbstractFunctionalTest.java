package com.turbointernational.metadata;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 * Created by dmytro.trunykov@zorallabs.com on 11-02-2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
@Transactional
@SqlConfig(
        dataSource = "dataSource",
        transactionManager = "transactionManagerMetadata"
)
public class AbstractFunctionalTest {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Qualifier("transactionManager")
    @Autowired
    protected PlatformTransactionManager txManager; // JPA

    @PersistenceContext(unitName = "metadata")
    protected EntityManager em;

    protected JdbcTemplate jdbcTemplate;

    @Before
    public void beforeTest() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
