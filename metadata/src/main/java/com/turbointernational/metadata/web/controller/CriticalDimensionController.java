package com.turbointernational.metadata.web.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CriticalDimension;
import com.turbointernational.metadata.entity.CriticalDimensionEnum;
import com.turbointernational.metadata.entity.CriticalDimensionEnumVal;
import com.turbointernational.metadata.service.CriticalDimensionService;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 06.04.16.
 */
@Controller
@RequestMapping(value = { "/criticaldimension", "/metadata/criticaldimension" })
public class CriticalDimensionController {

    enum PartTypesIndexByEnum {
        ID, NAME
    }

    @Autowired
    private CriticalDimensionService criticalDimensionService;

    @Secured("ROLE_READ")
    @RequestMapping(value = "/byparttypes", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getCriticalDimensionsByPartTypes(
            @RequestParam("indexBy") PartTypesIndexByEnum indexBy) {
        String body;
        switch (indexBy) {
        case ID:
            body = criticalDimensionService.getCriticalDimensionsCacheByIdAsJson();
            break;
        case NAME:
            body = criticalDimensionService.getCriticalDimensionsCacheByNameAsJson();
            break;
        default:
            throw new AssertionError("Unsupported value of 'indexBy' parameter: " + indexBy);
        }
        return new ResponseEntity<>(body, OK);
    }

    @Secured("ROLE_READ")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/{partId}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CriticalDimension> findForThePart(@PathVariable("partId") long partId) {
        List<CriticalDimension> criticalDimensions = criticalDimensionService.findForThePart(partId);
        return criticalDimensions;
    }

    @Secured("ROLE_READ")
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/enum/list", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CriticalDimensionEnum> getAllCritDimEnums() {
        return criticalDimensionService.getAllCritDimEnums();
    }

    @Secured("ROLE_READ")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/enums/vals", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CriticalDimensionEnum> getAllCritDimEnumVals() {
        return criticalDimensionService.getAllCritDimEnums();
    }

    @Secured("ROLE_READ")
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/enum/{enumId}/list", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CriticalDimensionEnumVal> getCritDimEnumVals(@PathVariable("enumId") Integer enumId) {
        return criticalDimensionService.getCritDimEnumVals(enumId);
    }

    @Secured("ROLE_ADMIN")
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/enum", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public CriticalDimensionEnum addCritDimEnum(@RequestBody CriticalDimensionEnum cde) {
        return criticalDimensionService.addCritDimEnum(cde);
    }

    @Secured("ROLE_ADMIN")
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/enum/{enumId}/item", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public CriticalDimensionEnumVal addCritDimEnumVal(@PathVariable("enumId") Integer enumId,
            @RequestBody CriticalDimensionEnumVal cdev) {
        cdev.setCriticalDimensionEnumId(enumId);
        return criticalDimensionService.addCritDimEnumVal(cdev);
    }

    @RequestMapping(value = "/enum/{enumId}", method = PUT)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_ADMIN")
    public CriticalDimensionEnum updateCritDimEnum(@RequestBody CriticalDimensionEnum cde) {
        return criticalDimensionService.updateCritDimEnum(cde);
    }

    @RequestMapping(value = "/enum/item/{id}", method = PUT)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_ADMIN")
    public CriticalDimensionEnumVal updateCritDimEnumVal(@RequestBody CriticalDimensionEnumVal cdev) {
        return criticalDimensionService.updateCritDimEnumVal(cdev);
    }

    @RequestMapping(value = "/enum/{enumId}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void removeCritDimEnum(@PathVariable("enumId") Integer enumId) {
        criticalDimensionService.removeCritDimEnum(enumId);
    }

    @RequestMapping(value = "/enum/item/{itmId}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void removeCritDimEnumVal(@PathVariable("itmId") Integer itmId) {
        criticalDimensionService.removeCritDimEnumVal(itmId);
    }

    @Secured("ROLE_ADMIN")
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/enum", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public CriticalDimensionEnum findCritDimEnumByName(@RequestParam("name") String name) {
        return criticalDimensionService.findCritDimEnumByName(name);
    }

    @Secured("ROLE_ADMIN")
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/enum/{enumId}/items", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public CriticalDimensionEnumVal findCritDimEnumValByName(@PathVariable("enumId") Integer enumId,
            @RequestParam("name") String name) {
        return criticalDimensionService.findCritDimEnumValByName(enumId, name);
    }

}
