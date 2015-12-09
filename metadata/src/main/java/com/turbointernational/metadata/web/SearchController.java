package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.util.AbstractElasticSearch;
import com.turbointernational.metadata.util.ApplicationElasticSearch;
import com.turbointernational.metadata.util.PartElasticSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/search")
public class SearchController {

    private static final Logger log = Logger.getLogger(SearchController.class.toString());

    @Autowired(required=true)
    PartElasticSearch partElasticSearch;

    @Autowired(required=true)
    ApplicationElasticSearch applicationElasticSearch;

    @Autowired
    PartDao partDao;

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

    @RequestMapping("/application")
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> searchApplication(@RequestBody String request) throws Exception {
        return _search(applicationElasticSearch, request);
    }

    @Async
    @RequestMapping(value="/index/{partId}")
    @ResponseBody
    // TODO
    //@Secured("ROLE_ADMIN")
    public void indexPart(@PathVariable("partId") Long partId) throws Exception {
        Part part = partDao.findOne(partId);
        partElasticSearch.indexPart(part);
    }

    @Async
    @RequestMapping(value="/part/indexAll")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // TODO
    //@Secured("ROLE_ADMIN")
    public void indexPartAll() throws Exception {
        partElasticSearch.indexAllParts();
    }

    @Async
    @RequestMapping(value="/application/indexAll")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // TODO
    //@Secured("ROLE_ADMIN")
    public void indexApplicationAll(
            @RequestParam(required=false) Integer page,
            @RequestParam(required=false) Integer maxPages,
            @RequestParam(required=false) Integer pageSize) throws Exception {

        if (maxPages == null) {
            maxPages = Integer.MAX_VALUE;
        }
        if (page == null) {
            page = 0;
        }
        if (pageSize == null) {
            pageSize = 250;
        }

        int result;
        try {
            do {

                // Clear Hibernate
                //CarModelEngineYear.entityManager().clear();

                result = applicationElasticSearch.indexApplications(page * pageSize, pageSize);
                log.log(Level.INFO, "Indexed applications {0}-{1}: {2}",
                        new Object[]{page * pageSize, (page * pageSize) + pageSize, result});
                page++;

            } while (result >= pageSize && page < maxPages);
            log.info("Indexing of application finished.");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Reindexing of application failed.", e);
            throw e;
        }
    }
}
