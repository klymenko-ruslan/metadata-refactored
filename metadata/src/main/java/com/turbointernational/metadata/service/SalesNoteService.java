package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.SALESNOTES;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART0;
import static com.turbointernational.metadata.util.FormatUtils.formatPart;
import static com.turbointernational.metadata.util.FormatUtils.formatSalesNote;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.dao.SalesNoteAttachmentDao;
import com.turbointernational.metadata.dao.SalesNoteDao;
import com.turbointernational.metadata.dao.SalesNotePartDao;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.SalesNote;
import com.turbointernational.metadata.entity.SalesNoteAttachment;
import com.turbointernational.metadata.entity.SalesNoteAttachmentRepository;
import com.turbointernational.metadata.entity.SalesNotePart;
import com.turbointernational.metadata.entity.SalesNoteRepository;
import com.turbointernational.metadata.entity.SalesNoteState;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.exception.RemovePrimaryPartException;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2/28/16.
 */
@Service
public class SalesNoteService {

    @Autowired
    private SalesNotePartDao salesNotePartDao;

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @Autowired
    private PartDao partDao;

    @Autowired
    private SalesNoteDao salesNoteDao;

    @Autowired
    private SalesNoteAttachmentDao salesNoteAttachmentDao;

    @Autowired
    private SalesNoteRepository salesNotes;

    @Autowired
    private SalesNoteAttachmentRepository attachments;

    @Value("${attachments.salesNote.dir}")
    private File attachmentDir;

    /**
     * @param partId
     * @return primary part ID or -1 if this part has no one
     */
    public Long findPrimaryPartIdForThePart(long partId) {
        Long primaryPartId = salesNotePartDao.findPrimaryPartIdForThePart(partId);
        return primaryPartId == null ? -1L : primaryPartId;
    }

    @Transactional
    public void addRelatedPart(HttpServletRequest request, User user, Long salesNoteId, Long partId) {
        // Find the entities
        SalesNote salesNote = salesNotes.findOne(salesNoteId);
        hasEditAccess(request, salesNote);
        Part part = partDao.findOne(partId);
        // Create the primary part association
        SalesNotePart snp = new SalesNotePart(salesNote, part, false, user);
        partDao.getEntityManager().persist(snp);
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(partId, PART0));
        changelogService.log(SALESNOTES,
                "Added related part " + formatPart(part) + " to sales note " + formatSalesNote(salesNoteId),
                relatedParts);
    }

    @Transactional
    public SalesNote createSalesNote(HttpServletRequest httpRequest, User user, Long primaryPartId, String comment,
            Long[] sourcesIds, Integer[] ratings, String description, Long[] attachIds) {
        // Create the sales note from the request
        SalesNote salesNote = new SalesNote();
        salesNote.setCreator(user);
        salesNote.setCreateDate(new Date());
        salesNote.setUpdater(user);
        salesNote.setUpdateDate(new Date());
        salesNote.setComment(comment);
        salesNote.setState(SalesNoteState.draft);
        // Create the primary part association
        Part primaryPart = partDao.findOne(primaryPartId);
        salesNote.getParts().add(new SalesNotePart(salesNote, primaryPart, true, user));
        // Save
        salesNotes.save(salesNote);
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(primaryPartId, PART0));
        Changelog changelog = changelogService.log(SALESNOTES, "Created sales note " + formatSalesNote(salesNote) + ".",
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        // Initialize a few properties before sending the response
        primaryPart.getManufacturer().getName();
        primaryPart.getPartType().getName();
        return salesNote;
    }

    public SalesNote getSalesNote(Long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        return salesNote;
    }

    @Transactional
    public void updateSalesNote(HttpServletRequest request, Long noteId, String comment) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        hasEditAccess(request, salesNote);
        salesNote.setComment(comment);
        // Save
        salesNotes.save(salesNote);
        changelogService.log(SALESNOTES, "Changed sales note (" + formatSalesNote(salesNote) + ") comment: \""
                + salesNote.getComment() + "\" -> \"" + comment + "\".", null);
    }

    @Transactional
    public void deleteRelatedPart(HttpServletRequest request, Long salesNoteId, final Long partId)
            throws RemovePrimaryPartException {
        SalesNotePart salesNotePart = salesNotePartDao.findOne(salesNoteId, partId);
        // Can't delete primary part
        if (salesNotePart.isPrimary()) {
            throw new RemovePrimaryPartException("Can't delete the primary part for a sales note.");
        }
        salesNotePartDao.delete(salesNotePart);
        /*
         * // Find the entities SalesNote salesNote =
         * salesNotes.findOne(salesNoteId); hasEditAccess(request, salesNote);
         * // Find the SNP SalesNotePart salesNotePart = Iterables.find(
         * salesNote.getParts(), snp -> snp.getPart().getId() == partId); //
         * Can't delete primary part if (salesNotePart.isPrimary()) { throw new
         * RemovePrimaryPartException("Can't delete the primary part for a sales note."
         * ); } // Delete the SNP salesNote.getParts().remove(salesNotePart);
         * salesNoteParts.delete(salesNotePart); // Save
         * salesNotes.save(salesNote);
         */
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(partId, PART0));
        changelogService.log(SALESNOTES,
                "Deleted related part " + formatPart(partId, null) + " from sales note " + formatSalesNote(salesNoteId),
                relatedParts);
    }

    public static class AttachmentDto {

        private final String name;

        private final File file;

        AttachmentDto(String name, File file) {
            this.name = name;
            this.file = file;
        }

        public String getName() {
            return name;
        }

        public File getFile() {
            return file;
        }

    }

    public AttachmentDto getAttachment(Long salesNoteId, Long attachmentId) throws IOException {
        // Get the attachment
        SalesNoteAttachment attachment = attachments.findOne(attachmentId);
        String name = attachment.getFilename();
        File salesNoteDir = new File(attachmentDir, Long.toString(salesNoteId));
        File file = new File(salesNoteDir, name);
        return new AttachmentDto(name, file);
    }

    @Transactional
    public SalesNote addAttachment(HttpServletRequest request, User user, Long salesNoteId, String name,
            byte[] bin) throws IOException {
        // Find the entities
        SalesNote salesNote = salesNotes.findOne(salesNoteId);
        hasEditAccess(request, salesNote);
        // Create the attachment directory for this sales note
        File salesNoteDir = new File(attachmentDir, Long.toString(salesNote.getId()));
        if (!salesNoteDir.exists()) {
            salesNoteDir.mkdirs();
        }
        // Copy the file
        //upload.transferTo(new File(salesNoteDir, name));
        writeByteArrayToFile(new File(salesNoteDir, name), bin);
        // Save the record
        SalesNoteAttachment attachment = new SalesNoteAttachment();
        attachment.setSalesNote(salesNote);
        attachment.setCreateDate(new Date());
        attachment.setCreator(user);
        attachment.setUpdateDate(new Date());
        attachment.setUpdater(user);
        attachment.setFilename(name);
        // Save
        salesNote.getAttachments().add(attachment);
        salesNotes.save(salesNote);
        changelogService.log(SALESNOTES, "Added attachment to sales note: " + formatSalesNote(salesNote) + ".",
                attachment, null);
        return salesNote;
    }

    @Transactional
    public SalesNote deleteAttachment(HttpServletRequest request, Long salesNoteId, Long attachmentId) {
        SalesNoteAttachment attachment = salesNoteAttachmentDao.getReference(attachmentId);
        salesNoteAttachmentDao.remove(attachment);
        SalesNote note = salesNoteDao.findOne(salesNoteId);
        // TODO: recursion during serialization
        // changelogService.log(SALESNOTES, "Deleted attachment from sales note " + formatSalesNote(note), attachment,
        //         null);
        return note;
    }

    @Transactional
    public void submit(User user, Long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        updateState(user, salesNote, SalesNoteState.submitted, SalesNoteState.draft, SalesNoteState.rejected);
    }

    @Transactional
    public void approve(User user, Long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        updateState(user, salesNote, SalesNoteState.approved, SalesNoteState.draft, SalesNoteState.submitted);
    }

    @Transactional
    public void reject(User user, Long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        updateState(user, salesNote, SalesNoteState.rejected, SalesNoteState.submitted, SalesNoteState.approved);
    }

    @Transactional
    public void publish(User user, Long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        updateState(user, salesNote, SalesNoteState.published, SalesNoteState.approved);
    }

    @Transactional
    public void retract(User user, Long noteId) {
        SalesNote salesNote = salesNotes.findOne(noteId);
        updateState(user, salesNote, SalesNoteState.approved, SalesNoteState.published);
    }

    @Transactional
    public void removeSalesNote(Long id) {
        salesNoteDao.delete(id);
    }

    private void hasEditAccess(HttpServletRequest request, SalesNote salesNote) {
        if (salesNote.getState() == SalesNoteState.published && request.isUserInRole("SALES_NOTE_PUBLISH")) {
            return;
        }

        if ((salesNote.getState() == SalesNoteState.approved || salesNote.getState() == SalesNoteState.submitted)
                && request.isUserInRole("SALES_NOTE_APPROVE")) {
            return;
        }

        if ((salesNote.getState() == SalesNoteState.draft || salesNote.getState() == SalesNoteState.rejected)
                && request.isUserInRole("SALES_NOTE_SUBMIT")) {
            return;
        }

        throw new AccessDeniedException(
                "You are not allowed to update sales notes with the " + salesNote.getState() + " state.");
    }

    private void updateState(User user, SalesNote salesNote, SalesNoteState newState, SalesNoteState... allowedStates) {
        Date now = new Date();
        SalesNoteState currentState = salesNote.getState();
        SalesNoteState.checkState(salesNote.getState(), allowedStates);
        salesNote.setState(newState);
        salesNote.setUpdateDate(now);
        salesNote.setUpdater(user);
        // Mark as dirty the associated SaleNotePart(s) in order
        // to trigger entities' lifecycle method @PostUpdate.
        // That method will update entities in an ElastcSearch index to reflect
        // changed state.
        // @see SalesNotePart#updateSearchIndex().
        salesNote.getParts().forEach(snp -> snp.setUpdateDate(now));
        salesNotes.save(salesNote);
        changelogService.log(SALESNOTES, "Changed state in the sales note " + formatSalesNote(salesNote) + ": "
                + currentState + " -> " + salesNote.getState(), null);
    }

}
