package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.OtherService;
import com.turbointernational.metadata.service.OtherService.GenerateApplicationsRequest;
import com.turbointernational.metadata.service.OtherService.GenerateApplicationsResponse;
import com.turbointernational.metadata.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-09-26.
 */
@Controller
@RequestMapping(value = {"/other", "/metadata/other"})
public class OtherController {

    @Autowired
    private OtherService otherService;

    @Transactional
    @RequestMapping(value = "/appsturbos/generate", method = POST,
            consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_APPLICATION_CRUD")
    public GenerateApplicationsResponse generateTurboApplications(@RequestBody GenerateApplicationsRequest request) {
        return otherService.generateTurboApplications(request);
    }

}
