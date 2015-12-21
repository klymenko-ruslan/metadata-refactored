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

/**
 * Created by trunikov on 14.12.15.
 */
@RequestMapping("/metadata/application")
@Controller
public class CarMakeController {

    @Autowired
    private CarMakeDao carMakeDao;

    @Transactional
    @RequestMapping(value = "/carmake", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public CarMake findByName(@RequestParam("name") String name) {
        return carMakeDao.findCarMakeByName(name);
    }

    @Transactional
    @RequestMapping(value = "/carmakes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public List<CarMake> findAllOrderedByName() {
        return carMakeDao.findAllOrderedByName();
    }

    @Transactional
    @RequestMapping(value = "/carmake", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public long create(@RequestBody CarMake carMake) {
        carMakeDao.persist(carMake);
        return carMake.getId();
    }

    @Transactional
    @RequestMapping(value = "/carmake/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@RequestBody CarMake carMake) {
        carMakeDao.merge(carMake);
    }

    @Transactional
    @RequestMapping(value = "/carmake/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carMakeDao.delete(id);
    }
}
