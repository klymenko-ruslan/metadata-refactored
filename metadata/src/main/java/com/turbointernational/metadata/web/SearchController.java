package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.util.ElasticSearch;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/search")
public class SearchController {
    private static final Logger log = Logger.getLogger(SearchController.class.toString());
    
    
    @Autowired(required=true)
    ElasticSearch elasticSearch;
    
    @Autowired
    PartDao partDao;
    
    @RequestMapping()
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> search(@RequestBody String request) throws Exception {
        String response = elasticSearch.search(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }
    
    @RequestMapping(value="/index/{partId}")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void indexPart(@PathVariable("partId") Long partId) throws Exception {
        Part part = partDao.findOne(partId);
        
        elasticSearch.indexPart(part);
    }
    
    @RequestMapping(value="/indexAll")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void indexAll() throws Exception {
        elasticSearch.indexAllParts();
    }
    
}
