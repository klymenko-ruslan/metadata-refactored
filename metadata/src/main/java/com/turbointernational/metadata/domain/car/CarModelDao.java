package com.turbointernational.metadata.domain.car;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by trunikov on 12/9/15.
 */
@Repository
public class CarModelDao extends AbstractDao<CarModel> {
    public CarModelDao() {
        super(CarModel.class);
    }

    public boolean exists(String name, long carMakeId) {
        Query q = em.createQuery("FROM CarModel WHERE name=:name AND make.id=:id");
        q.setParameter("name", name);
        q.setParameter("id", carMakeId);
        q.setMaxResults(1);
        List r = q.getResultList();
        return !r.isEmpty();
    }

    public List<CarModel> findCarModelsOfMake(long makeId) {
        Query q = em.createNamedQuery("findCarModelsOfMake", CarModel.class);
        q.setParameter("makeId", makeId);
        return q.getResultList();
    }

    public List<CarModel> findCarModelsByFilter(String name, long makeId, Integer maxResults) {
        Query q = em.createNamedQuery("findCarModelsByFilter", CarModel.class);
        q.setParameter("name", name);
        q.setParameter("makeId", makeId);
        if (maxResults != null) {
            q.setMaxResults(maxResults);
        }
        return q.getResultList();
    }

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
