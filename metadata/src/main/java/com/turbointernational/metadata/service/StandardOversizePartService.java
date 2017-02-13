package com.turbointernational.metadata.service;

import com.turbointernational.metadata.dao.StandardOversizePartDao;
import com.turbointernational.metadata.entity.part.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-13.
 */
@Service
public class StandardOversizePartService {

    @Autowired
    private StandardOversizePartDao standardOversizePartDao;

    @Secured("ROLE_READ")
    public List<Part> findOversizeParts(Long partId) {
        return standardOversizePartDao.findOversizeParts(partId);
    }

    @Secured("ROLE_READ")
    public List<Part> findStandardParts(Long partId) {
        return standardOversizePartDao.findStandardParts(partId);
    }

    @Transactional
    @Secured("ROLE_ALTER_PART")
    public void delete(Long standardPartId, Long oversizePartId) {
        standardOversizePartDao.delete(standardPartId, oversizePartId);
    }

}
