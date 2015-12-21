package com.turbointernational.metadata.domain.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/metadata/application")
@Controller
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private CarModelEngineYearDao carModelEngineYearDao;

    @Autowired
    private CarYearDao carYearDao;

    @Autowired
    private CarEngineDao carEngineDao;

    @Autowired
    private CarModelDao carModelDao;

    @Transactional
    @RequestMapping(value = "/carmodelengineyear/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> getCarmodelengineyear(@PathVariable("id") long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        CarModelEngineYear application = carModelEngineYearDao.findOne(id);
        String json = null;
        if (application != null) {
            json = application.toJson();
        }
        return new ResponseEntity<String>(json, headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/carmodelengineyear", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public long create(@RequestBody CarModelEngineYear cmey) {
        normalize(cmey);
        carModelEngineYearDao.persist(cmey);
        return cmey.getId();
    }

    @Transactional
    @RequestMapping(value = "/carmodelengineyear/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@RequestBody CarModelEngineYear cmey) {
        normalize(cmey);
        carModelEngineYearDao.merge(cmey);
    }

    @Transactional
    @RequestMapping(value = "/carmodelengineyear/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carModelEngineYearDao.delete(id);
    }

    private CarYear getOrCreateCarYear(CarYear carYear) {
        CarYear retVal = null;
        String year = carYear.getName();
        if (year != null && !year.equals("")) {
            retVal = carYearDao.findByName(year);
            if (retVal == null) {
                retVal = new CarYear(year);
                carYearDao.persist(retVal);
            }
        }
        return retVal;
    }

    private void normalize(CarModelEngineYear cmey) {
        CarYear carYear = cmey.getYear();
        if (carYear != null) {
            carYear = getOrCreateCarYear(carYear);
            cmey.setYear(carYear);
        }
        CarEngine carEngine = cmey.getEngine();
        if (carEngine != null) {
            Long id = carEngine.getId();
            if (id != null && id > 0) {
                carEngine = carEngineDao.findOne(id);
                cmey.setEngine(carEngine);
            }
        }
        CarModel carModel = cmey.getModel();
        if (carModel != null) {
            Long id = carModel.getId();
            if (id != null && id > 0) {
                carModel = carModelDao.findOne(id);
                cmey.setModel(carModel);
            }
        }
    }

}
