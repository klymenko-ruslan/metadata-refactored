package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.KitComponentDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.part.types.kit.KitComponent;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.util.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.KIT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping("/metadata/kit/{kitId}/component")
@Controller
public class KitComponentController {

    private static final Logger log = LoggerFactory.getLogger(KitComponentController.class);
    
    @Autowired
    ChangelogService changelogService;
    
    @Autowired
    PartDao partDao;
    
    @Autowired
    KitComponentDao kitComponentDao;
    
    @Transactional
    @RequestMapping(method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @Secured("ROLE_ALTER_PART")
    @ResponseBody
    @JsonView(View.Detail.class)
    public KitComponent create(@RequestBody KitComponent component) throws Exception {
        kitComponentDao.persist(component);

        // Update the changelog
        changelogService.log(KIT, "Created kit common component [" + component.getKit().getId() + "].",
                component, null);
        
        return component;
    }
    
    @Transactional
    @RequestMapping(method = GET,
            produces = APPLICATION_JSON_VALUE)
    @Secured("ROLE_ALTER_PART")
    @ResponseBody
    @JsonView(View.Detail.class)
    public List<KitComponent> listByKit(@PathVariable("kitId") long kitId) throws Exception {
        return kitComponentDao.findByKitId(kitId);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = PUT)
    @ResponseBody
    @Secured("ROLE_ALTER_PART")
    public void update(@PathVariable("id") Long id, @RequestParam Boolean exclude) throws Exception {
        
        // Get the item
        KitComponent component = kitComponentDao.findOne(id);
        
        // Update the changelog
        changelogService.log(KIT, "Changed kit component mapping exclude to " + exclude, component.toJson(),
                null);
        
        // Update
        component.setExclude(exclude);
        kitComponentDao.merge(component);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_ALTER_PART")
    public void delete(@PathVariable("id") Long id) throws Exception {
        
        // Get the object
        KitComponent component = kitComponentDao.findOne(id);
        
        // Remove from the component
        // TODO: line below commented out during migration to critical dimensions
        //component.getKit().getComponents().remove(component);
        kitComponentDao.remove(component);
        
        // Update the changelog
        changelogService.log(KIT, "Deleted kit component [" + id + "].", component, null);
    }
    
    
}
