package com.turbointernational.metadata.dao;

import static javax.persistence.criteria.JoinType.LEFT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.Group;
import com.turbointernational.metadata.entity.Group_;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.User_;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author jrodriguez
 */
@Repository
public class GroupDao extends AbstractDao<Group> {

    public final static String ALIAS_GROUP = "group";
    public final static String ALIAS_MEMBER = "member";

    public GroupDao() {
        super(Group.class);
    }

    /**
     * @param fltrName
     * @param fltrRole
     * @param sortProperty
     * @param sortOrder
     * @param offset
     * @param limit
     * @return
     */
    public Page<Tuple> filter(Long userId, Optional<String> fltrName, Optional<String> fltrRole,
            Optional<Boolean> fltrIsMemeber, Optional<String> sortProperty, Optional<String> sortOrder,
            Optional<Integer> offset, Optional<Integer> limit) {

        // select /*distinct*/ g.id, g.name, ug.user_id is not null
        // from
        // groups as g left outer join user_group as ug on g.id = ug.group_id
        // and ug.user_id = 4
        // where
        // exists(select 1 from group_role as gr join role as r on
        // gr.role_id=r.id where lower(r.name) like '%rb%')
        // order by 2

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cqg = cb.createTupleQuery();
        Root<Group> grpRoot = cqg.from(Group.class);
        SetJoin<Group, User> usrRoot = grpRoot.join(Group_.users, LEFT);
        usrRoot.on(cb.equal(usrRoot.get(User_.id), userId));
        cqg.select(cb.tuple(grpRoot.alias(ALIAS_GROUP), cb.isNotNull(usrRoot).alias(ALIAS_MEMBER)));
        Path<String> colName = grpRoot.get(Group_.name);
        List<Predicate> lstWherePredicates = new ArrayList<>(3);
        fltrName.ifPresent(s -> cb.equal(cb.lower(colName), "%" + s.toLowerCase() + "%"));
        Predicate[] arrWherePredicates = lstWherePredicates.toArray(new Predicate[lstWherePredicates.size()]);
        cqg.where(arrWherePredicates);
        sortOrder.ifPresent(so -> {
            Optional<Order> order = Optional.empty();
            boolean asc = so.equalsIgnoreCase("asc");
            if (!asc && !so.equalsIgnoreCase("desc")) {
                throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
            }
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            if (sortProperty.equals("name")) {
                order = Optional.of(asc ? cb.asc(colName) : cb.desc(colName));
            } else {
                throw new IllegalArgumentException("Invalid sort property: " + sortProperty);
            }
            order.ifPresent(ord -> cqg.orderBy(ord));
        });
        TypedQuery<Tuple> tq = em.createQuery(cqg);
        offset.ifPresent(off -> tq.setFirstResult(off));
        limit.ifPresent(lmt -> tq.setMaxResults(lmt));
        List<Tuple> recs = tq.getResultList();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(grpRoot));
        cq.where(arrWherePredicates);
        TypedQuery<Long> cntQuery = em.createQuery(cq);
        long total = cntQuery.getSingleResult();
        return new Page<>(total, recs);
    }

}
