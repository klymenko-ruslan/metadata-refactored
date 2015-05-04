package com.turbointernational.metadata.domain.other;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/metadata/other/turboType")
public class TurboTypeController {
    
    @Autowired
    ChangelogDao changelogDao;
    
    @Autowired
    TurboTypeDao turboTypeDao;
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    public ResponseEntity<String> createJson(@RequestBody String typeJson) {
        TurboType type = TurboType.fromJsonToTurboType(typeJson);
        turboTypeDao.persist(type);
        
        changelogDao.log("Created turbo type", type.toJson());
        
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(type.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    public ResponseEntity<String> updateJson(@RequestBody String turboTypeJson) {
        TurboType turboType = TurboType.fromJsonToTurboType(turboTypeJson);
        turboTypeDao.merge(turboType);
        
        changelogDao.log("Updated turbo type", turboType.toJson());
        
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(turboType.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value="/{turboTypeId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_TURBO_MODEL_CRUD")
    @Transactional
    public ResponseEntity<String> deleteJson(@PathVariable Long turboTypeId) {
        TurboType turboType = turboTypeDao.findOne(turboTypeId);
        changelogDao.log("Removed turbo type", turboType.toJson());
        turboTypeDao.remove(turboType);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>("", headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/manufacturer/{manufacturerId}", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> listJson(@PathVariable("manufacturerId") Long manufacturerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<TurboType> result = turboTypeDao.findTurboTypesByManufacturerId(manufacturerId);
        return new ResponseEntity<String>(TurboType.toJsonArray(result), headers, HttpStatus.OK);
    }
}
