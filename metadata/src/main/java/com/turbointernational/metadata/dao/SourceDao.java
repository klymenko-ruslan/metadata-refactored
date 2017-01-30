package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Date;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/16/17.
 */
@Repository
public class SourceDao extends AbstractDao<Source> {

    public SourceDao() {
        super(Source.class);
    }

    public Source findChangelogSourceByName(String name) {
        try {
            return em.createNamedQuery("findChangelogSourceByName", Source.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Source create(String name, String desctiption, String url, Long sourceNameId, User user) {
        Date now = new Date();
        SourceName sourceName = em.getReference(SourceName.class, sourceNameId);
        Source source = new Source();
        source.setName(name);
        source.setDescription(desctiption);
        source.setUrl(url);
        source.setSourceName(sourceName);
        source.setCreated(now);
        source.setCreateUser(user);
        source.setUpdated(now);
        source.setUpdateUser(user);
        em.persist(source);
        return source;
    }

    public Source update(Long id, String name, String desctiption, String url, Long sourceNameId, User user) {
        Source source = findOne(id);
        Date now = new Date();
        SourceName sourceName = em.getReference(SourceName.class, sourceNameId);
        source.setName(name);
        source.setDescription(desctiption);
        source.setUrl(url);
        source.setSourceName(sourceName);
        source.setUpdated(now);
        source.setUpdateUser(user);
        merge(source);
        return source;
    }

}
