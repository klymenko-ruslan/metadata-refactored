package com.turbointernational.metadata.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.Changelog.ServiceEnum;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.Changelog_;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.User_;
import com.turbointernational.metadata.util.FilterUtils.DateRange;
import com.turbointernational.metadata.web.dto.Page;

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

    public Page<Changelog> filter(List<ServiceEnum> services, List<Long> userIds, DateRange dateRange, Date startDate, Date finishDate, String description,
            String data, Long partId, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Changelog> ecq = cb.createQuery(Changelog.class);
        Root<Changelog> root = ecq.from(Changelog.class);
        Join<Changelog, User> userJoin = null;
        ListJoin<Changelog, ChangelogPart> changelogPartJoin = null;
        ecq.select(root);
        int numPredicates = 0;
        List<Predicate> lstPredicates = new ArrayList<>(5);
        if (services != null && !services.isEmpty()) {
            lstPredicates.add(root.get(Changelog_.service).in(services));
            numPredicates++;
        }
        if (userIds != null && !userIds.isEmpty()) {
            userJoin = root.join("user");
            lstPredicates.add(userJoin.get(User_.id).in(userIds));
            numPredicates++;
        }
        if (startDate != null) {
            lstPredicates.add(cb.greaterThanOrEqualTo(root.get(Changelog_.changeDate), startDate));
            numPredicates++;
        }
        if (finishDate != null) {
            lstPredicates.add(cb.lessThanOrEqualTo(root.get(Changelog_.changeDate), finishDate));
            numPredicates++;
        }
        if (description != null) {
            lstPredicates.add(cb.like(root.get(Changelog_.description), "%" + description + "%"));
            numPredicates++;
        }
        if (data != null) {
            lstPredicates.add(cb.like(root.get(Changelog_.data), "%" + data + "%"));
            numPredicates++;
        }
        if (partId != null) {
            changelogPartJoin = root.join(Changelog_.changelogParts);
            lstPredicates.add(cb.equal(changelogPartJoin.get("part").get("id"), partId));
            numPredicates++;
        }
        Predicate[] arrPredicates = lstPredicates.toArray(new Predicate[numPredicates]);
        ecq.where(arrPredicates);
        if (sortOrder != null) {
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            From<?, ?> f;
            if (sortProperty.equals("user.name")) {
                if (userJoin == null) {
                    userJoin = root.join(Changelog_.user);
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
        if (userIds != null && !userIds.isEmpty()) {
            changelogCountRoot.join(Changelog_.user);
        }
        if (partId != null) {
            changelogCountRoot.join(Changelog_.changelogParts);
        }
        ccq.select(cb.count(changelogCountRoot));
        ccq.where(arrPredicates);
        long total = em.createQuery(ccq).getSingleResult();
        return new Page<>(total, recs);
    }

}
