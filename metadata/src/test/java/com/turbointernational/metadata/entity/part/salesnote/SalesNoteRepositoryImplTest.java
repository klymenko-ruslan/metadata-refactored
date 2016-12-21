package com.turbointernational.metadata.entity.part.salesnote;

import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.google.common.collect.Iterables;
import com.turbointernational.metadata.dao.*;
import com.turbointernational.metadata.entity.*;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.web.dto.SalesNoteSearchRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;



/**
 *
 * @author jrodriguez
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
@DbUnitConfiguration
//@TestExecutionListeners({
//    DependencyInjectionTestExecutionListener.class,
//    DirtiesContextTestExecutionListener.class,
//    TransactionalTestExecutionListener.class,
//    DbUnitTestExecutionListener.class
//})
@Transactional
@SqlConfig(
        dataSource = "dataSource",
        transactionManager = "transactionManagerMetadata"
)
public class SalesNoteRepositoryImplTest {
    
    @Autowired(required=true)
    SalesNoteRepository salesNotes;
    
    @Autowired(required=true)
    UserDao userDao;
    
    @Autowired(required=true)
    PartDao partDao;
    
    @Autowired(required=true)
    InterchangeDao interchangeDao;
    
    @Autowired(required=true)
    PartTypeDao partTypeDao;
    
    @Autowired(required=true)
    ManufacturerDao manufacturerDao;
    
    @Autowired(required=true)
    ApplicationContext spring;
        
    final String testPartNumber = "TestSalesNoteQueryTest";
    
    List<User> userList;
    List<Part> partList;
    
    @Before
    public void setUp() {
        
        // Blow away all the sales notes
        salesNotes.deleteAll();
        salesNotes.flush();
        assertEquals("Expected 0 sales notes.", 0, salesNotes.count());
        
        // Delete the test part if it exists
        try {
            Part testPart = partDao.findByPartNumber(testPartNumber);
            if (testPart != null) {
                interchangeDao.delete(testPart.getInterchange().getId());
                partDao.delete(testPart.getId());
                partDao.flush();
            }
        } catch (NoResultException e) {}
        
        // Preload some dummy entities
        userList = userDao.findAll(0,10);
        partList = partDao.findAll(0, 10);
    }

    @Test(expected = Exception.class)
    @Transactional
    public void testSearch_stateRequired() {
        System.out.println("search");
        SalesNoteSearchRequest request = new SalesNoteSearchRequest(null, null);
        salesNotes.search(request);
    }
    
    @Test
    public void testSave() {
        System.out.println("save");
        
        // Dummy related entities
        User creator = userList.get(0);
        User updater = userList.get(1);
        Part expectedPart1 = partList.get(0);
        Part expectedPart2 = partList.get(1);
        
        // Create a note
        SalesNote expectedNote = new SalesNote();
        expectedNote.setState(SalesNoteState.draft);
        expectedNote.setComment("Draft note");
        expectedNote.setCreator(creator);
        expectedNote.setCreateDate(new Date());
        expectedNote.setUpdater(updater);
        expectedNote.setUpdateDate(new Date());
        
        // Primary Part
        expectedNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(expectedNote, expectedPart1),
                        new Date(), creator, new Date(), updater, true));
        
        // Related Part
        expectedNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(expectedNote, expectedPart2),
                        new Date(), creator, new Date(), updater, false));
        
        salesNotes.save(expectedNote);
        
        // Verify the note was saved
        List<SalesNote> actualNotes = salesNotes.findAll();
        assertEquals(1, actualNotes.size());
        
        // Verify the note
        SalesNote actualNote = actualNotes.get(0);
        assertEquals(expectedNote.getId(), actualNote.getId());
        assertEquals(expectedNote.getComment(), actualNote.getComment());
        assertEquals(expectedNote.getCreateDate(), actualNote.getCreateDate());
        assertEquals(expectedNote.getCreator(), actualNote.getCreator());
        assertEquals(expectedNote.getUpdater(), actualNote.getUpdater());
        assertEquals(expectedNote.getState(), actualNote.getState());
        
        // Verify the SalesNotePart relationship
        List<SalesNotePart> actualSnpSet = actualNote.getParts();
        assertEquals(2, actualSnpSet.size());
        
        // Verify the primary part
        SalesNotePart actualSNP1 = Iterables.get(actualSnpSet, 0);
        assertEquals(expectedPart1, actualSNP1.getPart());
        assertTrue(actualSNP1.isPrimary());
        
        // Verify the related part
        SalesNotePart actualSNP2 = Iterables.get(actualSnpSet, 1);
        assertEquals(expectedPart2, actualSNP2.getPart());
        assertFalse(actualSNP2.isPrimary());
    }

    @Test
    public void testSearch_stateFilter() {
        System.out.println("search: State filter");
        
        User creator = userList.get(0);
        User updater = userList.get(1);
        
        Part part = partList.get(0);
        
        // Create a draft note
        SalesNote draftNote = new SalesNote();
        draftNote.setState(SalesNoteState.draft);
        draftNote.setComment("Draft note");
        draftNote.setCreator(creator);
        draftNote.setCreateDate(new Date());
        draftNote.setUpdater(updater);
        draftNote.setUpdateDate(new Date());
        draftNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(draftNote, part),
                        new Date(), creator, new Date(), updater, true));
        
        salesNotes.save(draftNote);
        
        // Create a submitted note
        SalesNote submittedNote = new SalesNote();
        submittedNote.setState(SalesNoteState.submitted);
        submittedNote.setComment("Submitted note");
        submittedNote.setCreator(creator);
        submittedNote.setCreateDate(new Date());
        submittedNote.setUpdater(updater);
        submittedNote.setUpdateDate(new Date());
        submittedNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(submittedNote, part),
                        new Date(), creator, new Date(), updater, true));
        
        salesNotes.save(submittedNote);
        
        salesNotes.flush();
        
        // A few different searches on state
        SalesNoteSearchResponse draftSearchResults = salesNotes.search(new SalesNoteSearchRequest(null, null, SalesNoteState.draft));
        assertEquals(1, draftSearchResults.getTotal());
        
        SalesNoteSearchResponse submittedSearchResults = salesNotes.search(new SalesNoteSearchRequest(null, null, SalesNoteState.submitted));
        assertEquals(1, submittedSearchResults.getTotal());
        
        SalesNoteSearchResponse approvedSearchResults = salesNotes.search(new SalesNoteSearchRequest(null, null, SalesNoteState.approved));
        assertEquals(0, approvedSearchResults.getTotal());
        
        // Verify the results of a search
        assertEquals(1, draftSearchResults.getTotal());
        
        List<SalesNote> resultList = draftSearchResults.getContent();
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(1, resultList.get(0).getParts().size());
    }

    @Test
    public void testSearch_primaryPartId() {
        System.out.println("search: Primary Part ID");
        
        User creator = userList.get(0);
        User updater = userList.get(1);
        
        Part primaryPart = partList.get(0);
        Part relatedPart = partList.get(1);
        
        // Create a primary note
        SalesNote primaryNote = new SalesNote();
        primaryNote.setState(SalesNoteState.draft);
        primaryNote.setComment("Primary note");
        primaryNote.setCreator(creator);
        primaryNote.setCreateDate(new Date());
        primaryNote.setUpdater(updater);
        primaryNote.setUpdateDate(new Date());
        primaryNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(primaryNote, primaryPart),
                        new Date(), creator, new Date(), updater, true));
        
        salesNotes.save(primaryNote);
        
        // Create a related note
        SalesNote submittedNote = new SalesNote();
        submittedNote.setState(SalesNoteState.draft);
        submittedNote.setComment("Related note");
        submittedNote.setCreator(creator);
        submittedNote.setCreateDate(new Date());
        submittedNote.setUpdater(updater);
        submittedNote.setUpdateDate(new Date());
        submittedNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(submittedNote, relatedPart),
                        new Date(), creator, new Date(), updater, false));
        
        salesNotes.save(submittedNote);
        
        salesNotes.flush();
        assertEquals(2, salesNotes.count());
        
        // Search for the primary part
        SalesNoteSearchResponse draftSearchResults = salesNotes.search(new SalesNoteSearchRequest(primaryPart.getId(), null, SalesNoteState.values()));
        assertEquals(1, draftSearchResults.getTotal());
        
        SalesNote actualNote = draftSearchResults.getContent().get(0);
        SalesNotePart actualSNP = Iterables.get(actualNote.getParts(),0);
        assertTrue(actualSNP.isPrimary());
        assertEquals(primaryPart, actualSNP.getPart());
    }

    @Test
    public void testSearch_queryIncludes() {
        System.out.println("search: Includes");
        
        User creator = userList.get(0);
        User updater = userList.get(1);
        
        Part testPart = spring.getBean(Part.class);
        testPart.setPartType(partTypeDao.findOne(10L)); // Minor component
        testPart.setName("Part for query test");
        testPart.setManufacturerPartNumber(testPartNumber);
        testPart.setManufacturer(manufacturerDao.TI());
        testPart.setDescription("Some description");
        
        partDao.persist(testPart);
        
        // Create a note for the part
        SalesNote primaryNote = new SalesNote();
        primaryNote.setState(SalesNoteState.draft);
        primaryNote.setComment("Primary note");
        primaryNote.setCreator(creator);
        primaryNote.setCreateDate(new Date());
        primaryNote.setUpdater(updater);
        primaryNote.setUpdateDate(new Date());
        primaryNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(primaryNote, testPart),
                        new Date(), creator, new Date(), updater, true));
        
        salesNotes.save(primaryNote);
        
        // Create a related note
        SalesNote relatedNote = new SalesNote();
        relatedNote.setState(SalesNoteState.draft);
        relatedNote.setComment("Related note");
        relatedNote.setCreator(creator);
        relatedNote.setCreateDate(new Date());
        relatedNote.setUpdater(updater);
        relatedNote.setUpdateDate(new Date());
        relatedNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(relatedNote, testPart),
                        new Date(), creator, new Date(), updater, false));
        
        salesNotes.save(relatedNote);
        
        salesNotes.flush();
        assertEquals(2, salesNotes.count());
        
        // Search for primary and related by manufacturer part number
        SalesNoteSearchRequest primaryAndRelatedByPartNumber = new SalesNoteSearchRequest(null, testPart.getManufacturerPartNumber(), SalesNoteState.draft);
        primaryAndRelatedByPartNumber.setIncludePrimary(true);
        primaryAndRelatedByPartNumber.setIncludeRelated(true);
        
        SalesNoteSearchResponse primaryAndRelatedByPartNumberResult = salesNotes.search(primaryAndRelatedByPartNumber);
        
        assertEquals(2, primaryAndRelatedByPartNumberResult.getTotal());
        
        // Search for primary by manufacturer part number
        SalesNoteSearchRequest primaryByPartNumber = new SalesNoteSearchRequest(null, testPart.getManufacturerPartNumber(), SalesNoteState.draft);
        primaryByPartNumber.setIncludePrimary(true);
        primaryByPartNumber.setIncludeRelated(false);
        
        SalesNoteSearchResponse primaryByPartNumberResult = salesNotes.search(primaryByPartNumber);
        
        assertEquals(1, primaryByPartNumberResult.getTotal());
        assertEquals(primaryNote, primaryByPartNumberResult.getContent().get(0));
        
        // Search for related by manufacturer part number
        SalesNoteSearchRequest relatedByPartNumber = new SalesNoteSearchRequest(null, testPart.getManufacturerPartNumber(), SalesNoteState.draft);
        relatedByPartNumber.setIncludePrimary(false);
        relatedByPartNumber.setIncludeRelated(true);
        
        SalesNoteSearchResponse relatedByPartNumberResult = salesNotes.search(relatedByPartNumber);
        
        assertEquals(1, relatedByPartNumberResult.getTotal());
        assertEquals(relatedNote, relatedByPartNumberResult.getContent().get(0));
        
        // Search with nothing included
        SalesNoteSearchRequest nothingIncluded = new SalesNoteSearchRequest(null, testPart.getManufacturerPartNumber(), SalesNoteState.draft);
        
        nothingIncluded.setIncludePrimary(false);
        nothingIncluded.setIncludeRelated(false);
        
        SalesNoteSearchResponse nothingIncludedResponse = salesNotes.search(nothingIncluded);
        assertEquals(0, nothingIncludedResponse.getTotal());
    }

    @Test
    public void testSearch_querySalesNoteComment() {
        System.out.println("search: Sales Note Comment");
        
        User creator = userList.get(0);
        User updater = userList.get(1);
        
        Part testPart = spring.getBean(Part.class);
        testPart.setPartType(partTypeDao.findOne(10L)); // Minor component
        testPart.setName("Part for query test");
        testPart.setManufacturerPartNumber(testPartNumber);
        testPart.setManufacturer(manufacturerDao.TI());
        testPart.setDescription("Some description");
        
        partDao.persist(testPart);
        
        // Create a note for the part
        SalesNote primaryNote = new SalesNote();
        primaryNote.setState(SalesNoteState.draft);
        primaryNote.setComment("TESTPRIMARY");
        primaryNote.setCreator(creator);
        primaryNote.setCreateDate(new Date());
        primaryNote.setUpdater(updater);
        primaryNote.setUpdateDate(new Date());
        primaryNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(primaryNote, testPart),
                        new Date(), creator, new Date(), updater, true));
        
        salesNotes.save(primaryNote);
        
        // Create a related note
        SalesNote relatedNote = new SalesNote();
        relatedNote.setState(SalesNoteState.draft);
        relatedNote.setComment("TESTRELATED");
        relatedNote.setCreator(creator);
        relatedNote.setCreateDate(new Date());
        relatedNote.setUpdater(updater);
        relatedNote.setUpdateDate(new Date());
        relatedNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(relatedNote, testPart),
                        new Date(), creator, new Date(), updater, false));
        
        salesNotes.save(relatedNote);
        
        salesNotes.flush();
        assertEquals(2, salesNotes.count());
        
        // Search for primary
        SalesNoteSearchRequest primaryRequest = new SalesNoteSearchRequest(null, primaryNote.getComment(), primaryNote.getState());
        SalesNoteSearchResponse primaryResponse = salesNotes.search(primaryRequest);
        assertEquals(1, primaryResponse.getTotal());
        assertEquals(primaryNote, primaryResponse.getContent().get(0));
        
        SalesNoteSearchRequest relatedRequest = new SalesNoteSearchRequest(null, relatedNote.getComment(), relatedNote.getState());
        SalesNoteSearchResponse relatedResponse = salesNotes.search(relatedRequest);
        assertEquals(1, relatedResponse.getTotal());
        assertEquals(relatedNote, relatedResponse.getContent().get(0));
    }

    @Test
    public void testSearch_queryPartNumber() {
        System.out.println("search: Part Number");
        
        User creator = userList.get(0);
        User updater = userList.get(1);
        
        Part testPart = spring.getBean(Part.class);
        testPart.setPartType(partTypeDao.findOne(10L)); // Minor component
        testPart.setName("Part for query test");
        testPart.setManufacturerPartNumber(testPartNumber);
        testPart.setManufacturer(manufacturerDao.TI());
        testPart.setDescription("Some description");
        
        partDao.persist(testPart);
        
        // Create a note for the part
        SalesNote primaryNote = new SalesNote();
        primaryNote.setState(SalesNoteState.draft);
        primaryNote.setComment("TESTPRIMARY");
        primaryNote.setCreator(creator);
        primaryNote.setCreateDate(new Date());
        primaryNote.setUpdater(updater);
        primaryNote.setUpdateDate(new Date());
        primaryNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(primaryNote, testPart),
                        new Date(), creator, new Date(), updater, true));
        
        salesNotes.save(primaryNote);
        
        // Create a related note
        SalesNote relatedNote = new SalesNote();
        relatedNote.setState(SalesNoteState.draft);
        relatedNote.setComment("TESTRELATED");
        relatedNote.setCreator(creator);
        relatedNote.setCreateDate(new Date());
        relatedNote.setUpdater(updater);
        relatedNote.setUpdateDate(new Date());
        relatedNote.getParts().add(
                new SalesNotePart(
                        new SalesNotePartId(relatedNote, partList.get(0)),
                        new Date(), creator, new Date(), updater, false));
        
        salesNotes.save(relatedNote);
        salesNotes.flush();
        assertEquals(2, salesNotes.count());
        
        // Search for primary
        SalesNoteSearchRequest primaryRequest = new SalesNoteSearchRequest(null, testPartNumber, primaryNote.getState());
        SalesNoteSearchResponse primaryResponse = salesNotes.search(primaryRequest);
        assertEquals(1, primaryResponse.getTotal());
        assertEquals(primaryNote, primaryResponse.getContent().get(0));
    }

    @Test
    @Transactional
    public void testSearch_empty() {
        System.out.println("search");
        SalesNoteSearchRequest request = new SalesNoteSearchRequest(null, null, SalesNoteState.approved);
        SalesNoteSearchResponse result = salesNotes.search(request);
        assertEquals(0, result.getTotal());
    }
    
}
