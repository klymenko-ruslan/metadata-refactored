package com.turbointernational.metadata.domain.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Created by trunikov on 14.12.15.
 */
@RequestMapping("/metadata/application/carmake")
@Controller
public class CarMakeController {

    @Autowired
    private CarMakeDao carMakeDao;

    @Transactional
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    public CarMake findByName(@RequestParam("name") String name) {
        return carMakeDao.findCarMakeByName(name);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public long create(@RequestBody CarMake carMake) {
        return carMakeDao.create(carMake);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carMakeDao.delete(id);
    }
}
