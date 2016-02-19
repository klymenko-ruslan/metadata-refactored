package com.turbointernational.metadata.services;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.services.BOMService.FoundBomRecursionException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 18.02.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles("integration")
public class BOMServiceTest {

    @Autowired
    private PartDao partDao;

    @Autowired
    private BOMService bomService;

    /**
     * Test that BOMs tree of a part is fully loaded and ordered.
     *
     * Test case:
     * <ol>
     *     <li>Prepare in a database a part and BOMs for the part</li>
     *     <li>Call method {@link BOMService#loadAllBomsOfPart(Part)} with the part as argument</li>
     *     <li>Make sure that</li>
     *     <ul>
     *         <li>a returned list is not null</li>
     *         <li>a returned list contains expected number of elements</li>
     *         <li>a returned list contains expected elements</li>
     *         <li>a returned list is ordered.</li>
     *     </ul>
     * </ol>
     *
     * @throws FoundBomRecursionException
     */
    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/bom_service/bom_recursion_check_ok_0.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    })
    @Transactional
    public void testLoadAllBomsOfPartOk() throws FoundBomRecursionException {
        Part part = partDao.findOne(2122L);
        List<Long> bomIds = bomService.loadAllBomsOfPart(part);
        Assert.assertNotNull("Returned list of BOMs can't be null.", bomIds);
        int n = bomIds.size();
        Assert.assertEquals("Unexpected size of a list of BOMs of the part.", 9, n);
        Assert.assertEquals("Unexpected or unordered list of BOMs of the part.",
                Arrays.asList(2122L, 7650L, 32349L, 36389L, 42156L, 42870L, 42930L, 43900L, 63214L), bomIds);
    }

    /**
     * Test that loading of a BOMs tree with cycled recursions throws {@link FoundBomRecursionException}.
     *
     * Test case:
     * <ol>
     *     <li>Prepare in a database a part and BOMs with recursion for the part</li>
     *     <li>Call method {@link BOMService#loadAllBomsOfPart(Part)} with the part as argument</li>
     *     <li>Make sure that</li>
     *     <ul>
     *         <li>thrown {@link FoundBomRecursionException}</li>
     *         <li>a returned list contains expected number of elements</li>
     *         <li>the exception contains ID of a part that makes recursion.</li>
     *     </ul>
     * </ol>
     */
    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/bom_service/bom_recursion_check_bad.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    })
    @Transactional
    public void testLoadAllBomsOfPartFailure() {
        Part part = partDao.findOne(2122L);
        try {
            bomService.loadAllBomsOfPart(part);
            Assert.fail("A recursion in BOMs has not been found.");
        } catch (FoundBomRecursionException e) {
            Long failedId = e.getFailedId();
            Assert.assertNotNull(failedId);
            Assert.assertEquals("Unexpected ID of failed part.", 32349L, failedId.longValue());
        }
    }

    /**
     * Test that two parts with BOMs trees without intersection pass the recursion check.
     *
     * Test case:
     * <ol>
     *     <li>Prepare in a database two parts and BOMs without recursion</li>
     *     <li>Call method {@link BOMService#bomRecursionCheck(Part, Part)} with parts as arguments</li>
     *     <li>Make sure that</li>
     *     <ul>
     *         <li>{@link FoundBomRecursionException} has not been thrown</li>
     *     </ul>
     * </ol>
     */
    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/bom_service/bom_recursion_check_ok_0.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    })
    @Transactional
    public void testBomRecursionCheckOK() {
        Part mainPart = partDao.findOne(1144L);
        Part childPart = partDao.findOne(2122L);
        try {
            bomService.bomRecursionCheck(mainPart, childPart);
        } catch (FoundBomRecursionException e) {
            Assert.fail("Found non existent recursion. failedId=" + e.getFailedId());
        }
    }


    /**
     * Test that two parts with BOMs which form a united tree with recursion are detected.
     *
     * Test case:
     * <ol>
     *     <li>Prepare in a database two parts and BOMs without recursion, but united tree has recursion</li>
     *     <li>Call method {@link BOMService#bomRecursionCheck(Part, Part)} with parts as arguments</li>
     *     <li>Make sure that</li>
     *     <ul>
     *         <li>{@link FoundBomRecursionException} has been thrown</li>
     *     </ul>
     * </ol>
     */
    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/feed_dictionaries.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:integration_tests/bom_service/bom_recursion_check_bad_0.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_tables.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:integration_tests/clear_dictionaries.sql")
    })
    @Transactional
    public void testBomRecursionCheckFailure() {
        Part mainPart = partDao.findOne(1144L);
        Part childPart = partDao.findOne(2122L);
        try {
            bomService.bomRecursionCheck(mainPart, childPart);
            Assert.fail("A recursion in BOMs has not been found.");
        } catch (FoundBomRecursionException e) {
            Long failedId = e.getFailedId();
            Assert.assertNotNull(failedId);
            Assert.assertEquals("Unexpected ID of failed part.", 4309L, failedId.longValue());
        }
    }

}