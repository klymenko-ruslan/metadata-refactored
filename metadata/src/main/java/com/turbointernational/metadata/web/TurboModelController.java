package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.other.TurboModelDao;
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
@RequestMapping("/metadata/other/turboModel")
public class TurboModelController {
    
    @Autowired
    ChangelogDao changelogDao;
    
    @Autowired
    TurboModelDao turboModelDao;
    
    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    @JsonView(View.Detail.class)
    public TurboModel createJson(@RequestBody TurboModel turboModel) {
        turboModelDao.persist(turboModel);
        
        changelogDao.log("Created turbo model", turboModel.toJson());
        
        return turboModel;
    }
    
    @RequestMapping(method = RequestMethod.PUT,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    @JsonView(View.Detail.class)
    public TurboModel updateJson(@RequestBody TurboModel turboModel) {
        turboModelDao.merge(turboModel);
        
        changelogDao.log("Updated turbo model", turboModel.toJson());
        
        return turboModel;
    }
    
    @RequestMapping(value="/{turboModelId}", method = RequestMethod.DELETE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    public void deleteJson(@PathVariable Long turboModelId) {
        TurboModel turboModel = turboModelDao.findOne(turboModelId);
        turboModelDao.remove(turboModel);
        changelogDao.log("Removed turbo model", turboModel.toJson());
    }
    
    @RequestMapping(method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    public List<TurboModel> listByTurboTypeIdJson(@RequestParam Long turboTypeId) {
        return turboModelDao.findTurboModelsByTurboTypeId(turboTypeId);
    }
}
