package com.turbointernational.metadata.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.KitComponentDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.service.KitComponentService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.CommonComponent;
import com.turbointernational.metadata.web.dto.Page;

@RequestMapping("/metadata/kitcomponent")
@Controller
public class KitComponentController {

    @Qualifier("transactionManager")
    @Autowired
    private PlatformTransactionManager txManager; // JPA

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

    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class CreateResponse {

        @JsonView(View.Summary.class)
        private boolean failure;

        @JsonView(View.Summary.class)
        private String errorMessage;

        @JsonView(View.Summary.class)
        private CommonComponent commonComponent;

        public boolean isFailure() {
            return failure;
        }

        public void setFailure(boolean failure) {
            this.failure = failure;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public CommonComponent getCommonComponent() {
            return commonComponent;
        }

        public void setCommonComponent(CommonComponent commonComponent) {
            this.commonComponent = commonComponent;
        }

    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Secured("ROLE_ALTER_PART")
    @ResponseBody
    @JsonView(View.CommonComponentKitAndPart.class)
    public CreateResponse create(@RequestBody CreateRequest createRequest) {
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW);
        CreateResponse retVal = tt.execute(ts -> {
            CreateResponse response = new CreateResponse();
            CommonComponent commonComponent = null;
            try {
                commonComponent = kitComponentService.create(createRequest.getKitId(), createRequest.getPartId(),
                        createRequest.getExclude());
                response.setFailure(false);
                response.setCommonComponent(commonComponent);
            } catch (DataAccessException e) {
                response.setFailure(true);
                response.setErrorMessage(e.getMostSpecificCause().getMessage());
                ts.setRollbackOnly();
            }
            return response;
        });
        return retVal;
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
    public void update(@PathVariable("id") Long id, @RequestParam(name = "exclude", required = true) Boolean exclude)
            throws Exception {
        kitComponentService.update(id, exclude);
    }

    @Transactional
    @RequestMapping(value = "/kit/{kitId}/{ids}", method = DELETE)
    @ResponseBody
    @JsonView(View.CommonComponentPart.class)
    @Secured("ROLE_ALTER_PART")
    public List<CommonComponent> deleteInKit(@PathVariable("kitId") Long kitId, @PathVariable("ids") Long[] ids)
            throws Exception {
        kitComponentService.delete(ids);
        Page<CommonComponent> pg = kitComponentService.list(kitId, null, null, null, null, null);
        return pg.getRecs();

    }

    @Transactional
    @RequestMapping(value = "/part/{partId}/{ids}", method = DELETE)
    @ResponseBody
    @JsonView(View.CommonComponentKit.class)
    @Secured("ROLE_ALTER_PART")
    public List<CommonComponent> deleteInPart(@PathVariable("partId") Long partId, @PathVariable("ids") Long[] ids)
            throws Exception {
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
