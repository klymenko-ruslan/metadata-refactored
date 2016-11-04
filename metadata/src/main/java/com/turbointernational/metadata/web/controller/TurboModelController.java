package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.TurboModelDao;
import com.turbointernational.metadata.entity.TurboModel;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/metadata/other/turboModel")
public class TurboModelController {
    
    @Autowired
    ChangelogService changelogService;
    
    @Autowired
    TurboModelDao turboModelDao;
    
    @RequestMapping(method = POST,
                    consumes = APPLICATION_JSON_VALUE,
                    produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    @JsonView(View.Detail.class)
    public TurboModel createJson(@RequestBody TurboModel turboModel) {
        turboModelDao.persist(turboModel);
        
        changelogService.log("Created turbo model", turboModel.toJson());
        
        return turboModel;
    }
    
    @RequestMapping(method = PUT,
                    consumes = APPLICATION_JSON_VALUE,
                    produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    @JsonView(View.Detail.class)
    public TurboModel updateJson(@RequestBody TurboModel turboModel) {
        turboModelDao.merge(turboModel);
        
        changelogService.log("Updated turbo model", turboModel.toJson());
        
        return turboModel;
    }
    
    @RequestMapping(value="/{turboModelId}", method = DELETE,
                    produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    public void deleteJson(@PathVariable Long turboModelId) {
        TurboModel turboModel = turboModelDao.findOne(turboModelId);
        turboModelDao.remove(turboModel);
        changelogService.log("Removed turbo model", turboModel.toJson());
    }
    
    @RequestMapping(method = GET,
                    produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    protected List<TurboModel> listByTurboTypeIdJson(@RequestParam Long turboTypeId) {
        return turboModelDao.findTurboModelsByTurboTypeId(turboTypeId);
    }
}
