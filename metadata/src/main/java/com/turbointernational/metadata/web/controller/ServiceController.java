package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.Service;
import com.turbointernational.metadata.service.ServiceService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-15.
 */
@Controller
@RequestMapping(value = {"/service", "/metadata/service"})
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @RequestMapping(path = "getall", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    public List<Service> getAll() {
        return serviceService.getAll();
    }

    @RequestMapping(path = "list", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    public Page<Service> filter(@RequestParam(name = "sortProperty", required = false) String sortProperty,
                                @RequestParam(name = "sortOrder", required = false) String sortOrder,
                                @RequestParam(name = "offset", required = false) Integer offset,
                                @RequestParam(name = "limit", required = false) Integer limit) {
        return serviceService.filter(sortProperty, sortOrder, offset, limit);
    }

    @RequestMapping(path = "{id}", method = PUT)
    @ResponseBody
    @Transactional
    public void setChangelogSourceRequired(@PathVariable("id") Long serviceId,
                                           @RequestParam("required") Boolean required) {
        serviceService.setChangelogSourceRequired(serviceId, required);
    }

}
