package com.turbointernational.metadata.domain.car;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by trunikov on 12/15/15.
 */
@RequestMapping("/metadata/application")
@Controller
public class CarModelController {

    @Autowired
    private CarModelDao carModelDao;

    @Transactional
    @RequestMapping(value = "/carmodel/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.CarModel.class)
    @Secured("ROLE_READ")
    public CarModel get(@PathVariable("id") Long id) {
        return carModelDao.findOne(id);
    }

    @Transactional
    @RequestMapping(value = "/carmodels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.CarModel.class)
    @Secured("ROLE_READ")
    public List<CarModel> CarModelsOfMake(@RequestParam Long makeId) {
        return carModelDao.findCarModelsOfMake(makeId);
    }

    @Transactional
    @RequestMapping(value = "/carmodel", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public long create(@RequestBody CarModel carModel) {
        if (!carModelDao.exists(carModel.getName(), carModel.getMake().getId())) { // TODO: replace by UI validation
            carModelDao.persist(carModel);
            return carModel.getId();
        } else {
            return -1;
        }

    }

    @Transactional
    @RequestMapping(value = "/carmodel/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void update(@RequestBody CarModel carModel) {
        if (!carModelDao.exists(carModel.getName(), carModel.getMake().getId())) { // TODO: replace by UI validation
            carModelDao.merge(carModel);
        }
    }

    @Transactional
    @RequestMapping(value = "/carmodel/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public void remove(@PathVariable("id") long id) {
        carModelDao.delete(id);
    }
}
