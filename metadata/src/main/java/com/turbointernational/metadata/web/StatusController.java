
package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.dto.Status;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/status/")
public class StatusController {
    
    @Transactional
    @RequestMapping(value="/all")
    @Secured("ROLE_READ")
    public ResponseEntity<String> status() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        Status status = new Status();
        
        if (Part.getBomRebuildStart() != null) {
            status.setBomAncestryRebuilding(true);
        }
        
        return new ResponseEntity<String>(status.toJson(), headers, HttpStatus.OK);
    }
}
