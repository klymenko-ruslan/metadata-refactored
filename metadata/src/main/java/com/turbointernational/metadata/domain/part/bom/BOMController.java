package com.turbointernational.metadata.domain.part.bom;
import com.turbointernational.metadata.domain.changelog.Changelog;
import com.turbointernational.metadata.domain.part.Part;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/metadata/bom")
@Controller
public class BOMController {

    private static final Logger log = Logger.getLogger(BOMController.class.toString());
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_BOM")
    public ResponseEntity<String> create(@RequestBody String json) throws Exception {
        
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
            item.flush();
        
            // Update the changelog
            Changelog.log("Added bom item.", item.toJson());
            
            // TODO: Only change what we need to rather than rebuilding everything
            Part.rebuildBomAncestry();
            
        } catch (NoResultException e) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<String>("ok", headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_BOM")
    public void update(@PathVariable("id") Long id, @RequestParam(required=true) int quantity) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the item
        BOMItem item = BOMItem.findBOMItem(id);
        
        // Update the changelog
        Changelog.log("Changed BOM item quantity to " + quantity, item.toJson());
        
        // Update
        item.setQuantity(quantity);
        item.merge();
        item.flush();
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_BOM")
    public void delete(@PathVariable("id") Long id) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the object
        BOMItem item = BOMItem.findBOMItem(id);
        
        // Update the changelog
        Changelog.log("Deleted BOM item.", item.toJson());
        
        // Remove the BOM Item from the parent
        item.getParent().getBom().remove(item);
        item.getParent().merge();
        
        // Delete it
        Part childPart = item.getChild();
        item.remove();
        item.flush();
            
        // TODO: Only change what we need to rather than rebuilding everything
        Part.rebuildBomAncestry();
    }
    
    
}
