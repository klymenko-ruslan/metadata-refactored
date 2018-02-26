package com.turbointernational.metadata.dao;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.APPLICATIONS;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.BOM;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.CRITICALDIM;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.IMAGE;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.INTERCHANGE;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.KIT;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.MAS90SYNC;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.PART;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.SALESNOTES;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.TURBOMODEL;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.TURBOTYPE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.Changelog.ServiceEnum;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.Changelog_;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.User_;
import com.turbointernational.metadata.service.DtoMapperService;
import com.turbointernational.metadata.web.dto.ChangelogAggregation;
import com.turbointernational.metadata.web.dto.Page;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ChangelogDao extends AbstractDao<Changelog> {

    @Autowired
    private DtoMapperService dtoMapperService;

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

    public Page<Changelog> filter(List<ServiceEnum> services, List<Long> userIds, Date startDate, Date endDate,
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
        if (endDate != null) {
            lstPredicates.add(cb.lessThanOrEqualTo(root.get(Changelog_.changeDate), endDate));
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

    // @format:off
    public List<ChangelogAggregation> filterAggragation(List<Long> userIds, Date startDate, Date endDate,
            String description, String data) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cqUser = cb.createQuery(Object[].class);
        Root<User> cqRoot = cqUser.from(User.class);
        Subquery<Long> scqBom = getServiceCounter(cb, cqUser, cqRoot, BOM, startDate, endDate, description, data);
        Subquery<Long> scqInterchange = getServiceCounter(cb, cqUser, cqRoot, INTERCHANGE, startDate, endDate,
                description, data);
        Subquery<Long> scqMas90Sync = getServiceCounter(cb, cqUser, cqRoot, MAS90SYNC, startDate, endDate, description,
                data);
        Subquery<Long> scqSalesNotes = getServiceCounter(cb, cqUser, cqRoot, SALESNOTES, startDate, endDate,
                description, data);
        Subquery<Long> scqApplications = getServiceCounter(cb, cqUser, cqRoot, APPLICATIONS, startDate, endDate,
                description, data);
        Subquery<Long> scqKit = getServiceCounter(cb, cqUser, cqRoot, KIT, startDate, endDate, description, data);
        Subquery<Long> scqPart = getServiceCounter(cb, cqUser, cqRoot, PART, startDate, endDate, description, data);
        Subquery<Long> scqTurboModel = getServiceCounter(cb, cqUser, cqRoot, TURBOMODEL, startDate, endDate,
                description, data);
        Subquery<Long> scqTurboType = getServiceCounter(cb, cqUser, cqRoot, TURBOTYPE, startDate, endDate, description,
                data);
        Subquery<Long> scqCriticalDim = getServiceCounter(cb, cqUser, cqRoot, CRITICALDIM, startDate, endDate,
                description, data);
        Subquery<Long> scqImage = getServiceCounter(cb, cqUser, cqRoot, IMAGE, startDate, endDate, description, data);
        cqUser.select(cb.array(cqRoot, scqBom.getSelection(), scqInterchange.getSelection(),
                scqMas90Sync.getSelection(), scqSalesNotes.getSelection(), scqApplications.getSelection(),
                scqKit.getSelection(), scqPart.getSelection(), scqTurboModel.getSelection(),
                scqTurboType.getSelection(), scqCriticalDim.getSelection(), scqImage.getSelection()));
        if (userIds != null && !userIds.isEmpty()) {
            cqUser.where(cqRoot.get(User_.id).in(userIds));
        }
        TypedQuery<Object[]> tq = em.createQuery(cqUser);
        List<Object[]> result = tq.getResultList();
        return result.stream().map(rec -> {
            User userEntity = (User) rec[0];
            com.turbointernational.metadata.web.dto.User userDto = dtoMapperService.map(userEntity,
                    com.turbointernational.metadata.web.dto.User.class);
            Long bomCnt = (Long) rec[1];
            Long interchangeCnt = (Long) rec[2];
            Long mas90syncCnt = (Long) rec[3];
            Long salesNoteCnt = (Long) rec[4];
            Long applicationCnt = (Long) rec[5];
            Long kitCnt = (Long) rec[6];
            Long partCnt = (Long) rec[7];
            Long turboModelCnt = (Long) rec[8];
            Long turboTypeCnt = (Long) rec[9];
            Long criticalDimCnt = (Long) rec[10];
            Long imageCnt = (Long) rec[11];
            ChangelogAggregation chla = new ChangelogAggregation(userDto, bomCnt, interchangeCnt, mas90syncCnt,
                    salesNoteCnt, applicationCnt, kitCnt, partCnt, turboModelCnt, turboTypeCnt, criticalDimCnt,
                    imageCnt);
            return chla;
        }).collect(Collectors.toList());
    }
    // @format:on

    private Subquery<Long> getServiceCounter(CriteriaBuilder cb, CriteriaQuery<Object[]> cqUser, Root<User> cqRoot,
            ServiceEnum service, Date startDate, Date endDate, String description, String data) {
        Subquery<Long> scqCount = cqUser.subquery(Long.class);
        Root<Changelog> scqCountRoot = scqCount.from(Changelog.class);
        scqCount.select(cb.count(scqCountRoot));
        int numPredicates = 0;
        List<Predicate> lstPredicates = new ArrayList<>(7);
        if (startDate != null) {
            lstPredicates.add(cb.greaterThanOrEqualTo(scqCountRoot.get(Changelog_.changeDate), startDate));
            numPredicates++;
        }
        if (endDate != null) {
            lstPredicates.add(cb.lessThanOrEqualTo(scqCountRoot.get(Changelog_.changeDate), endDate));
            numPredicates++;
        }
        if (description != null) {
            lstPredicates.add(cb.like(scqCountRoot.get(Changelog_.description), "%" + description + "%"));
            numPredicates++;
        }
        if (data != null) {
            lstPredicates.add(cb.like(scqCountRoot.get(Changelog_.data), "%" + data + "%"));
            numPredicates++;
        }
        lstPredicates.add(cb.equal(scqCountRoot.get(Changelog_.user).get(User_.id), cqRoot.get(User_.id)));
        lstPredicates.add(cb.equal(scqCountRoot.get(Changelog_.service), service));
        Predicate[] arrPredicates = lstPredicates.toArray(new Predicate[numPredicates]);
        scqCount.where(arrPredicates);
        return scqCount;
    }

}
