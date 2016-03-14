package com.turbointernational.metadata.services;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.part.Interchange;
import com.turbointernational.metadata.domain.part.InterchangeDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2/10/16.
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
public class InterchangeServiceTest {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private InterchangeService interchangeService;

    @Autowired
    private PartDao partDao;

    @Autowired
    private InterchangeDao interchangeDao;

    @Before
    public void beforeTest() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    //@formatter:off
    /**
     * Test removing a part from interchangeable group when this part is an only member of the group.
     * <p>
     *   Test-case:
     * </p>
     * <ol>
     *   <li>Create a part and interchangeable's group for the part.</li>
     *   <li>Call method {@link InterchangeService#leaveInterchangeableGroup(long)} for the part.</li>
     *   <li>Make sure that:
     *     <ul>
     *       <li>part still be a member of the same group;</li>
     *       <li>no other groups created;</li>
     *       <li>no changes wrote to the 'changelog' table.</li>
     *     </ul>
     *   </li>
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
            scripts = "classpath:integration_tests/interchange_service/leave_interchangeable_group_0_before.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    public void testLeaveInterchangeableGroup_0() {
        // Check prerequisites before testing.
        int chlogCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "changelog");
        Assert.assertEquals("Table 'changelog' contains unexpected data.", 0, chlogCount);
        long partId = 42077L;
        interchangeService.leaveInterchangeableGroup(partId);
        partDao.getEntityManager().flush(); // Important: propagate changes to the database
        int ihcount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_header");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_header'.", 1, ihcount);
        int iicount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_item");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_item'.", 1, iicount);
        Part part = partDao.findOne(partId);
        Interchange interchange = part.getInterchange();
        Assert.assertNotNull("Absent a record in the 'interchange_header' table.", interchange);
        Assert.assertEquals("A new interchangeable group has been created.", 5L, (long) interchange.getId());
        // Check that record to the 'changelog' was inserted.
        chlogCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "changelog");
        Assert.assertEquals("Added record(s) to the table 'changelog'.", 0, chlogCount);
    }


    //@formatter:off
    /**
     * Test removing a part from interchangeable group when the group has several members.
     * <p>
     *   Test-case:
     * </p>
     * <ol>
     *   <li>Create two parts and interchangeable's group for them.</li>
     *   <li>Call method {@link InterchangeService#leaveInterchangeableGroup(long)} for the first part.</li>
     *   <li>Make sure that:
     *     <ul>
     *       <li>the firs part was moved to a new (created) group;</li>
     *       <li>the second part still be a member of the same group;</li>
     *       <li>no other groups created;</li>
     *       <li>changes wrote to the 'changelog' table.</li>
     *     </ul>
     *   </li>
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
            scripts = "classpath:integration_tests/interchange_service/leave_interchangeable_group_1_before.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    @WithUserDetails("mock@gmail.com")
    public void testLeaveInterchangeableGroup_1() {
        // Check prerequisites before testing.
        int chlogCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "changelog");
        Assert.assertEquals("Table 'changelog' contains unexpected data.", 0, chlogCount);
        // The test.
        long partId0 = 42077L, partId1 = 41405;
        interchangeService.leaveInterchangeableGroup(partId0);
        partDao.getEntityManager().flush(); // Important: propagate changes to the database
        int ihcount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_header");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_header'.", 2, ihcount);
        int iicount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_item");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_item'.", 2, iicount);
        // This part should be moved to a new group.
        Part part0 = partDao.findOne(partId0);
        Interchange interchange = part0.getInterchange();
        Assert.assertNotNull("The part does not belong to any interchangeable's group.", interchange);
        Assert.assertNotEquals("A new interchangeable group has not been created.", 5L, (long) interchange.getId());
        // This part must be in the sames group.
        Part part1 = partDao.findOne(partId1);
        Interchange interchange1 = part1.getInterchange();
        Assert.assertNotNull("The part does not belong to any interchangeable's group.", interchange1);
        Assert.assertEquals("The part migrated to other group.", 5L, (long) interchange1.getId());
        // Check that record to the 'changelog' was inserted.
        chlogCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "changelog");
        Assert.assertEquals("Table 'changelog' has no record about last changes.", 1, chlogCount);
    }


    //@formatter:off
    /**
     * Test movement of a part from its interchangeable group to an interchangeable group of other part.
     * <p>
     *   Test-case:
     * </p>
     * <ol>
     *   <li>Create two interchangeable groups.</li>
     *   <li>Let part with ID 'partId' belong to the group 'A' and 'pickedPartId' to the group 'B'.</li>
     *   <li>Call method {@link InterchangeService#mergePickedAloneToPart(long partId, long pickedPartId)}</li>
     *   <li>Make sure that:
     *     <ul>
     *       <li>the 'pickedPartId' belongs to the same group as 'partId' -- 'A';</li>
     *       <li>the group 'B' exists and contains the same elements except 'pickedPartId';</li>
     *       <li>no other groups created;</li>
     *       <li>changes wrote to the 'changelog' table.</li>
     *     </ul>
     *   </li>
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
            scripts = "classpath:integration_tests/interchange_service/merge_interchangeables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    @WithUserDetails("mock@gmail.com")
    public void testMergePickedAloneToPart() {
        // Check prerequisites before testing.
        int chlogCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "changelog");
        Assert.assertEquals("Table 'changelog' contains unexpected data.", 0, chlogCount);
        int ihcount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_header");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_header'.", 2, ihcount);
        int iicount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_item");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_item'.", 5, iicount);
        // The test.
        long pickedPartId = 40393L, partId = 41405L;
        interchangeService.mergePickedAloneToPart(partId, pickedPartId);
        partDao.getEntityManager().flush(); // Important: propagate changes to the database
        ihcount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_header");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_header'.", 2, ihcount);
        iicount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_item");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_item'.", 5, iicount);
        Part part = partDao.findOne(partId);
        Part pickedPart = partDao.findOne(pickedPartId);
        Assert.assertEquals("Part and picked part belongs to different groups.",
                pickedPart.getInterchange(), part.getInterchange());
        Assert.assertTrue("Targeted group has unexpected size.", pickedPart.getInterchange().getParts().size() == 3);
        // Check that targeted group has expected members.
        Set<Long> expectedDstGroup = new HashSet<>(Arrays.asList(42077L, 41405L, 40393L));
        pickedPart.getInterchange().getParts().forEach(
                p -> Assert.assertTrue("Found unexpected part in the target group.", expectedDstGroup.contains(p.getId()))
        );
        // Check that source group has expected members.
        Set<Long> expectedSrcGroup = new HashSet<>(Arrays.asList(40392L, 41587L));
        Part partFromSrcGrp = partDao.findOne(40392L);
        partFromSrcGrp.getInterchange().getParts().forEach(
                p -> Assert.assertTrue("Found unexpected part in the source group.", expectedSrcGroup.contains(p.getId()))
        );
        // Check that record to the 'changelog' was inserted.
        chlogCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "changelog");
        Assert.assertEquals("Table 'changelog' has no record about last changes.", 1, chlogCount);
    }


    /**
     * The same test as described in {@link #testMergePickedAloneToPart()} but in reverse order.
     */
    @Test
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/interchange_service/merge_interchangeables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    @WithUserDetails("mock@gmail.com")
    public void testMergePartAloneToPicked() {
        // Check prerequisites before testing.
        int chlogCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "changelog");
        Assert.assertEquals("Table 'changelog' contains unexpected data.", 0, chlogCount);
        int ihcount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_header");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_header'.", 2, ihcount);
        int iicount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_item");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_item'.", 5, iicount);
        // The test.
        long pickedPartId = 40393L, partId = 41405L;
        interchangeService.mergePartAloneToPicked(partId, pickedPartId);
        partDao.getEntityManager().flush(); // Important: propagate changes to the database
        ihcount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_header");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_header'.", 2, ihcount);
        iicount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_item");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_item'.", 5, iicount);
        Part part = partDao.findOne(partId);
        Part pickedPart = partDao.findOne(pickedPartId);
        Assert.assertEquals("Part and picked part belongs to different groups.",
                pickedPart.getInterchange(), part.getInterchange());
        Assert.assertTrue("Targeted group has unexpected size.", pickedPart.getInterchange().getParts().size() == 4);
        // Check that targeted group has expected members.
        Set<Long> expectedDstGroup = new HashSet<>(Arrays.asList(40392L, 40393L, 41587L, 41405L));
        pickedPart.getInterchange().getParts().forEach(
                p -> Assert.assertTrue("Found unexpected part in the target group.", expectedDstGroup.contains(p.getId()))
        );
        // Check that source group has expected members.
        Set<Long> expectedSrcGroup = new HashSet<>(Arrays.asList(42077L));
        Part partFromSrcGrp = partDao.findOne(42077L);
        partFromSrcGrp.getInterchange().getParts().forEach(
                p -> Assert.assertTrue("Found unexpected part in the source group.", expectedSrcGroup.contains(p.getId()))
        );
        // Check that record to the 'changelog' was inserted.
        chlogCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "changelog");
        Assert.assertEquals("Table 'changelog' has no record about last changes.", 1, chlogCount);
    }


    //@formatter:off
    /**
     * Test a movement of a part and all members of the interchangeable group to an interchangeable group of other part.
     * <p>
     *   Test-case:
     * </p>
     * <ol>
     *   <li>Create two interchangeable groups.</li>
     *   <li>Let part with ID 'partId' belong to the group 'A' and 'pickedPartId' to the group 'B'.</li>
     *   <li>Call method {@link InterchangeService#mergePickedAllToPart(long partId, long pickedPartId)}</li>
     *   <li>Make sure that:
     *     <ul>
     *       <li>the 'pickedPartId' belongs to the same group as 'partId' -- 'A';</li>
     *       <li>all other members of the group 'B' have been moved to the group 'A';</li>
     *       <li>the group 'B' does not exists anymore;</li>
     *       <li>no other groups created;</li>
     *       <li>changes wrote to the 'changelog' table.</li>
     *     </ul>
     *   </li>
     * </ol>
     * See also ticket #590.
     */
    //@formatter:on
    @Test
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/feed_dictionaries.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = "classpath:integration_tests/interchange_service/merge_interchangeables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_tables.sql"
    )
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = "classpath:integration_tests/clear_dictionaries.sql"
    )
    @WithUserDetails("mock@gmail.com")
    public void testMergePickedAllToPart() {
        // Check prerequisites before testing.
        int chlogCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "changelog");
        Assert.assertEquals("Table 'changelog' contains unexpected data.", 0, chlogCount);
        int ihcount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_header");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_header'.", 2, ihcount);
        int iicount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_item");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_item'.", 5, iicount);
        // The test.
        long pickedPartId = 40393L, partId = 41405L;
        interchangeService.mergePickedAllToPart(partId, pickedPartId);
        partDao.getEntityManager().flush(); // Important: propagate changes to the database
        ihcount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_header");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_header'.", 1, ihcount);
        iicount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "interchange_item");
        Assert.assertEquals("Unexpected number of rows in the table 'interchange_item'.", 5, iicount);
        Part part = partDao.findOne(partId);
        Part pickedPart = partDao.findOne(pickedPartId);
        Assert.assertEquals("Part and picked part belongs to different groups.",
                pickedPart.getInterchange(), part.getInterchange());
        Assert.assertTrue("Targeted group has unexpected size.", pickedPart.getInterchange().getParts().size() == 5);
        // Check that targeted group has expected members.
        Set<Long> expectedDstGroup = new HashSet<>(Arrays.asList(42077L, 41405L, 40392L, 40393L, 41587L));
        pickedPart.getInterchange().getParts().forEach(
                p -> Assert.assertTrue("Found unexpected part in the target group.", expectedDstGroup.contains(p.getId()))
        );
        // Check that record to the 'changelog' was inserted.
        chlogCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "changelog");
        Assert.assertEquals("Table 'changelog' has no record about last changes.", 1, chlogCount);
    }


}
