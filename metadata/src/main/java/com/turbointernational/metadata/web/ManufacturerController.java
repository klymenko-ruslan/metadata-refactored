package com.turbointernational.metadata.web;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.other.ManufacturerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 *
 * @author jrodriguez
 */

@RequestMapping(value = {"/metadata/other/manufacturer", "/other/manufacturer"})
@Controller
public class ManufacturerController {
    
    @Autowired
    private ManufacturerDao manufacturerDao;

    @RequestMapping(value = "/list", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public List<Manufacturer> list() {
        return manufacturerDao.findAllManufacturers();
    }

    @RequestMapping(value = "/{id}", method = GET, headers = "Accept=application/json")
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Manufacturer manufacturer = manufacturerDao.findManufacturer(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (manufacturer == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(manufacturer.toJson(), headers, HttpStatus.OK);
    }
    
}
