package com.turbointernational.metadata.domain.changelog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.domain.security.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.turbointernational.metadata.web.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ChangelogDao extends AbstractDao<Changelog> {
    
    @Autowired(required=true)
    ObjectMapper json;
    
    public ChangelogDao() {
        super(Changelog.class);
    }

    @Transactional
    public Changelog log(String description) {
        User user = User.getCurrentUser();
        return log(user, description, "");
    }

    @Transactional
    public Changelog log(User user, String description) {
        return log(user, description, "");
    }

    @Transactional
    public Changelog log(String description, String data) {
        User user = User.getCurrentUser();
        return log(user, description, data);
    }

    @Transactional
    public Changelog log(User user, String description, String data) {
        Changelog changelog = new Changelog();
        changelog.setDescription(description);
        changelog.setChangeDate(new Date());
        changelog.setData(data);
        changelog.setUser(user);
        persist(changelog);
        return changelog;
    }

    @Transactional
    public Changelog log(String description, Serializable data) {
        User user = User.getCurrentUser();
        return log(user, description, data);
    }

    @Transactional
    public Changelog log(User user, String description, Serializable data) {
        try {
            Changelog changelog = new Changelog();
            changelog.setDescription(description);
            changelog.setChangeDate(new Date());
            changelog.setData(json.writeValueAsString(data));
            changelog.setUser(user);

            persist(changelog);

            return changelog;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize data.", e);
        }
    }


    public Page<Changelog> filter(Long userId, Date startDate, Date finishDate, String description,
                                  String sortProperty, String sortOrder,
                                  Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Changelog> ecq = cb.createQuery(Changelog.class);
        Root<Changelog> changelogRoot = ecq.from(Changelog.class);
        ecq.select(changelogRoot);
        // TODO: where
        if (sortOrder != null) {
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            if (sortOrder.equalsIgnoreCase("asc")) {
                ecq.orderBy(cb.asc(changelogRoot.get(sortProperty)));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                ecq.orderBy(cb.desc(changelogRoot.get(sortProperty)));
            } else {
                throw new AssertionError("Unknown sort order: " + sortOrder);
            }
        }
        TypedQuery<Changelog> q = em.createQuery(ecq);
        if (offset != null) {
            q.setFirstResult(offset);
        }
        if (limit != null) {
            q.setMaxResults(limit);
        }
        List<Changelog> recs = q.getResultList();
        CriteriaQuery<Long> ccq = cb.createQuery(Long.class);
        Root<Changelog> changelogCountRoot = ccq.from(Changelog.class);
        ccq.select(cb.count(changelogCountRoot));
        // TODO: where
        long total = em.createQuery(ccq).getSingleResult();
        return new Page(total, recs);
    }

}
