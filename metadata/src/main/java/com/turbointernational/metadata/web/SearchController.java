package com.turbointernational.metadata.web;

import com.turbointernational.metadata.util.ElasticSearch;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
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
    
    @RequestMapping(value="/search/indexAll")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> indexAll(@RequestParam(required=false) Integer maxPages) throws Exception {
        
        if (maxPages == null ) {
            maxPages = Integer.MAX_VALUE;
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        int pageSize = 25;
        int page = 0;
        int result;
        do {
                result = elasticSearch.indexParts(page * pageSize, pageSize);
                log.log(Level.INFO, "Indexing parts {0}-{1}", new Object[]{page * pageSize, (page * pageSize) + pageSize});
            page++;
            
        } while (result >= pageSize && page < maxPages);

        return new ResponseEntity<Void>((Void) null, headers, HttpStatus.OK);
    }
    
}
