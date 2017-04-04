package com.turbointernational.metadata.dao;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.chlogsrc.ChangelogSourceLinkDescriptionAttachment;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Repository
public class ChangelogSourceLinkDescriptionAttachmentDao extends AbstractDao<ChangelogSourceLinkDescriptionAttachment> {

    public ChangelogSourceLinkDescriptionAttachmentDao() {
        super(ChangelogSourceLinkDescriptionAttachment.class);
    }

}
