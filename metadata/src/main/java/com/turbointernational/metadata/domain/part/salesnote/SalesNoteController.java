package com.turbointernational.metadata.domain.part.salesnote;

import com.turbointernational.metadata.domain.part.salesnote.dto.UpdateSalesNoteRequest;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.salesnote.dto.CreateSalesNoteRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import static com.turbointernational.metadata.domain.part.salesnote.SalesNoteState.*;
import com.turbointernational.metadata.domain.part.salesnote.dto.SalesNoteSearchRequest;
import com.turbointernational.metadata.domain.part.salesnote.exception.RemovePrimaryPartException;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.services.SearchService;
import com.turbointernational.metadata.web.View;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/metadata/other/salesNote")
public class SalesNoteController {

    private static final Logger log = LoggerFactory.getLogger(SalesNoteController.class);

    @Autowired
    private ChangelogDao changelogDao;

    @Autowired
    private SalesNoteRepository salesNotes;
    
    @Autowired
    private SalesNoteAttachmentRepository attachments;
    
    @Autowired
    private SalesNotePartRepository salesNoteParts;

    @Autowired
    private PartDao partDao;

    @Autowired
    private SearchService searchService;

    @Value("attachments.salesNote")
    private File attachmentDir;

    //<editor-fold defaultstate="collapsed" desc="CRUDS">
    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_SALES_NOTE_SUBMIT")
    @Transactional
    @JsonView(View.DetailWithPartsAndAttachments.class)
    public @ResponseBody SalesNote createSalesNote(
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @RequestBody CreateSalesNoteRequest request) {

        // Create the sales note from the request
        SalesNote salesNote = new SalesNote();
        salesNote.setCreator(user);
        salesNote.setCreateDate(new Date());
        salesNote.setUpdater(user);
        salesNote.setUpdateDate(new Date());
        salesNote.setComment(request.getComment());
        salesNote.setState(SalesNoteState.draft);

        // Create the primary part association
        Part primaryPart = partDao.findOne(request.getPrimaryPartId());
        salesNote.getParts().add(new SalesNotePart(salesNote, primaryPart, true, user));

        // Save
        salesNotes.save(salesNote);
        changelogDao.log("Created sales note " + salesNote.getId(), request);

        // Initialize a few properties before sending the response
        primaryPart.getManufacturer().getName();
        primaryPart.getPartType().getName();

        return salesNote;
    }

    @RequestMapping(value="{noteId}", method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_READ")
    @JsonView(View.DetailWithPartsAndAttachments.class)
    public SalesNote getSalesNote(@PathVariable("noteId") long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        salesNote.getParts().size();
        for (SalesNotePart snp : salesNote.getParts()) {
            snp.getPart().getManufacturer().getName();
            snp.getPart().getPartType().getName();
        }
        return salesNote;
    }
    
    @ResponseBody
    @Transactional
    @RequestMapping(value="{noteId}", method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateSalesNote(
            HttpServletRequest request,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") long noteId,
            @RequestBody UpdateSalesNoteRequest updateRequest) {
        
        SalesNote salesNote = salesNotes.findOne(noteId);
        
        canEditAccess(request, salesNote);
        
        salesNote.setComment(updateRequest.getComment());

        // Save
        salesNotes.save(salesNote);
        changelogDao.log("Changed sales note comment from " + salesNote.getComment(), noteId);
    }
    
    private void canEditAccess(HttpServletRequest request, SalesNote salesNote) {
        if (salesNote.getState() == published
                && request.isUserInRole("SALES_NOTE_PUBLISH")) {
            return;
        }
        
        if ((salesNote.getState() == approved || salesNote.getState() == submitted)
                && request.isUserInRole("SALES_NOTE_APPROVE")) {
            return;
        }
        
        if ((salesNote.getState() == draft || salesNote.getState() == rejected)
                && request.isUserInRole("SALES_NOTE_SUBMIT")) {
            return;
        }

        throw new AccessDeniedException("You are not allowed to update sales notes with the " + salesNote.getState() + " state.");
    }

//    @ResponseBody
//    @Secured("ROLE_SALES_NOTE_READ")
//    @JsonView(View.Detail.class)
//    @RequestMapping(value = "search", method = RequestMethod.POST,
//                    consumes = MediaType.APPLICATION_JSON_VALUE,
//                    produces = MediaType.APPLICATION_JSON_VALUE)
//    public SalesNoteSearchResponse search(@RequestBody SalesNoteSearchRequest req) {
//        return salesNotes.search(req);
//    }
    
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_READ")
    @JsonView(View.DetailWithParts.class)
    @RequestMapping(value = "searchWithParts", method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchWithParts(@RequestBody SalesNoteSearchRequest req) {
        return searchService.filterSalesNotes(req.getQuery(), req.getStates(), req.isIncludePrimary(),
                req.isIncludeRelated(), null, null, req.getPage()*req.getPageSize(), req.getPageSize());
    }
    
//    @RequestMapping(value="listByPartId/{partId}", method = RequestMethod.GET)
//    @ResponseBody
//    @Secured("ROLE_READ")
//    public ResponseEntity<String> listByPartId(@PathVariable("partId") Long partId) throws JsonProcessingException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        Page<SalesNote> result = salesNotes.findByPartId(new PageRequest(0, 100), partId);
//        return new ResponseEntity<String>(json.writeValueAsString(result), headers, HttpStatus.OK);
//    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Related Parts">
    @ResponseBody
    @Transactional
    @RequestMapping(value="{salesNoteId}/part/{partId}", method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public void addRelatedPart(
            HttpServletRequest request,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("salesNoteId") long salesNoteId,
            @PathVariable("partId") long partId) {
        
        // Find the entities
        SalesNote salesNote = salesNotes.findOne(salesNoteId);
        
        canEditAccess(request, salesNote);
        
        Part part = partDao.findOne(partId);
        
        // Create the primary part association
        SalesNotePart snp = new SalesNotePart(salesNote, part, false, user);
        salesNote.getParts().add(snp);
        
        // Save
        salesNotes.save(salesNote);
        
        changelogDao.log("Added related part to sales note " + salesNoteId, partId);
    }
    
    @ResponseBody
    @Transactional
    @RequestMapping(value="{salesNoteId}/part/{partId}", method = RequestMethod.DELETE,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteRelatedPart(
            HttpServletRequest request,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("salesNoteId") long salesNoteId,
            @PathVariable("partId") final long partId) throws RemovePrimaryPartException {
        
        // Find the entities
        SalesNote salesNote = salesNotes.findOne(salesNoteId);
        
        canEditAccess(request, salesNote);
        
        // Find the SNP
//        SalesNotePart salesNotePart = salesNote.getParts().stream()
//                .filter(snp -> snp.getPart().getId() == partId)
//                .findFirst().get();
        SalesNotePart salesNotePart = Iterables.find(
                salesNote.getParts(),
                snp -> snp.getPart().getId() == partId);
        
        // Can't delete primary part
        if (salesNotePart.isPrimary()) {
            throw new RemovePrimaryPartException("Can't delete the primary part for a sales note.");
        }
        
        // Delete the SNP
        salesNote.getParts().remove(salesNotePart);
        salesNoteParts.delete(salesNotePart);
        
        // Save
        salesNotes.save(salesNote);
        changelogDao.log("Deleted related part from sales note " + salesNoteId, partId);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Attachments">
    @RequestMapping(value = "attachment/{id}", method = RequestMethod.GET)
    @Secured("ROLE_SALES_NOTE_READ")
    public @ResponseBody ResponseEntity<byte[]> getAttachment(@PathVariable Long id) throws Exception {
        
        // Get the attachment
        SalesNoteAttachment attachment = attachments.findOne(id);
        
        // Load it from disk
        File attachmentFile = new File(attachmentDir, attachment.getFilename());
        
        try {
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/octet-stream");
            headers.add("Content-Disposition", "attachment; filename=\"" + attachment.getFilename() + "\"");

            byte[] bytes = FileUtils.readFileToByteArray(attachmentFile);

            return new ResponseEntity(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.warn("Couldn't load attachment file: " + attachmentFile.getAbsolutePath(), e);
            throw e;
        }
    }
    
    @RequestMapping(value="{salesNoteId}/attachment", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SalesNote addAttachment(
            HttpServletRequest request,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("salesNoteId") long salesNoteId,
            @RequestParam("file") MultipartFile upload) throws IOException {
        
        // Find the entities
        SalesNote salesNote = salesNotes.findOne(salesNoteId);
        
        canEditAccess(request, salesNote);
        
        // Create the attachment directory for this sales note
        File salesNoteDir = new File(attachmentDir, Long.toString(salesNote.getId()));
        if (!salesNoteDir.exists()) {
          salesNoteDir.mkdirs();
        }
        
        // Copy the file
        upload.transferTo(new File(salesNoteDir, upload.getOriginalFilename()));
        
        // Save the record
        SalesNoteAttachment attachment = new SalesNoteAttachment();
        attachment.setSalesNote(salesNote);
        attachment.setCreateDate(new Date());
        attachment.setCreator(user);
        attachment.setUpdateDate(new Date());
        attachment.setUpdater(user);
        attachment.setFilename(upload.getOriginalFilename());
        
        // Save
        salesNote.getAttachments().add(attachment);
        salesNotes.save(salesNote);
        changelogDao.log("Added attachment to sales note " + salesNoteId, attachment);
        
        return salesNote;
    }
    
    @RequestMapping(value="{salesNoteId}/attachment/{attachmentId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteAttachment(
            HttpServletRequest request,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("salesNoteId") long salesNoteId,
            @PathVariable("attachmentId") long attachmentId) {
        
        // Find the entities
        SalesNote salesNote = salesNotes.findOne(salesNoteId);
        
        canEditAccess(request, salesNote);
        
        // Find the attachment
        SalesNoteAttachment salesNoteAttachment = Iterables.find(
                salesNote.getAttachments(),
                attachment -> attachment.getId() == attachmentId);
        
        
        // Delete the attachment
        salesNote.getAttachments().remove(salesNoteAttachment);
        
        // Save
        salesNotes.save(salesNote);
        changelogDao.log("Deleted attachment from sales note " + salesNoteId, salesNoteAttachment);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="State changes">
    @RequestMapping(value="{noteId}/submit", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_SUBMIT")
    @Transactional
    public void submit(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        
        if (salesNote == null) {
            throw new NoSuchEntityException(SalesNote.class, noteId);
        }
        
        updateState(user, salesNote, SalesNoteState.submitted,
                SalesNoteState.draft,
                SalesNoteState.rejected);
    }
    
    @RequestMapping(value="{noteId}/approve", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_APPROVE")
    @Transactional
    public void approve(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        
        updateState(user, salesNote, SalesNoteState.approved,
                SalesNoteState.draft,
                SalesNoteState.submitted);
    }
    
    @RequestMapping(value="{noteId}/reject", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_REJECT")
    @Transactional
    public void reject(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        
        updateState(user, salesNote, SalesNoteState.rejected,
                SalesNoteState.submitted,
                SalesNoteState.approved);
    }
    
    @RequestMapping(value="{noteId}/publish", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_PUBLISH")
    @Transactional
    public void publish(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        
        updateState(user, salesNote, SalesNoteState.published,
                SalesNoteState.approved);
    }
    
    @RequestMapping(value="{noteId}/retract", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_SALES_NOTE_RETRACT")
    @Transactional
    public void retract(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
            @PathVariable("noteId") long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        
        updateState(user, salesNote, SalesNoteState.approved,
                SalesNoteState.published);
    }
    
    private void updateState(User user, SalesNote salesNote, SalesNoteState newState, SalesNoteState... allowedStates) {
        SalesNoteState currentState = salesNote.getState();
        
        SalesNoteState.checkState(salesNote.getState(), allowedStates);
        
        salesNote.setState(newState);
        salesNote.setUpdateDate(new Date());
        salesNote.setUpdater(user);
        
        salesNotes.save(salesNote);
        
        changelogDao.log("Sales note " + salesNote.getId()
                + " state changed from " + currentState
                + " to " + salesNote.getState(), null);
    }
    //</editor-fold>
}
