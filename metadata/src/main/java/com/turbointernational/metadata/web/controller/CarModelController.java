package com.turbointernational.metadata.web.controller;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.APPLICATIONS;
import static com.turbointernational.metadata.util.FormatUtils.formatCarModel;
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
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.util.SerializationUtils;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2015-12015.
 */
@RequestMapping("/metadata/application")
@Controller
public class CarModelController {

    @Autowired
    private ChangelogService changelogService;

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
        changelogService.log(APPLICATIONS, "Created Car Fuel Type " + formatCarModel(carModel) + ".", null);
        return carModel;
    }

    @Transactional
    @RequestMapping(value = "/carmodel/{id}", method = PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@PathVariable("id") long id, @RequestBody CarModel carModel) {
        CarModel original = carModelDao.findOne(id);
        String jsonOriginal = original.toJson();
        CarModel updated = carModelDao.merge(carModel);
        String jsonUpdated = updated.toJson();
        String json = SerializationUtils.update(jsonOriginal, jsonUpdated);
        changelogService.log(APPLICATIONS, "The Car Model " + formatCarModel(updated) + " has been updated.", json,
              null);
    }

    @Transactional
    @RequestMapping(value = "/carmodel/{id}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carModelDao.delete(id);
        changelogService.log(APPLICATIONS, "Removed Car Model " + formatCarModel(id) + ".", null);
    }

    @RequestMapping(value = "/carmodel/exists", method = GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public boolean exists(@RequestParam("name") String name, @RequestParam("carMakeId") Long carMakeId) {
      return carModelDao.exists(name, carMakeId);
    }

}
