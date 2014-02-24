package com.turbointernational.metadata.domain.part.bom;
import com.turbointernational.metadata.domain.changelog.Changelog;
import com.turbointernational.metadata.domain.part.*;
import java.security.Principal;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/metadata/bom")
@Controller
public class BOMController {

    private static final Logger log = Logger.getLogger(BOMController.class.toString());
    
    @Transactional
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @Secured("ROLE_BOM")
    public ResponseEntity<String> create(Principal principal, @RequestBody String json) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Create the object
        BOMItem item = BOMItem.fromJsonToBOMItem(json);
        
        // Link it with the Hibernate parts
        try {
            Part child = Part.findPart(item.getChild().getId());
            Part parent = Part.findPart(item.getParent().getId());
            
            item.setChild(child);
            item.setParent(parent);

            item.persist();
            parent.getBom().add(item);
            parent.merge();

            parent.indexTurbos();
            parent.updateIndex();
        } catch (NoResultException e) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        // Update the changelog
        Changelog.log(principal, "Added bom item: ", json);
        
        return new ResponseEntity<String>("ok", headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @Secured("ROLE_BOM")
    public ResponseEntity<String> update(Principal principal, @RequestBody String json) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Parse the new item
        BOMItem item = BOMItem.fromJsonToBOMItem(json);
        
        item.merge();
        
        // Update the changelog
        Changelog.log(principal, "Changed BOM item.", json);
        
        return new ResponseEntity<String>("ok", headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Secured("ROLE_BOM")
    public ResponseEntity<String> delete(Principal principal, @PathVariable("id") Long id) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Create the object
        BOMItem item = BOMItem.findBOMItem(id);
        
        // Remove the BOM Item from the parent
        item.getParent().getBom().remove(item);
        item.getParent().merge();
        
        // Delete it
        item.remove();
        
        // Link it with the Hibernate parts
        try {
            Part child = Part.findPart(item.getChild().getId());
            Part parent = Part.findPart(item.getParent().getId());
            
            item.setChild(child);
            item.setParent(parent);

            item.persist();
            parent.getBom().add(item);
            parent.merge();

            parent.indexTurbos();
            parent.updateIndex();
        } catch (NoResultException e) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        // Update the changelog
        Changelog.log(principal, "Deleted BOM item: ", item.toJson());
        
        return new ResponseEntity<String>("ok", headers, HttpStatus.OK);
    }
    
    
}
