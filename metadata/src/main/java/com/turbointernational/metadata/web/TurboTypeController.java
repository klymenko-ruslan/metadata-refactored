package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.other.TurboType;
import com.turbointernational.metadata.domain.other.TurboTypeDao;
import com.turbointernational.metadata.web.View;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/metadata/other/turboType")
public class TurboTypeController {
    
    @Autowired
    ChangelogDao changelogDao;
    
    @Autowired
    TurboTypeDao turboTypeDao;
    
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    @JsonView(View.Detail.class)
    public TurboType createJson(@RequestBody TurboType type) {
        turboTypeDao.persist(type);
        
        changelogDao.log("Created turbo type", type.toJson());
        
        return type;
    }
    
    @RequestMapping(method = RequestMethod.PUT,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    @JsonView(View.Detail.class)
    public TurboType updateJson(@RequestBody TurboType turboType) {
        turboTypeDao.merge(turboType);
        changelogDao.log("Updated turbo type", turboType.toJson());
        return turboType;
    }
    
    @RequestMapping(value="/{turboTypeId}", method = RequestMethod.DELETE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    public void deleteJson(@PathVariable Long turboTypeId) {
        TurboType turboType = turboTypeDao.findOne(turboTypeId);
        changelogDao.log("Removed turbo type", turboType.toJson());
        turboTypeDao.remove(turboType);
    }
    
    @RequestMapping(value = "/manufacturer/{manufacturerId}", method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    public List<TurboType> listJson(@PathVariable("manufacturerId") Long manufacturerId) {
        return turboTypeDao.findTurboTypesByManufacturerId(manufacturerId);
    }
}
