package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.CarEngineDao;
import com.turbointernational.metadata.dao.CarModelDao;
import com.turbointernational.metadata.dao.CarModelEngineYearDao;
import com.turbointernational.metadata.dao.CarYearDao;
import com.turbointernational.metadata.entity.*;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.util.FormatUtils;
import com.turbointernational.metadata.util.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.APPLICATIONS;
import static com.turbointernational.metadata.util.FormatUtils.formatApplication;
import static com.turbointernational.metadata.util.FormatUtils.formatCarYear;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping("/metadata/application")
@Controller
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private CarModelEngineYearDao carModelEngineYearDao;

    @Autowired
    private CarYearDao carYearDao;

    @Autowired
    private CarEngineDao carEngineDao;

    @Autowired
    private CarModelDao carModelDao;

    @Transactional
    @RequestMapping(value = "/carmodelengineyear/{id}", method = GET)
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
        return new ResponseEntity<String>(json, headers, OK);
    }

    @Transactional
    @RequestMapping(value = "/carmodelengineyear/exists", method = GET)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public Boolean exists(@RequestParam(name = "carModelId", required = false) Long carModelId,
                          @RequestParam(name = "carEngineId", required = false) Long carEngineId,
                          @RequestParam(name = "year", required = false) String year) {
        CarYear carYear = carYearDao.findByName(year);
        Long carYearId = null;
        if (carYear != null) {
            carYearId = carYear.getId();
        }
        List<CarModelEngineYear> apps = carModelEngineYearDao.find(carModelId, carEngineId, carYearId, 1);
        return apps.isEmpty() ? FALSE : TRUE;
    }

    @Transactional
    @RequestMapping(value = "/carmodelengineyear", method = POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public long create(@RequestBody CarModelEngineYear cmey) {
        normalize(cmey);
        carModelEngineYearDao.persist(cmey);
        changelogService.log(APPLICATIONS, "Created application " + formatApplication(cmey) + ".");
        return cmey.getId();
    }

    public static class BulkCreateRequest implements Serializable {

        @JsonView({View.Summary.class})
        private List<CarModel> models;

        @JsonView({View.Summary.class})
        private List<CarEngine> engines;

        @JsonView({View.Summary.class})
        private List<CarYear> years;

        public List<CarModel> getModels() {
            return models;
        }

        public void setModels(List<CarModel> models) {
            this.models = models;
        }

        public List<CarEngine> getEngines() {
            return engines;
        }

        public void setEngines(List<CarEngine> engines) {
            this.engines = engines;
        }

        public List<CarYear> getYears() {
            return years;
        }

        public void setYears(List<CarYear> years) {
            this.years = years;
        }

    }

    @JsonInclude(ALWAYS)
    public static class BulkCreateResonse implements Serializable {

        @JsonView({View.Summary.class})
        private int created = 0;

        @JsonView({View.Summary.class})
        private int ignored = 0;

        public BulkCreateResonse() {
        }

        public BulkCreateResonse(int created, int ignored) {
            this.created = created;
            this.ignored = ignored;
        }

        public int incCreated() {
            return ++created;
        }

        public int incIngored() {
            return ++ignored;
        }

        public int getCreated() {
            return created;
        }

        public void setCreated(int created) {
            this.created = created;
        }

        public int getIgnored() {
            return ignored;
        }

        public void setIgnored(int ignored) {
            this.ignored = ignored;
        }

    }

    @Transactional
    @RequestMapping(value = "/carmodelengineyear/bulkcreate", method = POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public BulkCreateResonse bulkCreate(@RequestBody BulkCreateRequest bkr) {
        BulkCreateResonse retVal = new BulkCreateResonse();
        if (bkr.getModels().isEmpty() && bkr.getEngines().isEmpty() && bkr.getYears().isEmpty()) {
            return retVal;
        }
        List<CarModel> models = new ArrayList<>(bkr.getModels().size() + 1);
        List<CarEngine> engines = new ArrayList<>(bkr.getEngines().size() + 1);
        List<CarYear> years = new ArrayList<>(bkr.getYears().size() + 1);

        EntityManager em = carModelDao.getEntityManager();

        bkr.getModels().forEach(cm -> models.add(em.getReference(CarModel.class, cm.getId())));
        bkr.getEngines().forEach(ce -> engines.add(em.getReference(CarEngine.class, ce.getId())));
        bkr.getYears().forEach(yr -> years.add(getOrCreateCarYear(yr)));

        if (models.isEmpty()) {
            models.add(null);
        }
        if (engines.isEmpty()) {
            engines.add(null);
        }
        if (years.isEmpty()) {
            years.add(null);
        }

        models.forEach(cm -> engines.forEach(ce -> years.forEach(cy -> {

            Long carModelId = null;
            if (cm != null) {
                carModelId = cm.getId();
            }

            Long carEngineId = null;
            if (ce != null) {
                carEngineId = ce.getId();
            }

            Long carYearId = null;
            if (cy != null) {
                carYearId = cy.getId();
            }

            List<CarModelEngineYear> apps = carModelEngineYearDao.find(carModelId, carEngineId, carYearId, 1);
            if (apps.isEmpty()) { // not found
                CarModelEngineYear cmey = new CarModelEngineYear();
                cmey.setModel(cm);
                cmey.setEngine(ce);
                cmey.setYear(cy);
                carModelEngineYearDao.persist(cmey);
                changelogService.log(APPLICATIONS, "Created application: " + formatApplication(cmey) + ".");
                retVal.incCreated();
            } else {
                retVal.incIngored();
            }

        })));


        return retVal;
    }

    @Transactional
    @RequestMapping(value = "/carmodelengineyear/{id}", method = PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@RequestBody CarModelEngineYear cmey) {
        normalize(cmey);
        carModelEngineYearDao.merge(cmey);
        changelogService.log(APPLICATIONS, "The application " + formatApplication(cmey) + " has been updated.");
    }

    @Transactional
    @RequestMapping(value = "/carmodelengineyear/{id}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carModelEngineYearDao.delete(id);
        changelogService.log(APPLICATIONS, "Removed application " + formatApplication(id) + ".");
    }

    private CarYear getOrCreateCarYear(CarYear carYear) {
        CarYear retVal = null;
        String year = carYear.getName();
        if (year != null && !year.equals("")) {
            retVal = carYearDao.findByName(year);
            if (retVal == null) {
                retVal = new CarYear(year);
                carYearDao.persist(retVal);
                changelogService.log(APPLICATIONS, "Created CarYear " + formatCarYear(retVal) + ".");
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
