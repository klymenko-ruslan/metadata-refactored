package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.part.types.kit.KitType;
import com.turbointernational.metadata.dao.KitTypeDao;
import com.turbointernational.metadata.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-20.
 */
@Controller
@RequestMapping(value = {"/kittype", "/metadata/kittype"})
public class KitTypeController {

    @Autowired
    private KitTypeDao kitTypeDao;


    @RequestMapping(value = "/list", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public List<KitType> list() {
        return kitTypeDao.findAll();
    }

}
