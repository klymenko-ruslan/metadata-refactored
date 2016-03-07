package com.turbointernational.metadata.services;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.other.Mas90Sync;
import com.turbointernational.metadata.domain.part.Part;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * Created by dmytro.trunykov on 3/6/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles("integration")
@SqlConfig(dataSource = "dataSource", transactionMode = SqlConfig.TransactionMode.ISOLATED)
public class Mas90SyncServiceTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Mas90SyncService mas90SyncService;

    @Autowired
    private UserDao userDao;

    private JdbcTemplate jdbcTemplate;

    private User user;

    private Mas90Sync record;

    private Mas90SyncService.Mas90Synchronizer mas90Synchronizer;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.user = userDao.findOne(1L); // admin
        this.record = mas90SyncService.prepareStart(user);
        this.mas90Synchronizer = mas90SyncService.new Mas90Synchronizer(user, record);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/create_mas90.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/feed_mas90.sql")
    /*
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/clear_mas90.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql")
    */
    @Transactional
    public void testInsertNewPart_0() {
        int numPartsBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "part");
        Assert.assertEquals("Table 'part' is not empty before test.", 0, numPartsBefore);
        // mas90Synchronizer.run();
        EntityManager em = userDao.getEntityManager();
        Part p = new Part();
        p.setInactive(Boolean.TRUE);
        Manufacturer m = em.getReference(Manufacturer.class, 1L);
        p.setManufacturer(m);
        em.persist(p);
        System.out.println("The new part ID: " + p.getId());
        em.flush();
        int numPartsAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "part");
        Assert.assertEquals("Some part(s) were inserted.", 1, numPartsAfter);
    }


}