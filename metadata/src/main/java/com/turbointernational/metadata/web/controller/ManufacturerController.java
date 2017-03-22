package com.turbointernational.metadata.web.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.Manufacturer;
import com.turbointernational.metadata.service.ManufacturerService;
import com.turbointernational.metadata.service.ManufacturerService.DeleteResponse;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Page;

/**
 *
 * @author jrodriguez
 */

@RequestMapping(value = { "/metadata/other/manufacturer", "/other/manufacturer" })
@Controller
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;

    public static class ManufacturerRequest {

        private String name;

        private Long typeId;

        private Boolean notExternal;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getTypeId() {
            return typeId;
        }

        public void setTypeId(Long typeId) {
            this.typeId = typeId;
        }

        public Boolean getNotExternal() {
            return notExternal;
        }

        public void setNotExternal(Boolean notExternal) {
            this.notExternal = notExternal;
        }

    }

    @RequestMapping(value = "/all", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public List<Manufacturer> list() {
        return manufacturerService.findAllManufacturers();
    }

    @RequestMapping(value = "/filter", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_READ")
    public Page<Manufacturer> filter(@RequestParam(name = "fltrName", required = false) String fltrName,
            @RequestParam(name = "fltrTypeId", required = false) Long fltrTypeId,
            @RequestParam(name = "fltrNotExternal", required = false) Boolean fltrNotExternal,
            @RequestParam(name = "sortProperty", required = false) String sortProperty,
            @RequestParam(name = "sortOrder", required = false) String sortOrder, Integer offset, Integer limit) {
        return manufacturerService.filter(fltrName, fltrTypeId, fltrNotExternal, sortProperty, sortOrder, offset,
                limit);
    }

    @RequestMapping(value = "/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_MANUFACTURER_CRUD")
    public DeleteResponse getManufacturerRefs(@PathVariable("id") Long manufacturerId) {
        return manufacturerService.delete(manufacturerId);
    }

    @RequestMapping(value = "/name/unique", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_READ")
    public boolean isNameInique(@RequestParam(name = "manufacturerId", required = false) Long manufacturerId,
            @RequestParam(name = "name") String name) {
        Manufacturer manufacturer = manufacturerService.findManufacurerByName(name);
        if (manufacturer == null) {
            return true;
        } else {
            if (manufacturerId != null) {
                return manufacturerId.equals(manufacturer.getId());
            } else {
                return false;
            }
        }
    }

    @RequestMapping(method = POST)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_MANUFACTURER_CRUD")
    @Transactional
    public Manufacturer create(@RequestBody ManufacturerRequest request) {
        return manufacturerService.create(request.getName(), request.getTypeId(), request.getNotExternal());
    }

    @RequestMapping(value = "/{id}", method = PUT, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_MANUFACTURER_CRUD")
    @Transactional
    public Manufacturer update(@PathVariable("id") Long manufacturerId, @RequestParam(name = "name") String name,
            @RequestParam(name = "typeId") Long typeId, @RequestParam(name = "notExternal") boolean notExternal) {
        return manufacturerService.update(manufacturerId, name, typeId, notExternal);
    }

    @RequestMapping(value = "/{id}", method = GET, headers = "Accept=application/json")
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Manufacturer manufacturer = manufacturerService.findManufacturer(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (manufacturer == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(manufacturer.toJson(), headers, HttpStatus.OK);
    }

}
