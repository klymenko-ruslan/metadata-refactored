package com.turbointernational.metadata.web.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.TurboCarModelEngineYearService;
import com.turbointernational.metadata.util.View;

@RequestMapping("/metadata/part")
@RestController
public class TurboCarModelEngineYearController {

    @Autowired
    private TurboCarModelEngineYearService paService;

    public static class AddRequest {

        @JsonView(View.Summary.class)
        private Long[] cmeyIds;

        /**
         * Changelog source IDs which should be linked to the changelog. See
         * ticket #891 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] sourcesIds;

        /**
         * IDs of uploaded files which should be attached to this changelog. See
         * ticket #933 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] attachIds;

        @JsonView(View.Summary.class)
        private Integer[] chlogSrcRatings;

        @JsonView(View.Summary.class)
        private String chlogSrcLnkDescription;

        public Long[] getCmeyIds() {
            return cmeyIds;
        }

        public void setCmeyIds(Long[] cmeyIds) {
            this.cmeyIds = cmeyIds;
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

        public Long[] getAttachIds() {
            return attachIds;
        }

        public void setAttachIds(Long[] attachIds) {
            this.attachIds = attachIds;
        }

    }

    @RequestMapping(value = "{partId}/application", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<String> getApplications(@PathVariable("partId") Long partId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        String json = paService.getApplications(partId);
        return new ResponseEntity<>(json, headers, OK);
    }

    @Transactional
    @RequestMapping(value = "/{partId}/application", method = POST)
    @Secured("ROLE_APPLICATION_CRUD")
    public ResponseEntity<String> add(HttpServletRequest httpRequest, @PathVariable("partId") Long partId,
            @RequestBody AddRequest request) throws Exception {
        paService.add(httpRequest, partId, request.getCmeyIds(), request.getSourcesIds(), request.getChlogSrcRatings(),
                request.getChlogSrcLnkDescription(), request.getAttachIds());
        return new ResponseEntity<>("", OK);
    }

    @Transactional
    @RequestMapping(value = "/{partId}/application/{applicationId}", method = DELETE)
    @Secured("ROLE_APPLICATION_CRUD")
    public ResponseEntity<String> delete(@PathVariable("partId") Long partId,
            @PathVariable("applicationId") Long applicationId) throws Exception {
        paService.delete(partId, applicationId);
        String json = paService.getApplications(partId);
        return new ResponseEntity<>(json, OK);
    }

}
