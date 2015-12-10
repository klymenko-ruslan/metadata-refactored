package com.turbointernational.metadata.domain.part.types;

import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by trunikov on 12/4/15.
 */
@Repository
public class TurboCarModelEngineYearDao extends AbstractDao<TurboCarModelEngineYear> {

   private static final Logger log = LoggerFactory.getLogger(TurboCarModelEngineYearDao.class);

    public TurboCarModelEngineYearDao() {
        super(TurboCarModelEngineYear.class);
    }

    public List<TurboCarModelEngineYear> getPartLinkedApplications(Long partId) {
        return em.createNamedQuery("partLinkedApplications", TurboCarModelEngineYear.class).
                setParameter("partId", partId).
                getResultList();
    }

    public void add(Long partId, Long applicationId) {
        Turbo turbo = em.getReference(Turbo.class, partId);
        CarModelEngineYear application = em.getReference(CarModelEngineYear.class, applicationId);
        TurboCarModelEngineYear partApplication = new TurboCarModelEngineYear();
        partApplication.setTurbo(turbo);
        partApplication.setCarModelEngineYear(application);
        em.persist(partApplication);
    }

    public int delete(Long partId, Long applicationId) {
        final Query delQuery = em.createNamedQuery("delelePartApplication");
        delQuery.setParameter("partId", partId);
        delQuery.setParameter("applicationId", applicationId);
        int deleted = delQuery.executeUpdate();
        return deleted;
    }
}
