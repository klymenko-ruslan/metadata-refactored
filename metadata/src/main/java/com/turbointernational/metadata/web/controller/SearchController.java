package com.turbointernational.metadata.web.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.SalesNoteState;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.service.SearchService;
import com.turbointernational.metadata.service.SearchService.IndexingStatus;
import com.turbointernational.metadata.util.View;

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
                                              @RequestParam(name = "partTypeId", required = false) Long partTypeId,
                                              @RequestParam(name = "manufacturerName", required = false) String manufacturerName,
                                              @RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "description", required = false) String description,
                                              @RequestParam(name = "inactive", required = false) Boolean inactive,
                                              @RequestParam(name = "turboTypeName", required = false) String turboTypeName,
                                              @RequestParam(name = "turboModelName", required = false) String turboModelName,
                                              @RequestParam(name = "year", required = false) String year,
                                              @RequestParam(name = "make", required = false) String make,
                                              @RequestParam(name = "model", required = false) String model,
                                              @RequestParam(name = "engine", required = false) String engine,
                                              @RequestParam(name = "fuelType", required = false) String fuelType,
                                              WebRequest webRequest,
                                              @RequestParam(name = "pgSortProperty", required = false) String sortProperty,
                                              @RequestParam(name = "pgSortOrder", required = false) String sortOrder,
                                              @RequestParam(name = "pgOffset", defaultValue = "0") Integer offset,
                                              @RequestParam(name = "pgLimit", defaultValue = "10") Integer limit) throws Exception {

        Map<String, String[]> queriedCriticalDimensions = webRequest.getParameterMap();
        String json = searchService.filterParts(partNumber, partTypeId, manufacturerName, name, description, inactive,
                turboTypeName, turboModelName, year, make, model, engine, fuelType, queriedCriticalDimensions,
                sortProperty, sortOrder, offset, limit);
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

    @ResponseBody
    @Secured("ROLE_SALES_NOTE_READ")
    @JsonView(View.DetailWithParts.class)
    @RequestMapping(value = "changelog/sources", method = GET, produces = APPLICATION_JSON_VALUE)
    public String filterChangelogSources(@RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "description", required = false) String descripiton,
                                  @RequestParam(name = "url", required = false) String url,
                                  @RequestParam(name = "sourceNameId", required = false) Long sourceNameId,
                                  @RequestParam("sortProperty") String sortProperty,
                                  @RequestParam("sortOrder") String sortOrder,
                                  @RequestParam("offset") int offset,
                                  @RequestParam("limit") int limit) {
        log.debug("name: {}, description: {}, url: {}, sourceNameId: {}", name, descripiton, url, sourceNameId,
                sortProperty, sortOrder, offset, limit);
        return searchService.filterChanglelogSources(name, descripiton, url, sourceNameId,
                sortProperty, sortOrder, offset, limit);
    }

    // TODO: is this method used by anyone?
    @RequestMapping(value = "/index/{partId}")
    @ResponseBody
    @Secured("ROLE_READ")
    public void indexPart(@PathVariable("partId") Long partId) throws Exception {
        new Thread(() -> {
            try {
                searchService.indexPart(partId);
            } catch (Exception e) {
                log.error("Indexing of the part (ID: {}) failed.", partId);
            }
        }).start();
    }

    @RequestMapping(value = "/part/{partId}/index", method = PUT)
    @ResponseBody
    @Secured("ROLE_READ")
    public void indexPartSync(@PathVariable("partId") Long partId) throws Exception {
        searchService.indexPart(partId);
    }


    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/indexing/start", method = POST)
    @ResponseBody
    @JsonView(View.Summary.class)
    public IndexingStatus startIndexing(Authentication authentication, @RequestBody Map<String, Boolean> toIndex) throws Exception {
        boolean indexParts = toIndex.getOrDefault("parts", false);
        boolean indexApplications = toIndex.getOrDefault("applications", false);
        boolean indexSalesNotes = toIndex.getOrDefault("salesNotes", false);
        boolean indexChangelogSources = toIndex.getOrDefault("changelogSources", false);
        boolean recreateIndex = toIndex.getOrDefault("recreateIndex", false);
        log.debug("startIndexing: parts={}, applications={}, salesNotes={}, indexChangelogSources={}, recreateIndex={}",
                indexParts, indexApplications, indexSalesNotes, indexChangelogSources, recreateIndex);
        User user = null;
        if (authentication != null) {
            user = (User) authentication.getPrincipal();
        }
        return searchService.startIndexing(user, indexParts, indexApplications, indexSalesNotes,
                indexChangelogSources, recreateIndex);
    }

    @Secured("ROLE_READ")
    @ResponseBody
    @RequestMapping(value = "/indexing/status", method = GET)
    @JsonView(View.Summary.class)
    public IndexingStatus getIndexingStatus() throws Exception {
        return searchService.getIndexingStatus();
    }

    @RequestMapping(value = "/indexall", method = POST)
    @ResponseBody
    @PreAuthorize("hasIpAddress('127.0.0.1/32')")
    public void indexAll() throws Exception {
        searchService.indexAll();
    }

}
