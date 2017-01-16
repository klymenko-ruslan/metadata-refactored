package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.chlogsrc.Source;
import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/16/17.
 */
@Repository
public class ChangelogSourceDao extends AbstractDao<Source> {

    public ChangelogSourceDao() {
        super(Source.class);
    }

    public Source findChangelogSourceByName(String name) {
        try {
            return em.createNamedQuery("findChangelogSourceByName", Source.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<SourceName> getAllSourceNames() {
        return em.createNamedQuery("findAllChangelogSourceNames", SourceName.class).getResultList();
    }

}
