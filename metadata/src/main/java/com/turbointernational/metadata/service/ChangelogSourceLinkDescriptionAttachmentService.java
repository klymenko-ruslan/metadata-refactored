package com.turbointernational.metadata.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ChangelogSourceLinkDao changelogSourceLinkDao;

    @Autowired
    private ChangelogSourceLinkDescriptionAttachmentDao changelogSourceLinkDescriptionAttachmentDao;

    @Transactional
    public ChangelogSourceLinkDescriptionAttachment uploadFile(String name, String originalName, String mime, Long size,
            byte[] bin) {
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
        retVal.setFilename("TODO"); // TODO: save to file
        changelogSourceLinkDescriptionAttachmentDao.persist(retVal);
        return retVal;
    }

    public ChangelogSourceLinkDescriptionAttachment getDownload(Long id) {
        return changelogSourceLinkDescriptionAttachmentDao.findOne(id);
    }

    public byte[] downloadFile(Long id) {
        // TODO
        byte[] prefix = new byte[] { 'A', 'B', 'C', 'D', 'E', 'F' };
        byte[] bytes = id.toString().getBytes();
        byte[] retVal = new byte[prefix.length + bytes.length];
        System.arraycopy(prefix, 0, retVal, 0, prefix.length);
        System.arraycopy(bytes, 0, retVal, prefix.length, bytes.length);
        return retVal;
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
