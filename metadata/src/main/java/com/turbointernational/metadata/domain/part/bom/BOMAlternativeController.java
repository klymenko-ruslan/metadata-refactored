package com.turbointernational.metadata.domain.part.bom;

import com.turbointernational.metadata.domain.changelog.Changelog;
import com.turbointernational.metadata.domain.part.Part;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RequestMapping("/metadata/bom/{bomId}/alt")
@Controller
public class BOMAlternativeController {

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
        BOMItem item = BOMItem.findBOMItem(bomId);
        
        // Create the bom alternative
        BOMAlternative bomAlt = BOMAlternative.fromJsonToBOMAlternative(json);
        bomAlt.setBomItem(item);
        item.getAlternatives().add(bomAlt);
        
        // Refresh the part
        Part altPart = Part.findPart(bomAlt.getPart().getId());
        bomAlt.setPart(altPart);
        
        // Create a new header if we need to
        if (bomAlt.getHeader() == null) {
            BOMAlternativeHeader header = new BOMAlternativeHeader();
            header.setName(item.getParent().getId().toString());
            header.setDescription(bomAlt.getPart().getId().toString());
            header.persist();
            
            // Add it to the alternate
            bomAlt.setHeader(header);
        } else {
            BOMAlternativeHeader header = BOMAlternativeHeader.findBOMAlternativeHeader(bomAlt.getHeader().getId());
            bomAlt.setHeader(header);
        }
        
        // Save the alternate and item
        bomAlt.persist();
        item.merge();
        
        // Update the changelog
        Changelog.log("Added bom alternative.", bomAlt.toJson());
        
        return new ResponseEntity<String>("ok", headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/{altItemId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_BOM_ALT")
    public void delete(@PathVariable("bomId") Long bomId, @PathVariable("altItemId") Long altId) throws Exception {
        
        // Get the BOM item and alternate
        BOMItem item = BOMItem.findBOMItem(bomId);
        BOMAlternative alt = BOMAlternative.findBOMAlternative(altId);
        
        // Update the changelog
        Changelog.log("Deleted BOM alternative.", alt.toJson());
        
        // Remove the alternate item
        item.getAlternatives().remove(alt);
        item.getParent().merge();
        
        // Delete it
        alt.remove();
    }
    
}
