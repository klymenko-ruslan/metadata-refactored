package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.chlogsrc.SourceName;
import com.turbointernational.metadata.web.dto.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-01-27.
 */
@Repository
public class SourceNameDao extends AbstractDao<SourceName> {

    public SourceNameDao() {
        super(SourceName.class);
    }

    public SourceName findChangelogSourceNameByName(String name) {
        try {
            return em.createNamedQuery("findChangelogSourceNameByName", SourceName.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<SourceName> getAllSourceNames() {
        return em.createNamedQuery("findAllChangelogSourceNames", SourceName.class).getResultList();
    }

    public Page<SourceName> filterSourceNames(String sortProperty, String sortOrder, Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SourceName> ecq = cb.createQuery(SourceName.class);
        Root<SourceName> root = ecq.from(SourceName.class);
        if (sortOrder != null) {
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            From f = root;
            if (sortOrder.equalsIgnoreCase("asc")) {
                ecq.orderBy(cb.asc(f.get(sortProperty)));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                ecq.orderBy(cb.desc(f.get(sortProperty)));
            } else {
                throw new AssertionError("Unknown sort order: " + sortOrder);
            }
        }
        TypedQuery<SourceName> q = em.createQuery(ecq);
        if (offset != null) {
            q.setFirstResult(offset);
        }
        if (limit != null) {
            q.setMaxResults(limit);
        }
        List<SourceName> recs = q.getResultList();
        CriteriaQuery<Long> ccq = cb.createQuery(Long.class);
        Root<SourceName> sourcenameCountRoot = ccq.from(SourceName.class);
        ccq.select(cb.count(sourcenameCountRoot));
        long total = em.createQuery(ccq).getSingleResult();
        return new Page(total, recs);
    }

    public SourceName create(String name) {
        SourceName sourceName = new SourceName();
        sourceName.setName(name);
        em.persist(sourceName);
        return sourceName;
    }

    public SourceName update(Long id, String name) {
        SourceName sourceName = findOne(id);
        sourceName.setName(name);
        em.merge(sourceName);
        return sourceName;
    }

}
