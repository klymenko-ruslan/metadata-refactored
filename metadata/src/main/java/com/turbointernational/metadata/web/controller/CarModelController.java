package com.turbointernational.metadata.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.CarModelDao;
import com.turbointernational.metadata.entity.CarModel;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2015-12015.
 */
@RequestMapping("/metadata/application")
@Controller
public class CarModelController {

    @Autowired
    private CarModelDao carModelDao;

    @Transactional
    @RequestMapping(value = "/carmodel/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.CarModelDetailed.class)
    @Secured("ROLE_READ")
    public CarModel get(@PathVariable("id") Long id) {
        return carModelDao.findOne(id);
    }

    @Transactional
    @RequestMapping(value = "/carmodels", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public List<CarModel> CarModelsOfMake(@RequestParam Long makeId) {
        return carModelDao.findCarModelsOfMake(makeId);
    }

    @Transactional
    @RequestMapping(value = "/carmodel", method = POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public CarModel create(@RequestBody CarModel carModel) {
        carModelDao.persist(carModel);
        return carModel;
    }

    @Transactional
    @RequestMapping(value = "/carmodel/{id}", method = PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@RequestBody CarModel carModel) {
        carModelDao.merge(carModel);
    }

    @Transactional
    @RequestMapping(value = "/carmodel/{id}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carModelDao.delete(id);
    }

    @RequestMapping(value = "/carmodel/exists", method = GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public boolean exists(@RequestParam("name") String name, @RequestParam("carMakeId") Long carMakeId) {
      return carModelDao.exists(name, carMakeId);
    }

}
