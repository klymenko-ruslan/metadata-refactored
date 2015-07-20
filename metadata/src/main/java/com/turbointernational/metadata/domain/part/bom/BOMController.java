package com.turbointernational.metadata.domain.part.bom;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.part.bom.dto.CreateBomItemRequest;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/metadata/bom")
@Controller
public class BOMController {
    
    @Autowired
    ChangelogDao changelogDao;
    
    @Autowired
    PartDao partDao;
    
    @Autowired
    BOMItemDao bomItemDao;

    private static final Logger log = Logger.getLogger(BOMController.class.toString());
    
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
        
        BOMItem item = new BOMItem();
        item.setParent(parent);
        item.setChild(child);
        item.setQuantity(request.getQuantity());
        item.getParent().getBom().add(item);
        
        bomItemDao.persist(item);
        partDao.merge(parent);
        bomItemDao.flush();

        // Update the changelog
        changelogDao.log("Added bom item.", item.toJson());

        // TODO: Only change what we need to rather than rebuilding everything
        partDao.rebuildBomDescendancy();
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
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
