package com.turbointernational.metadata.domain.car;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

@RequestMapping("/metadata/application")
@Controller
public class ApplicationController {

    private static final Logger log = Logger.getLogger(ApplicationController.class.toString());

    @Transactional
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> getApplicatation(@PathVariable("id") long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        CarModelEngineYear application = CarModelEngineYear.findById(id);
        String json = null;
        if (application != null) {
            json = application.toJson();
        }
        return new ResponseEntity<String>(json, headers, HttpStatus.OK);
    }

}
