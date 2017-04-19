package com.turbointernational.metadata.dao;

import static java.sql.Types.BIGINT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.TurboType;
import com.turbointernational.metadata.entity.part.Interchange;

/**
 * @author jrodriguez
 */
@Repository
public class InterchangeDao extends AbstractDao<Interchange> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public InterchangeDao() {
        super(Interchange.class);
    }

    public List<TurboType> findTurboTypesByManufacturerId(Long manufacturerId) {
        // @formatter:off
        return em.createQuery(
                "SELECT o\n" +
                "FROM\n" +
                "  TurboType o\n" +
                "  JOIN o.manufacturer\n" +
                "WHERE o.manufacturer.id = :manufacturerId\n" + "ORDER BY o.name", TurboType.class)
                .setParameter("manufacturerId", manufacturerId).getResultList();
        // @formatter:on
    }

    /**
     * Get interchagnes for a specified part.
     *
     * @param partId
     * @return Array of part IDs (interchanges) or empty array. Never null.
     */
    public List<Long> getInterchanges(Long partId) {
        // @formatter:off
        List<Long> interchageIds = jdbcTemplate.query(
                "select part_id from interchange_item where part_id != ? " +
                "and interchange_header_id in(select interchange_header_id from interchange_item where part_id = ?)",
                new Object[] {partId, partId}, new int[] {BIGINT, BIGINT}, (rs, rowNum) -> {
                    return rs.getLong(1);
                });
        // @formatter:on
        return interchageIds;
    }

}
