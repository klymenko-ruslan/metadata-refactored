package com.turbointernational.metadata.web.controller;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.APPLICATIONS;
import static com.turbointernational.metadata.util.FormatUtils.formatCarEngine;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
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
import com.turbointernational.metadata.dao.CarEngineDao;
import com.turbointernational.metadata.entity.CarEngine;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.util.SerializationUtils;
import com.turbointernational.metadata.util.View;

/**
 * Created by trunikov on 12/15/15.
 */
@RequestMapping("/metadata/application")
@Controller
public class CarEngineController {

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private CarEngineDao carEngineDao;

    @Transactional
    @RequestMapping(value = "/carengine/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.CarEngineDetailed.class)
    @Secured("ROLE_READ")
    public CarEngine get(@PathVariable("id") Long id) {
        return carEngineDao.findOne(id);
    }

    @Transactional
    @RequestMapping(value = "/carengines", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.CarEngineDetailed.class)
    @Secured("ROLE_READ")
    public List<CarEngine> findAllOrderedByName() {
        return carEngineDao.findAllOrderedByName();
    }

    @Transactional
    @RequestMapping(value = "/carengine/exists", method = GET)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public Boolean exists(@RequestParam(name = "engineSize", required = false) String engineSize,
                          @RequestParam(name = "fuelTypeId", required = false) Long fuelTypeId) {
        List<CarEngine> engines = carEngineDao.findByName(engineSize, fuelTypeId, 1);
        return engines.isEmpty() ? FALSE : TRUE;
    }

    @Transactional
    @RequestMapping(value = "/carengine", method = POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public CarEngine create(@RequestBody CarEngine carEngine) {
        if (!carEngineDao.exists(carEngine.getEngineSize(), carEngine.getFuelType().getId())) { // TODO: replace by UI validation
            carEngineDao.persist(carEngine);
        }
        changelogService.log(APPLICATIONS, "Created Car Engine " + formatCarEngine(carEngine) + ".", null);
        return carEngine;
    }

    @Transactional
    @RequestMapping(value = "/carengine/{id}", method = PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@PathVariable("id") long id, @RequestBody CarEngine carEngine) {
        if (!carEngineDao.exists(carEngine.getEngineSize(), carEngine.getFuelType().getId())) { // TODO: replace by UI validation
            CarEngine original = carEngineDao.findOne(id);
            String jsonOriginal = original.toJson();
            CarEngine updated = carEngineDao.merge(carEngine);
            String jsonUpdated = updated.toJson();
            String json = SerializationUtils.update(jsonOriginal, jsonUpdated);
            changelogService.log(APPLICATIONS, "The Car Engine " + formatCarEngine(updated) + " has been updated.", json,
                null);
        }
    }

    @Transactional
    @RequestMapping(value = "/carengine/{id}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carEngineDao.delete(id);
        changelogService.log(APPLICATIONS, "Removed Car Engine " + formatCarEngine(id) + ".", null);
    }
}
