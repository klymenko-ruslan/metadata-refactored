package com.turbointernational.metadata.dao;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.chlogsrc.ChangelogSource;

/**
 * Created by dimytro.trunykov@zorallabs.com on 2017-01-30.
 */
@Repository
public class ChangelogSourceDao extends AbstractDao<ChangelogSource> {

    public ChangelogSourceDao() {
        super(ChangelogSource.class);
    }

}
