package com.turbointernational.metadata.domain.part;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.web.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Set;

@RequestMapping("/metadata/interchange")
@Controller
public class InterchangeController {

    private final static Logger log = LoggerFactory.getLogger(InterchangeController.class);

    private final static int MERGE_OPT_PICKED_ALONE_TO_PART = 1; // Add picked part to interchange group of this part and remove picked part from its existing interchange
    private final static int MERGE_OPT_PART_ALONE_TO_PICKED = 2; // Add this part to interchange group of the picked part and remove this part from its existing interchange
    private final static int MERGE_OPT_PICKED_ALL_TO_PART = 3; // Add the picked part and all its existing interchange parts to interchange group of this part

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    ChangelogDao changelogDao;
    
    @Autowired
    PartDao partDao;
    
    @Autowired
    InterchangeDao interchangeDao;
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_INTERCHANGE")
    public ResponseEntity<String> create(@RequestBody String json) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Create the object
        Interchange interchange = Interchange.fromJsonToInterchange(json);
        
        // Link it with the Hibernate parts
        Set<Part> canonicalParts = Sets.newTreeSet();
        
        // Map the incoming part IDs to their canonical part
        Iterator<Part> it = interchange.getParts().iterator();
        while (it.hasNext()) {
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
        
        for (Part canonicalPart: canonicalParts) {
            canonicalPart.setInterchange(interchange);
            partDao.merge(canonicalPart);
        }
        interchangeDao.flush();
        
        // Update the changelog
        changelogDao.log("Created interchange: ", json);
            
        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
        
        return new ResponseEntity<>("ok", headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value="/{partId}/part/{pickedPartId}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_INTERCHANGE")
    public void update(@PathVariable("partId") long partId, @PathVariable("pickedPartId") long pickedPartId,
                       @RequestParam(name = "mergeChoice", required = true) int mergeChoice,
                       HttpServletResponse response) throws Exception {
        log.debug("partId: {}, pickedPartid: {}, mergeChoice: {}", partId, pickedPartId, mergeChoice);
        if (mergeChoice != MERGE_OPT_PICKED_ALONE_TO_PART
                && mergeChoice != MERGE_OPT_PART_ALONE_TO_PICKED
                && mergeChoice != MERGE_OPT_PICKED_ALL_TO_PART) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        Part part = partDao.findOne(partId);
        normalizePartInterchange(part);
        Part pickedPart = partDao.findOne(pickedPartId);
        normalizePartInterchange(pickedPart);
        switch (mergeChoice) {
            case MERGE_OPT_PICKED_ALONE_TO_PART:
                mergePickedAloneToPart(part, pickedPart);
                break;
            case MERGE_OPT_PART_ALONE_TO_PICKED:
                mergePartAloneToPicked(part, pickedPart);
                break;
            case MERGE_OPT_PICKED_ALL_TO_PART:
                mergePickedAllToPart(part, pickedPart);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
        }
        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
    }
    
    @ResponseBody
    // Commented as fix for #536. @Secured("ROLE_INTERCHANGE")
    @Secured("ROLE_READ") // Ticket #536.
    @JsonView({View.SummaryWithInterchangeParts.class})
    @RequestMapping(value="{id}", method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Interchange get(@PathVariable("id") long interchangeId) {
        return interchangeDao.findOne(interchangeId);
    }
    
    @Transactional
    @RequestMapping(value = "/part/{partId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_INTERCHANGE")
    public void delete(@PathVariable("partId") Long partId) throws Exception {
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
     * Assign interchange to the part if it is absent.
     *
     * Every part should be assigned to an interchange record even if it is the only part in the interchange group.
     *
     * This method is executed in a separate transaction.
     *
     * See also #482, #484.
     *
     * @param part a part for normalization.
     * @return true when a new interchange group has been created.
     *
     */
    private Boolean normalizePartInterchange(Part part) {
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // new transaction
        Boolean modified  = tt.execute(ts -> {
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
        for(Iterator<Part> srcPartsIter = srcGrp.iterator(); srcPartsIter.hasNext();) {
            Part srcPart = srcPartsIter.next();
            srcPartsIter.remove();
            srcPart.setInterchange(dstInterchange);
            dstGrp.add(srcPart);
        }
        interchangeDao.remove(srcInterchange); // Delete interchange group.
        interchangeDao.merge(dstInterchange);
    }

    /**
     * Add picked part to interchange group of this part and remove picked part from its existing interchange.
     *
     * @param part
     * @param pickedPart
     */
    private void mergePickedAloneToPart(Part part, Part pickedPart) {
        movePartToOtherInterchangeGroup(pickedPart, part);
        changelogDao.log("Added picked part " + pickedPart.getId() + " as interchange to the part " + part.getId());
    }

    /**
     * Add this part to interchange group of the picked part and remove this part from its existing interchange.
     *
     * @param part
     * @param pickedPart
     */
    private void mergePartAloneToPicked(Part part, Part pickedPart) {
        movePartToOtherInterchangeGroup(part, pickedPart);
        changelogDao.log("Added part " + part.getId() + " as interchange to the picked part " + pickedPart.getId());
    }

    /**
     * Add the picked part and all its existing interchange parts to interchange group of this part.
     *
     * @param part
     * @param pickedPart
     */
    private void mergePickedAllToPart(Part part, Part pickedPart) {
        moveInterchangeableGroupToOtherGroup(pickedPart, part);
        changelogDao.log("Added picked part " + pickedPart.getId() + " and all its interchanges to the part " + part.getId());
    }

}
