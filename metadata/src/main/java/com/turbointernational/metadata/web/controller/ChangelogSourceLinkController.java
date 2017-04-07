package com.turbointernational.metadata.web.controller;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.chlogsrc.ChangelogSourceLinkDescriptionAttachment;
import com.turbointernational.metadata.service.ChangelogSourceLinkDescriptionAttachmentService;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 *
 */
@RequestMapping("/metadata/changelogsourcelink")
@Controller
public class ChangelogSourceLinkController {

  @Autowired
  private ChangelogSourceLinkDescriptionAttachmentService sourceLinkDescriptionAttachmentService;

  @JsonInclude(ALWAYS)
  public static class UploadAttachmentResponseRow {

    @JsonView(View.Summary.class)
    private Long id;

    @JsonView(View.Summary.class)
    private String name;

    public UploadAttachmentResponseRow() {
    }

    public UploadAttachmentResponseRow(Long id, String name) {
      this.id = id;
      this.name = name;
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

  }

  @RequestMapping(value = "/description/attachment/upload", method = POST)
  @ResponseBody
  @Transactional
  @JsonView(View.Summary.class)
  @Secured("ROLE_CHLOGSRC_UPDATE")
  public List<UploadAttachmentResponseRow> uploadAttachmentForDescription(MultipartHttpServletRequest request)
      throws IOException {
    List<UploadAttachmentResponseRow> retVal = new ArrayList<>();
    Iterator<String> itr = request.getFileNames();
    while (itr.hasNext()) {
      String uploadedFile = itr.next();
      MultipartFile file = request.getFile(uploadedFile);
      String name = file.getName();
      String originalFilename = file.getOriginalFilename();
      String mimeType = file.getContentType();
      long size = file.getSize();
      Long fileSize = size == 0 ? null : size;
      byte[] bin = file.getBytes();
      ChangelogSourceLinkDescriptionAttachment upload = sourceLinkDescriptionAttachmentService.uploadFile(name,
          originalFilename, mimeType, fileSize, bin);
      retVal.add(new UploadAttachmentResponseRow(upload.getId(), originalFilename));
    }
    return retVal;
  }

  @RequestMapping(value = "/description/attachment/{id}", method = DELETE)
  @Transactional
  @Secured("ROLE_CHLOGSRC_DELETE")
  public ResponseEntity<Boolean> deleteAttachmentForDescription(@PathVariable("id") Long id) throws IOException {
    boolean deleted = sourceLinkDescriptionAttachmentService.deleteFile(id);
    return new ResponseEntity<>(deleted, deleted ? OK : NOT_FOUND);
  }

  @RequestMapping(value = "/description/attachment/download/{id}", method = GET)
  @Secured("ROLE_CHLOGSRC_READ")
  public ResponseEntity<byte[]> downloadAttachmentForDescription(@PathVariable("id") Long uploadId) throws IOException {
    ChangelogSourceLinkDescriptionAttachment download = sourceLinkDescriptionAttachmentService.getDownload(uploadId);
    if (download == null) {
      return new ResponseEntity<>(null, NOT_FOUND);
    }
    // Generate the http headers with the file properties
    HttpHeaders headers = new HttpHeaders();
    String name = download.getOriginalName();
    if (StringUtils.isBlank(name)) {
        name = download.getName();
    }
    if (StringUtils.isBlank(name)) {
        name = download.getFilename();
    }
    headers.add("content-disposition", "attachment; filename=" + name);
    MimeType mimeType = MimeTypeUtils.parseMimeType(download.getMime());
    headers.setContentType(new MediaType(mimeType.getType(), mimeType.getSubtype()));
    byte[] bin = sourceLinkDescriptionAttachmentService.downloadFile(uploadId);
    return new ResponseEntity<>(bin, headers, OK);
  }

}
