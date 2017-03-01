package com.turbointernational.metadata.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.Mas90Sync;
import com.turbointernational.metadata.web.dto.Page;

/**
 * Created by dmytro.trunykov@zorallabs.com on 13.01.16.
 */
@Repository
public class Mas90SyncDao extends AbstractDao<Mas90Sync> {

    public Mas90SyncDao() {
        super(Mas90Sync.class);
    }

    public Page<Mas90Sync> findHistory(int startPosition, int maxResults) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        // Page.
        CriteriaQuery<Mas90Sync> recsCriteriaQuery = criteriaBuilder.createQuery(Mas90Sync.class);
        Root<Mas90Sync> recsRoot = recsCriteriaQuery.from(Mas90Sync.class);
        recsCriteriaQuery.select(recsRoot);
        recsCriteriaQuery.where(criteriaBuilder.notEqual(recsRoot.get("status"), Mas90Sync.Status.IN_PROGRESS));
        recsCriteriaQuery.orderBy(criteriaBuilder.desc(recsRoot.get("id")));
        List<Mas90Sync> recs = em.createQuery(recsCriteriaQuery).setFirstResult(startPosition).setMaxResults(maxResults)
                .getResultList();
        // Count.
        CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Mas90Sync> countRoot = countCriteriaQuery.from(Mas90Sync.class);
        countCriteriaQuery.select(criteriaBuilder.count(countRoot));
        recsCriteriaQuery.where(criteriaBuilder.notEqual(countRoot.get("status"), Mas90Sync.Status.IN_PROGRESS));
        long total = em.createQuery(countCriteriaQuery).getSingleResult();

        // long total = 64;
        return new Page<>(total, recs);
    }

}
