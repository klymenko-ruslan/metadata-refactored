package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/search")
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private PartElasticSearch partElasticSearch;

    @Autowired
    private CarModelEngineYearElasticSearch carModelEngineYearElasticSearch;

    @Autowired
    private CarEngineElasticSearch carEngineElasticSearch;

    @Autowired
    private CarFuelTypeElasticSearch carFuelTypeElasticSearch;

    @Autowired
    private CarMakeElasticSearch carMakeElasticSearch;

    @Autowired
    private CarModelElasticSearch carModelElasticSearch;

    @Autowired
    private PartDao partDao;

    protected ResponseEntity<String> _search(AbstractElasticSearch elasticSearch, String request) throws Exception {
        String response = elasticSearch.search(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    @RequestMapping("/part")
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> searchPart(@RequestBody String request) throws Exception {
        return _search(partElasticSearch, request);
    }

    @RequestMapping(value="/carmodelengineyear", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> searchCarModelEngineYear(@RequestBody String request) throws Exception {
        return _search(carModelEngineYearElasticSearch, request);
    }

    @Async
    @RequestMapping(value="/index/{partId}")
    @ResponseBody
    // TODO
    //@Secured("ROLE_ADMIN")
    public void indexPart(@PathVariable("partId") Long partId) throws Exception {
        Part part = partDao.findOne(partId);
        partElasticSearch.index(part);
    }

    @Async
    @RequestMapping(value="/part/indexAll")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // TODO
    //@Secured("ROLE_ADMIN")
    public void indexPartAll() throws Exception {
        partElasticSearch.indexAll();
    }

    @Async
    @RequestMapping(value="/application/indexAll")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // TODO
    //@Secured("ROLE_ADMIN")
    public void indexApplicationAll() throws Exception {
        carModelEngineYearElasticSearch.indexAll();
        carEngineElasticSearch.indexAll();
        carFuelTypeElasticSearch.indexAll();
        carMakeElasticSearch.indexAll();
        carModelElasticSearch.indexAll();
    }

}
