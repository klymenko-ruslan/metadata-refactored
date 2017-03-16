package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.INTERCHANGE;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART0;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.turbointernational.metadata.dao.InterchangeDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.part.Interchange;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.util.FormatUtils;

/**
 * Created by trunikov on 2/11/16.
 */
@Service
public class InterchangeService {

    @Autowired
    private BOMService bomService;

    @Autowired
    private PartDao partDao;

    @Autowired
    private InterchangeDao interchangeDao;

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @Qualifier("transactionManagerMetadata")
    @Autowired
    private PlatformTransactionManager txManagerMetadata;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Find an interchangeable by its ID.
     *
     * @param id
     * @return the found entity instance or null if the entity does not exist
     */
    @Transactional
    public Interchange findById(long id) {
        return interchangeDao.findOne(id);
    }

    /**
     * Persists interchangeable in a storage.
     */
    @Transactional
    public void create(HttpServletRequest httpRequest, List<Long> partIds, Long[] sourcesIds, Integer[] ratings,
            String description) {
        // Link it with the Hibernate parts
        Set<Part> canonicalParts = new HashSet<>();
        // Map the incoming part IDs to their canonical part
        for (Long partId : partIds) {
            Part canonicalPart = partDao.findOne(partId);
            if (canonicalPart.getInterchange() != null) {
                throw new IllegalArgumentException(
                        "Part " + FormatUtils.formatPart(canonicalPart) + " already has interchangeable parts.");
            }
            canonicalParts.add(canonicalPart);
        }
        Interchange interchange = new Interchange();
        interchange.getParts().addAll(canonicalParts);
        interchangeDao.persist(interchange);
        List<RelatedPart> relatedParts = new ArrayList<>(canonicalParts.size());
        for (Part canonicalPart : canonicalParts) {
            canonicalPart.setInterchange(interchange);
            partDao.merge(canonicalPart);
            relatedParts.add(new RelatedPart(canonicalPart.getId(), null));
        }
        interchangeDao.flush();
        // Update the changelog.
        Changelog changelog = changelogService.log(INTERCHANGE,
                "Created interchange: " + FormatUtils.formatInterchange(interchange) + ".", interchange.toJson(),
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description);
        bomService.rebuildBomDescendancyForParts(partIds, true);
    }

    public void create(Part part, Part asInterchange) {
        Interchange interchange = asInterchange.getInterchange();
        if (interchange == null) {
            interchange = part.getInterchange();
        }
        interchange.getParts().add(asInterchange);
        asInterchange.setInterchange(interchange);
        part.setInterchange(interchange);
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(part.getId(), PART0));
        relatedParts.add(new RelatedPart(asInterchange.getId(), PART1));
        changelogService.log(INTERCHANGE, "Created interchange: " + FormatUtils.formatInterchange(interchange) + ".",
                interchange.toJson(), relatedParts);
        bomService.rebuildBomDescendancyForParts(Arrays.asList(part.getId(), asInterchange.getId()), true);
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
    public void leaveInterchangeableGroup(Long partId) {
        Part part = partDao.findOne(partId);
        normalizePartInterchange(part);
        Interchange interchange = part.getInterchange();
        if (interchange.isAlone()) {
            return;
        }
        interchange.getParts().remove(part);
        interchangeDao.merge(interchange);
        // Create a new interchange group.
        // Any part must belong to an interchange group (see #589, #482).
        Interchange newInterchange = new Interchange();
        interchangeDao.persist(newInterchange);
        part.setInterchange(newInterchange);
        newInterchange.getParts().add(part);
        interchangeDao.merge(newInterchange);
        // Update the changelog
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(partId, PART0));
        changelogService.log(INTERCHANGE, "The part " + FormatUtils.formatPart(part)
                + " migrated from interchange group [" + interchange.getId() + "] to [" + newInterchange.getId() + "].",
                relatedParts);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForParts(interchange.getParts().iterator(), true);
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
            Long[] sourcesIds, Integer[] ratings, String description) {
        Part part = partDao.findOne(partId);
        normalizePartInterchange(part);
        Part pickedPart = partDao.findOne(pickedPartId);
        normalizePartInterchange(pickedPart);
        movePartToOtherInterchangeGroup(pickedPart, part);
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Changelog changelog = changelogService.log(INTERCHANGE, "Part " + FormatUtils.formatPart(pickedPart)
                + " added to the part " + FormatUtils.formatPart(part) + " as interchange.", relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForPart(pickedPartId, true);
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
            Long[] sourcesIds, Integer[] ratings, String description) {
        Part part = partDao.findOne(partId);
        normalizePartInterchange(part);
        Part pickedPart = partDao.findOne(pickedPartId);
        normalizePartInterchange(pickedPart);
        movePartToOtherInterchangeGroup(part, pickedPart);
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Changelog changelog = changelogService.log(INTERCHANGE, "Part " + FormatUtils.formatPart(part)
                + " added to the part " + FormatUtils.formatPart(pickedPart) + " as interchange.", relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForPart(pickedPartId, true);
    }

    /**
     * Add the picked part and all its existing interchange parts to interchange
     * group of this part.
     *
     * @param partId
     * @param pickedPartId
     */
    @Transactional
    public void mergePickedAllToPart(HttpServletRequest httpRequest, long partId, long pickedPartId, Long[] sourcesIds,
            Integer[] ratings, String description) {
        Part part = partDao.findOne(partId);
        normalizePartInterchange(part);
        Part pickedPart = partDao.findOne(pickedPartId);
        normalizePartInterchange(pickedPart);
        moveInterchangeableGroupToOtherGroup(pickedPart, part);
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(pickedPartId, PART1));
        Changelog changelog = changelogService.log(INTERCHANGE, "Part " + FormatUtils.formatPart(pickedPart)
                + " and all its interchanges added to the part " + FormatUtils.formatPart(part) + " as interchanges.",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description);
        bomService.rebuildBomDescendancyForPart(partId, true);
        bomService.rebuildBomDescendancyForPart(pickedPartId, true);
    }

    /**
     * Move a part from its current interchange group to an interchange group of
     * other part.
     *
     * @param scrPart
     *            the part which is being migrated
     * @param dstPart
     */
    private void movePartToOtherInterchangeGroup(Part scrPart, Part dstPart) {
        Interchange srcInterchange = scrPart.getInterchange();
        boolean alone = srcInterchange.isAlone();
        srcInterchange.getParts().remove(scrPart);
        if (alone) {
            // Delete current interchange group because empty groups are not
            // allowed.
            interchangeDao.remove(srcInterchange); // Remove a record in the
                                                   // table
                                                   // 'interchange_header'.
        } else {
            // Remove srcPart part from its current interchangeable group.
            interchangeDao.merge(srcInterchange);
        }
        // Add srcPart to an interchangeable group of the destPart part.
        Interchange dstInterchange = dstPart.getInterchange();
        scrPart.setInterchange(dstInterchange);
        dstInterchange.getParts().add(scrPart);
        interchangeDao.merge(dstInterchange);
    }

    /**
     * Move parts from one interchangeable groups to other one.
     *
     * @param scrPart
     *            a part which interchangeable group be joined to a destination
     *            group.
     * @param dstPart
     */
    private void moveInterchangeableGroupToOtherGroup(Part scrPart, Part dstPart) {
        Interchange srcInterchange = scrPart.getInterchange();
        Set<Part> srcGrp = srcInterchange.getParts();
        Interchange dstInterchange = dstPart.getInterchange();
        Set<Part> dstGrp = dstInterchange.getParts();
        for (Iterator<Part> srcPartsIter = srcGrp.iterator(); srcPartsIter.hasNext();) {
            Part srcPart = srcPartsIter.next();
            srcPartsIter.remove();
            srcPart.setInterchange(dstInterchange);
            dstGrp.add(srcPart);
        }
        interchangeDao.remove(srcInterchange); // Delete interchange group.
        interchangeDao.merge(dstInterchange);
    }

    /**
     * Assign interchange to the part if it is absent.
     * <p/>
     * Every part should be assigned to an interchange record even if it is the
     * only part in the interchange group.
     * <p/>
     * This method is executed in a separate transaction.
     * <p/>
     * See also #482, #484.
     *
     * @param part
     *            a part for normalization.
     * @return true when a new interchange group has been created.
     */
    private Boolean normalizePartInterchange(Part part) {
        TransactionTemplate tt = new TransactionTemplate(txManagerMetadata);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // new
                                                                                   // transaction
        Boolean modified = tt.execute(ts -> {
            Interchange interchange = part.getInterchange();
            if (interchange == null) {
                interchange = new Interchange();
                interchangeDao.persist(interchange);
                part.setInterchange(interchange);
                partDao.merge(part);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        });
        return modified;
    }

}
