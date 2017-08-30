package com.turbointernational.metadata.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;

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
import com.turbointernational.metadata.service.ArangoDbConnectorService.CreateAltBomResponse;
import com.turbointernational.metadata.service.ArangoDbConnectorService.GetBomsResponse.Row;
import com.turbointernational.metadata.service.BOMService;
import com.turbointernational.metadata.service.BOMService.CreateBOMsRequest;
import com.turbointernational.metadata.service.BOMService.CreateBOMsResponse;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Bom;
import com.turbointernational.metadata.web.dto.PartGroup;

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

    @RequestMapping(value = "/byParentPart/{partId}/type", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public Bom[] getByParentAndTypeIds(@PathVariable("partId") Long partId, @RequestParam("typeId") Long typeId)
            throws Exception {
        Row[] rows = bomService.getByParentAndTypeIds(partId, typeId);
        return Bom.from(rows);
    }

    @RequestMapping(value = "/byParentPart/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public Bom[] getPartBOMs(@PathVariable("id") Long id) throws Exception {
        Row[] rows = bomService.getByParentId(id);
        return Bom.from(rows);
    }

    @RequestMapping(value = "/part/{id}/parents", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.SummaryWithBOMDetail.class)
    public Bom[] getParentsForBom(@PathVariable("id") Long partId) throws Exception {
        Row[] rows = bomService.getParentsForBom(partId);
        return Bom.from(rows);
    }

    @Transactional
    @RequestMapping(value = "/part/{id}/parents", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.SummaryWithBOMDetail.class)
    @Secured("ROLE_BOM")
    public CreateBOMsResponse addToParentsBOMs(HttpServletRequest httpRequest, @PathVariable("id") Long partId,
            @RequestBody BOMService.AddToParentBOMsRequest request) throws Exception {
        return bomService.addToParentsBOMs(httpRequest, partId, request);
    }

    @Transactional
    @RequestMapping(value = "/{parentPartId}/descendant/{childPartId}", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_BOM")
    public void update(@PathVariable("parentPartId") Long parentPartId, @PathVariable("childPartId") Long childPartId,
            @RequestParam(required = true) Integer quantity) throws Exception {
        bomService.update(parentPartId, childPartId, quantity);
    }

    @Transactional
    @RequestMapping(value = "/{parentPartId}/descendant/{childPartId}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_BOM")
    public Bom[] delete(@PathVariable("parentPartId") Long parentPartId, @PathVariable("childPartId") Long childPartId)
            throws Exception {
        return bomService.delete(parentPartId, childPartId);
    }

    @RequestMapping(value = "/{parentPartId}/descendant/{childPartId}/alternatives", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_BOM")
    public PartGroup[] getAlternatives(@PathVariable("parentPartId") Long parentPartId,
            @PathVariable("childPartId") Long childPartId) {
        return bomService.getAlternatives(parentPartId, childPartId);
    }
    
    public static class CreateAltBomRequest {
        
        private Long altHeaderId;

        public Long getAltHeaderId() {
			return altHeaderId;
		}

		public void setAltHeaderId(Long altHeaderId) {
			this.altHeaderId = altHeaderId;
		}
        
    }

    @Secured("ROLE_BOM_ALT")
    @ResponseBody
    @RequestMapping(value = "/{parentPartId}/descendant/{childPartId}/alternatives/{altPartId}", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Long createBomAlternative(@PathVariable("parentPartId") Long parentPartId,
            @PathVariable("childPartId") Long childPartId, @PathVariable("altPartId") Long altPartId,
            @RequestBody CreateAltBomRequest request) throws Exception {
        CreateAltBomResponse response = bomService.createAltBom(parentPartId, childPartId, request.getAltHeaderId(),
            altPartId);
        Long altBomId = response.getAltHeaderId();
        return altBomId;
    }

    @Secured("ROLE_BOM_ALT")
    @ResponseBody
    @RequestMapping(value = "/alternatives/{altPartId}/headers/{altHeaderId}", method = DELETE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public PartGroup[] removePartFromAltBom(@PathVariable("altHeaderId") Long altHeaderId,
            @PathVariable("altPartId") Long altPartId) throws Exception {
        return bomService.deleteAltBom(altHeaderId, altPartId);
    }

}
