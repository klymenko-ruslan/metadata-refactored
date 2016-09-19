package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.other.TurboType;
import com.turbointernational.metadata.domain.other.TurboTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/metadata/other/turboType")
public class TurboTypeController {
    
    @Autowired
    ChangelogDao changelogDao;
    
    @Autowired
    TurboTypeDao turboTypeDao;
    
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    @JsonView(View.Detail.class)
    public TurboType createJson(@RequestBody TurboType type) {
        turboTypeDao.persist(type);
        changelogDao.log("Created turbo type", type.toJson());
        return type;
    }
    
    @RequestMapping(method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    @JsonView(View.Detail.class)
    public TurboType updateJson(@RequestBody TurboType turboType) {
        turboTypeDao.merge(turboType);
        changelogDao.log("Updated turbo type", turboType.toJson());
        return turboType;
    }
    
    @RequestMapping(value="/{turboTypeId}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    public void deleteJson(@PathVariable Long turboTypeId) {
        TurboType turboType = turboTypeDao.findOne(turboTypeId);
        changelogDao.log("Removed turbo type", turboType.toJson());
        turboTypeDao.remove(turboType);
    }
    
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    public List<TurboType> listJson(@RequestParam("manufacturerId") Long manufacturerId) {
        return turboTypeDao.findTurboTypesByManufacturerId(manufacturerId);
    }
}
