package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.ChangelogPart;
import org.springframework.stereotype.Repository;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-07.
 */
@Repository
public class ChangelogPartDao extends AbstractDao<ChangelogPart> {

    public ChangelogPartDao() {
        super(ChangelogPart.class);
    }

}
