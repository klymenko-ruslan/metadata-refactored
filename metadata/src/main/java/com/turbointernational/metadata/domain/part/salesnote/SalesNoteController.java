package com.turbointernational.metadata.domain.part.salesnote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.security.User;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/metadata/other/salesNote")
public class SalesNoteController {

    @Autowired(required = true)
    ChangelogDao changelogDao;

    @Autowired(required = true)
    SalesNoteRepository salesNotes;

    @Autowired(required = true)
    PartDao partDao;

    @Autowired(required = true)
    ObjectMapper json;

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_SALES_NOTE_CREATE")
    @Transactional
    public @ResponseBody
    SalesNote create(@AuthenticationPrincipal(errorOnInvalidType = true) User user, @RequestBody CreateSalesNoteRequest request) {

        // Create the sales note from the request
        SalesNote salesNote = new SalesNote();
        salesNote.setCreator(user);
        salesNote.setCreateDate(new Date());
        salesNote.setUpdater(user);
        salesNote.setUpdateDate(new Date());
        salesNote.setComment(request.getComment());
        salesNote.setState(SalesNoteState.draft);

        // Create the primary part association
        Part primaryPart = partDao.findOne(request.getPrimaryPartId());
        salesNote.getParts().add(new SalesNotePart(salesNote, primaryPart, true, user));

        // Save
        salesNotes.save(salesNote);
        changelogDao.log("Created sales note " + salesNote.getId(), request);
        
        // Initialize a few properties before sending the response
        primaryPart.getManufacturer().getName();
        primaryPart.getPartType().getName();
        
        return salesNote;
    }

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
    @RequestMapping(value = "search", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_READ")
    public Page<SalesNote> search(@RequestBody SalesNoteSearchRequest req) {
        Page<SalesNote> results = salesNotes.search(req);
        return results;
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
