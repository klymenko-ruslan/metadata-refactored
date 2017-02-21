package com.turbointernational.metadata.web.controller;

import com.turbointernational.metadata.service.TurboCarModelEngineYearService;
import com.turbointernational.metadata.service.TurboCarModelEngineYearService.AddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping("/metadata/part")
@RestController
public class TurboCarModelEngineYearController {

    @Autowired
    private TurboCarModelEngineYearService paService;

    @RequestMapping(value = "{partId}/application", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<String> getApplications(@PathVariable("partId") Long partId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        String json = paService.getApplications(partId);
        return new ResponseEntity<>(json, headers, OK);
    }

    @Transactional
    @RequestMapping(value = "/{partId}/application", method = POST)
    @Secured("ROLE_APPLICATION_CRUD")
    public ResponseEntity<String> add(HttpServletRequest httpRequest, @PathVariable("partId") Long partId,
                                      @RequestBody AddRequest request) throws Exception {
        paService.add(httpRequest, partId, request);
        return new ResponseEntity<>("", OK);
    }

    @Transactional
    @RequestMapping(value = "/{partId}/application/{applicationId}", method = DELETE)
    @Secured("ROLE_APPLICATION_CRUD")
    public ResponseEntity<String> delete(@PathVariable("partId") Long partId,
                                         @PathVariable("applicationId") Long applicationId) throws Exception {
        paService.delete(partId, applicationId);
        return new ResponseEntity<>("", OK);
    }

}
