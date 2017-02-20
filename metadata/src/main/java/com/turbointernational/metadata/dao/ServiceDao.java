package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.Service;
import com.turbointernational.metadata.web.dto.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-15.
 */
@Repository
public class ServiceDao extends AbstractDao<Service> {

    public static final long SERVICEID_BOM = 1;
    public static final long SERVICEID_INTERCHANGE = 2;
    public static final long SERVICEID_MAS90SYNC = 3;
    public static final long SERVICEID_SALESNOTES = 4;
    public static final long SERVICEID_APPLICATIONS = 5;
    public static final long SERVICEID_KIT = 6;
    public static final long SERVICEID_PART = 7;
    public static final long SERVICEID_TURBOMODEL = 8;
    public static final long SERVICEID_TURBOTYPE = 9;

    public ServiceDao() {
        super(Service.class);
    }

    public Page<Service> filter(String sortProperty, String sortOrder, Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Service> ecq = cb.createQuery(Service.class);
        Root<Service> root = ecq.from(Service.class);
        ecq.select(root);
        if (sortOrder != null) {
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            if (sortOrder.equalsIgnoreCase("asc")) {
                ecq.orderBy(cb.asc(root.get(sortProperty)));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                ecq.orderBy(cb.desc(root.get(sortProperty)));
            } else {
                throw new AssertionError("Unknown sort order: " + sortOrder);
            }
        }
        TypedQuery<Service> q = em.createQuery(ecq);
        if (offset != null) {
            q.setFirstResult(offset);
        }
        if (limit != null) {
            q.setMaxResults(limit);
        }
        List<Service> recs = q.getResultList();
        CriteriaQuery<Long> ccq = cb.createQuery(Long.class);
        Root<Service> countRoot = ccq.from(Service.class);
        ccq.select(cb.count(countRoot));
        long total = em.createQuery(ccq).getSingleResult();
        return new Page(total, recs);
    }

    public void setChangelogSourceRequired(Long serviceId, Boolean required) {
        Service service = findOne(serviceId);
        service.setRequiredSource(required);
        merge(service);
    }

    public boolean isChangelogSourceRequired(long serviceId) {
        return findOne(serviceId).isRequiredSource();
    }

}
