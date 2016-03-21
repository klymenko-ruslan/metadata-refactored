package com.turbointernational.metadata.domain.security;

import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.web.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by trunikov on 21.03.16.
 */
@Repository
public class AuthProviderDao extends AbstractDao<AuthProvider> {

    public AuthProviderDao() {
        super(AuthProvider.class);
    }

    public Page getAllAuthProviders(String sortProperty, String sortOrder, int offset, int limit) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        // Page.
        CriteriaQuery<AuthProvider> recsCriteriaQuery = criteriaBuilder.createQuery(AuthProvider.class);
        Root<AuthProvider> recsRoot = recsCriteriaQuery.from(AuthProvider.class);
        recsCriteriaQuery.select(recsRoot);
        if (sortProperty != null && sortOrder != null) {
            if (sortOrder.equalsIgnoreCase("asc")) {
                recsCriteriaQuery.orderBy(criteriaBuilder.asc(recsRoot.get(sortProperty)));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                recsCriteriaQuery.orderBy(criteriaBuilder.desc(recsRoot.get(sortProperty)));
            }
        }
        List<AuthProvider> recs = em.createQuery(recsCriteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        // Count.
        CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<AuthProvider> countRoot = countCriteriaQuery.from(AuthProvider.class);
        countCriteriaQuery.select(criteriaBuilder.count(countRoot));
        long total = em.createQuery(countCriteriaQuery).getSingleResult();
        return new Page(total, recs);
    }

    public Long createAuthProviderLDAP(AuthProviderLdap authProviderLDAP) {
        em.persist(authProviderLDAP);
        return authProviderLDAP.getId();
    }

}
