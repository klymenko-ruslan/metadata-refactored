package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.GenericDao;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class TurboModelDao extends GenericDao<TurboModel> {
    
    public TurboModelDao() {
        super(TurboModel.class);
    }
    
    public List<TurboModel> findTurboModelsByTurboTypeId(Long turboTypeId) {
        return em.createQuery(
              "SELECT o\n"
            + "FROM\n"
            + "  TurboModel o\n"
            + "  JOIN o.turboType\n"
            + "WHERE o.turboType.id = :turboTypeId\n"
            + "ORDER BY o.name",
            TurboModel.class)
            .setParameter("turboTypeId", turboTypeId)
            .getResultList();
    }
}
