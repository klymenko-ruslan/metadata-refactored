package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.chlogsrc.ChangelogSourceLink;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

/**
 * Created by dimytro.trunykov@zorallabs.com on 2017-02-01.
 */
@Repository
public class ChangelogSourceLinkDao extends AbstractDao<ChangelogSourceLink> {

    public ChangelogSourceLinkDao() {
        super(ChangelogSourceLink.class);
    }

    public ChangelogSourceLink findByChangelogId(Long changelogId) {
        try {
            return em.createNamedQuery("getChangelogSourceLinkForChangelog", ChangelogSourceLink.class)
                    .setParameter("changelogId", changelogId)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

}
