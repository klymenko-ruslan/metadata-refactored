package com.turbointernational.metadata.services;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.other.Mas90Sync;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.domain.security.UserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * Created by dmytro.trunykov on 3/6/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles("integration")
@SqlConfig(dataSource = "dataSource", transactionMode = SqlConfig.TransactionMode.ISOLATED)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = "classpath:integration_tests/feed_dictionaries.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
                scripts = "classpath:integration_tests/clear_tables.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
                scripts = "classpath:integration_tests/clear_dictionaries.sql")
})
public class Mas90SyncServiceTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Mas90SyncService mas90SyncService;

    private Mas90SyncService.Mas90Synchronizer mas90Synchronizer;

    @Autowired
    private UserDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        User admin = userDao.findOne(1L);
        Mas90Sync mas90Sync = new Mas90Sync();
        this.mas90Synchronizer = mas90SyncService.new Mas90Synchronizer(admin, mas90Sync);
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
                    scripts = "classpath:integration_tests/mas90sync_service/create_mas90.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                    config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
                    scripts = "classpath:integration_tests/mas90sync_service/feed_mas90.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
                    config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
                    scripts = "classpath:integration_tests/mas90sync_service/clear_mas90.sql")
    })
    @Transactional
    public void testInsertNewPart_0() {
        int numPartsBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "part");
        Assert.assertEquals("Table 'part' is not empty before test.", 0, numPartsBefore);
        userDao.getEntityManager().flush();
        //mas90Synchronizer.run();
        int numPartsAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "part");
        Assert.assertEquals("Some part(s) were inserted.", 0, numPartsAfter);
    }


}