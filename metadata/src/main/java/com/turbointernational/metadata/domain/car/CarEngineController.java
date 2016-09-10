package com.turbointernational.metadata.domain.car;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by trunikov on 12/15/15.
 */
@RequestMapping("/metadata/application")
@Controller
public class CarEngineController {

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
    public List<CarEngine> findAllOrderedByName(@RequestParam(value = "detailed", required = false,
            defaultValue = "false") Boolean detailed) {
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
        return carEngine;
    }

    @Transactional
    @RequestMapping(value = "/carengine/{id}", method = PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@RequestBody CarEngine carEngine) {
        if (!carEngineDao.exists(carEngine.getEngineSize(), carEngine.getFuelType().getId())) { // TODO: replace by UI validation
            carEngineDao.merge(carEngine);
        }
    }

    @Transactional
    @RequestMapping(value = "/carengine/{id}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carEngineDao.delete(id);
    }
}
