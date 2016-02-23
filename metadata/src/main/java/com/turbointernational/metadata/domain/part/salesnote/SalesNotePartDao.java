package com.turbointernational.metadata.domain.part.salesnote;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-02-23.
 */
@Repository
public class SalesNotePartDao extends AbstractDao<SalesNotePart> {

    public SalesNotePartDao() {
        this(SalesNotePart.class);
    }

    public SalesNotePartDao(Class<SalesNotePart> clazz) {
        super(clazz);
    }

}
