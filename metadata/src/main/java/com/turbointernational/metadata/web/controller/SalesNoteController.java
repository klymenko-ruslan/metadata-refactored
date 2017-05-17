package com.turbointernational.metadata.web.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.SalesNote;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.exception.RemovePrimaryPartException;
import com.turbointernational.metadata.service.SalesNoteService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.CreateSalesNoteRequest;
import com.turbointernational.metadata.web.dto.UpdateSalesNoteRequest;

@Controller
@RequestMapping("/metadata/other/salesNote")
@SuppressWarnings("deprecation")
public class SalesNoteController {

    private static final Logger log = LoggerFactory.getLogger(SalesNoteController.class);

    @Autowired
    private SalesNoteService salesNoteService;

    @Value("attachments.salesNote")
    private File attachmentDir;

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
    @ResponseBody
    @Transactional
    @RequestMapping(value = "{salesNoteId}/part/{partId}", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public void addRelatedPart(HttpServletRequest request,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("salesNoteId") long salesNoteId, @PathVariable("partId") long partId) {
        salesNoteService.addRelatedPart(request, user, salesNoteId, partId);
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "{salesNoteId}/part/{partId}", method = RequestMethod.DELETE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public void deleteRelatedPart(HttpServletRequest request, @PathVariable("salesNoteId") Long salesNoteId,
            @PathVariable("partId") Long partId) throws RemovePrimaryPartException {
        salesNoteService.deleteRelatedPart(request, salesNoteId, partId);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Attachments">
    @RequestMapping(value = "attachment/{id}", method = GET)
    @Secured("ROLE_SALES_NOTE_READ")
    public @ResponseBody ResponseEntity<byte[]> getAttachment(@PathVariable Long id) throws Exception {
        try {
            SalesNoteService.AttachmentDto attachmentDto = salesNoteService.getAttachment(id);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/octet-stream");
            headers.add("Content-Disposition", "attachment; filename=\"" + attachmentDto.getFileName() + "\"");
            return new ResponseEntity<>(attachmentDto.getContent(), headers, OK);
        } catch (IOException e) {
            log.warn("Couldn't load attachment file.", e);
            throw e;
        }
    }

    @RequestMapping(value = "{salesNoteId}/attachment", method = POST)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_SUBMIT")
    public SalesNote addAttachment(HttpServletRequest request,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("salesNoteId") long salesNoteId,
            @RequestParam(value = "name", required = false) String name,
            @RequestBody byte[] file) throws IOException {
        SalesNote salesNote = salesNoteService.addAttachment(request, user, salesNoteId, name, file);
        return salesNote;
    }

    @RequestMapping(value = "{salesNoteId}/attachment/{attachmentId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteAttachment(HttpServletRequest request, @PathVariable("salesNoteId") Long salesNoteId,
            @PathVariable("attachmentId") Long attachmentId) {
        salesNoteService.deleteAttachment(request, salesNoteId, attachmentId);
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
