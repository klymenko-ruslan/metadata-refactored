package com.turbointernational.metadata.web.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.ManufacturerType;
import com.turbointernational.metadata.service.ManufacturerTypeService;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Controller
@RequestMapping(value = { "/metadata/other/manufacturertype", "/other/manufacturertype" })
public class ManufacturerTypeController {

    @Autowired
    private ManufacturerTypeService manufacturerTypeService;

    @RequestMapping(value = "/all", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public List<ManufacturerType> all() {
        return manufacturerTypeService.getAll();
    }

}
