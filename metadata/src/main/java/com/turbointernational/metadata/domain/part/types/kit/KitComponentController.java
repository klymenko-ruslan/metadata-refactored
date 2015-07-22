package com.turbointernational.metadata.domain.part.types.kit;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.web.View;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    
    @Autowired
    ChangelogDao changelogDao;
    
    @Autowired
    PartDao partDao;
    
    @Autowired
    KitComponentDao kitComponentDao;
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ALTER_PART")
    @ResponseBody
    @JsonView(View.Detail.class)
    public KitComponent create(@RequestBody KitComponent component) throws Exception {
        kitComponentDao.persist(component);

        // Update the changelog
        changelogDao.log("Added kit common component to kit" + component.getKit().getId(), component.getPart().getId());
        
        return component;
    }
    
    @Transactional
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ALTER_PART")
    @ResponseBody
    @JsonView(View.Detail.class)
    public List<KitComponent> listByKit(@PathVariable("kitId") long kitId) throws Exception {
        return kitComponentDao.findByKitId(kitId);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_ALTER_PART")
    public void update(@PathVariable("id") Long id, @RequestParam(required=true) Boolean exclude) throws Exception {
        
        // Get the item
        KitComponent component = kitComponentDao.findOne(id);
        
        // Update the changelog
        changelogDao.log("Changed kit component mapping exclude to " + exclude, component.toJson());
        
        // Update
        component.setExclude(exclude);
        kitComponentDao.merge(component);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ALTER_PART")
    public void delete(@PathVariable("id") Long id) throws Exception {
        
        // Get the object
        KitComponent component = kitComponentDao.findOne(id);
        
        // Remove from the component
        component.getKit().getComponents().remove(component);
        kitComponentDao.remove(component);
        
        // Update the changelog
        changelogDao.log("Deleted kit common component mapping.", component.toJson());
    }
    
    
}
