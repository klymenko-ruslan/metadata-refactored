package com.turbointernational.metadata.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
        if (StringUtils.isNotBlank(name)) {
            filename += ("_" + name);
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
            return null;
        }
        String filename = rec.getFilename();
        if (filename == null) {
            return null;
        }
        File file = new File(attachmentsDir, filename);
        byte[] bin = FileUtils.readFileToByteArray(file);
        return bin;
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
