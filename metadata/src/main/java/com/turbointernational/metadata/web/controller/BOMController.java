package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.BOMItem;
import com.turbointernational.metadata.service.BOMService.CreateBOMsResponse;
import com.turbointernational.metadata.service.BOMService.CreateBOMsRequest;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.service.BOMService;
import com.turbointernational.metadata.service.BOMService.FoundBomRecursionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping("/metadata/bom")
@Controller
public class BOMController {

    @Autowired
    private BOMService bomService;

    private static final Logger log = LoggerFactory.getLogger(BOMController.class);

    @RequestMapping(value="rebuild/start", method = POST)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_ADMIN")
    public BOMService.IndexingStatus startRebuild(Authentication authentication,
                                                  @RequestBody Map<String, Boolean> options) throws Exception {
        User user = (User) authentication.getPrincipal();
        boolean indexBoms = options.getOrDefault("indexBoms", false);
        BOMService.IndexingStatus status = bomService.startRebuild(user, null, indexBoms);
        return status;
    }

    @RequestMapping(value="rebuild/status", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    public BOMService.IndexingStatus getRebuildStatus() throws Exception {
        BOMService.IndexingStatus status = bomService.getRebuildStatus();
        return status;
    }

    @ResponseBody
    @Transactional
    @Secured("ROLE_BOM")
    @RequestMapping(method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @JsonView(View.SummaryWithBOMDetail.class)
    public CreateBOMsResponse create(HttpServletRequest httpRequest, @RequestBody CreateBOMsRequest request)
            throws Exception {
        return bomService.createBOMs(httpRequest, request);
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
