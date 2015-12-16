package com.turbointernational.metadata.domain.car;

import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.web.View;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by trunikov on 12/9/15.
 */
@Repository
public class CarFuelTypeDao extends AbstractDao<CarFuelType> {
    public CarFuelTypeDao() {
        super(CarFuelType.class);
    }

    public CarFuelType findCarFuelTypeByName(String name) {
        Query carFuelTypeByName = em.createNamedQuery("findCarFuelTypeByName");
        carFuelTypeByName.setParameter("name", name);
        try {
            return (CarFuelType) carFuelTypeByName.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<View.CarFuelType> findAllOrderedByName() {
        Query query = em.createNamedQuery("findAllCarFuelTypeOrderedByName");
        return query.getResultList();
    }
}
