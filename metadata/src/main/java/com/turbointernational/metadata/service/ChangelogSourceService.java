package com.turbointernational.metadata.service;

import com.turbointernational.metadata.dao.ChangelogSourceDao;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/16/17.
 */
@Service
public class ChangelogSourceService {

    @Autowired
    private ChangelogSourceDao changelogSourceDao;

    public List<SourceName> getAllChangelogSourceNames() {
        return changelogSourceDao.getAllSourceNames();
    }

    public Source findChangelogSourceByName(String name) {
        return changelogSourceDao.findChangelogSourceByName(name);
    }

    public Source createChangelogSource(String name, String desctiption, String url, Long sourceNameId) {
        User user = User.getCurrentUser();
        Source source = changelogSourceDao.create(name, desctiption, url, sourceNameId, user);
        return source;
    }

}
