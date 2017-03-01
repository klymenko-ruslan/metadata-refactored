package com.turbointernational.metadata.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.CarEngine;

/**
 * Created by trunikov on 12/9/15.
 */
@Repository
public class CarEngineDao extends AbstractDao<CarEngine> {
    public CarEngineDao() {
        super(CarEngine.class);
    }

    public boolean exists(String engineSize, long carMakeId) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM CarEngine WHERE engineSize =:engineSize AND fuelType.id=:id", CarEngine.class);
        q.setParameter("engineSize", engineSize);
        q.setParameter("id", carMakeId);
        q.setMaxResults(1);
        List<?> r = q.getResultList();
        return !r.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public List<CarEngine> findAllOrderedByName() {
        Query query = em.createNamedQuery("findAllCarEngineOrderedByName", CarEngine.class);
        return query.getResultList();
    }

    public List<CarEngine> findByName(String engineSize, Long fuelTypeId, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CarEngine> ecq = cb.createQuery(CarEngine.class);
        Root<CarEngine> root = ecq.from(CarEngine.class);
        Path<Object> attrEngineSize = root.get("engineSize");
        Predicate predicateEngineSize;
        if (engineSize == null) {
            predicateEngineSize = cb.isNull(attrEngineSize);
        } else {
            predicateEngineSize = cb.equal(attrEngineSize, engineSize);
        }
        Path<Object> attrFuelType = root.get("fuelType");
        Predicate predicateFuelType;
        if (fuelTypeId == null) {
            predicateFuelType = cb.isNull(attrFuelType);
        } else {
            predicateFuelType = cb.equal(attrFuelType, fuelTypeId);
        }
        ecq.where(predicateEngineSize, predicateFuelType);
        TypedQuery<CarEngine> q = em.createQuery(ecq);
        if (limit != null) {
            q.setMaxResults(limit);
        }
        return q.getResultList();
    }

}
