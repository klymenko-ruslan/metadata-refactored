package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnum;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.services.CriticalDimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 06.04.16.
 */
@Controller
@RequestMapping(value = {"/criticaldimension", "/metadata/criticaldimension"})
public class CriticalDimensionController {

    @Autowired
    private CriticalDimensionService criticalDimensionService;

    @Secured("ROLE_READ")
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/byparttypes", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<Long, List<CriticalDimension>> getCriticalDimensionsByPartTypes() {
        return criticalDimensionService.getCriticalDimensionsCache();
    }

    @Secured("ROLE_READ")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/{partId}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CriticalDimension> findForThePart(@PathVariable("partId") long partId) {
        return criticalDimensionService.findForThePart(partId);
    }

    @Secured("ROLE_ADMIN")
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/enum/list", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CriticalDimensionEnum> getAllCritDimEnums() {
        return criticalDimensionService.getAllCritDimEnums();
    }

    @Secured("ROLE_ADMIN")
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
    public CriticalDimensionEnumVal addCritDimEnumVal(@PathVariable("enumId") Integer enumId, @RequestBody CriticalDimensionEnumVal cdev) {
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
    public CriticalDimensionEnumVal findCritDimEnumValByName(@PathVariable("enumId") Integer enumId, @RequestParam("name") String name) {
        return criticalDimensionService.findCritDimEnumValByName(enumId, name);
    }

}
