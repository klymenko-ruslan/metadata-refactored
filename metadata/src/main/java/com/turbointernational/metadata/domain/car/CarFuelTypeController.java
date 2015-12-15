package com.turbointernational.metadata.domain.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Created by trunikov on 12/15/15.
 */
@RequestMapping("/metadata/application/carfueltype")
@Controller
public class CarFuelTypeController {

    @Autowired
    private CarFuelTypeDao carFuelTypeDao;

    @Transactional
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    public CarFuelType findByName(@RequestParam("name") String name) {
        return carFuelTypeDao.findCarFuelTypeByName(name);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public long create(@RequestBody CarFuelType carFuelType) {
        carFuelTypeDao.persist(carFuelType);
        return carFuelType.getId();
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@RequestBody CarFuelType carFuelType) {
        carFuelTypeDao.merge(carFuelType);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carFuelTypeDao.delete(id);
    }
}
