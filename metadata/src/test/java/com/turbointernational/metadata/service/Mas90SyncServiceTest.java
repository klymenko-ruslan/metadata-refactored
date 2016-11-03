package com.turbointernational.metadata.service;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.entity.Mas90Sync;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.dao.UserDao;
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

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

/**
 * Created by dmytro.trunykov on 3/6/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles("integration")
@Transactional
@SqlConfig(
        dataSource = "dataSource",
        transactionManager = "transactionManagerMetadata"
)
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
        // We have to reset sync.process status in case when prev run test failed.
        // This reset re-initialize value of the status field from 'running' to 'finished'.
        // Without this reinitialization an assert exception will be thrown in the 'prepareStart(user)' procedure
        // during validation of the status.
        this.mas90SyncService.syncProcessStatus.reset();
        // Method 'prepareStart()' should be called in a separate transaction to emulate its call from controller.
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW);
        this.record = tt.execute(ts -> mas90SyncService.prepareStart(/*user*/ null));
        this.mas90Synchronizer = mas90SyncService.new Mas90Synchronizer(user, record);
    }


    //@formatter:off
    /**
     * Test that synchronization procedure does not process records which manufacturer numbers does not match to
     * the patter of eligible numbers.
     * <p>
     *   Test case:
     * </p>
     * <ol>
     *   <li>Insert into MAS90 database a record with manufacturer number '/BH2350'.</li>
     *   <li>Call method {@link Mas90SyncService.Mas90Synchronizer#run()}</li>
     *   <li>Make sure that</li>
     *   <ul>
     *     <li>Table 'part' has no any new record.</li>
     *     <li>
     *       Record with result of the execution ("History of synchronization sessions")
     *       has correct values and persistent.
     *     <li/>
     *   </ul>
     * </ol>
     */
    //@formatter:on
    @Test
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/create_mas90.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/feed_mas90.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/insertnewpart_mas90_0.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/clear_mas90.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    public void testInsertNewPart_0() {
        int numMas90Sync = JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "mas90sync");
        Assert.assertEquals("Table 'mas90sync' is empty.", 1, numMas90Sync);
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
        Assert.assertEquals("Unexpected sync.process status.", Mas90Sync.Status.FINISHED, record.getStatus());
    }


    //@formatter:off
    /**
     * Test that synchronization procedure creates a new part.
     * <p>
     *   Test case:
     * </p>
     * <ol>
     *   <li>Insert into MAS90 database a record with eligible manufacturer number.</li>
     *   <li>Call method {@link Mas90SyncService.Mas90Synchronizer#run()}</li>
     *   <li>Make sure that</li>
     *   <ul>
     *     <li>Table 'part' has a new record.</li>
     *     <li>Make sure that just created part has expected correct values.</li>
     *     <li>
     *       Record with result of the execution ("History of synchronization sessions")
     *       has correct values and persistent.
     *     </li>
     *   </ul>
     * </ol>
     */
    //@formatter:on
    @Test
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/create_mas90.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/feed_mas90.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/insertnewpart_mas90_1.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/clear_mas90.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
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
        Assert.assertEquals("Wrong description.", "*NLA - USE 1-A-1046* CARTRIDGE", part.getDescription());
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
        Assert.assertEquals("Unexpected sync.process status.", Mas90Sync.Status.FINISHED, record.getStatus());
    }


    //@formatter:off
    /**
     * Test that synchronization procedure creates a new part and BOMs.
     * This test is extension of the previous test {@link #testInsertNewPart_1}.
     * <p>
     *   Test case:
     * </p>
     * <ol>
     *   <li>Insert into MAS90 database a record with eligible manufacturer number and corresponding BOMs.</li>
     *   <li>Call method {@link Mas90SyncService.Mas90Synchronizer#run()}</li>
     *   <li>Make sure that</li>
     *   <ul>
     *     <li>Table 'part' has a new record.</li>
     *     <li>Make sure that just created part has expected correct values.</li>
     *     <li>Make sure that corresponding BOMs created too.</li>
     *     <li>
     *       Record with result of the execution ("History of synchronization sessions")
     *       has correct values and persistent.
     *     </li>
     *   </ul>
     * </ol>
     */
    //@formatter:on
    @Test
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/insertnewpart_metadata_2.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/create_mas90.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/feed_mas90.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/insertnewpart_mas90_2.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/mas90sync_service/clear_mas90.sql",
            config = @SqlConfig(dataSource = "dataSourceMas90", transactionManager = "transactionManagerMas90")
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    public void testInsertNewPart_2() {
        int numPartsBefore = JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "part");
        Assert.assertEquals("Unexpected number of rows in the table 'part'.", 2, numPartsBefore);
        int numCiItemBefore = JdbcTestUtils.countRowsInTable(jdbcTemplateMas90, "ci_item");
        Assert.assertEquals("Table 'ci_item' has unexpected number of records.", 1, numCiItemBefore);
        int numBmBilldetail = JdbcTestUtils.countRowsInTable(jdbcTemplateMas90, "bm_billdetail");
        Assert.assertEquals("Table 'bm_billdetail' has unexpected number of records.", 2, numBmBilldetail);
        int numBmBillheader = JdbcTestUtils.countRowsInTable(jdbcTemplateMas90, "bm_billheader");
        Assert.assertEquals("Table 'bm_billheader' has unexpected number of records.", 1, numBmBillheader);
        mas90Synchronizer.run();
        int numPartsAfter = JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "part");
        Assert.assertEquals("A new part has not been inserted.", 3, numPartsAfter); // 3 = 2 in the begin + 1 new
        Assert.assertEquals("A new part (kit) has been created partially. " +
                        "Table 'kit' has no corresponding record.", 2,
                JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "kit"));
        Part part = partDao.findByPartNumber("14-A-5383");
        Assert.assertNotNull("Part (14-A-5383) not found.", part);
        Assert.assertNotNull(part.getManufacturer());
        Assert.assertNotNull(part.getManufacturer().getId());
        Assert.assertEquals("Wrong manufacturer.", Mas90SyncService.TURBO_INTERNATIONAL_MANUFACTURER_ID,
                (long) part.getManufacturer().getId());
        Assert.assertEquals("Wrong description.", "CHRA & Nozzle Ring assy, GT174", part.getDescription());
        Assert.assertFalse("Wrong 'inactive'.", part.getInactive());
        Assert.assertEquals("Unexpected number of BOMs.", 2,
                JdbcTestUtils.countRowsInTable(jdbcTemplateMetadata, "bom"));
        // Check record with result.
        Assert.assertNotNull("The 'mas90sync' record was not persistent.", record.getId());
        Assert.assertNotNull("Field 'started' was not initialized.", record.getStarted());
        Assert.assertNotNull("Field 'finished' was not initialized.", record.getFinished());
        Assert.assertTrue("Value of the field 'started' must be lower or equal to a value in the field 'finished.'",
                record.getFinished().compareTo(record.getStarted()) >= 0);
        Assert.assertEquals("Not fetched any record from MAS90 to process.", 1L, (long) record.getToProcess());
        Assert.assertEquals("Unexpected number of updated record(s).", 1L, (long) record.getUpdated());
        Assert.assertEquals("There is skipped record(s).", 0L, (long) record.getSkipped());
        Assert.assertEquals("Unexpected sync.process status.", Mas90Sync.Status.FINISHED, record.getStatus());
    }


}