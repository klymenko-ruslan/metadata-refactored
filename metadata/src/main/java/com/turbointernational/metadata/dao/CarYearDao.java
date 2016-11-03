package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.CarYear;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * Created by trunikov on 12/20/15.
 */
@Repository
public class CarYearDao extends AbstractDao<CarYear> {

    public CarYearDao() {
        super(CarYear.class);
    }

    public CarYear findByName(String name) {
        Query q = em.createNamedQuery("findCarYearByName", CarYear.class);
        q.setParameter("name", name);
        try {
            return (CarYear) q.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

}
