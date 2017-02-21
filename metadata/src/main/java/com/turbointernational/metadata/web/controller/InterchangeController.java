package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.InterchangeDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.part.Interchange;
import com.turbointernational.metadata.service.InterchangeService;
import com.turbointernational.metadata.util.View;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
    private PartDao partDao;

    @Autowired
    private InterchangeDao interchangeDao;

    public static class CreateInterchangeRequest {

        @JsonView(View.Summary.class)
        private Long partId;

        @JsonView(View.Summary.class)
        private Long pickedPartId;

        /**
         * Changelog source IDs which should be linked to the changelog.
         * See ticket #891 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] sourcesIds;

        @JsonView(View.Summary.class)
        private Integer[] chlogSrcRatings;

        @JsonView(View.Summary.class)
        private String chlogSrcLnkDescription;

        public Long getPartId() {
            return partId;
        }

        public void setPartId(Long partId) {
            this.partId = partId;
        }

        public Long getPickedPartId() {
            return pickedPartId;
        }

        public void setPickedPartId(Long pickedPartId) {
            this.pickedPartId = pickedPartId;
        }

        public Long[] getSourcesIds() {
            return sourcesIds;
        }

        public void setSourcesIds(Long[] sourcesIds) {
            this.sourcesIds = sourcesIds;
        }

        public Integer[] getChlogSrcRatings() {
            return chlogSrcRatings;
        }

        public void setChlogSrcRatings(Integer[] chlogSrcRatings) {
            this.chlogSrcRatings = chlogSrcRatings;
        }

        public String getChlogSrcLnkDescription() {
            return chlogSrcLnkDescription;
        }

        public void setChlogSrcLnkDescription(String chlogSrcLnkDescription) {
            this.chlogSrcLnkDescription = chlogSrcLnkDescription;
        }

    }

    public static class UpdateInterchangeRequest {

        @JsonView(View.Summary.class)
        private int mergeChoice;

        /**
         * Changelog source IDs which should be linked to the changelog.
         * See ticket #891 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] sourcesIds;

        @JsonView(View.Summary.class)
        private Integer[] chlogSrcRatings;

        @JsonView(View.Summary.class)
        private String chlogSrcLnkDescription;

        public int getMergeChoice() {
            return mergeChoice;
        }

        public void setMergeChoice(int mergeChoice) {
            this.mergeChoice = mergeChoice;
        }

        public Long[] getSourcesIds() {
            return sourcesIds;
        }

        public void setSourcesIds(Long[] sourcesIds) {
            this.sourcesIds = sourcesIds;
        }

        public Integer[] getChlogSrcRatings() {
            return chlogSrcRatings;
        }

        public void setChlogSrcRatings(Integer[] chlogSrcRatings) {
            this.chlogSrcRatings = chlogSrcRatings;
        }

        public String getChlogSrcLnkDescription() {
            return chlogSrcLnkDescription;
        }

        public void setChlogSrcLnkDescription(String chlogSrcLnkDescription) {
            this.chlogSrcLnkDescription = chlogSrcLnkDescription;
        }

        @Override
        public String toString() {
            return "UpdateInterchangeRequest{" +
                    "mergeChoice=" + mergeChoice +
                    ", sourcesIds=" + Arrays.toString(sourcesIds) +
                    ", chlogSrcRatings=" + Arrays.toString(chlogSrcRatings) +
                    ", chlogSrcLnkDescription='" + chlogSrcLnkDescription + '\'' +
                    '}';
        }

    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    @Secured("ROLE_INTERCHANGE")
    public ResponseEntity<String> create(HttpServletRequest httpRequest, CreateInterchangeRequest request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        List<Long> partIds = new ArrayList<>(2);
        partIds.add(request.getPartId());
        partIds.add(request.getPickedPartId());
        interchangeService.create(httpRequest, partIds, request.getSourcesIds(), request.getChlogSrcRatings(),
                request.getChlogSrcLnkDescription());
        return new ResponseEntity<>("ok", headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value="/{partId}/part/{pickedPartId}", method = RequestMethod.PUT)
    @ResponseBody
    @Secured("ROLE_INTERCHANGE")
    public void update(HttpServletRequest httpRequest, HttpServletResponse response,
                       @PathVariable("partId") long partId, @PathVariable("pickedPartId") long pickedPartId,
                       @RequestBody UpdateInterchangeRequest request) throws Exception {
        log.debug("partId: {}, pickedPartid: {}, request: {}", partId, pickedPartId, request);
        int mergeChoice = request.getMergeChoice();
        if (mergeChoice != MERGE_OPT_PICKED_ALONE_TO_PART
                && mergeChoice != MERGE_OPT_PART_ALONE_TO_PICKED
                && mergeChoice != MERGE_OPT_PICKED_ALL_TO_PART) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        Long[] sourcesIds = request.getSourcesIds();
        Integer[] ratings = request.getChlogSrcRatings();
        String description = request.getChlogSrcLnkDescription();
        switch (mergeChoice) {
            case MERGE_OPT_PICKED_ALONE_TO_PART:
                interchangeService.mergePickedAloneToPart(httpRequest, partId, pickedPartId,
                        sourcesIds, ratings, description);
                break;
            case MERGE_OPT_PART_ALONE_TO_PICKED:
                interchangeService.mergePartAloneToPicked(httpRequest, partId, pickedPartId,
                        sourcesIds, ratings, description);
                break;
            case MERGE_OPT_PICKED_ALL_TO_PART:
                interchangeService.mergePickedAllToPart(httpRequest, partId, pickedPartId,
                        sourcesIds, ratings, description);
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
