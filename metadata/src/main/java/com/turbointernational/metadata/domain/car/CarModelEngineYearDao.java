package com.turbointernational.metadata.domain.car;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/4/15.
 */
@Repository
public class CarModelEngineYearDao extends AbstractDao<CarModelEngineYear> {

    public CarModelEngineYearDao() {
        super(CarModelEngineYear.class);
    }

    public List<CarModelEngineYear> find(Long carModelId, Long carEngineId, Long carYearId, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CarModelEngineYear> ecq = cb.createQuery(CarModelEngineYear.class);
        Root<CarModelEngineYear> root = ecq.from(CarModelEngineYear.class);
        Path<Object> attrModel = root.get("model");
        Predicate predicateModel = carModelId == null ? cb.isNull(attrModel) : cb.equal(attrModel, carModelId);
        Path<Object> attrEngine = root.get("engine");
        Predicate predicateEngine = carEngineId == null ? cb.isNull(attrEngine) : cb.equal(attrEngine, carEngineId);
        Path<Object> attrYear = root.get("year");
        Predicate predicateYear = carYearId == null ? cb.isNull(attrYear) : cb.equal(attrYear, carYearId);
        ecq.where(predicateModel, predicateEngine, predicateYear);
        TypedQuery<CarModelEngineYear> q = em.createQuery(ecq);
        if (limit != null) {
            q.setMaxResults(limit);
        }
        return q.getResultList();
    }

}
