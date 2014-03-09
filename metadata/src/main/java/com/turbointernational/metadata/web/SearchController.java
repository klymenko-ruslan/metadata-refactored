package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.part.Part;
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
public class SearchController {
    private static final Logger log = Logger.getLogger(SearchController.class.toString());
    
    
    @Autowired(required=true)
    ElasticSearch elasticSearch;
    
    @RequestMapping("/search")
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> search(@RequestBody String request) throws Exception {
        String response = elasticSearch.search(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }
    
    @Async
    @RequestMapping(value="/search/index/{partId}")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void indexAll(@PathVariable("partId") Long partId) throws Exception {
        
        Part part = Part.findPart(partId);
        
        elasticSearch.indexPart(part);
    }
    
    @Async
    @RequestMapping(value="/search/indexAll")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void indexAll(
            @RequestParam(required=false) Integer page,
            @RequestParam(required=false) Integer maxPages,
            @RequestParam(required=false) Integer pageSize) throws Exception {
        
        if (maxPages == null ) {
            maxPages = Integer.MAX_VALUE;
        }
        if (page == null ) {
            page = 0;
        }
        if (pageSize == null ) {
            pageSize = 250;
        }

        int result;
        do {
            
            // Clear Hibernate
            Part.entityManager().clear();
            
            result = elasticSearch.indexParts(page * pageSize, pageSize);
            log.log(Level.INFO, "Indexed parts {0}-{1}: {2}", new Object[]{page * pageSize, (page * pageSize) + pageSize, result});
            page++;
            
        } while (result >= pageSize && page < maxPages);
    }
    
}
