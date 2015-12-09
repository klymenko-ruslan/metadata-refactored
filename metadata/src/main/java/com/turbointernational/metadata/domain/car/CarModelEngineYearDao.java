package com.turbointernational.metadata.domain.car;

import com.turbointernational.metadata.domain.GenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by trunikov on 12/4/15.
 */
@Repository
public class CarModelEngineYearDao  extends GenericDao<CarModelEngineYear> {

    private static final Logger log = LoggerFactory.getLogger(CarModelEngineYearDao.class);

    public CarModelEngineYear findById(Long id) {
        return em.find(CarModelEngineYear.class, id);
    }

    public List<CarModelEngineYear> findApplicationEntries(int firstResult, int maxResults) {
        return em.createNamedQuery("allApplications").
                setFirstResult(firstResult).
                setMaxResults(maxResults).
                getResultList();
    }

}
