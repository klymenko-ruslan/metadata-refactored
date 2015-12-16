package com.turbointernational.metadata.domain.car;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Created by trunikov on 12/15/15.
 */
@RequestMapping("/metadata/application/carengine")
@Controller
public class CarEngineController {

    @Autowired
    private CarEngineDao carEngineDao;

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.CarEngine.class)
    @Secured("ROLE_READ")
    public CarEngine get(@PathVariable("id") Long id) {
        return carEngineDao.findOne(id);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public long create(@RequestBody CarEngine carEngine) {
        if (!carEngineDao.exists(carEngine.getEngineSize(), carEngine.getFuelType().getId())) { // TODO: replace by UI validation
            carEngineDao.persist(carEngine);
            return carEngine.getId();
        } else {
            return -1;
        }

    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@RequestBody CarEngine carEngine) {
        if (!carEngineDao.exists(carEngine.getEngineSize(), carEngine.getFuelType().getId())) { // TODO: replace by UI validation
            carEngineDao.merge(carEngine);
        }
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carEngineDao.delete(id);
    }
}
