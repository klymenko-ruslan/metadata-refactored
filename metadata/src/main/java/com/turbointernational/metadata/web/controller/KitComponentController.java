package com.turbointernational.metadata.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.KitComponentDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.part.types.kit.KitComponent;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.service.KitComponentService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.CommonComponent;
import com.turbointernational.metadata.web.dto.Page;

@RequestMapping("/metadata/kitcomponent")
@Controller
public class KitComponentController {

    @Autowired
    private KitComponentService kitComponentService;

    @Autowired
    ChangelogService changelogService;

    @Autowired
    PartDao partDao;

    @Autowired
    KitComponentDao kitComponentDao;

    public static class CreateRequest {

        private Long kitId;

        private Long partId;

        private Boolean exclude;

        public Long getKitId() {
            return kitId;
        }

        public void setKitId(Long kitId) {
            this.kitId = kitId;
        }

        public Long getPartId() {
            return partId;
        }

        public void setPartId(Long partId) {
            this.partId = partId;
        }

        public Boolean getExclude() {
            return exclude;
        }

        public void setExclude(Boolean exclude) {
            this.exclude = exclude;
        }

    }

    @Transactional
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Secured("ROLE_ALTER_PART")
    @ResponseBody
    @JsonView(View.Summary.class)
    public KitComponent create(@RequestBody CreateRequest createRequest) throws Exception {
        return kitComponentService.create(createRequest.getKitId(), createRequest.getPartId(),
                createRequest.getExclude());
    }

    @Transactional
    @RequestMapping(value = "/listbykit/{kitId}", method = GET, produces = APPLICATION_JSON_VALUE)
    @Secured("ROLE_ALTER_PART")
    @ResponseBody
    @JsonView(View.CommonComponentPart.class)
    public Page<CommonComponent> listByKit(@PathVariable("kitId") Long kitId,
            @RequestParam(name = "sortProperty", required = false) String sortProperty,
            @RequestParam(name = "sortOrder", required = false) String sortOrder,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit) throws Exception {
        return kitComponentService.list(kitId, null, sortProperty, sortOrder, offset, limit);
    }

    @Transactional
    @RequestMapping(value = "/listbypart/{partId}", method = GET, produces = APPLICATION_JSON_VALUE)
    @Secured("ROLE_ALTER_PART")
    @ResponseBody
    @JsonView(View.CommonComponentKit.class)
    public Page<CommonComponent> listByPart(@PathVariable("partId") Long partId,
            @RequestParam(name = "sortProperty", required = false) String sortProperty,
            @RequestParam(name = "sortOrder", required = false) String sortOrder,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit) throws Exception {
        return kitComponentService.list(null, partId, sortProperty, sortOrder, offset, limit);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = PUT)
    @ResponseBody
    @Secured("ROLE_ALTER_PART")
    public void update(@PathVariable("id") Long id, @RequestParam(name = "exclude", required = true) Boolean exclude) throws Exception {
        kitComponentService.update(id, exclude);
    }

    @Transactional
    @RequestMapping(value = "/kit/{kitId}/{ids}", method = DELETE)
    @ResponseBody
    @JsonView(View.CommonComponentPart.class)
    @Secured("ROLE_ALTER_PART")
    public List<CommonComponent> deleteInKit(@PathVariable("kitId") Long kitId, @PathVariable("ids") Long[] ids) throws Exception {
        kitComponentService.delete(ids);
        Page<CommonComponent> pg = kitComponentService.list(kitId, null, null, null, null, null);
        return pg.getRecs();

    }

    @Transactional
    @RequestMapping(value = "/part/{partId}/{ids}", method = DELETE)
    @ResponseBody
    @JsonView(View.CommonComponentKit.class)
    @Secured("ROLE_ALTER_PART")
    public List<CommonComponent> deleteInPart(@PathVariable("partId") Long partId, @PathVariable("ids") Long[] ids) throws Exception {
        kitComponentService.delete(ids);
        Page<CommonComponent> pg = kitComponentService.list(null, partId, null, null, null, null);
        return pg.getRecs();

    }

    @RequestMapping(value = "/commonturbotypes/{partId}", method = GET)
    @ResponseBody
    @JsonView(View.CommonComponentKit.class)
    @Secured("ROLE_ALTER_PART")
    public List<CommonComponent> listCommonTurboTypes(@PathVariable("partId") Long partId /* Turbo */) {
        return kitComponentService.listCommonTurboTypes(partId);
    }

}
