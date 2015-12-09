package com.turbointernational.metadata.domain.part.application;

import com.turbointernational.metadata.domain.part.types.TurboCarModelEngineYear;
import com.turbointernational.metadata.domain.part.types.TurboCarModelEngineYearDao;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RequestMapping("/metadata/part")
@Controller
public class PartApplicationController {

    private static final Logger log = Logger.getLogger(PartApplicationController.class.toString());

    @Autowired
    private TurboCarModelEngineYearDao turboCarModelEngineYearDao;

    @Transactional
    @RequestMapping(value = "{partId}/application", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> getApplications(@PathVariable("partId") Long partId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<TurboCarModelEngineYear> partLinkedApplications =
                turboCarModelEngineYearDao.getPartLinkedApplications(partId);
        String json = new JSONSerializer()
                .transform(new HibernateTransformer(), TurboCarModelEngineYear.class)
                .include("carModelEngineYear.id")
                .include("carModelEngineYear.year.name")
                .include("carModelEngineYear.model.name")
                .include("carModelEngineYear.model.make.name")
                .include("carModelEngineYear.engine.engineSize")
                .include("carModelEngineYear.engine.fuelType.name")
                .exclude("turbo")
                .exclude("*.class")
                .serialize(partLinkedApplications);
        return new ResponseEntity<String>(json, headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{partId}/application", method = RequestMethod.POST)
    @Secured("ROLE_APPLICATION_CRUD")
    public ResponseEntity<String> add(@PathVariable("partId") Long partId, @RequestBody Long[] cmeyIds) throws Exception {
        if (cmeyIds != null) {
            for (Long cmeyId : cmeyIds) {
                turboCarModelEngineYearDao.add(partId, cmeyId);
            }
        }
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{partId}/application/{applicationId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_APPLICATION_CRUD")
    public ResponseEntity<String> delete(@PathVariable("partId") Long partId,
                                         @PathVariable("applicationId") Long applicationId) throws Exception {
        int deleted = turboCarModelEngineYearDao.delete(partId, applicationId);
        return new ResponseEntity<String>("", HttpStatus.OK);
    }
}
