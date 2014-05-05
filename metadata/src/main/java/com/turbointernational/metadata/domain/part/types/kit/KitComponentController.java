package com.turbointernational.metadata.domain.part.types.kit;
import com.turbointernational.metadata.domain.changelog.Changelog;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.types.Kit;
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

@RequestMapping("/metadata/kit/{kitId}/component")
@Controller
public class KitComponentController {

    private static final Logger log = Logger.getLogger(KitComponentController.class.toString());
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_ALTER_PART")
    public ResponseEntity<String> create(@RequestBody String json) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Create the object
        KitComponent component = KitComponent.fromJson(json);
        
        // Link it with the Hibernate parts
        try {
            Kit kit = (Kit) Part.findPart(component.getKit().getId());
            Part part = Part.findPart(component.getPart().getId());
            
            component.setKit(kit);
            component.setPart(part);

            component.persist();
            
            kit.getComponents().add(component);
            kit.merge();
        
            // Update the changelog
            Changelog.log("Added kit common component.", component.toJson());
            
        } catch (NoResultException e) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<String>("ok", headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_ALTER_PART")
    public void update(@PathVariable("id") Long id, @RequestParam(required=true) Boolean exclude) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the item
        KitComponent component = KitComponent.find(id);
        
        // Update the changelog
        Changelog.log("Changed kit component mapping exclude to " + exclude, component.toJson());
        
        // Update
        component.setExclude(exclude);
        component.merge();
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ALTER_PART")
    public void delete(@PathVariable("id") Long id) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the object
        KitComponent component = KitComponent.find(id);
        
        // Update the changelog
        Changelog.log("Deleted kit common component mapping.", component.toJson());
        
        // Remove from the kit (orphan removal will delete the component)
        component.getKit().getComponents().remove(component);
        component.getKit().merge();
    }
    
    
}
