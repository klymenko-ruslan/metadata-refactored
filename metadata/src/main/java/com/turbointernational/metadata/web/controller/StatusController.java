
package com.turbointernational.metadata.web.controller;

import com.turbointernational.metadata.service.BOMService;
import com.turbointernational.metadata.web.dto.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.OK;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/status/")
public class StatusController {

    @Autowired
    private BOMService bomService;
    
    @Transactional
    @RequestMapping(value="/all")
    @Secured("ROLE_READ")
    public ResponseEntity<String> status() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        Status status = new Status();
        
        if (bomService.getBomRebuildStart() != null) {
            status.setBomRebuilding(true);
        }
        
        return new ResponseEntity<>(status.toJson(), headers, OK);
    }
}
