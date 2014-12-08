package com.turbointernational.metadata.domain.other;
import com.turbointernational.metadata.domain.changelog.Changelog;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/metadata/other/turboModel")
public class TurboModelController {
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    public ResponseEntity<String> createJson(@RequestBody String turboModelJson) {
        TurboModel turboModel = TurboModel.fromJsonToTurboModel(turboModelJson);
        turboModel.persist();
        
        Changelog.log("Created turbo model", turboModel.toJson());
        
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(turboModel.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    public ResponseEntity<String> updateJson(@RequestBody String turboModelJson) {
        TurboModel turboModel = TurboModel.fromJsonToTurboModel(turboModelJson);
        turboModel.merge();
        
        Changelog.log("Updated turbo model", turboModel.toJson());
        
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(turboModel.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value="/{turboModelId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    public ResponseEntity<String> deleteJson(@PathVariable Long turboModelId) {
        TurboModel turboModel = TurboModel.findTurboModel(turboModelId);
        
        Changelog.log("Removed turbo model", turboModel.toJson());
        
        turboModel.remove();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>("", headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> listByTurboTypeIdJson(@RequestParam Long turboTypeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<TurboModel> result = TurboModel.findTurboModelsByTurboTypeId(turboTypeId);
        return new ResponseEntity<String>(TurboModel.toJsonArray(result), headers, HttpStatus.OK);
    }
}
