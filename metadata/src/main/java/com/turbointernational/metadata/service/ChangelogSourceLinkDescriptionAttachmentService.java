package com.turbointernational.metadata.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

import com.turbointernational.metadata.dao.ChangelogSourceLinkDao;
import com.turbointernational.metadata.dao.ChangelogSourceLinkDescriptionAttachmentDao;
import com.turbointernational.metadata.entity.chlogsrc.ChangelogSourceLink;
import com.turbointernational.metadata.entity.chlogsrc.ChangelogSourceLinkDescriptionAttachment;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class ChangelogSourceLinkDescriptionAttachmentService {

  private static final Logger log = LoggerFactory.getLogger(ChangelogSourceLinkDescriptionAttachmentService.class);

  @Autowired
  private ChangelogSourceLinkDao changelogSourceLinkDao;

  @Autowired
  private ChangelogSourceLinkDescriptionAttachmentDao changelogSourceLinkDescriptionAttachmentDao;

  @Value("${changelog.source.link.description.attachments.dir}")
  private File attachmentsDir;

  @Transactional
  public ChangelogSourceLinkDescriptionAttachment uploadFile(String name, String originalName, String mime, Long size,
      byte[] bin) throws IOException {
    String mimeType = mime;
    if (StringUtils.isBlank(mime)) {
      mimeType = "application/octet-stream";
    }
    MimeTypeUtils.parseMimeType(mimeType); // validation
    ChangelogSourceLinkDescriptionAttachment retVal = new ChangelogSourceLinkDescriptionAttachment();
    retVal.setCreated(new Date()); // now
    retVal.setName(name);
    retVal.setOriginalName(originalName);
    retVal.setMime(mimeType);
    retVal.setSize(size);
    changelogSourceLinkDescriptionAttachmentDao.persist(retVal);
    String filename = retVal.getId().toString() + "_" + Long.toString(System.currentTimeMillis());
    if (StringUtils.isNotBlank(originalName)) {
      filename += ("_" + originalName);
    }
    retVal.setFilename(filename);
    File file = new File(attachmentsDir, filename);
    FileUtils.writeByteArrayToFile(file, bin);
    return retVal;
  }

  public ChangelogSourceLinkDescriptionAttachment getDownload(Long id) {
    return changelogSourceLinkDescriptionAttachmentDao.findOne(id);
  }

  public byte[] downloadFile(Long id) throws IOException {
    ChangelogSourceLinkDescriptionAttachment rec = changelogSourceLinkDescriptionAttachmentDao.findOne(id);
    if (rec == null) {
      log.warn("An attachment for a changelog source link description not found. Attachament ID: {}.", id);
      return null;
    }
    String filename = rec.getFilename();
    if (filename == null) {
      log.warn("An attachment for a changelog source link description has no initialized file name. "
          + "Attachament ID: {}.", id);
      return null;
    }
    File file = new File(attachmentsDir, filename);
    if (!file.exists()) {
      log.warn(
          "An attachment for a changelog source link description has no file. " + "Attachament ID: {}. Filename: {}.",
          id, filename);
      return null;
    }
    byte[] bin = FileUtils.readFileToByteArray(file);
    return bin;
  }

  @Transactional
  public boolean deleteFile(Long id) {
    ChangelogSourceLinkDescriptionAttachment entity = changelogSourceLinkDescriptionAttachmentDao.findOne(id);
    if (entity == null) {
      log.warn("Deletion of an attachment [{}] for a changelog source link description failed. Record not found.", id);
      return false;
    }
    return deleteFile(entity);
  }
  
  @Transactional
  public boolean deleteFile(ChangelogSourceLinkDescriptionAttachment entity) {
    Long id = entity.getId();
     String filename = entity.getFilename();
    if (filename == null) {
      log.warn("Deletion of an attachment [{}] for a changelog source link description failed. "
          + "A filename not initialized.", id);
      return false;
    }
    changelogSourceLinkDescriptionAttachmentDao.getEntityManager().remove(entity);
    File file = new File(attachmentsDir, filename);
    if (file.exists()) {
      file.delete();
    } else {
      log.warn("File was not found during deletion of an attachment [{}] "
          + "for a changelog source link descriptio. Ignored.", id);
    }
    return true;
  }

  /**
   * Link 'changelog source link' and 'changelog source link description
   * attachment'.
   *
   * @param changelogSourceLinkId
   * @param descriptionAttachmentId
   */
  @Transactional
  public void linkSourceLinkDescription(Long changelogSourceLinkId, Long descriptionAttachmentId) {
    ChangelogSourceLink sourceLink = changelogSourceLinkDao.getReference(changelogSourceLinkId);
    ChangelogSourceLinkDescriptionAttachment descriptionAttachment = changelogSourceLinkDescriptionAttachmentDao
        .findOne(descriptionAttachmentId);
    descriptionAttachment.setChangelogSourceLink(sourceLink);
  }

}
