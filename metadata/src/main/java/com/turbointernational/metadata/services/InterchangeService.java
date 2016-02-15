package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.Interchange;
import com.turbointernational.metadata.domain.part.InterchangeDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by trunikov on 2/11/16.
 */
@Service
public class InterchangeService {

    @Autowired
    private PartDao partDao;

    @Autowired
    private InterchangeDao interchangeDao;

    @Autowired
    private ChangelogDao changelogDao;

    @Autowired
    private PlatformTransactionManager txManager;

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
     *
     * @param interchange
     */
    @Transactional
    public void create(Interchange interchange) {
        // Link it with the Hibernate parts
        Set<Part> canonicalParts = new HashSet<>();
        // Map the incoming part IDs to their canonical part
        for(Iterator<Part> it = interchange.getParts().iterator(); it.hasNext();) {
            Part interchangePart = it.next();
            Part canonicalPart = partDao.findOne(interchangePart.getId());
            if (canonicalPart.getInterchange() != null) {
                throw new IllegalArgumentException("Part " + interchangePart.getId() + " already has interchangeable parts.");
            }
            it.remove();
            canonicalParts.add(canonicalPart);
        }
        interchange.getParts().clear();
        interchange.getParts().addAll(canonicalParts);
        interchangeDao.persist(interchange);
        for (Part canonicalPart : canonicalParts) {
            canonicalPart.setInterchange(interchange);
            partDao.merge(canonicalPart);
        }
        interchangeDao.flush();
        // Update the changelog
        changelogDao.log("Created interchange: ", interchange.toJson());
        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
    }

    /**
     * Remove part from interchangeable group.
     * <p/>
     * Every part must belong to an interchangeable group.
     * When part is removed from an interchangeable group a new group should be created and
     * this part added to that group.
     *
     * @param partId ID of a part form remove.
     */
    @Transactional
    public void leaveInterchangeableGroup(long partId) {
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
        changelogDao.log("Deleted " + partId + " from interchange " + newInterchange.getId());
        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
    }

    /**
     * Add picked part to interchange group of this part and remove picked part from its existing interchange.
     *
     * @param partId
     * @param pickedPartId
     */
    @Transactional
    public void mergePickedAloneToPart(long partId, long pickedPartId) {
        Part part = partDao.findOne(partId);
        normalizePartInterchange(part);
        Part pickedPart = partDao.findOne(pickedPartId);
        normalizePartInterchange(pickedPart);
        movePartToOtherInterchangeGroup(pickedPart, part);
        changelogDao.log("Added picked part " + pickedPart.getId() + " as interchange to the part " + part.getId());
    }

    /**
     * Add this part to interchange group of the picked part and remove this part from its existing interchange.
     *
     * @param partId
     * @param pickedPartId
     */
    @Transactional
    public void mergePartAloneToPicked(long partId, long pickedPartId) {
        Part part = partDao.findOne(partId);
        normalizePartInterchange(part);
        Part pickedPart = partDao.findOne(pickedPartId);
        normalizePartInterchange(pickedPart);
        movePartToOtherInterchangeGroup(part, pickedPart);
        changelogDao.log("Added part " + part.getId() + " as interchange to the picked part " + pickedPart.getId());
    }

    /**
     * Add the picked part and all its existing interchange parts to interchange group of this part.
     *
     * @param partId
     * @param pickedPartId
     */
    @Transactional
    public void mergePickedAllToPart(long partId, long pickedPartId) {
        Part part = partDao.findOne(partId);
        normalizePartInterchange(part);
        Part pickedPart = partDao.findOne(pickedPartId);
        normalizePartInterchange(pickedPart);
        moveInterchangeableGroupToOtherGroup(pickedPart, part);
        changelogDao.log("Added picked part " + pickedPart.getId() + " and all its interchanges to the part " + part.getId());
    }

    /**
     * Move a part from its current interchange group to an interchange group of other part.
     *
     * @param scrPart the part which is being migrated
     * @param dstPart
     */
    private void movePartToOtherInterchangeGroup(Part scrPart, Part dstPart) {
        Interchange srcInterchange = scrPart.getInterchange();
        boolean alone = srcInterchange.isAlone();
        srcInterchange.getParts().remove(scrPart);
        if (alone) {
            // Delete current interchange group because empty groups are not allowed.
            interchangeDao.remove(srcInterchange); // Remove a record in the table 'interchange_header'.
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
     * @param scrPart a part which interchangeable group be joined to a destination group.
     * @param dstPart
     */
    private void moveInterchangeableGroupToOtherGroup(Part scrPart, Part dstPart) {
        Interchange srcInterchange = scrPart.getInterchange();
        Set<Part> srcGrp = srcInterchange.getParts();
        Interchange dstInterchange = dstPart.getInterchange();
        Set<Part> dstGrp = dstInterchange.getParts();
        for (Iterator<Part> srcPartsIter = srcGrp.iterator(); srcPartsIter.hasNext(); ) {
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
     * Every part should be assigned to an interchange record even if it is the only part in the interchange group.
     * <p/>
     * This method is executed in a separate transaction.
     * <p/>
     * See also #482, #484.
     *
     * @param part a part for normalization.
     * @return true when a new interchange group has been created.
     */
    private Boolean normalizePartInterchange(Part part) {
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // new transaction
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
