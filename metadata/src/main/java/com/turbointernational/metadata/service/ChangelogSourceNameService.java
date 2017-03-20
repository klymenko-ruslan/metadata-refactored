package com.turbointernational.metadata.service;

import com.turbointernational.metadata.dao.SourceNameDao;
import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import com.turbointernational.metadata.web.dto.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-01-27.
 */
@Service
public class ChangelogSourceNameService {

    @Autowired
    private SourceNameDao sourceNameDao;

    @PersistenceContext(unitName = "metadata")
    private EntityManager em;

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

    public boolean remove(Long id) {
        Long numRefs = em.createNamedQuery("getNumChangelogSourcesForSourceName", Long.class)
                .setParameter("sourceNameId", id)
                .getSingleResult();
        if (numRefs == 0) {
            sourceNameDao.delete(id);
            return true;
        } else {
            return false;
        }
    }

}
