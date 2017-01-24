package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import com.turbointernational.metadata.service.ChangelogSourceService;
import com.turbointernational.metadata.util.View;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/16/17.
 */
@Controller
@RequestMapping(value = {"/changelog/source", "/metadata/changelog/source"})
public class ChangelogSourceController {

    private final static Logger log = LoggerFactory.getLogger(ChangelogSourceController.class);

    private final static String SESSION_ATTR_UPLOADS = "uploads_4ec503be252d765ea37621a629afdaa6";

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

            @JsonView(View.Summary.class)
            private String name;

            @JsonView(View.Summary.class)
            private String description;

            @JsonIgnore
            private File tmpFile;

            public Row() {
            }

            public Row(String name, String description, File tmpFile) {
                this.name = name;
                this.description = description;
                this.tmpFile= tmpFile;
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
                if (r.getTmpFile().exists()) {
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

    private void cleanAttachments(HttpSession session) {
        AttachmentsResponse attachments = getAttachments(session);
        attachments.clear();
    }

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @RequestMapping(value = "/sourcename/list", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public List<SourceName> getAllChangelogSourceNames() {
        List<SourceName> retVal = changelogSourceService.getAllChangelogSourceNames();
        return retVal;
    }

    @RequestMapping(method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public Source findChangelogSourceByName(@RequestParam("name") String name) {
        Source retVal = changelogSourceService.findChangelogSourceByName(name);
        return retVal;
    }

    @RequestMapping(value = "begin", method = POST)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public AttachmentsResponse beginEdit(HttpSession session) {
        AttachmentsResponse attachments = getAttachments(session);
        attachments.clear();
        return attachments;
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
        Source retVal = changelogSourceService.createChangelogSource(sr.getName(), sr.getDescription(),
                sr.getUrl(), sr.getSourceNameId(), attachments);
        attachments.clear();
        return retVal;
    }

    @RequestMapping(path = "/{id}", method = DELETE)
    @Transactional
    public void delete(@PathVariable("id") Long id) {
        changelogSourceService.delete(id);
    }

    @RequestMapping(value = "attachment/tmp", method = POST, produces = APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public AttachmentsResponse uploadAttachmentTmp(
            HttpSession session, @RequestBody byte[] binData,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description) throws IOException {
        AttachmentsResponse attachments = getAttachments(session);
        File tmpfile = File.createTempFile("metadata", ".chlgupload");
        FileUtils.writeByteArrayToFile(tmpfile, binData);
        AttachmentsResponse.Row row = new AttachmentsResponse.Row(name, description, tmpfile);
        attachments.getRows().add(row);
        return attachments;
    }

    @RequestMapping(value = "attachment/tmp/{idx}", method = DELETE)
    @Transactional
    @ResponseBody
    @JsonView(View.Summary.class)
    // TODO: security!
    public AttachmentsResponse removeAttachmentTmp(
            HttpSession session, @PathVariable int idx) {
        AttachmentsResponse attachments = getAttachments(session);
        AttachmentsResponse.Row toDel = attachments.getRows().get(idx);
        toDel.getTmpFile().delete();
        attachments.getRows().remove(idx);
        return attachments;
    }

}
