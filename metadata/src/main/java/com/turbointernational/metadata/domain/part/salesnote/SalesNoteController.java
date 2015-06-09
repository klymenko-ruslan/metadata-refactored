package com.turbointernational.metadata.domain.part.salesnote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/metadata/other/salesNote")
public class SalesNoteController {
    
    @Autowired(required=true)
    ChangelogDao changelogDao;
    
    @Autowired(required=true)
    SalesNoteRepository salesNotes;
    
    @Autowired(required=true)
    ObjectMapper json;
    
//    @RequestMapping(method = RequestMethod.POST)
//    @ResponseBody
//    @Secured("ROLE_SALES_NOTE_CREATE")
//    @Transactional
//    public ResponseEntity<SalesNote> createJson(@RequestBody SalesNote salesNote) {
//        salesNotes.save(salesNote);
//        
//        changelogDao.log("Created sales note.", salesNote.getId().toString());
//        
//        
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<SalesNote>(salesNote, headers, HttpStatus.OK);
//    }
    
//    @RequestMapping(method = RequestMethod.PUT)
//    @ResponseBody
//    @Secured("ROLE_SALES_NOTE_UPDATE")
//    @Transactional
//    public ResponseEntity<String> updateJson(@RequestBody String turboModelJson) {
//        TurboModel turboModel = TurboModel.fromJsonToTurboModel(turboModelJson);
//        turboModelDao.merge(turboModel);
//        
//        changelogDao.log("Updated turbo model", turboModel.toJson());
//        
//        
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(turboModel.toJson(), headers, HttpStatus.OK);
//    }
//    
//    @RequestMapping(value="/{turboModelId}", method = RequestMethod.DELETE)
//    @ResponseBody
//    @Secured("ROLE_SALES_NOTE_DELETE")
//    @Transactional
//    public ResponseEntity<String> deleteJson(@PathVariable Long turboModelId) {
//        TurboModel turboModel = turboModelDao.findOne(turboModelId);
//        turboModelDao.remove(turboModel);
//        changelogDao.log("Removed turbo model", turboModel.toJson());
//        
//        
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>("", headers, HttpStatus.OK);
//    }
    
    
    
    @RequestMapping(value="search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    public Page<SalesNote> search(SalesNoteSearchRequest req) {
        // TODO: Check sales note state permissions
        return salesNotes.search(req);
    }   
    
//    @RequestMapping(value="listByPartId/{partId}", method = RequestMethod.GET)
//    @ResponseBody
//    @Secured("ROLE_READ")
//    public ResponseEntity<String> listByPartId(@PathVariable("partId") Long partId) throws JsonProcessingException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        Page<SalesNote> result = salesNotes.findByPartId(new PageRequest(0, 100), partId);
//        return new ResponseEntity<String>(json.writeValueAsString(result), headers, HttpStatus.OK);
//    }
}
