package com.turbointernational.metadata.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.CarMake;

/**
 * Created by trunikov on 12/9/15.
 */
@Repository
public class CarMakeDao extends AbstractDao<CarMake> {
    public CarMakeDao() {
        super(CarMake.class);
    }

    public CarMake findCarMakeByName(String name) {
        Query carMakeByName = em.createNamedQuery("findCarMakeByName");
        carMakeByName.setParameter("name", name);
        try {
            return (CarMake) carMakeByName.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<CarMake> findAllOrderedByName() {
        Query query = em.createNamedQuery("findAllCarMakeOrderedByName", CarMake.class);
        return query.getResultList();
    }

}
