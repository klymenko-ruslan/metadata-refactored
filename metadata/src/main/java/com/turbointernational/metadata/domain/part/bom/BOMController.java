package com.turbointernational.metadata.domain.part.bom;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.part.bom.dto.CreateBomItemRequest;
import com.turbointernational.metadata.web.View;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;

@RequestMapping("/metadata/bom")
@Controller
public class BOMController {
    
    @Autowired
    ChangelogDao changelogDao;
    
    @Autowired
    PartDao partDao;
    
    @Autowired
    BOMItemDao bomItemDao;

    private static final Logger log = LoggerFactory.getLogger(BOMController.class);

    @ResponseBody
    @Transactional
    @Secured("ROLE_BOM")
    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody CreateBomItemRequest request) throws Exception {
        
        // Create a new BOM item
        Part child = partDao.findOne(request.getChildPartId());
        Part parent = partDao.findOne(request.getParentPartId());

        if (child.getManufacturer().getId() != parent.getManufacturer().getId()) {
            throw new AssertionError("Child part must have the same manufacturer as the Parent part.");
        }
        
        BOMItem item = new BOMItem();
        item.setParent(parent);
        item.setChild(child);
        item.setQuantity(request.getQuantity());
        parent.getBom().add(item);
        
        bomItemDao.persist(item);
        bomItemDao.flush();

        // Update the changelog
        changelogDao.log("Added bom item.", item.toJson());

        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
    }
    
    @RequestMapping(value = "/byParentPart/{id}", method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public List<BOMItem> getByParentId(@PathVariable("id") long id) throws Exception {
        return bomItemDao.findByParentId(id);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_BOM")
    public void update(@PathVariable("id") Long id, @RequestParam(required=true) int quantity) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the item
        BOMItem item = bomItemDao.findOne(id);
        
        // Update the changelog
        changelogDao.log("Changed BOM item quantity to " + quantity, item.toJson());
        
        // Update
        item.setQuantity(quantity);
        bomItemDao.merge(item);
        bomItemDao.flush();
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_BOM")
    public void delete(@PathVariable("id") Long id) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the object
        BOMItem item = bomItemDao.findOne(id);
        
        // Update the changelog
        changelogDao.log("Deleted BOM item.", item.toJson());
        
        // Remove the BOM Item from the parent
        item.getParent().getBom().remove(item);
        partDao.merge(item.getParent());
        
        // Delete it
        Part childPart = item.getChild();
        bomItemDao.remove(item);
        bomItemDao.flush();
            
        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
    }
    
    
}
