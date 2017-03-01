package com.turbointernational.metadata.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.CarFuelType;

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

    @SuppressWarnings("unchecked")
    public List<CarFuelType> findAllOrderedByName() {
        Query query = em.createNamedQuery("findAllCarFuelTypeOrderedByName", CarFuelType.class);
        return query.getResultList();
    }
}
