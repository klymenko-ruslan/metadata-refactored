package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CarYear;
import com.turbointernational.metadata.dao.CarYearDao;
import com.turbointernational.metadata.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by trunikov on 12/20/15.
 */
@RequestMapping("/metadata/application")
@Controller
public class CarYearController {

    @Autowired
    private CarYearDao carYearDao;

    @Transactional
    @RequestMapping(value = "/caryear", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Summary.class)
    @ResponseBody
    @Secured("ROLE_READ")
    public CarYear findByName(@RequestParam("name") String name) {
        return carYearDao.findByName(name);
    }

}
