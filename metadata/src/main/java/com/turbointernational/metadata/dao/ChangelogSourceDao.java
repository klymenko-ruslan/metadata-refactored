package com.turbointernational.metadata.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.chlogsrc.ChangelogSource;
import com.turbointernational.metadata.web.dto.Page;

/**
 * Created by dimytro.trunykov@zorallabs.com on 2017-01-30.
 */
@Repository
public class ChangelogSourceDao extends AbstractDao<ChangelogSource> {

    public ChangelogSourceDao() {
        super(ChangelogSource.class);
    }

    public Page<ChangelogSource> filter(Long sourceId, Long changelogId, String sortProperty, String sortOrder,
                                        Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ChangelogSource> ecq = cb.createQuery(ChangelogSource.class);
        Root<ChangelogSource> root = ecq.from(ChangelogSource.class);
        ecq.select(root);
        List<Predicate> lstPredicates = new ArrayList<>(5);
        if (sourceId != null) {
            lstPredicates.add(cb.equal(root.get("pk").get("source").get("id"), sourceId));
        }
        if (changelogId != null) {
            lstPredicates.add(cb.equal(root.get("pk").get("changelog").get("id"), changelogId));
        }
        Predicate[] arrPredicates = lstPredicates.toArray(new Predicate[lstPredicates.size()]);
        ecq.where(arrPredicates);
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
        TypedQuery<ChangelogSource> q = em.createQuery(ecq);
        if (offset != null) {
            q.setFirstResult(offset);
        }
        if (limit != null) {
            q.setMaxResults(limit);
        }
        List<ChangelogSource> recs = q.getResultList();
        CriteriaQuery<Long> ccq = cb.createQuery(Long.class);
        Root<ChangelogSource> countRoot = ccq.from(ChangelogSource.class);
        //countRoot.join("user");
        ccq.select(cb.count(countRoot));
        ccq.where(arrPredicates);
        long total = em.createQuery(ccq).getSingleResult();
        return new Page<>(total, recs);
    }

}
