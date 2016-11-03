package com.turbointernational.metadata.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.turbointernational.metadata.web.dto.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

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


    public Page<Changelog> filter(Long userId, Date startDate, Date finishDate,
                                  String description, String data,
                                  String sortProperty, String sortOrder,
                                  Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Changelog> ecq = cb.createQuery(Changelog.class);
        Root<Changelog> root = ecq.from(Changelog.class);
        Join<Object, Object> userJoin = root.join("user");
        ecq.select(root);
        int numPredicates = 0;
        List<Predicate> lstPredicates = new ArrayList<>(5);
        if (userId != null) {
            lstPredicates.add(cb.equal(userJoin.get("id"), userId));
            numPredicates++;
        }
        if (startDate != null) {
            lstPredicates.add(cb.greaterThanOrEqualTo(root.get("changeDate"), startDate));
            numPredicates++;
        }
        if (finishDate != null) {
            lstPredicates.add(cb.lessThanOrEqualTo(root.get("changeDate"), finishDate));
            numPredicates++;
        }
        if (description != null) {
            lstPredicates.add(cb.like(root.get("description"), "%" + description + "%"));
            numPredicates++;
        }
        if (data != null) {
            lstPredicates.add(cb.like(root.get("data"), "%" + data + "%"));
            numPredicates++;
        }
        Predicate[] arrPredicates = lstPredicates.toArray(new Predicate[numPredicates]);
        ecq.where(arrPredicates);
        if (sortOrder != null) {
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            From f;
            if (sortProperty.equals("user.name")) {
                f = userJoin;
                sortProperty = "name";
            } else {
                f = root;
            }
            if (sortOrder.equalsIgnoreCase("asc")) {
                ecq.orderBy(cb.asc(f.get(sortProperty)));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                ecq.orderBy(cb.desc(f.get(sortProperty)));
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
        changelogCountRoot.join("user");
        ccq.select(cb.count(changelogCountRoot));
        ccq.where(arrPredicates);
        long total = em.createQuery(ccq).getSingleResult();
        return new Page(total, recs);
    }

}
