package com.turbointernational.metadata.web;

import com.turbointernational.metadata.services.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/search")
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/part", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> searchPart(@RequestParam(required = false) String partNumber,
                                             @RequestParam(required = false) String partTypeName,
                                             @RequestParam(required = false) String manufacturerName,
                                             @RequestParam(required = false) String kitType,
                                             @RequestParam(required = false) String gasketType,
                                             @RequestParam(required = false) String sealType,
                                             @RequestParam(required = false) String coolType,
                                             @RequestParam(required = false) String turboType,
                                             @RequestParam(required = false) String turboModel,
                                             @RequestParam(required = false) String sortProperty,
                                             @RequestParam(required = false) String sortOrder,
                                             @RequestParam(defaultValue = "0") Integer offset,
                                             @RequestParam(defaultValue = "10") Integer limit) throws Exception {
        String json = searchService.filterParts(partNumber, partTypeName, manufacturerName, kitType, gasketType,
                sealType, coolType, turboType, turboModel, sortProperty, sortOrder,  offset,  limit);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @Async
    @RequestMapping(value="/index/{partId}")
    @ResponseBody
    // TODO: @Secured("ROLE_ADMIN")
    public void indexPart(@PathVariable("partId") Long partId) throws Exception {
        searchService.indexPart(partId);
    }

    @Async
    @RequestMapping(value="/part/indexAll")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // TODO: @Secured("ROLE_ADMIN")
    public void indexPartAll() throws Exception {
        searchService.indexAllParts();
    }

    @Async
    @RequestMapping(value="/application/indexAll")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // TODO: @Secured("ROLE_ADMIN")
    public void indexApplicationAll() throws Exception {
        searchService.indexAllApplications();
    }

}
