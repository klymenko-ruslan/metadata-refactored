package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.bom.dto.CreateBomItemRequest;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.services.BOMService;
import com.turbointernational.metadata.services.BOMService.FoundBomRecursionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.web.BOMController.BOMErrorStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping("/metadata/bom")
@Controller
public class BOMController {

    @Autowired
    private BOMService bomService;

    private static final Logger log = LoggerFactory.getLogger(BOMController.class);

    enum BOMErrorStatus { OK, ASSERTION_ERROR, FOUND_BOM_RECURSION }

    static class BOMResult {

        @JsonView(View.Summary.class)
        private final BOMErrorStatus status;

        @JsonView(View.Summary.class)
        private final Long failedId;

        @JsonView(View.Summary.class)
        private final String message;

        BOMResult() {
            this.status = OK;
            this.failedId = null;
            this.message = null;
        }

        BOMResult(FoundBomRecursionException e) {
            this.status = FOUND_BOM_RECURSION;
            this.failedId = e.getFailedId();
            this.message = "Found BOM recursion. Failed part ID: " + this.failedId;
        }

        BOMResult(AssertionError e) {
            this.status = ASSERTION_ERROR;
            this.failedId = null;
            this.message = e.getMessage();
        }

    }

    @RequestMapping(value="rebuild/start", method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_ADMIN")
    public BOMService.IndexingStatus startRebuild(Authentication authentication,
                                                  @RequestBody Map<String, Boolean> options) throws Exception {
        User user = (User) authentication.getPrincipal();
        boolean indexBoms = options.get("indexBoms");
        BOMService.IndexingStatus status = bomService.startRebuild(user, indexBoms);
        return status;
    }

    @ResponseBody
    @Transactional
    @Secured("ROLE_BOM")
    @RequestMapping(method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @JsonView(View.Summary.class)
    public BOMResult create(@RequestBody CreateBomItemRequest request) {
        Long parentPartId = request.getParentPartId();
        Long childPartId = request.getChildPartId();
        Integer quantity = request.getQuantity();
        try {
            bomService.create(parentPartId, childPartId, quantity, true);
            return new BOMResult(); // OK
        } catch (FoundBomRecursionException e) {
            return new BOMResult(e);
        } catch (AssertionError e) {
            return new BOMResult(e);
        }
    }

    @RequestMapping(value = "/byParentPart/{id}", method = GET,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public List<BOMItem> getPartBOMs(@PathVariable("id") Long id) throws Exception {
        return bomService.getByParentId(id);
    }

    @RequestMapping(value = "/byParentPart/{partId}/type", method = GET,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public List<BOMItem> getByParentAndTypeIds(@PathVariable("partId") Long partId,
                                               @RequestParam("typeId") Long typeId) throws Exception {
        return bomService.getByParentAndTypeIds(partId, typeId);
    }

    @RequestMapping(value = "/part/{id}/parents", method = GET,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public List<BOMItem> getParentsForBom(@PathVariable("id") Long id) throws Exception {
        return bomService.getParentsForBom(id);
    }

    @Transactional
    @RequestMapping(value = "/part/{id}/parents", method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.SummaryWithBOMDetail.class)
    @Secured("ROLE_BOM")
    public BOMService.AddToParentBOMsResponse addToParentsBOMs(
            @PathVariable("id") Long partId,
            @RequestBody BOMService.AddToParentBOMsRequest request) throws Exception {
        return bomService.addToParentsBOMs(partId, request);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_BOM")
    public void update(@PathVariable("id") Long id, @RequestParam(required = true) Integer quantity) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        bomService.update(id, quantity);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseBody
    @Secured("ROLE_BOM")
    public void delete(@PathVariable("id") Long id) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        bomService.delete(id);
    }

}
