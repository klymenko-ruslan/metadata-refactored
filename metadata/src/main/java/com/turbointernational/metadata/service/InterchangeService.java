package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.INTERCHANGE;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART0;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART1;
import static com.turbointernational.metadata.service.GraphDbService.checkSuccess;
import static com.turbointernational.metadata.util.FormatUtils.formatInterchange;
import static com.turbointernational.metadata.util.FormatUtils.formatPart;
import static com.turbointernational.metadata.web.dto.Part.SORTBY_PARTNUM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.service.GraphDbService.GetInterchangeResponse;
import com.turbointernational.metadata.service.GraphDbService.MigrateInterchangeResponse;
import com.turbointernational.metadata.web.dto.Interchange;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-11-02.
 */
@Service
public class InterchangeService {

    @Autowired
    private PartDao partDao;

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @Qualifier("transactionManagerMetadata")
    @Autowired
    private PlatformTransactionManager txManagerMetadata;

    @Autowired
    private PartChangeService partChangeService;

    @Autowired
    private GraphDbService graphDbService;

    @Autowired
    private DtoMapperService dtoMapperService;

    @Autowired
    private SearchService searchService;

    /**
     * Find an interchangeable by its ID.
     *
     * @param id
     * @return the found entity instance or null if the entity does not exist
     */
    @Transactional
    public Interchange findById(Long id) {
        GetInterchangeResponse response = graphDbService.findInterchangeById(id);
        Interchange retVal = dtoMapperService.map(response, Interchange.class);
        Arrays.sort(retVal.getParts(), SORTBY_PARTNUM);
        return retVal;
    }

    public Interchange findForPart(Part part) {
        return findForPart(part.getId());
    }

    /**
     * Get interchange for a part.
     *
     * Returned interchange contains all parts except this one.
     *
     * @param partId
     * @return return interchange for a part.
     */
    public Interchange findForPart(Long partId) {
        GetInterchangeResponse response = graphDbService.findInterchangeForPart(partId);
        Interchange retVal =  dtoMapperService.map(response, Interchange.class);
        com.turbointernational.metadata.web.dto.Part[] parts = retVal.getParts();
        if (parts != null && parts.length > 0) {
            // Ticket (Redmine) #277. Sorting of interchangeable parts.
            Arrays.sort(parts, (p0, p1) -> p0.getPartNumber().compareTo(p1.getPartNumber()));
        }
        return retVal;
    }

    public void initInterchange(Part part) {
        if (part != null) {
            Interchange interchange = findForPart(part);
            part.setInterchange(interchange);
        }
    }

    /**
     * Set 'asInterchange' as interchange for the 'part'.
     *
     * Group of interchanges for the 'part' is removed and 'part' is added to a
     * group of the 'asInterchange' so 'asInterchange' becomes interchange for
     * the 'part'.
     *
     * @param part
     * @param asInterchange
     */
    public void create(Long partId, Long asInterchangePartId) throws IOException {
        MigrateInterchangeResponse response = graphDbService.moveGroupToOtherInterchangeableGroup(partId,
                asInterchangePartId);
        checkSuccess(response);
        Long headerId = response.getNewHeaderId();
        Interchange interchange = findById(headerId);
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(asInterchangePartId, ChangelogPart.Role.PART1));
        changelogService.log(INTERCHANGE, "Created interchange: " + formatInterchange(interchange) + ".",
                interchange.toJson(), relatedParts);
        partChangeService.changedInterchange(asInterchangePartId, headerId);
    }

    /**
     * Remove part from interchangeable group.
     *
     * Every part must belong to an interchangeable group. When part is removed
     * from an interchangeable group a new group should be created and this part
     * added to that group.
     *
     * @param partId
     *            ID of a part form remove.
     */
    @Transactional
    public void leaveInterchangeableGroup(Long partId) throws IOException {
        Part part = partDao.findOne(partId);
        Interchange interchange = findForPart(partId);
        part.setInterchange(interchange);
        leaveInterchangeableGroup(part);
    }

    public void leaveInterchangeableGroup(Part part) throws IOException {
        Long partId = part.getId();
        Interchange interchange = part.getInterchange();
        if (interchange == null) {
            throw new NullPointerException("Member 'interchange' must be initialized in the part.");
        }
        MigrateInterchangeResponse response = graphDbService.leaveInterchangeableGroup(partId);
        checkSuccess(response);
        // Update 'part.interchange' in an ElasticSearch index.
        Set<Long> interchangeablePartsToReindex = new HashSet<>();
        interchangeablePartsToReindex.add(partId);
        com.turbointernational.metadata.web.dto.Part[] otherParts = interchange.getParts();
        if (otherParts != null) {
            for (com.turbointernational.metadata.web.dto.Part p : otherParts) {
                interchangeablePartsToReindex.add(p.getPartId());
            }
        }
        reindexIntechangeableParts(interchangeablePartsToReindex);
        // Update the changelog.
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(partId, PART0));
        Long newInterchangeId = response.getNewHeaderId();
        changelogService.log(INTERCHANGE, "The part [" + formatPart(part) + "] migrated from interchange group ["
                + interchange.getId() + "] to [" + newInterchangeId + "].", relatedParts);
        partChangeService.changedInterchange(interchange.getId(), newInterchangeId);
    }

    /**
     * Add picked part to interchange group of this part and remove picked part
     * from its existing interchange.
     *
     * @param partId
     * @param pickedPartId
     */
    @Transactional
    public void mergePickedAloneToPart(HttpServletRequest httpRequest, Long partId, Long pickedPartId,
            Long[] sourcesIds, Integer[] ratings, String description, Long[] attachIds) throws IOException {
        Set<Long> interchangeablePartsToReindex = new HashSet<>();
        collectInterchangeablePartIdsFor(partId, interchangeablePartsToReindex);
        collectInterchangeablePartIdsFor(pickedPartId, interchangeablePartsToReindex);
        MigrateInterchangeResponse response = graphDbService.movePartToOtherInterchangeGroup(pickedPartId, partId);
        checkSuccess(response);
        collectInterchangeablePartIdsFor(partId, interchangeablePartsToReindex);
        collectInterchangeablePartIdsFor(pickedPartId, interchangeablePartsToReindex);
        reindexIntechangeableParts(interchangeablePartsToReindex);
        Long oldInterchangeHeaderId = response.getOldHeaderId();
        Long newInterchangeHeaderId = response.getNewHeaderId();
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Part pickedPart = partDao.findOne(pickedPartId);
        Part part = partDao.findOne(partId);
        Changelog changelog = changelogService.log(INTERCHANGE,
                "Part " + formatPart(pickedPart) + " added to the part " + formatPart(part) + " as interchange.",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        partChangeService.changedInterchange(oldInterchangeHeaderId, newInterchangeHeaderId);
    }

    /**
     * Add this part to interchange group of the picked part and remove this
     * part from its existing interchange.
     *
     * @param partId
     * @param pickedPartId
     */
    @Transactional
    public void mergePartAloneToPicked(HttpServletRequest httpRequest, long partId, long pickedPartId,
            Long[] sourcesIds, Integer[] ratings, String description, Long[] attachIds) throws IOException {
        Set<Long> interchangeablePartsToReindex = new HashSet<>();
        collectInterchangeablePartIdsFor(partId, interchangeablePartsToReindex);
        collectInterchangeablePartIdsFor(pickedPartId, interchangeablePartsToReindex);
        MigrateInterchangeResponse response = graphDbService.movePartToOtherInterchangeGroup(partId, pickedPartId);
        checkSuccess(response);
        collectInterchangeablePartIdsFor(partId, interchangeablePartsToReindex);
        collectInterchangeablePartIdsFor(pickedPartId, interchangeablePartsToReindex);
        reindexIntechangeableParts(interchangeablePartsToReindex);
        Long oldInterchangeHeaderId = response.getOldHeaderId();
        Long newInterchangeHeaderId = response.getNewHeaderId();
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Part pickedPart = partDao.findOne(pickedPartId);
        Part part = partDao.findOne(partId);
        Changelog changelog = changelogService.log(INTERCHANGE,
                "Part " + formatPart(part) + " added to the part " + formatPart(pickedPart) + " as interchange.",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        partChangeService.changedInterchange(oldInterchangeHeaderId, newInterchangeHeaderId);
    }

    /**
     * Add the picked part and all its existing interchange parts to interchange
     * group of this part.
     *
     * @param partId
     * @param pickedPartId
     */
    @Transactional
    public void mergePickedAllToPart(HttpServletRequest httpRequest, Long partId, Long pickedPartId, Long[] sourcesIds,
            Integer[] ratings, String description, Long[] attachIds) throws IOException {
        Set<Long> interchangeablePartsToReindex = new HashSet<>();
        collectInterchangeablePartIdsFor(partId, interchangeablePartsToReindex);
        collectInterchangeablePartIdsFor(pickedPartId, interchangeablePartsToReindex);
        MigrateInterchangeResponse response = graphDbService.moveGroupToOtherInterchangeableGroup(pickedPartId,
                partId);
        checkSuccess(response);
        collectInterchangeablePartIdsFor(partId, interchangeablePartsToReindex);
        collectInterchangeablePartIdsFor(pickedPartId, interchangeablePartsToReindex);
        reindexIntechangeableParts(interchangeablePartsToReindex);
        Long oldInterchangeHeaderId = response.getOldHeaderId();
        Long newInterchangeHeaderId = response.getNewHeaderId();
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Part pickedPart = partDao.findOne(pickedPartId);
        Part part = partDao.findOne(partId);
        Changelog changelog = changelogService.log(INTERCHANGE, "Part " + formatPart(pickedPart)
                + " and all its interchanges added to the part " + formatPart(part) + " as interchanges.",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        partChangeService.changedInterchange(oldInterchangeHeaderId, newInterchangeHeaderId);
    }

    private void collectInterchangeablePartIdsFor(Long partId, Set<Long> result) {
        result.add(partId);
        Interchange interchange = findForPart(partId);
        com.turbointernational.metadata.web.dto.Part[] parts = interchange.getParts();
        if (parts != null && parts.length > 0) {
            for (com.turbointernational.metadata.web.dto.Part p : parts) {
                result.add(p.getPartId());
            }
        }
    }

    private void reindexIntechangeableParts(Set<Long> partIds) {
        if (partIds != null && !partIds.isEmpty()) {
            partIds.forEach(pid -> searchService.indexPart(pid));
        }
    }

}
