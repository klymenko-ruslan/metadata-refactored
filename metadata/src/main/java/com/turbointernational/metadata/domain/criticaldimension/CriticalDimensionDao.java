package com.turbointernational.metadata.domain.criticaldimension;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
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

    public CriticalDimensionEnum getCriticalDimensionEnumById(Integer id) {
        return em.find(CriticalDimensionEnum.class, id);
    }

    public List<CriticalDimensionEnum> getAllCritDimEnums() {
        return em.createNamedQuery("getAllCritDimEnums", CriticalDimensionEnum.class)
                .getResultList();
    }

    public List<CriticalDimensionEnumVal> getCritDimEnumVals(Integer enumId) {
        return em.createNamedQuery("getAllCritDimEnumVals", CriticalDimensionEnumVal.class)
                .setParameter("enumId", enumId)
                .getResultList();
    }

    @Transactional
    public CriticalDimensionEnum addCritDimEnum(CriticalDimensionEnum cde) {
        em.persist(cde);
        return cde;
    }

    @Transactional
    public CriticalDimensionEnumVal addCritDimEnumVal(CriticalDimensionEnumVal cdev) {
        em.persist(cdev);
        return cdev;
    }

    @Transactional
    public CriticalDimensionEnum updateCritDimEnum(CriticalDimensionEnum cde) {
        em.merge(cde);
        return cde;
    }

    @Transactional
    public CriticalDimensionEnumVal updateCritDimEnumVal(CriticalDimensionEnumVal cdev) {
        em.merge(cdev);
        return cdev;
    }

    @Transactional
    public void removeCritDimEnum(Integer cdeId) {
        CriticalDimensionEnum cde = em.getReference(CriticalDimensionEnum.class, cdeId);
        em.remove(cde);
    }

    @Transactional
    public void removeCritDimEnumVal(Integer cdevId) {
        CriticalDimensionEnumVal cdev = em.getReference(CriticalDimensionEnumVal.class, cdevId);
        em.remove(cdev);
    }

    public CriticalDimensionEnum findCritDimEnumByName(String name) {
        try {
            return em.createNamedQuery("findCritDimEnumByName", CriticalDimensionEnum.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch(PersistenceException e) {
            return null;
        }
    }

    public CriticalDimensionEnumVal findCritDimEnumValByName(Integer enumId, String name) {
        try {
            return em.createNamedQuery("findCritDimEnumValByName", CriticalDimensionEnumVal.class)
                    .setParameter("enumId", enumId)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch(PersistenceException e) {
            return null;
        }
    }

}
