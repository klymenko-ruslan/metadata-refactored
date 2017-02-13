package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.StandardOversizePart;
import com.turbointernational.metadata.entity.StandardOversizePartId;
import com.turbointernational.metadata.entity.part.Part;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-13.
 */
@Repository
public class StandardOversizePartDao extends AbstractDao<StandardOversizePart> {

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

    public void delete(Long standardPartId, Long oversizePartId) {
        Part standard = new Part();
        standard.setId(standardPartId);
        Part oversize = new Part();
        oversize.setId(oversizePartId);
        StandardOversizePart sop = em.getReference(StandardOversizePart.class, new StandardOversizePartId(standard, oversize));
        em.remove(sop);
    }

}
