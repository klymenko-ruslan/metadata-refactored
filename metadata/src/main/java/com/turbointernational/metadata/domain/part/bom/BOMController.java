package com.turbointernational.metadata.domain.part.bom;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.bom.dto.CreateBomItemRequest;
import com.turbointernational.metadata.services.BOMService;
import com.turbointernational.metadata.web.View;
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

@RequestMapping("/metadata/bom")
@Controller
public class BOMController {

    @Autowired
    private BOMService bomService;

    private static final Logger log = LoggerFactory.getLogger(BOMController.class);

    @ResponseBody
    @Transactional
    @Secured("ROLE_BOM")
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody CreateBomItemRequest request) throws Exception {
        Long parentPartId = request.getParentPartId();
        Long childPartId = request.getChildPartId();
        Integer quantity = request.getQuantity();
        bomService.create(parentPartId, childPartId, quantity);
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
