package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.SalesNote;
import com.turbointernational.metadata.entity.SalesNotePart;
import com.turbointernational.metadata.entity.SalesNotePartId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-02-23.
 */
@Repository
public class SalesNotePartDao extends AbstractDao<SalesNotePart> {

    @Autowired
    private DataSource dataSource;

    public SalesNotePartDao() {
        this(SalesNotePart.class);
    }

    public SalesNotePartDao(Class<SalesNotePart> clazz) {
        super(clazz);
    }

    public Long findPrimaryPartIdForThePart(long partId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Long primaryPartId = jdbcTemplate.query(
                "select snp.part_id " +
                "from sales_note_part as snp " +
                "where " +
                "    snp.primary_part != 0 " +
                "    and sales_note_id in( " +
                "        select snp2.sales_note_id " +
                "        from part as p " +
                "        join sales_note_part as snp2 on p.id = snp2.part_id " +
                "        where p.id = ? " +
                "    );", ps -> ps.setLong(1, partId), rs -> rs.next() ? rs.getLong(1) : null);
        return primaryPartId;
    }

    public SalesNotePart findOne(Long salesNoteId, Long partId) {
        SalesNote salesNote = em.getReference(SalesNote.class, salesNoteId);
        Part part = em.getReference(Part.class, partId);
        SalesNotePartId id = new SalesNotePartId(salesNote, part);
        return em.find(SalesNotePart.class, id);
    }

    @Transactional
    public void delete(SalesNotePart salesNotePart) {
        em.remove(salesNotePart);
    }

}