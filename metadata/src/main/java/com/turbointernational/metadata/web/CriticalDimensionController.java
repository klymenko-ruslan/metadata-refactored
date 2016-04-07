package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.services.CriticalDimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by dmytro.trunykov@zorallabs.com on 06.04.16.
 */
@Controller
@RequestMapping(value = {"/criticaldimension", "/metadata/criticaldimension"})
public class CriticalDimensionController {

    @Autowired
    private CriticalDimensionService criticalDimensionService;

    @Secured("ROLE_READ")
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/part/{partId}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CriticalDimension> findForThePart(@PathVariable("partId") long partId) {
        return criticalDimensionService.findForThePart(partId);
    }

}
