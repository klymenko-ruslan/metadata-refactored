package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.AbstractDao;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class TurboTypeDao extends AbstractDao<TurboType> {

    public TurboTypeDao() {
        super(TurboType.class);
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
