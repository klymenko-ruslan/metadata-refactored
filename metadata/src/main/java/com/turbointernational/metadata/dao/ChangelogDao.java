package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.Changelog.ServiceEnum;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.web.dto.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ChangelogDao extends AbstractDao<Changelog> {

    public ChangelogDao() {
        super(Changelog.class);
    }

    @Transactional
    public Changelog log(ServiceEnum service, User user, String description, String data) {

        Changelog changelog = new Changelog();
        changelog.setService(service);
        changelog.setDescription(description);
        changelog.setChangeDate(new Date());
        changelog.setData(data);
        changelog.setUser(user);

        persist(changelog);

        return changelog;
    }

    public Page<Changelog> filter(ServiceEnum service, Long userId, Date startDate, Date finishDate,
                                  String description, String data, Long partId,
                                  String sortProperty, String sortOrder,
                                  Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Changelog> ecq = cb.createQuery(Changelog.class);
        Root<Changelog> root = ecq.from(Changelog.class);
        Join<Changelog, User> userJoin = null;
        Join<Changelog, ChangelogPart> changelogPartJoin = null;
        ecq.select(root);
        int numPredicates = 0;
        List<Predicate> lstPredicates = new ArrayList<>(5);
        if (service != null) {
            lstPredicates.add(cb.greaterThanOrEqualTo(root.get("service"), service));
            numPredicates++;
        }
        if (userId != null) {
            userJoin = root.join("user");
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
        if (partId != null) {
            changelogPartJoin = root.join("changelogParts");
            lstPredicates.add(cb.equal(changelogPartJoin.get("part").get("id"), partId));
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
                if (userJoin == null) {
                    userJoin = root.join("user");
                }
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
        if (userId != null) {
            changelogCountRoot.join("user");
        }
        if (partId != null) {
            changelogCountRoot.join("changelogParts");
        }
        ccq.select(cb.count(changelogCountRoot));
        ccq.where(arrPredicates);
        long total = em.createQuery(ccq).getSingleResult();
        return new Page(total, recs);
    }

}
