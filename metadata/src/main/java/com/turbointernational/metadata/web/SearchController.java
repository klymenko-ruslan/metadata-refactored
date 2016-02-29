package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.salesnote.SalesNoteState;
import com.turbointernational.metadata.services.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
    public ResponseEntity<String> filterParts(@RequestParam(required = false) String partNumber,
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
                sealType, coolType, turboType, turboModel, sortProperty, sortOrder, offset, limit);
        return new ResponseEntity<>(json, HttpStatus.OK);
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
        return new ResponseEntity<>(json, HttpStatus.OK);
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
        return new ResponseEntity<>(json, HttpStatus.OK);
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
        return new ResponseEntity<>(json, HttpStatus.OK);
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
        return new ResponseEntity<>(json, HttpStatus.OK);
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
        return new ResponseEntity<>(json, HttpStatus.OK);
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
        });
    }

    @RequestMapping(value = "/part/indexAll")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void indexPartAll() throws Exception {
        new Thread(() -> {
            try {
                searchService.indexAllParts();
            } catch (Exception e) {
                log.error("Indexing of all parts failed.");
            }
        });
    }

    @RequestMapping(value = "/application/indexAll")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void indexApplicationAll() throws Exception {
        new Thread(() -> {
            try {
                searchService.indexAllApplications();
            } catch (Exception e) {
                log.error("Indexing of applications failed.");
            }
        });
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/salesnotesparts/indexAll")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void indexSalesNotesPartsAll() throws Exception {
        new Thread(() -> {
            try {
                searchService.indexAllSalesNotes();
            } catch (Exception e) {
                log.error("Indexing of sales notes parts failed.", e);
            }
        }).start();
    }
}
