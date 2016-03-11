package com.turbointernational.metadata.services;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.other.Mas90Sync;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.domain.security.UserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_MANDATORY;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

/**
 * Created by dmytro.trunykov on 3/6/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles("integration")
public class Mas90SyncServiceTest {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    @Qualifier("dataSourceMas90")
    private DataSource dataSourceMas90;

    @Qualifier("transactionManager")
    @Autowired
    private PlatformTransactionManager txManager; // JPA

    @Autowired
    private Mas90SyncService mas90SyncService;

    @Autowired
    private PartDao partDao;

    @Autowired
    private UserDao userDao;

    private JdbcTemplate jdbcTemplateMetadata;

    private JdbcTemplate jdbcTemplateMas90;

    private User user;

    private Mas90Sync record;

    private Mas90SyncService.Mas90Synchronizer mas90Synchronizer;

    @Before
    public void setUp() {
        this.jdbcTemplateMetadata = new JdbcTemplate(dataSource);
        this.jdbcTemplateMas90 = new JdbcTemplate(dataSourceMas90);
        this.user = userDao.findOne(1L); // admin
        TransactionTemplate tt = new TransactionTemplate(txManager);
        //tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW);
        tt.setPropagationBehavior(PROPAGATION_MANDATORY);
        this.record = tt.execute(ts -> mas90SyncService.prepareStart(user));
        this.mas90Synchronizer = mas90SyncService.new Mas90Synchronizer(user, record);
    }

    /**
     * Test that synchronization procedure does not process records which have manufacturer numbers
     * unmatched with the pattern.
     *
     * Test case:
     * <ol>
     *     <li>Insert into MAS90 database a record with manufacturer number '/BH2350'.</li>
     *     <li>Call method {@link Mas90SyncService.Mas90Synchronizer#run()}</li>
     *     <li>Make sure that</li>
     *     <ul>
     *         <li>Table 'part' has no any new record.</li>
     *         <li>Record with result of the execution has correct values and persistent.<li/>
     *     </ul>
     * </ol>
     */
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/create_mas90.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/feed_mas90.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/insertnewpart_mas90_0.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/clear_mas90.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql")
    public void testInsertNewPart_0() {
        int numPartsBefore = JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "part");
        Assert.assertEquals("Table 'part' is not empty before test.", 0, numPartsBefore);
        int numCiItemBefore = JdbcTestUtils.countRowsInTable(jdbcTemplateMas90, "ci_item");
        Assert.assertEquals("Table 'ci_item' has unexpected number of records.", 1, numCiItemBefore);
        mas90Synchronizer.run();
        int numPartsAfter = JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "part");
        // Check record with result.
        Assert.assertEquals("Some part(s) were inserted.", 0, numPartsAfter);
        Assert.assertNotNull("The 'mas90sync' record was not persistent.", record.getId());
        Assert.assertNotNull("Field 'started' was not initialized.", record.getStarted());
        Assert.assertNotNull("Field 'finished' was not initialized.", record.getFinished());
        Assert.assertTrue("Value of the field 'started' must be lower or equal to a value in the field 'finished.'",
                record.getFinished().compareTo(record.getStarted()) >= 0);
        Assert.assertEquals("Not fetched any record from MAS90 to process.", 1L, (long) record.getToProcess());
        Assert.assertEquals("There is updated record(s).", 0L, (long) record.getUpdated());
        Assert.assertEquals("There is skipped record(s).", 0L, (long) record.getSkipped());
    }

    /**
     * Test that synchronization procedure does not process records which have manufacturer numbers
     * unmatched with the pattern.
     *
     * Test case:
     * <ol>
     *     <li>Insert into MAS90 database a record with manufacturer number '/BH2350'.</li>
     *     <li>Call method {@link Mas90SyncService.Mas90Synchronizer#run()}</li>
     *     <li>Make sure that</li>
     *     <ul>
     *         <li>Table 'part' has no any new record.</li>
     *         <li>Record with result of the execution has correct values and persistent.<li/>
     *     </ul>
     * </ol>
     */
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/create_mas90.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/feed_mas90.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/insertnewpart_mas90_1.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90"),
            scripts = "classpath:integration_tests/mas90sync_service/clear_mas90.sql")
    /*
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql")
    */
    @Transactional
    public void testInsertNewPart_1() {
        int numPartsBefore = JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "part");
        Assert.assertEquals("Table 'part' is not empty before test.", 0, numPartsBefore);
        int numCiItemBefore = JdbcTestUtils.countRowsInTable(jdbcTemplateMas90, "ci_item");
        Assert.assertEquals("Table 'ci_item' has unexpected number of records.", 1, numCiItemBefore);
        mas90Synchronizer.run();
        int numPartsAfter = JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "part");
        Assert.assertEquals("A new part has not been inserted.", 1, numPartsAfter);
        Assert.assertEquals("A new part (cartridge) has been created partially. " +
                "Table 'cartridge' has no corresponding record.", 1,
                JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "cartridge"));
        Part part = partDao.findByPartNumber("1-A-1047");
        Assert.assertNotNull("Part (1-A-1047) not found.", part);
        Assert.assertNotNull(part.getManufacturer());
        Assert.assertNotNull(part.getManufacturer().getId());
        Assert.assertEquals("Wrong manufacturer.", Mas90SyncService.TURBO_INTERNATIONAL_MANUFACTURER_ID,
                (long) part.getManufacturer().getId());
        Assert.assertEquals("Wrong description.", "*NLA - USE 1-A-1046* CARTRIDGE" , part.getDescription());
        Assert.assertTrue("Wrong 'inactive'.", part.getInactive());
        Assert.assertEquals("Table BOM must be empty.", 0,
                JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "bom"));
        // Check record with result.
        Assert.assertNotNull("The 'mas90sync' record was not persistent.", record.getId());
        Assert.assertNotNull("Field 'started' was not initialized.", record.getStarted());
        Assert.assertNotNull("Field 'finished' was not initialized.", record.getFinished());
        Assert.assertTrue("Value of the field 'started' must be lower or equal to a value in the field 'finished.'",
                record.getFinished().compareTo(record.getStarted()) >= 0);
        Assert.assertEquals("Not fetched any record from MAS90 to process.", 1L, (long) record.getToProcess());
        Assert.assertEquals("There is updated record(s).", 0L, (long) record.getUpdated());
        Assert.assertEquals("There is skipped record(s).", 0L, (long) record.getSkipped());
    }

}