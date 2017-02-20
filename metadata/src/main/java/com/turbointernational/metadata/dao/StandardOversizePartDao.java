package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.StandardOversizePart;
import com.turbointernational.metadata.entity.StandardOversizePartId;
import com.turbointernational.metadata.entity.part.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-13.
 */
@Repository
public class StandardOversizePartDao extends AbstractDao<StandardOversizePart> {

    @Autowired
    private PartDao partDao;

    @Autowired
    private DataSource dataSource = null;

    private JdbcTemplate jdbcTemplate;

    public StandardOversizePartDao() {
        super(StandardOversizePart.class);
    }

    @PostConstruct
    public void init() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Part> findOversizeParts(Long partId) {
        List<Part>  retVal;
        List<Long> ids = jdbcTemplate.query(
                "select oversize_part_id from standard_oversize_part where standard_part_id = ?",
                (rm, rowNum) -> rm.getLong(1), partId);
        if (ids.isEmpty()) {
            retVal = new ArrayList<>(0);
        } else {
            retVal = em.createNamedQuery("findPartsByIds", Part.class)
                    .setParameter("ids", ids).getResultList();
        }
        return retVal;
    }

    public List<Part> findStandardParts(Long partId) {
        List<Part>  retVal;
        List<Long> ids = jdbcTemplate.query(
                "select standard_part_id from standard_oversize_part where oversize_part_id = ?",
                (rm, rowNum) -> rm.getLong(1), partId);
        if (ids.isEmpty()) {
            retVal = new ArrayList<>(0);
        } else {
            retVal = em.createNamedQuery("findPartsByIds", Part.class)
                    .setParameter("ids", ids).getResultList();
        }
        return retVal;
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
