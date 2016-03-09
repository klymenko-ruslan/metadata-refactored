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

    /**
     * @param partId
     * @return primary part ID or -1 if this part has no one
     */
    public Long findPrimaryPartIdForThePart(long partId) {
        Long primaryPartId = salesNotePartDao.findPrimaryPartIdForThePart(partId);
        return primaryPartId == null ? -1L : primaryPartId;
    }

}
