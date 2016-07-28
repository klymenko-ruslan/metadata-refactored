package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.salesnote.SalesNoteState;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.services.SearchService;
import com.turbointernational.metadata.services.SearchService.IndexingStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author jrodriguez
 */
@Controller
@RequestMapping("/metadata/search")
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/parts", method = GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> filterParts(@RequestParam(name = "partNumber", required = false) String partNumber,
                                              @RequestParam(name = "partTypeName", required = false) String partTypeName,
                                              @RequestParam(name = "manufacturerName", required = false) String manufacturerName,
                                              @RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "description", required = false) String description,
                                              @RequestParam(name = "inactive", required = false) Boolean inactive,
                                              @RequestParam(name = "turboTypeName", required = false) String turboTypeName,
                                              @RequestParam(name = "turboModelName", required = false) String turboModelName,
                                              WebRequest webRequest,
                                              @RequestParam(name = "pgSortProperty", required = false) String sortProperty,
                                              @RequestParam(name = "pgSortOrder", required = false) String sortOrder,
                                              @RequestParam(name = "pgOffset", defaultValue = "0") Integer offset,
                                              @RequestParam(name = "pgLimit", defaultValue = "10") Integer limit) throws Exception {

        Map<String, String[]> queriedCriticalDimensions = webRequest.getParameterMap();
        String json = searchService.filterParts(partNumber, partTypeName, manufacturerName, name, description, inactive,
                turboTypeName, turboModelName, queriedCriticalDimensions, sortProperty, sortOrder, offset, limit);
        return new ResponseEntity<>(json, OK);
    }

    @RequestMapping(value = "/carmodelengineyears", method = GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> filterCarModelEngineYears(@RequestParam(required = false) String carModelEngineYear,
                                                            @RequestParam(required = false) String year,
                                                            @RequestParam(required = false) String make,
                                                            @RequestParam(required = false) String model,
                                                            @RequestParam(required = false) String engine,
                                                            @RequestParam(required = false) String fuel,
                                                            @RequestParam(required = false) String sortProperty,
                                                            @RequestParam(required = false) String sortOrder,
                                                            @RequestParam(defaultValue = "0") Integer offset,
                                                            @RequestParam(defaultValue = "10") Integer limit) throws Exception {
        String json = searchService.filterCarModelEngineYears(carModelEngineYear, year, make, model, engine, fuel,
                sortProperty, sortOrder, offset, limit);
        return new ResponseEntity<>(json, OK);
    }

    @RequestMapping(value = "/carmakes", method = GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> filterCarMake(@RequestParam(required = false) String make,
                                                @RequestParam(required = false) String sortProperty,
                                                @RequestParam(required = false) String sortOrder,
                                                @RequestParam(defaultValue = "0") Integer offset,
                                                @RequestParam(defaultValue = "10") Integer limit) throws Exception {
        String json = searchService.filterCarMakes(make, sortProperty, sortOrder, offset, limit);
        return new ResponseEntity<>(json, OK);
    }

    @RequestMapping(value = "/carmodels", method = GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> filterCarModels(@RequestParam(required = false) String model, String make,
                                                  @RequestParam(required = false) String sortProperty,
                                                  @RequestParam(required = false) String sortOrder,
                                                  @RequestParam(defaultValue = "0") Integer offset,
                                                  @RequestParam(defaultValue = "10") Integer limit) throws Exception {
        String json = searchService.filterCarModels(model, make, sortProperty, sortOrder, offset, limit);
        return new ResponseEntity<>(json, OK);
    }

    @RequestMapping(value = "/carengines", method = GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> filterCarEngines(@RequestParam(required = false) String engine, String fuelType,
                                                   @RequestParam(required = false) String sortProperty,
                                                   @RequestParam(required = false) String sortOrder,
                                                   @RequestParam(defaultValue = "0") Integer offset,
                                                   @RequestParam(defaultValue = "10") Integer limit) throws Exception {
        String json = searchService.filterCarEngines(engine, fuelType, sortProperty, sortOrder, offset, limit);
        return new ResponseEntity<>(json, OK);
    }

    @RequestMapping(value = "/carfueltypes", method = GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> filterCarFuelTypes(@RequestParam(required = false) String fuelType,
                                                     @RequestParam(required = false) String sortProperty,
                                                     @RequestParam(required = false) String sortOrder,
                                                     @RequestParam(defaultValue = "0") Integer offset,
                                                     @RequestParam(defaultValue = "10") Integer limit) throws Exception {
        String json = searchService.filterCarFuelTypes(fuelType, sortProperty, sortOrder, offset, limit);
        return new ResponseEntity<>(json, OK);
    }

    @ResponseBody
    @Secured("ROLE_SALES_NOTE_READ")
    @JsonView(View.DetailWithParts.class)
    @RequestMapping(value = "salesnotes", method = GET, produces = APPLICATION_JSON_VALUE)
    public String filterSalesNotes(@RequestParam(name = "partNumber", required = false) String partNumber,
                                  @RequestParam(name = "comment", required = false) String comment,
                                  @RequestParam(name = "primaryPartId", required = false) Long primaryPartId,
                                  @RequestParam("includePrimary") boolean includePrimary,
                                  @RequestParam("includeRelated") boolean includeRelated,
                                  @RequestParam("states") Set<SalesNoteState> states,
                                  @RequestParam("sortProperty") String sortProperty,
                                  @RequestParam("sortOrder") String sortOrder,
                                  @RequestParam("offset") int offset,
                                  @RequestParam("limit") int limit) {
        log.debug("partNumber: {}, comment: {}, primaryPartId: {}, includePrimary: {}, includeRelated: {}, " +
                "states: {}, sortProperty: {}, sortOrder: {}, offset: {}, limit: {}", partNumber, comment,
                primaryPartId, includePrimary, includeRelated, states, sortProperty, sortOrder, offset, limit);
        return searchService.filterSalesNotes(partNumber, comment, primaryPartId, states, includePrimary,
                includeRelated, sortProperty, sortOrder, offset, limit);
    }

    @RequestMapping(value = "/index/{partId}")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void indexPart(@PathVariable("partId") Long partId) throws Exception {
        new Thread(() -> {
            try {
                searchService.indexPart(partId);
            } catch (Exception e) {
                log.error("Indexing of the part (ID: {}) failed.", partId);
            }
        }).start();
    }


    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/indexing/start", method = POST)
    @ResponseBody
    @JsonView(View.Summary.class)
    public IndexingStatus startIndexing(Authentication authentication, @RequestBody Map<String, Boolean> toIndex) throws Exception {
        boolean indexParts = toIndex.getOrDefault("parts", false);
        boolean indexApplications = toIndex.getOrDefault("applications", false);
        boolean indexSalesNotes = toIndex.getOrDefault("salesNotes", false);
        log.debug("startIndexing: parts={}, applications={}, salesNotes={}", indexParts, indexApplications,
                indexSalesNotes);
        User user = null;
        if (authentication != null) {
            user = (User) authentication.getPrincipal();
        }
        return searchService.startIndexing(user, indexParts, indexApplications, indexSalesNotes);
    }

    @Secured("ROLE_READ")
    @ResponseBody
    @RequestMapping(value = "/indexing/status", method = GET)
    @JsonView(View.Summary.class)
    public IndexingStatus getIndexingStatus() throws Exception {
        return searchService.getIndexingStatus();
    }

}
