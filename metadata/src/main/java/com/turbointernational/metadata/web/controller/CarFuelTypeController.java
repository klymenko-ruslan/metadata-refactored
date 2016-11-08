package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CarFuelType;
import com.turbointernational.metadata.dao.CarFuelTypeDao;
import com.turbointernational.metadata.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by trunikov on 12/15/15.
 */
@RequestMapping("/metadata/application")
@Controller
public class CarFuelTypeController {

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
        return carFuelType.getId();
    }

    @Transactional
    @RequestMapping(value = "/carfueltype/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@RequestBody CarFuelType carFuelType) {
        carFuelTypeDao.merge(carFuelType);
    }

    @Transactional
    @RequestMapping(value = "/carfueltype/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carFuelTypeDao.delete(id);
    }
}