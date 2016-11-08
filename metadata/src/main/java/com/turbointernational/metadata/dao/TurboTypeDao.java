package com.turbointernational.metadata.dao;

import java.util.List;

import com.turbointernational.metadata.entity.TurboType;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

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
        return em.createNamedQuery("findTurboTypesByManufacturerId", TurboType.class)
                .setParameter("manufacturerId", manufacturerId)
                .getResultList();
    }

    public TurboType findTurboType(Long manufacturerId, String name) {
        try {
            return em.createNamedQuery("findTurboTypesByManufacturerIdAndName", TurboType.class)
                    .setParameter("manufacturerId", manufacturerId)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

}