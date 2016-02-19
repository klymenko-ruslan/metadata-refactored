package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.bom.dto.CreateBomItemRequest;
import com.turbointernational.metadata.services.BOMService;
import com.turbointernational.metadata.services.BOMService.FoundBomRecursionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.turbointernational.metadata.web.BOMController.BOMErrorStatus.ASSERTION_ERROR;
import static com.turbointernational.metadata.web.BOMController.BOMErrorStatus.FOUND_BOM_RECURSION;
import static com.turbointernational.metadata.web.BOMController.BOMErrorStatus.OK;

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

    @ResponseBody
    @Transactional
    @Secured("ROLE_BOM")
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(View.Summary.class)
    public BOMResult create(@RequestBody CreateBomItemRequest request) {
        Long parentPartId = request.getParentPartId();
        Long childPartId = request.getChildPartId();
        Integer quantity = request.getQuantity();
        try {
            bomService.create(parentPartId, childPartId, quantity);
            return new BOMResult(); // OK
        } catch (FoundBomRecursionException e) {
            return new BOMResult(e);
        } catch (AssertionError e) {
            return new BOMResult(e);
        }
    }

    @RequestMapping(value = "/byParentPart/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public List<BOMItem> getByParentId(@PathVariable("id") Long id) throws Exception {
        return bomService.getByParentId(id);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_BOM")
    public void update(@PathVariable("id") Long id, @RequestParam(required = true) Integer quantity) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        bomService.update(id, quantity);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_BOM")
    public void delete(@PathVariable("id") Long id) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        bomService.delete(id);
    }

}
