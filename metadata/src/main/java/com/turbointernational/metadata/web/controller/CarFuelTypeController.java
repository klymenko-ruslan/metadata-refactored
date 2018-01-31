package com.turbointernational.metadata.web.controller;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.APPLICATIONS;
import static com.turbointernational.metadata.util.FormatUtils.formatCarFuelType;

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
import com.turbointernational.metadata.dao.CarFuelTypeDao;
import com.turbointernational.metadata.entity.CarFuelType;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.util.FormatUtils;
import com.turbointernational.metadata.util.SerializationUtils;
import com.turbointernational.metadata.util.View;

/**
 * Created by trunikov on 12/15/15.
 */
@RequestMapping("/metadata/application")
@Controller
public class CarFuelTypeController {

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private CarFuelTypeDao carFuelTypeDao;

    @Transactional
    @RequestMapping(value = "/carfueltype", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public CarFuelType findByName(@RequestParam("name") String name) {
        return carFuelTypeDao.findCarFuelTypeByName(name);
    }

    @Transactional
    @RequestMapping(value = "/carfueltypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public List<CarFuelType> findAllOrderedByName() {
        return carFuelTypeDao.findAllOrderedByName();
    }

    @Transactional
    @RequestMapping(value = "/carfueltype", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public long create(@RequestBody CarFuelType carFuelType) {
        carFuelTypeDao.persist(carFuelType);
        changelogService.log(APPLICATIONS, "Created Car Fuel Type " + formatCarFuelType(carFuelType) + ".", null);
        return carFuelType.getId();
    }

    @Transactional
    @RequestMapping(value = "/carfueltype/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@PathVariable("id") long id, @RequestBody CarFuelType carFuelType) {
        CarFuelType original = carFuelTypeDao.findOne(id);
        String jsonOriginal = original.toJson();
        CarFuelType updated = carFuelTypeDao.merge(carFuelType);
        String jsonUpdated = updated.toJson();
        String json = SerializationUtils.update(jsonOriginal, jsonUpdated);
        changelogService.log(APPLICATIONS, "The Car Fuel Type " + formatCarFuelType(updated) + " has been updated.", json,
              null);
    }

    @Transactional
    @RequestMapping(value = "/carfueltype/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carFuelTypeDao.delete(id);
        changelogService.log(APPLICATIONS, "Removed Car Fuel Type " + formatCarFuelType(id) + ".", null);
    }
}
