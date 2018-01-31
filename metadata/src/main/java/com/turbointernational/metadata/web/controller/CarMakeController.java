package com.turbointernational.metadata.web.controller;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.APPLICATIONS;
import static com.turbointernational.metadata.util.FormatUtils.formatCarMake;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.CarMakeDao;
import com.turbointernational.metadata.entity.CarMake;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.util.SerializationUtils;
import com.turbointernational.metadata.util.View;

/**
 * Created by trunikov on 14.12.15.
 */
@RequestMapping("/metadata/application")
@Controller
public class CarMakeController {

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private CarMakeDao carMakeDao;

    @Transactional
    @RequestMapping(value = "/carmake", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public CarMake findByName(@RequestParam("name") String name) {
        return carMakeDao.findCarMakeByName(name);
    }

    @Transactional
    @RequestMapping(value = "/carmakes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public List<CarMake> findAllOrderedByName() {
        return carMakeDao.findAllOrderedByName();
    }

    @Transactional
    @RequestMapping(value = "/carmake", method = POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public CarMake create(@RequestBody CarMake carMake) {
        carMakeDao.persist(carMake);
        changelogService.log(APPLICATIONS, "Created Car Make " + formatCarMake(carMake) + ".", null);
        return carMake;
    }

    @Transactional
    @RequestMapping(value = "/carmake/{id}", method = PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@PathVariable("id") long id, @RequestBody CarMake carMake) {
        CarMake original = carMakeDao.findOne(id);
        String jsonOriginal = original.toJson();
        CarMake updated = carMakeDao.merge(carMake);
        String jsonUpdated = updated.toJson();
        String json = SerializationUtils.update(jsonOriginal, jsonUpdated);
        changelogService.log(APPLICATIONS, "The Car Make " + formatCarMake(updated) + " has been updated.", json,
            null);
    }

    @Transactional
    @RequestMapping(value = "/carmake/{id}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carMakeDao.delete(id);
        changelogService.log(APPLICATIONS, "Removed Car Make " + formatCarMake(id) + ".", null);
    }

}
