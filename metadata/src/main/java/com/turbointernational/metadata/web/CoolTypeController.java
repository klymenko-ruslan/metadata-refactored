package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.other.CoolType;
import com.turbointernational.metadata.domain.other.CoolTypeDao;
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
@RequestMapping(value = {"/cooltype", "/metadata/cooltype"})
public class CoolTypeController {

    @Autowired
    private CoolTypeDao coolTypeDao;

    public CoolTypeController() {
    }

    @RequestMapping(value = "/list", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public List<CoolType> list() {
        return coolTypeDao.findAll();
    }

}
