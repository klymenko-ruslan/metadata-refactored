package com.turbointernational.metadata.domain.part.bom;

import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.*;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/metadata/bom/{bomId}/alt")
@Controller
public class BOMAlternativeController {
    
    @Autowired
    ChangelogDao changelogDao;

    @Autowired
    PartDao partDao;

    @Autowired
    BOMItemDao bomItemDao;
    
    @Autowired
    BOMAlternativeDao bomAltDao;
    
    @Autowired
    BOMAlternativeHeaderDao bomAltHeaderDao;
    
    private static final Logger log = Logger.getLogger(BOMAlternativeController.class.toString());
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_BOM_ALT")
    public ResponseEntity<String> create(
            @PathVariable("bomId") Long bomId,
            @RequestBody String json) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the BOM Item
        BOMItem item = bomItemDao.findOne(bomId);
        
        // Create the bom alternative
        BOMAlternative bomAlt = BOMAlternative.fromJsonToBOMAlternative(json);
        bomAlt.setBomItem(item);
        item.getAlternatives().add(bomAlt);
        
        // Refresh the part
        Part altPart = partDao.findOne(bomAlt.getPart().getId());
        bomAlt.setPart(altPart);
        
        // Create a new header if we need to
        if (bomAlt.getHeader() == null) {
            BOMAlternativeHeader header = new BOMAlternativeHeader();
            header.setName(item.getParent().getId().toString());
            header.setDescription(bomAlt.getPart().getId().toString());
            bomAltHeaderDao.persist(header);
            
            
            // Add it to the alternate
            bomAlt.setHeader(header);
        } else {
            BOMAlternativeHeader header = bomAltHeaderDao.findOne(bomAlt.getHeader().getId());
            bomAlt.setHeader(header);
        }
        
        // Save the alternate and item
        bomAltDao.persist(bomAlt);
        bomItemDao.merge(item);
        
        // Update the changelog
        changelogDao.log("Added bom alternative.", bomAlt.toJson());
        
        return new ResponseEntity<String>("ok", headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/{altItemId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_BOM_ALT")
    public void delete(@PathVariable("bomId") Long bomId, @PathVariable("altItemId") Long altId) throws Exception {
        
        // Get the BOM item and alternate
        BOMItem item = bomItemDao.findOne(bomId);
        BOMAlternative alt = bomAltDao.findOne(altId);
        
        // Update the changelog
        changelogDao.log("Deleted BOM alternative.", alt.toJson());
        
        // Remove the alternate item
        item.getAlternatives().remove(alt);
        partDao.merge(item.getParent());
        
        // Delete it
        bomAltDao.remove(alt);
    }
    
}
