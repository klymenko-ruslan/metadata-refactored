package com.turbointernational.metadata.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.CarModel;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2015-12-09.
 */
@Repository
public class CarModelDao extends AbstractDao<CarModel> {
    public CarModelDao() {
        super(CarModel.class);
    }

    public boolean exists(String name, long carMakeId) {
        List<?> r = findCarModelsByFilter(name, carMakeId, 1);
        return !r.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public List<CarModel> findCarModelsOfMake(long makeId) {
        Query q = em.createNamedQuery("findCarModelsOfMake", CarModel.class);
        q.setParameter("makeId", makeId);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<CarModel> findCarModelsByFilter(String name, long makeId, Integer maxResults) {
        Query q = em.createNamedQuery("findCarModelsByFilter", CarModel.class);
        q.setParameter("name", name);
        q.setParameter("makeId", makeId);
        if (maxResults != null) {
            q.setMaxResults(maxResults);
        }
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<CarModel> findCarModelsByFilter2(long id, String name, long makeId, Integer maxResults) {
        Query q = em.createNamedQuery("findCarModelsByFilter2", CarModel.class);
        q.setParameter("id", id);
        q.setParameter("name", name);
        q.setParameter("makeId", makeId);
        if (maxResults != null) {
            q.setMaxResults(maxResults);
        }
        return q.getResultList();
    }

}
