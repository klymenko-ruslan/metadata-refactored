package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.part.salesnote.SalesNotePartDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2/28/16.
 */
@Service
public class SalesNoteService {

    @Autowired
    private SalesNotePartDao salesNotePartDao;

    public Long findPrimaryPartIdForThePart(long partId) {
        return salesNotePartDao.findPrimaryPartIdForThePart(partId);
    }

}
