package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import com.turbointernational.metadata.service.ChangelogSourceService;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/16/17.
 */
@Controller
@RequestMapping(value = {"/changelog/source", "/metadata/changelog/source"})
public class ChangelogSourceController {

    private final static Logger log = LoggerFactory.getLogger(ChangelogSourceController.class);

    private final static String SESSION_ATTR_UPLOADS = "uploads_4ec503be252d765ea37621a629afdaa6";

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @JsonInclude(ALWAYS)
    public static class SourceRequest {

        @JsonView(View.Summary.class)
        private String name;

        @JsonView(View.Summary.class)
        private String description;

        @JsonView(View.Summary.class)
        private String url;

        @JsonView(View.Summary.class)
        private Long sourceNameId;

        public SourceRequest() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Long getSourceNameId() {
            return sourceNameId;
        }

        public void setSourceNameId(Long sourceNameId) {
            this.sourceNameId = sourceNameId;
        }

    }

    @JsonInclude(ALWAYS)
    public static class AttachmentsResponse {

        @JsonInclude(ALWAYS)
        public static class Row {

            /**
             * Attachment ID.
             *
             * For temporary attachments (loaded for a changelog source instance but not saved)
             * a value is negative. A positive value is ID in a table source_attachment.
             */
            @JsonView(View.Summary.class)
            private Long id;

            @JsonView(View.Summary.class)
            private String name;

            @JsonView(View.Summary.class)
            private String description;

            @JsonIgnore
            private File tmpFile;

            public Row() {
            }

            public Row(Long id, String name, String description, File tmpFile) {
                this.id = id;
                this.name = name;
                this.description = description;
                this.tmpFile= tmpFile;
            }

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public File getTmpFile() {
                return tmpFile;
            }

            public void setTmpFile(File tmpFile) {
                this.tmpFile = tmpFile;
            }

        }

        @JsonView(View.Summary.class)
        private List<Row> rows = new ArrayList<>();

        public AttachmentsResponse() {

        }

        public AttachmentsResponse(List<Row> rows) {
            this.rows = rows;
        }

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }

        void clear() {
            this.rows.forEach(r -> {
                // tmpFile below can be null when attach is persistent.
                File tmpFile = r.getTmpFile();
                if (tmpFile != null && tmpFile.exists()) {
                    r.getTmpFile().delete();
                }
            });
            this.rows.clear();
        }

    }

    private AttachmentsResponse getAttachments(HttpSession session) {
        synchronized (session) {
            AttachmentsResponse attachments = (AttachmentsResponse) session.getAttribute(SESSION_ATTR_UPLOADS);
            if (attachments == null) {
                attachments = new AttachmentsResponse();
                session.setAttribute(SESSION_ATTR_UPLOADS, attachments);
            }
            return attachments;
        }
    }

    private AttachmentsResponse cleanAttachments(HttpSession session) {
        AttachmentsResponse attachments = getAttachments(session);
        attachments.clear();
        return attachments;
    }

    @RequestMapping(method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public Source findChangelogSourceByName(@RequestParam("name") String name) {
        Source retVal = changelogSourceService.findChangelogSourceByName(name);
        return retVal;
    }

    @RequestMapping(value = "begin/{id}", method = POST)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public AttachmentsResponse beginEdit(HttpSession session, @PathVariable("id") Long sourceId) {
        AttachmentsResponse retVal = cleanAttachments(session);
        if (sourceId > 0) {
            Source source = changelogSourceService.findChangelogSourceById(sourceId);
            source.getAttachments().forEach(
                    a -> retVal.getRows().add(
                            new AttachmentsResponse.Row(a.getId(), a.getName(), a.getDescription(), null)
                    )
            );
        }
        return retVal;
    }

    @RequestMapping(path = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseBody
    @JsonView(View.Detail.class)
    // TODO: security!
    public Source get(@PathVariable("id") Long id) throws IOException {
        return changelogSourceService.findChangelogSourceById(id);
    }

    @RequestMapping(method = POST)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public Source create(HttpSession session, @RequestBody SourceRequest sr) throws IOException {
        AttachmentsResponse attachments = getAttachments(session);
        Source retVal = changelogSourceService.create(sr.getName(), sr.getDescription(),
                sr.getUrl(), sr.getSourceNameId(), attachments);
        cleanAttachments(session);
        return retVal;
    }

    @RequestMapping(path = "/{id}", method = PUT)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public Source update(HttpSession session, @PathVariable("id") Long sourceId, @RequestBody SourceRequest sr) throws IOException {
        AttachmentsResponse attachments = getAttachments(session);
        Source retVal = changelogSourceService.update(sourceId, sr.getName(), sr.getDescription(),
                sr.getUrl(), sr.getSourceNameId(), attachments);
        cleanAttachments(session);
        return retVal;
    }

    @RequestMapping(path = "/{id}/links/count", method = GET)
    @ResponseBody
    @Transactional
    public Long getLinksCount(@PathVariable("id") Long id) {
        return changelogSourceService.getNumLinks(id);
    }

    @RequestMapping(path = "/{id}", method = DELETE)
    @ResponseBody
    @Transactional
    // TODO: security!
    public boolean delete(@PathVariable("id") Long id) {
        changelogSourceService.delete(id);
        // TODO: delete attachments
        return true;
    }

    @RequestMapping(value = "attachment", method = POST, produces = APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public AttachmentsResponse uploadAttachment(
            HttpSession session, @RequestBody byte[] binData,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description) throws IOException {
        AttachmentsResponse attachments = getAttachments(session);
        return changelogSourceService.uploadAttachment(name, description, attachments, binData);
    }

    @RequestMapping(value = "attachment/{id}", method = DELETE)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public AttachmentsResponse removeAttachment(HttpSession session, @PathVariable Long id) {
        AttachmentsResponse attachments = getAttachments(session);
        return changelogSourceService.removeAttachment(id, attachments);
    }

    @RequestMapping(value = "attachment/{id}", method = GET)
    @Transactional
    @ResponseBody
    // TODO: security!
    public void downloadAttachment(@PathVariable("id") Long attachmentId, HttpServletResponse response) throws IOException {
        changelogSourceService.downloadAttachment(attachmentId, response);
    }

    @RequestMapping(value = "lastpicked", method = GET)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    public List<Source> getLastPicked() {
        return changelogSourceService.getLastPicked(5);
    }

}
