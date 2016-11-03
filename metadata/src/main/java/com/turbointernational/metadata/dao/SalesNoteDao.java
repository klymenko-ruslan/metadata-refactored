package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.SalesNote;
import org.springframework.stereotype.Repository;

/**
 * Created by dmytro.trunykov@zorallabs.com on 29.03.16.
 */
@Repository
public class SalesNoteDao extends AbstractDao<SalesNote> {

    public SalesNoteDao() {
        super(SalesNote.class);
    }

}
