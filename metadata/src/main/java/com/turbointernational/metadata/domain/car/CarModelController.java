package com.turbointernational.metadata.domain.car;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
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
@RequestMapping("/metadata/application/carmodel")
@Controller
public class CarModelController {

    @Autowired
    private CarModelDao carModelDao;

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.CarModel.class)
    @Secured("ROLE_READ")
    public CarModel get(@PathVariable("id") Long id) {
        return carModelDao.findOne(id);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public long create(@RequestBody CarModel carModel) {
        carModelDao.persist(carModel);
        return carModel.getId();
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@RequestBody CarModel carModel) {
        carModelDao.merge(carModel);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carModelDao.delete(id);
    }
}
