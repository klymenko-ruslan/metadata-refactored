package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.other.*;
import com.turbointernational.metadata.domain.AbstractDao;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class InterchangeDao extends AbstractDao<Interchange> {
    
    public InterchangeDao() {
        super(Interchange.class);
    }

    public List<TurboType> findTurboTypesByManufacturerId(Long manufacturerId) {
        return em.createQuery(
                "SELECT o\n"
                + "FROM\n"
                + "  TurboType o\n"
                + "  JOIN o.manufacturer\n"
                + "WHERE o.manufacturer.id = :manufacturerId\n"
                + "ORDER BY o.name", TurboType.class
        ).setParameter("manufacturerId", manufacturerId).getResultList();
    }

}