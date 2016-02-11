package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.Interchange;
import com.turbointernational.metadata.domain.part.InterchangeDao;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.services.InterchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/metadata/interchange")
@Controller
public class InterchangeController {

    private final static Logger log = LoggerFactory.getLogger(InterchangeController.class);

    /**
     * Add picked part to interchange group of this part and remove picked part from its existing interchange.
     */
    private final static int MERGE_OPT_PICKED_ALONE_TO_PART = 1;

    /**
     * Add this part to interchange group of the picked part and remove this part from its existing interchange.
     */
    private final static int MERGE_OPT_PART_ALONE_TO_PICKED = 2;

    /**
     * Add the picked part and all its existing interchange parts to interchange group of this part.
     */
    private final static int MERGE_OPT_PICKED_ALL_TO_PART = 3;

    @Autowired
    private InterchangeService interchangeService;

    @Autowired
    ChangelogDao changelogDao;

    @Autowired
    PartDao partDao;

    @Autowired
    InterchangeDao interchangeDao;

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_INTERCHANGE")
    public ResponseEntity<String> create(@RequestBody String json) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        // Create the object
        Interchange interchange = Interchange.fromJsonToInterchange(json);
        interchangeService.create(interchange);
        return new ResponseEntity<>("ok", headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value="/{partId}/part/{pickedPartId}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_INTERCHANGE")
    public void update(@PathVariable("partId") long partId, @PathVariable("pickedPartId") long pickedPartId,
                       @RequestParam(name = "mergeChoice", required = true) int mergeChoice,
                       HttpServletResponse response) throws Exception {
        log.debug("partId: {}, pickedPartid: {}, mergeChoice: {}", partId, pickedPartId, mergeChoice);
        if (mergeChoice != MERGE_OPT_PICKED_ALONE_TO_PART
                && mergeChoice != MERGE_OPT_PART_ALONE_TO_PICKED
                && mergeChoice != MERGE_OPT_PICKED_ALL_TO_PART) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        switch (mergeChoice) {
            case MERGE_OPT_PICKED_ALONE_TO_PART:
                interchangeService.mergePickedAloneToPart(partId, pickedPartId);
                break;
            case MERGE_OPT_PART_ALONE_TO_PICKED:
                interchangeService.mergePartAloneToPicked(partId, pickedPartId);
                break;
            case MERGE_OPT_PICKED_ALL_TO_PART:
                interchangeService.mergePickedAllToPart(partId, pickedPartId);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
        }
    }
    
    @ResponseBody
    // Commented as fix for #536. @Secured("ROLE_INTERCHANGE")
    @Secured("ROLE_READ") // Ticket #536.
    @JsonView({View.SummaryWithInterchangeParts.class})
    @RequestMapping(value="{id}", method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Interchange get(@PathVariable("id") long interchangeId) {
        return interchangeService.findById(interchangeId);
    }
    
    @Transactional
    @RequestMapping(value = "/part/{partId}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_INTERCHANGE")
    public void delete(@PathVariable("partId") long partId) {
        interchangeService.leaveInterchangeableGroup(partId);
    }

}
