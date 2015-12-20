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
public class CarEngineDao extends AbstractDao<CarEngine> {
    public CarEngineDao() {
        super(CarEngine.class);
    }

    public boolean exists(String engineSize, long carMakeId) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM CarEngine WHERE engineSize =:engineSize AND fuelType.id=:id");
        q.setParameter("engineSize", engineSize);
        q.setParameter("id", carMakeId);
        q.setMaxResults(1);
        List r = q.getResultList();
        return !r.isEmpty();
    }

    public List<CarEngine> findAllOrderedByName() {
        Query query = em.createNamedQuery("findAllCarEngineOrderedByName");
        return query.getResultList();
    }
}

