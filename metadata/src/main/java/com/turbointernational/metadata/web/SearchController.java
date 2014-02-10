package com.turbointernational.metadata.web;

import com.turbointernational.metadata.util.ElasticSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jrodriguez
 */
@Controller
public class SearchController {
    
    @Autowired(required=true)
    ElasticSearch elasticSearch;
    
    @RequestMapping("/search")
    @ResponseBody
    public ResponseEntity<String> search(@RequestBody String request) throws Exception {
        String response = elasticSearch.search(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }
}
