package com.turbointernational.metadata.domain.other;

import java.util.List;
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

/**
 *
 * @author jrodriguez
 */

@RequestMapping("/metadata/other/manufacturer")
@Controller
public class ManufacturerController {
    
    @Autowired
    ManufacturerDao manufacturerDao;
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
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
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Manufacturer> result = manufacturerDao.findAllManufacturers();
        return new ResponseEntity<String>(Manufacturer.toJsonArray(result), headers, HttpStatus.OK);
    }
}
