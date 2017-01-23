package com.turbointernational.metadata.service;

import com.turbointernational.metadata.dao.ChangelogSourceDao;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import com.turbointernational.metadata.web.controller.ChangelogSourceController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/16/17.
 */
@Service
public class ChangelogSourceService {

    @Autowired
    private ChangelogSourceDao changelogSourceDao;

    @Value("${changelog.sources.dir}")
    private File changelogSourcesDir;

    public List<SourceName> getAllChangelogSourceNames() {
        return changelogSourceDao.getAllSourceNames();
    }

    public Source findChangelogSourceById(Long id) {
        return changelogSourceDao.findOne(id);
    }

    public Source findChangelogSourceByName(String name) {
        return changelogSourceDao.findChangelogSourceByName(name);
    }

    public Source createChangelogSource(String name, String desctiption, String url, Long sourceNameId,
                                        ChangelogSourceController.AttachmentsResponse attachments) throws IOException {
        User user = User.getCurrentUser();
        Source source = changelogSourceDao.create(name, desctiption, url, sourceNameId, user);
        if (attachments != null) {
            Long srcId = source.getId();
            File destDir = new File(changelogSourcesDir, srcId.toString());
            for(ChangelogSourceController.AttachmentsResponse.Row row : attachments.getRows()) {
                FileUtils.moveFileToDirectory(row.getTmpFile(), destDir, true);
                String origName = row.getName();
                if (StringUtils.isNotBlank(origName)) {
                    FileUtils.moveFile(new File(destDir, row.getTmpFile().getName()), new File(destDir, origName));
                }
            }
        }
        return source;
    }

}
