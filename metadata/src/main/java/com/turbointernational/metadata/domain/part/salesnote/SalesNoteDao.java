package com.turbointernational.metadata.domain.part.salesnote;

import com.turbointernational.metadata.domain.AbstractDao;
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
