package com.turbointernational.metadata.service;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.apache.commons.io.FileUtils.moveFile;
import static org.apache.commons.io.FileUtils.moveFileToDirectory;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.dao.ChangelogSourceLinkDao;
import com.turbointernational.metadata.dao.SourceDao;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.Changelog.ServiceEnum;
import com.turbointernational.metadata.entity.Role;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.chlogsrc.ChangelogSourceLink;
import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.chlogsrc.SourceAttachment;
import com.turbointernational.metadata.web.controller.ChangelogSourceController.AttachmentsResponse;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/16/17.
 */
@Service
public class ChangelogSourceService {

    private final static Logger log = LoggerFactory.getLogger(ChangelogSourceService.class);

    @Autowired
    private SourceDao sourceDao;

    @Autowired
    private ChangelogSourceLinkDao changelogSourceLinkDao;

    @Autowired
    private ChangelogSourceLinkDescriptionAttachmentService changelogSourceLinkDescriptionAttachmentService;

    @Autowired
    private ServiceService serviceService;

    @PersistenceContext(unitName = "metadata")
    private EntityManager em;

    @Value("${changelog.sources.dir}")
    private File changelogSourcesDir;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Source findChangelogSourceById(Long id) {
        return sourceDao.findOne(id);
    }

    public Source findChangelogSourceByName(String name) {
        return sourceDao.findChangelogSourceByName(name);
    }

    public Source create(String name, String desctiption, String url, Long sourceNameId,
            AttachmentsResponse attachments) throws IOException {
        User user = User.getCurrentUser();
        Source source = sourceDao.create(name, desctiption, url, sourceNameId, user);
        saveAttachments(source, attachments);
        return source;
    }

    public Source update(Long id, String name, String desctiption, String url, Long sourceNameId,
            AttachmentsResponse attachments) throws IOException {
        User user = User.getCurrentUser();
        Source source = sourceDao.update(id, name, desctiption, url, sourceNameId, user);
        saveAttachments(source, attachments);
        return source;
    }

    public void delete(Long id) {
        Source source = sourceDao.getEntityManager().getReference(Source.class, id);
        sourceDao.remove(source);
    }

    public void link(HttpServletRequest httpRequest, Changelog changelog, Long[] sourcesIds, Integer[] ratings,
            String description, Long[] descriptionAttachmentIds) throws AssertionError {
        if (sourcesIds != null && sourcesIds.length > 0) {
            User user = User.getCurrentUser();
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "insert into changelog_source_link(created, create_user_id, changelog_id, description) " +
                        "values(?, ?, ?, ?)",
                        new String[] { "id" });
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                ps.setLong(2, user.getId());
                ps.setLong(3, changelog.getId());
                ps.setString(4, description);
                return ps;
            }, keyHolder);
            Long chlgsrclnkid = keyHolder.getKey().longValue();
            for (int i = 0; i < sourcesIds.length; i++) {
                Long srcId = sourcesIds[i];
                Integer rating = ratings[i];
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "insert into changelog_source(lnk_id, source_id, rating) values(?, ?, ?)");
                    ps.setLong(1, chlgsrclnkid);
                    ps.setLong(2, srcId);
                    ps.setInt(3, rating);
                    return ps;
                });
            }
            if (descriptionAttachmentIds != null) {
                for(int i = 0; i < descriptionAttachmentIds.length; i++) {
                    changelogSourceLinkDescriptionAttachmentService.linkSourceLinkDescription(chlgsrclnkid, descriptionAttachmentIds[i]);
                }
            }
            /*
             * ChangelogSourceLink link = new ChangelogSourceLink(changelog,
             * user, new Date(), description);
             * changelogSourceLinkDao.persist(link); for (int i = 0; i <
             * sourcesIds.length; i++) { Long srcId = sourcesIds[i]; Integer
             * rating = ratings[i]; Source source =
             * sourceDao.getReference(srcId); ChangelogSourceId chlgsrcid = new
             * ChangelogSourceId(link, source); ChangelogSource chlgsrc = new
             * ChangelogSource(chlgsrcid, rating);
             * link.getChangelogSources().add(chlgsrc);
             * changelogSourceDao.persist(chlgsrc);
             * searchService.indexChangelogSource(source); // update partIds in
             * the index // I have no idea why... but without flush below the
             * record is not saved // to the changelog_source. em.flush(); }
             */
        } else if (httpRequest != null && !httpRequest.isUserInRole(Role.ROLE_CHLOGSRC_SKIP)) {
            // httpRequest above can be null in integration tests
            ServiceEnum service = changelog.getService();
            boolean required = serviceService.isChangelogSourceRequired(service);
            if (required) {
                throw new AssertionError("User must provide changelog source.");
            }
        }
    }

    public Long getNumLinks(Long srcId) {
        return em.createNamedQuery("getChangelogSourceCountForSource", Long.class).setParameter("srcId", srcId)
                .getSingleResult();
    }

    public List<Source> getLastPicked(int limit) {
        User user = User.getCurrentUser();
        List<Source> sources = em.createNamedQuery("findLastPickedChangelogSources", Source.class)
                .setParameter("userId", user.getId()).setMaxResults(limit).getResultList();
        TypedQuery<Date> q = em.createNamedQuery("findLastChangelogSourceLinkForSource", Date.class);
        for (Source s : sources) {
            Date d = q.setParameter("sourceId", s.getId()).getSingleResult();
            s.setLastLinked(d);
        }
        return sources;
    }

    public ChangelogSourceLink findLinkByChangelogId(Long changelogId) {
        return changelogSourceLinkDao.findByChangelogId(changelogId);
    }

    public ChangelogSourceLink findLinkById(Long id) {
        return changelogSourceLinkDao.findOne(id);
    }

    public AttachmentsResponse uploadAttachment(String name, String description, AttachmentsResponse attachments,
            byte[] binData) throws IOException {
        File tmpfile = File.createTempFile("metadata", ".chlgupload");
        FileUtils.writeByteArrayToFile(tmpfile, binData);
        Long id = -(System.currentTimeMillis()); // negative value
        AttachmentsResponse.Row row = new AttachmentsResponse.Row(id, name, description, tmpfile);
        attachments.getRows().add(row);
        return attachments;
    }

    public AttachmentsResponse removeAttachment(Long id, AttachmentsResponse attachments) {
        List<AttachmentsResponse.Row> rows = attachments.getRows();
        int idx = -1;
        for (int i = 0; i < rows.size(); i++) {
            AttachmentsResponse.Row row = rows.get(i);
            if (row.getId().equals(id)) {
                idx = i;
                if (id > 0) {
                    // Delete persistent file and record in the table.
                    SourceAttachment attachment = em.find(SourceAttachment.class, id);
                    Long srcId = attachment.getSource().getId();
                    File destDir = getAttachmendDir(srcId);
                    String fileName = getAttachmentFilename(id);
                    File attachmentFile = new File(destDir, fileName);
                    if (attachmentFile.exists()) {
                        attachmentFile.delete();
                    }
                    em.remove(attachment);
                } else {
                    // Delete only temporary file.
                    row.getTmpFile().delete();
                }
                break;
            }
        }
        if (idx > -1) {
            rows.remove(idx);
        }
        return attachments;
    }

    public void downloadAttachment(Long attachmentId, HttpServletResponse response) throws IOException {
        SourceAttachment attachment = em.find(SourceAttachment.class, attachmentId);
        Long srcId = attachment.getSource().getId();
        File attachmentFile = getAttachmentFile(srcId, attachmentId);
        if (!attachmentFile.exists()) {
            log.warn("Changelog source attachment [{}] not found.", attachmentId);
            response.setStatus(SC_NOT_FOUND);
            return;
        }
        ServletOutputStream out = response.getOutputStream();
        String downloadName = attachment.getName();
        if (StringUtils.isBlank(downloadName)) {
            downloadName = attachmentId.toString();
        }
        String mimeType = URLConnection.guessContentTypeFromName(downloadName);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", downloadName));
        response.setContentLength((int) attachmentFile.length());
        FileUtils.copyFile(attachmentFile, out);
    }

    private void saveAttachments(Source source, AttachmentsResponse attachments) throws IOException {
        if (attachments != null) {
            SourceAttachment attachment;
            for (AttachmentsResponse.Row row : attachments.getRows()) {
                if (row.getId() > 0) {
                    // Update meta information only.
                    attachment = em.find(SourceAttachment.class, row.getId());
                    attachment.setName(row.getName());
                    attachment.setDescription(row.getDescription());
                    em.merge(attachment);
                } else {
                    // Save metainformation to the database and move uploaded
                    // file
                    // from temporary place to a persistent file storage.
                    Long srcId = source.getId();
                    File destDir = getAttachmendDir(srcId);
                    moveFileToDirectory(row.getTmpFile(), destDir, true);
                    attachment = new SourceAttachment(null, source, row.getName(), row.getDescription());
                    em.persist(attachment);
                    String fileName = getAttachmentFilename(attachment.getId());
                    moveFile(new File(destDir, row.getTmpFile().getName()), new File(destDir, fileName));
                }
            }
        }
    }

    private File getAttachmendDir(Long srcId) {
        return new File(changelogSourcesDir, srcId.toString());
    }

    private String getAttachmentFilename(Long attachmentId) {
        return attachmentId.toString();
    }

    private File getAttachmentFile(Long srcId, Long attachmentId) {
        return new File(getAttachmendDir(srcId), getAttachmentFilename(attachmentId));
    }

}
