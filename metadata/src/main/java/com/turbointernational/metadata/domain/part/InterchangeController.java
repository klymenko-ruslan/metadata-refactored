package com.turbointernational.metadata.domain.part;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.web.View;
import java.security.Principal;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/metadata/interchange")
@Controller
public class InterchangeController {

    private static final Logger log = Logger.getLogger(InterchangeController.class.toString());
    
    @Autowired
    ChangelogDao changelogDao;
    
    @Autowired
    PartDao partDao;
    
    @Autowired
    InterchangeDao interchangeDao;
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_INTERCHANGE")
    public ResponseEntity<String> create(Principal principal, @RequestBody String json) throws Exception {
        
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
        
        return new ResponseEntity<String>("ok", headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value="/{interchangeId}/part/{partId}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_INTERCHANGE")
    public void update(Principal principal, @PathVariable("interchangeId") Long id, @PathVariable("partId") Long partId) throws Exception {
        
        // Get the part and it's original interchange
        Part iPart = partDao.findOne(partId);
        
        Interchange oldInterchange = interchangeDao.findOne(iPart.getInterchange().getId()); // Hacky fix to multiple session problem
        
        // Update the interchange
        Interchange newInterchange = interchangeDao.findOne(id);
        if (newInterchange == null) {
            throw new IllegalArgumentException("Could not find interchange " + id);
        }
        
        // Save the part into the new interchange
        newInterchange.getParts().add(iPart);
        iPart.setInterchange(newInterchange);
        interchangeDao.merge(newInterchange);
        
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
        
        interchangeDao.flush();
        
        // Update the changelog
        changelogDao.log("Added part " + partId + " to interchange " + id, "");
            
        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
    }
    
    @ResponseBody
    @Secured("ROLE_INTERCHANGE")
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
    public void delete(Principal principal, @PathVariable("partId") Long partId) throws Exception {
        
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
