package com.turbointernational.metadata.domain.part.bom;

import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.*;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    @Secured("ROLE_BOM_ALT")
    @ResponseBody
    @RequestMapping(value="{altPartId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(
            @PathVariable("bomId") long bomId,
            @PathVariable("altPartId") long altPartId,
            @RequestParam(value="headerId", required=false) Long headerId
    ) throws Exception {
        
        // Get the BOM Item and alternate part
        BOMItem bomItem = bomItemDao.findOne(bomId);
        Part altPart = partDao.findOne(altPartId);
        
        // Get or create a header
        BOMAlternativeHeader header;
        if (headerId == null) {
            header = new BOMAlternativeHeader();
            
            header.setName(bomItem.getParent().getId().toString());
            header.setDescription(altPart.getId().toString());
        } else {
            header = bomAltHeaderDao.findOne(headerId);
        }
        
        // Create the bom alternative
        BOMAlternative bomAlt = new BOMAlternative();
        bomAlt.setHeader(header);
        bomAlt.setBomItem(bomItem);
        bomAlt.setPart(altPart);
        bomItem.getAlternatives().add(bomAlt);
        
        // Save the alternate
        bomAltDao.persist(bomAlt);
        
        // Update the changelog
        changelogDao.log("Added bom alternative.", bomAlt.toJson());
    }
    
    @Transactional
    @RequestMapping(value = "/{altItemId}", method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
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
