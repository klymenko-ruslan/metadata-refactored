package com.turbointernational.metadata.service;

import com.turbointernational.metadata.dao.SourceNameDao;
import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import com.turbointernational.metadata.web.dto.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-01-27.
 */
@Service
public class ChangelogSourceNameService {

    @Autowired
    private SourceNameDao sourceNameDao;

    public SourceName findChangelogSourceNameByName(String name) {
        return sourceNameDao.findChangelogSourceNameByName(name);
    }

    public List<SourceName> getAllChangelogSourceNames() {
        return sourceNameDao.getAllSourceNames();
    }

    public Page<SourceName> filterChangelogSourceNames(String sortProperty, String sortOrder, Integer offset, Integer limit) {
        return sourceNameDao.filterSourceNames(sortProperty, sortOrder, offset, limit);
    }

    public SourceName create(String name) {
        return sourceNameDao.create(name);
    }

    public SourceName update(Long id, String name) {
        return sourceNameDao.update(id, name);
    }

}
