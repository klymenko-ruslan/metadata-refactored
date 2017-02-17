package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.StandardOversizePart;
import com.turbointernational.metadata.entity.StandardOversizePartId;
import com.turbointernational.metadata.entity.part.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-13.
 */
@Repository
public class StandardOversizePartDao extends AbstractDao<StandardOversizePart> {

    @Autowired
    private PartDao partDao;

    public StandardOversizePartDao() {
        super(StandardOversizePart.class);
    }

    public List<Part> findOversizeParts(Long partId) {
        return em.createNamedQuery("findOversizeParts", Part.class)
                .setParameter("partId", partId).getResultList();
    }

    public List<Part> findStandardParts(Long partId) {
        return em.createNamedQuery("findStandardParts", Part.class)
                .setParameter("partId", partId).getResultList();
    }

    public StandardOversizePart create(Long standardPartId, Long oversizePartId) {
        Part standard = partDao.findOne(standardPartId);
        Part oversize = partDao.findOne(oversizePartId);
        if (standard.getId().equals(oversize.getId())) {
            throw new AssertionError(String.format("A part can't be related to itself: %1$d - %2$d",
                    standardPartId, oversizePartId));
        }
        if (!standard.getManufacturer().getId().equals(oversize.getManufacturer().getId())) {
            throw new AssertionError(String.format("Manufacturers must be the same: %1$d - %2$d",
                    standardPartId, oversizePartId));
        }
        if (!standard.getPartType().getId().equals(oversize.getPartType().getId())) {
            throw new AssertionError(String.format("Part types be the same: %1$d - %2$d",
                    standardPartId, oversizePartId));
        }
        StandardOversizePartId pk = new StandardOversizePartId(standard, oversize);
        StandardOversizePart sop = new StandardOversizePart(pk);
        em.persist(sop);
        return sop;
    }

    public void delete(Long standardPartId, Long oversizePartId) {
        Part standard = em.getReference(Part.class, standardPartId);
        Part oversize = em.getReference(Part.class, oversizePartId);
        StandardOversizePart sop = em.getReference(StandardOversizePart.class, new StandardOversizePartId(standard, oversize));
        em.remove(sop);
    }

}
