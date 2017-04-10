package com.turbointernational.metadata;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.turbointernational.metadata.entity.chlogsrc.ChangelogSourceLinkDescriptionAttachment;
import com.turbointernational.metadata.service.ChangelogSourceLinkDescriptionAttachmentService;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Component
public class Schedulers {

  private final static Logger log = LoggerFactory.getLogger(Schedulers.class);

  @Autowired
  private ChangelogSourceLinkDescriptionAttachmentService changelogSourceLinkDescriptionAttachmentService;

  @PersistenceContext
  private EntityManager em;

  /**
   * Periodically remove records (and files) of attachments for 'changelog source link description'.
   *
   * When a changelog source is linked to a changelod an user how a possibility (on UI) to add to
   * a field 'description' an attachments. These attachments are registered in the database and
   * stored on a filesystem. In case when user cancels the linking process these attachments
   * still be saved in the database and file storage (but not associated with any changelog source link).
   * Such records are called 'orphan'. This periodical process removes such 'orphan' records (and files).
   */
  @Scheduled(fixedRate = 24 * 3600 * 1000)
  @Transactional
  public void changelogSourceLinkDescriptionAttachmentsCleanup() {
    List<ChangelogSourceLinkDescriptionAttachment> orphanAttachments = em
        .createNamedQuery("findOrphanAttachments", ChangelogSourceLinkDescriptionAttachment.class)
        .setParameter("period", /* one hour */ 3600D).getResultList();
    long deleted = orphanAttachments.stream()
        .peek(orphan -> changelogSourceLinkDescriptionAttachmentService.deleteFile(orphan)).count();
    log.info("Cleanup of orphan attachments for 'changelog source link description' finished. "
        + "Removed {} orphan attachemt(s).", deleted);
  }

}
