package com.turbointernational.metadata.dao;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.turbointernational.metadata.web.dto.ChangelogAggregation;
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

    public Page<Changelog> filter(List<ServiceEnum> services, List<Long> userIds, Date startDate, Date finishDate,
            String description, String data, Long partId, String sortProperty, String sortOrder, Integer offset,
            Integer limit) {
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

    //@format:off
    public List<ChangelogAggregation> filterAggragation(List<ServiceEnum> services, List<Long> userIds, Date startDate,
            Date finishDate, String description, String data) {
        return Arrays.asList(
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(1L, "Jeff Rodriguez"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(2L, "Paul Thiry"),
            10, 1, 1367, 1, 0, 0, 45, 1, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(3L, "Jeff Wesson"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(4L, "Brian Malewicz"),
            169, 25, 0, 9, 21, 0, 12, 0, 1, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(5L, "pthiry"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(6L, "Seth Parks"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(7L, "Salman Malik"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(8L, "zoral"),
            28, 2, 719, 37, 26, 322, 65, 0, 0, 1, 5),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(9L, "Paul Test"),
            0, 0, 0, 0, 0, 0, 65, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(10L, "test"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(11L, "test1"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(12L, "test2 msuen"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(13L, "Ryan Estrada"),
            1899, 509, 2642, 105, 1320, 0, 481, 18, 6, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(14L, "Trent Kolb"),
            1274, 455, 1354, 29, 60, 0, 654, 25, 4, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(15L, "Zane Fralick"),
            336, 79, 0, 45, 398, 0, 174, 9, 1, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(16L, "LDAP"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(18L, "Paul TI LDAP"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(19L, "Alex Jimenez"),
            80, 15, 0, 6, 57, 0, 58, 5, 1, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(20L, "Manny Moreno"),
            17, 60, 0, 713, 0, 0, 72, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(21L, "Gail Gibson"),
            264, 2, 0, 0, 0, 0, 4, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(22L, "Richard Franzwa"),
            0, 2, 0, 0, 0, 0, 641, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(10000L, "Sync Agent"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(10001L, "Coby Reddick"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(10002L, "Mike Giordano"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(10003L, "Kevin Schultz"),
            0, 0, 0, 0, 0, 0, 637, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(10004L, "Kyree Phillips"),
            7066, 353, 0, 0, 101, 0, 1038, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(10005L, "Amy Wilson"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(10006L, "Sophia Cwiklinski"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(10007L, "Mart Robbenhaar"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
          new ChangelogAggregation(
            new com.turbointernational.metadata.web.dto.User(10008L, "Carol Amos"),
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        );
    }
    //@format:on

}
