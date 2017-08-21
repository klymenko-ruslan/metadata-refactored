package com.turbointernational.metadata.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.BOMItem;
import com.turbointernational.metadata.service.ArangoDbConnectorService.GetBomsResponse.Row;
import com.turbointernational.metadata.service.BOMService;
import com.turbointernational.metadata.service.BOMService.CreateBOMsRequest;
import com.turbointernational.metadata.service.BOMService.CreateBOMsResponse;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Bom;

@RequestMapping("/metadata/bom")
@Controller
public class BOMController {

    @Autowired
    private BOMService bomService;

    @ResponseBody
    @Transactional
    @Secured("ROLE_BOM")
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @JsonView(View.SummaryWithBOMDetail.class)
    public CreateBOMsResponse create(HttpServletRequest httpRequest, @RequestBody CreateBOMsRequest request)
            throws Exception {
        return bomService.createBOMs(httpRequest, request);
    }

    @RequestMapping(value = "/byParentPart/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public Bom[] getPartBOMs(@PathVariable("id") Long id) throws Exception {
        Row[] rows = bomService.getByParentId(id);
        return Bom.from(rows);
    }

    /*
    @RequestMapping(value = "/byParentPart/{partId}/type", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public List<BOMItem> getByParentAndTypeIds(@PathVariable("partId") Long partId, @RequestParam("typeId") Long typeId)
            throws Exception {
        return bomService.getByParentAndTypeIds(partId, typeId);
    }
    */

    @RequestMapping(value = "/part/{id}/parents", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public List<BOMItem> getParentsForBom(@PathVariable("id") Long id) throws Exception {
        return bomService.getParentsForBom(id);
    }

    @Transactional
    @RequestMapping(value = "/part/{id}/parents", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.SummaryWithBOMDetail.class)
    @Secured("ROLE_BOM")
    public BOMService.AddToParentBOMsResponse addToParentsBOMs(HttpServletRequest httpRequest,
            @PathVariable("id") Long partId, @RequestBody BOMService.AddToParentBOMsRequest request) throws Exception {
        return bomService.addToParentsBOMs(httpRequest, partId, request);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
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
