package com.turbointernational.metadata.domain.car;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

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

}
