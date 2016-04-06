package com.turbointernational.metadata.domain.criticaldimension;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 06.04.16.
 */
@Repository
public class CriticalDimensionDao extends AbstractDao<CriticalDimension> {

    public CriticalDimensionDao() {
        super(CriticalDimension.class);
    }

    public List<CriticalDimension> findForPartType(long partTypeId) {
        return em.createNamedQuery("findCriticalDimensionsForPartType", CriticalDimension.class)
                .setParameter("partTypeId", partTypeId).getResultList();
    }

}
