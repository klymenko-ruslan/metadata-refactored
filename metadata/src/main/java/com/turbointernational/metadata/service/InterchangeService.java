package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.INTERCHANGE;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART0;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART1;
import static com.turbointernational.metadata.service.ArangoDbConnectorService.checkSuccess;
import static com.turbointernational.metadata.util.FormatUtils.formatInterchange;
import static com.turbointernational.metadata.util.FormatUtils.formatPart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.ArangoDbConnectorService.GetInterchangeResponse;
import com.turbointernational.metadata.service.ArangoDbConnectorService.MigrateInterchangeResponse;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
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
    private ArangoDbConnectorService arangoDbConnector;

    /**
     * Find an interchangeable by its ID.
     *
     * @param id
     * @return the found entity instance or null if the entity does not exist
     */
    @Transactional
    public Interchange findById(Long id) {
        GetInterchangeResponse response = arangoDbConnector.findInterchangeById(id);
        return Interchange.from(response);
    }

    public Interchange findForPart(Part part) {
        return findForPart(part.getId());
    }

    public Interchange findForPart(Long partId) {
        GetInterchangeResponse response = arangoDbConnector.findInterchangeForPart(partId);
        return Interchange.from(response);
    }

    public void initInterchange(Part part) {
        Interchange interchange = findForPart(part);
        part.setInterchange(interchange);
    }

    /**
     * Persists interchangeable in a storage.
     */
    @Transactional
    @Deprecated
    public void create(HttpServletRequest httpRequest, List<Long> partIds, Long[] sourcesIds, Integer[] ratings,
            String description, Long[] attachIds) throws IOException {
        /*
         * // Link it with the Hibernate parts Set<Part> canonicalParts = new
         * HashSet<>(); // Map the incoming part IDs to their canonical part for
         * (Long partId : partIds) { Part canonicalPart =
         * partDao.findOne(partId); if (canonicalPart.getInterchange() != null)
         * { throw new IllegalArgumentException( "Part " +
         * formatPart(canonicalPart) + " already has interchangeable parts."); }
         * canonicalParts.add(canonicalPart); } Interchange interchange = new
         * Interchange(); interchange.getParts().addAll(canonicalParts);
         * interchangeDao.persist(interchange); List<RelatedPart> relatedParts =
         * new ArrayList<>(canonicalParts.size()); for (Part canonicalPart :
         * canonicalParts) { canonicalPart.setInterchange(interchange);
         * partDao.merge(canonicalPart); relatedParts.add(new
         * RelatedPart(canonicalPart.getId(), null)); } interchangeDao.flush();
         * // Update the changelog. Changelog changelog =
         * changelogService.log(INTERCHANGE, "Created interchange: " +
         * formatInterchange(interchange) + ".", interchange.toJson(),
         * relatedParts); changelogSourceService.link(httpRequest, changelog,
         * sourcesIds, ratings, description, attachIds);
         * partChangeService.changedInterchange(interchange.getId(), null);
         */
        throw new NotImplementedException();
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
        MigrateInterchangeResponse response = arangoDbConnector.moveGroupToOtherInterchangeableGroup(partId,
                asInterchangePartId);
        checkSuccess(response);
        Long headerId = response.getNewHeaderId();
        Interchange interchange = findById(headerId);
        List<RelatedPart> relatedParts = new ArrayList<>(1);
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
        MigrateInterchangeResponse response = arangoDbConnector.leaveInterchangeableGroup(partId);
        checkSuccess(response);
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
    public void mergePickedAloneToPart(HttpServletRequest httpRequest, long partId, long pickedPartId,
            Long[] sourcesIds, Integer[] ratings, String description, Long[] attachIds) throws IOException {
        MigrateInterchangeResponse response = arangoDbConnector.movePartToOtherInterchangeGroup(pickedPartId, partId);
        checkSuccess(response);
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
        MigrateInterchangeResponse response = arangoDbConnector.movePartToOtherInterchangeGroup(partId, pickedPartId);
        checkSuccess(response);
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
        MigrateInterchangeResponse response = arangoDbConnector.moveGroupToOtherInterchangeableGroup(pickedPartId,
                partId);
        checkSuccess(response);
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

}
