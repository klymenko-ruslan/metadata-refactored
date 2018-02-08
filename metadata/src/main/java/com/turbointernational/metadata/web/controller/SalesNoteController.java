package com.turbointernational.metadata.web.controller;

import static com.turbointernational.metadata.util.DownloadUtils.download;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.SalesNote;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.exception.RemovePrimaryPartException;
import com.turbointernational.metadata.service.SalesNoteService;
import com.turbointernational.metadata.util.View;

@Controller
@RequestMapping(value = { "/other/salesNote", "/metadata/other/salesNote" })
public class SalesNoteController {

    private static final Logger log = LoggerFactory.getLogger(SalesNoteController.class);

    @Autowired
    private SalesNoteService salesNoteService;

    @Value("attachments.salesNote")
    private File attachmentDir;

    public static class CreateSalesNoteRequest implements Serializable {

        private static final long serialVersionUID = -4604795204191095582L;

        @NotNull
        @JsonView(View.Summary.class)
        private Long primaryPartId;

        @NotNull
        @JsonView(View.Summary.class)
        private String comment;

        /**
         * Changelog source IDs which should be linked to the changelog. See ticket #891 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] sourcesIds;

        /**
         * IDs of uploaded files which should be attached to this changelog. See ticket #933 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] attachIds;

        @JsonView(View.Summary.class)
        private Integer[] chlogSrcRatings;

        @JsonView(View.Summary.class)
        private String chlogSrcLnkDescription;

        public Long getPrimaryPartId() {
            return primaryPartId;
        }

        public void setPrimaryPartId(Long primaryPartId) {
            this.primaryPartId = primaryPartId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
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

    public static class UpdateSalesNoteRequest implements Serializable {

        private static final long serialVersionUID = -7660933380532503595L;

        @NotNull
        private String comment;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "primarypartidforthepart", method = GET, produces = APPLICATION_JSON_VALUE)
    public Long findPrimaryPartIdForThePart(@RequestParam(name = "partId") long partId) {
        return salesNoteService.findPrimaryPartIdForThePart(partId);
    }

    // <editor-fold defaultstate="collapsed" desc="CRUDS">
    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Secured("ROLE_SALES_NOTE_SUBMIT")
    @Transactional
    @JsonView(View.DetailWithPartsAndAttachments.class)
    public @ResponseBody SalesNote createSalesNote(HttpServletRequest httpRequest,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @RequestBody CreateSalesNoteRequest request) {
        SalesNote salesNote = salesNoteService.createSalesNote(httpRequest, user, request.getPrimaryPartId(),
                request.getComment(), request.getSourcesIds(), request.getChlogSrcRatings(),
                request.getChlogSrcLnkDescription(), request.getAttachIds());
        return salesNote;
    }

    @RequestMapping(value = "{noteId}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_READ")
    @JsonView(View.DetailWithPartsAndAttachments.class)
    public SalesNote getSalesNote(@PathVariable("noteId") Long noteId) {
        SalesNote salesNote = salesNoteService.getSalesNote(noteId);
        return salesNote;
    }

    @ResponseBody
    @Secured("ROLE_SALES_NOTE_SUBMIT")
    @Transactional
    @RequestMapping(value = "{noteId}", method = POST, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public void updateSalesNote(HttpServletRequest request, @PathVariable("noteId") Long noteId,
            @RequestBody UpdateSalesNoteRequest updateRequest) {
        salesNoteService.updateSalesNote(request, noteId, updateRequest.getComment());
    }

    @ResponseBody
    @Secured("ROLE_SALES_NOTE_SUBMIT")
    @Transactional
    @RequestMapping(value = "{noteId}", method = DELETE)
    public void removeSalesNote(@PathVariable("noteId") Long noteId) {
        salesNoteService.removeSalesNote(noteId);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Related Parts">
    @Transactional
    @ResponseBody
    @JsonView(View.DetailWithPartsAndAttachments.class)
    @RequestMapping(value = "{salesNoteId}/parts", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SalesNote addRelatedPart(HttpServletRequest request,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("salesNoteId") long salesNoteId, @RequestBody Long[] partIds) {
        SalesNote retVal = salesNoteService.addRelatedPart(request, user, salesNoteId, partIds);
        return retVal;
    }

    @Transactional
    @ResponseBody
    @JsonView(View.DetailWithPartsAndAttachments.class)
    @RequestMapping(value = "{salesNoteId}/part/{partId}", method = DELETE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SalesNote deleteRelatedPart(HttpServletRequest request, @PathVariable("salesNoteId") Long salesNoteId,
            @PathVariable("partId") Long partId) throws RemovePrimaryPartException {
        SalesNote salesNote = salesNoteService.deleteRelatedPart(request, salesNoteId, partId);
        return salesNote;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Attachments">
    @Transactional
    @RequestMapping(value = "{salesNoteId}/attachment/{attachmentId}", method = GET)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_READ")
    public void getAttachment(HttpServletResponse response, @PathVariable("salesNoteId") Long salesNoteId,
            @PathVariable("attachmentId") Long attachmentId) throws Exception {
        try {
            SalesNoteService.AttachmentDto attachment = salesNoteService.getAttachment(salesNoteId, attachmentId);
            download(attachment.getFile(), attachment.getName(), response);
        } catch (IOException e) {
            log.warn("Couldn't load attachment file.", e);
            throw e;
        }
    }

    @RequestMapping(value = "{salesNoteId}/attachment", method = POST)
    @ResponseBody
    @JsonView(View.DetailWithPartsAndAttachments.class)
    @Secured("ROLE_SALES_NOTE_SUBMIT")
    public SalesNote addAttachment(HttpServletRequest request,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("salesNoteId") long salesNoteId, @RequestParam(value = "name", required = false) String name,
            @RequestBody byte[] file) throws IOException {
        return salesNoteService.addAttachment(request, user, salesNoteId, name, file);
    }

    @Transactional
    @RequestMapping(value = "{salesNoteId}/attachment/{attachmentId}", method = DELETE)
    @JsonView(View.DetailWithPartsAndAttachments.class)
    @Secured("ROLE_SALES_NOTE_SUBMIT")
    @ResponseBody
    public SalesNote deleteAttachment(HttpServletRequest request, @PathVariable("salesNoteId") Long salesNoteId,
            @PathVariable("attachmentId") Long attachmentId) {
        return salesNoteService.deleteAttachment(request, salesNoteId, attachmentId);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="State changes">
    @RequestMapping(value = "{noteId}/submit", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_SUBMIT")
    @Transactional
    public void submit(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") Long noteId) {
        salesNoteService.submit(user, noteId);
    }

    @RequestMapping(value = "{noteId}/approve", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_APPROVE")
    @Transactional
    public void approve(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") Long noteId) {
        salesNoteService.approve(user, noteId);
    }

    @RequestMapping(value = "{noteId}/reject", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_REJECT")
    @Transactional
    public void reject(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") Long noteId) {
        salesNoteService.reject(user, noteId);
    }

    @RequestMapping(value = "{noteId}/publish", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_PUBLISH")
    @Transactional
    public void publish(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") Long noteId) {
        salesNoteService.publish(user, noteId);
    }

    @RequestMapping(value = "{noteId}/retract", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_RETRACT")
    @Transactional
    public void retract(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") Long noteId) {
        salesNoteService.retract(user, noteId);
    }
    // </editor-fold>

}
