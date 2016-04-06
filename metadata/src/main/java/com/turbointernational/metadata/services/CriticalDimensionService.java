package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 06.04.16.
 */
@Service
public class CriticalDimensionService {

    @Autowired
    private CriticalDimensionDao criticalDimensionDao;

    @Autowired
    private PartDao partDao;

    public List<CriticalDimension> findForThePart(long partId) {
        Part part = partDao.findOne(partId);
        Long partTypeId = part.getPartType().getId();
        return criticalDimensionDao.findForPartType(partTypeId);
    }

}
