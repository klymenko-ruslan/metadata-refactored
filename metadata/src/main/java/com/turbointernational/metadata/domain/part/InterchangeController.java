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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Add picked part to interchange group of this part and remove picked part from its existing interchange.
     *
     * @param part
     * @param pickedPart
     */
    private void mergePickedAloneToPart(Part part, Part pickedPart) {
        Interchange ppi = pickedPart.getInterchange();
        pickedPart.setInterchange(null);
        partDao.merge(pickedPart);
        partDao.flush(); // important
        if (ppi != null) {
            interchangeDao.remove(ppi);
        }
        Interchange interchange = part.getInterchange();
        if (interchange == null) {
            interchange = new Interchange();
            interchangeDao.persist(interchange);
            part.setInterchange(interchange);
            partDao.merge(part);
        }
        pickedPart.setInterchange(interchange);
        Set<Part> parts = interchange.getParts();
        parts.add(pickedPart);
        interchangeDao.merge(interchange);
        partDao.merge(part);
        changelogDao.log("Added picked part " + pickedPart.getId() + " as interchange to the part " + part.getId());
    }

    /**
     * Add this part to interchange group of the picked part and remove this part from its existing interchange.
     *
     * @param part
     * @param pickedPart
     */
    private void mergePartAloneToPicked(Part part, Part pickedPart) {
        Interchange pi = part.getInterchange();
        part.setInterchange(null);
        partDao.merge(part);
        partDao.flush(); // important
        if (pi != null) {
            interchangeDao.remove(pi);
        }
        Interchange interchange = pickedPart.getInterchange();
        if (interchange == null) {
            interchange = new Interchange();
            interchangeDao.persist(interchange);
            pickedPart.setInterchange(interchange);
            partDao.merge(pickedPart);
        }
        part.setInterchange(interchange);
        Set<Part> parts = interchange.getParts();
        parts.add(part);
        interchangeDao.merge(interchange);
        changelogDao.log("Added part " + part.getId() + " as interchange to the picked part " + pickedPart.getId());
    }

    /**
     * Add the picked part and all its existing interchange parts to interchange group of this part.
     *
     * @param part
     * @param pickedPart
     */
    private void mergePickedAllToPart(Part part, Part pickedPart) {
        Interchange ppi = pickedPart.getInterchange();
        if (ppi != null) {
            // Remove current interchanges.  They will be copied later (see below).
            pickedPart.setInterchange(null);
            partDao.merge(pickedPart);
            partDao.flush(); // important
        }
        Interchange interchange = part.getInterchange();
        if (interchange == null) {
            interchange = new Interchange();
            interchangeDao.persist(interchange);
            part.setInterchange(interchange);
            partDao.merge(part);
        }
        Set<Part> iParts = interchange.getParts();
        pickedPart.setInterchange(interchange);
        partDao.merge(pickedPart);
        partDao.flush(); // important
        iParts.add(pickedPart);
        if (ppi != null) {
            for (Part p : ppi.getParts()) {
                p.setInterchange(interchange);
                iParts.add(p);
            }
        }
        interchangeDao.merge(interchange);
        pickedPart.setInterchange(null);
        partDao.merge(pickedPart);
        changelogDao.log("Added picked part " + pickedPart.getId() + " and all its interchanges to the part " + part.getId());
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
        Part pickedPart = partDao.findOne(pickedPartId);
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

        /*
        // Get the part and it's original interchange
        Part iPart = partDao.findOne(partId);

        // Update the interchange
        Interchange newInterchange = interchangeDao.findOne(id);
        if (newInterchange == null) {
            throw new IllegalArgumentException("Could not find interchange " + id);
        }

        // Save the part into the new interchange
        newInterchange.getParts().add(iPart);
        iPart.setInterchange(newInterchange);
        interchangeDao.merge(newInterchange);

        Interchange iPartInterchange = iPart.getInterchange();
        if (iPartInterchange != null) {
            Interchange oldInterchange = interchangeDao.findOne(iPartInterchange.getId()); // Hacky fix to multiple session problem
            // Update the old interchange
            if (oldInterchange != null) {
                oldInterchange.getParts().remove(iPart);
                // Delete the interchange if it's empty, otherwise save it
                if (oldInterchange.getParts().isEmpty()) {
                    interchangeDao.remove(oldInterchange);
                } else {
                    interchangeDao.merge(oldInterchange);
                }
            }
        }

        interchangeDao.flush();
        
        // Update the changelog
        changelogDao.log("Added part " + partId + " to interchange " + id, "");
            
        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
*/
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
        
        // Get the part and interchange
        Part iPart = partDao.findOne(partId);
        Interchange interchange = iPart.getInterchange();
        
        // Stop now if the part doesn't have an interchange
        if (interchange == null) {
            return;
        }
        
        Long interchangeId = interchange.getId();

        // Remove the part from the interchange
        interchange.getParts().remove(iPart);

        // Delete the interchange if it's empty, otherwise save it
        if (interchange.getParts().isEmpty()) {
            interchangeDao.remove(interchange);
        } else {
            interchangeDao.merge(interchange);
        }
        
        // Remove the interchange from the part
        iPart.setInterchange(null);
        partDao.merge(iPart);
        partDao.flush();
        
        // Update the changelog
        changelogDao.log("Deleted " + partId + " from interchange " + interchangeId, "");
            
        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
    }
    
    
}
