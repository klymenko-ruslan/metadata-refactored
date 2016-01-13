package com.turbointernational.metadata.domain.other;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.web.View;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 13.01.16.
 */
@Repository
public class Mas90SyncDao extends AbstractDao<Mas90Sync> {

    public class Page {

        @JsonView({View.Summary.class})
        private final long total;

        @JsonView({View.Summary.class})
        private final List<Mas90Sync> recs;

        public Page(long total, List<Mas90Sync> recs) {
            this.total = total;
            this.recs = recs;
        }

        public long getTotal() {
            return total;
        }

        public List<Mas90Sync> getRecs() {
            return recs;
        }
    }

    public Mas90SyncDao() {
        super(Mas90Sync.class);
    }

    public Page findHistory(int startPosition, int maxResults) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        // Page.
        CriteriaQuery<Mas90Sync> recsCriteriaQuery = criteriaBuilder.createQuery(Mas90Sync.class);
        Root<Mas90Sync> recsRoot = recsCriteriaQuery.from(Mas90Sync.class);
        recsCriteriaQuery.select(recsRoot);
        recsCriteriaQuery.where(criteriaBuilder.notEqual(recsRoot.get("status"), Mas90Sync.Status.IN_PROGRESS));
        recsCriteriaQuery.orderBy(criteriaBuilder.desc(recsRoot.get("id")));
        List<Mas90Sync> recs = em.createQuery(recsCriteriaQuery)
                .setFirstResult(startPosition)
                .setMaxResults(maxResults)
                .getResultList();
        // Count.
        CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Mas90Sync> countRoot = countCriteriaQuery.from(Mas90Sync.class);
        countCriteriaQuery.select(criteriaBuilder.count(countRoot));
        recsCriteriaQuery.where(criteriaBuilder.notEqual(countRoot.get("status"), Mas90Sync.Status.IN_PROGRESS));
        long total = em.createQuery(countCriteriaQuery).getSingleResult();

        //long total = 64;
        return new Page(total, recs);
    }

}
