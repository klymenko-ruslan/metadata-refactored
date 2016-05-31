package com.turbointernational.metadata.web;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.domain.type.PartTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = {"/parttype", "/metadata/parttype"})
public class PartTypeController {

    @Autowired
    private PartTypeDao partTypeDao;

    @Secured("ROLE_READ")
    @ResponseBody
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    public PartType getPartType(@PathVariable("id") Long id) {
        PartType partType = partTypeDao.findOne(id);
        return partType;
    }

    @RequestMapping(value = "list2", method = GET)
    @ResponseBody
    @JsonView(View.Detail.class)
    //@PreAuthorize("hasRole('ROLE_READ') or hasIpAddress('127.0.0.1/32')")
    @Secured("ROLE_READ")
    public List<PartType> getAllPartTypes() {
        List<PartType> retVal = partTypeDao.findAll();
        return retVal;
    }

}
