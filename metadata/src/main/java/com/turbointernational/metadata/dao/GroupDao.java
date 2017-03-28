package com.turbointernational.metadata.dao;

import static com.turbointernational.metadata.util.JpaUtils.broadLike;
import static javax.persistence.criteria.JoinType.LEFT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.Group;
import com.turbointernational.metadata.entity.Group_;
import com.turbointernational.metadata.entity.Role;
import com.turbointernational.metadata.entity.Role_;
import com.turbointernational.metadata.entity.UserGroup;
import com.turbointernational.metadata.entity.UserGroup_;
import com.turbointernational.metadata.entity.User_;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author jrodriguez
 */
@Repository
public class GroupDao extends AbstractDao<Group> {

    public final static String ALIAS_GROUP_ID = "group_id";
    public final static String ALIAS_MEMBER = "member";

    public GroupDao() {
        super(Group.class);
    }

    /**
     * Filter groups for a user.
     *
     * The method returns a list of tuples. Each tuple consists of two members:
     *  * [0, ALIAS_GROUP_ID] - Group ID
     *  * [1, ALIAS_MEMBER] - a boolean value is this user
     * member of this group or not
     *
     * @param userId
     * @param fltrName
     * @param fltrRole
     * @param fltrIsMemeber
     * @param sortProperty
     * @param sortOrder
     * @param offset
     * @param limit
     * @return
     */
    public Page<Tuple> filter(Long userId, Optional<String> fltrName, Optional<String> fltrRole,
            Optional<Boolean> fltrIsMemeber, Optional<String> sortProperty, Optional<String> sortOrder,
            Optional<Integer> offset, Optional<Integer> limit) {

        // select g.id, g.name, ug.user_id is not null
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
        Expression<Boolean> colMember;
        if (userId == null) {
            colMember = cb.literal(false);
        } else {
            ListJoin<Group, UserGroup> usrGrpRoot = grpRoot.join(Group_.userGroups, LEFT);
            usrGrpRoot.on(cb.equal(usrGrpRoot.get(UserGroup_.user).get(User_.id), userId));
            colMember = cb.isNotNull(usrGrpRoot);
        }
        CompoundSelection<Tuple> tuple = cb.tuple(grpRoot.get(Group_.id).alias(ALIAS_GROUP_ID),
                colMember.alias(ALIAS_MEMBER));
        cqg.select(tuple);
        Path<String> colName = grpRoot.get(Group_.name);
        List<Predicate> lstWherePredicates = new ArrayList<>(3);
        fltrName.ifPresent(s -> lstWherePredicates.add(broadLike(cb, colName, s)));
        fltrRole.ifPresent(r -> {
            Subquery<Long> subqueryRoles = cqg.subquery(Long.class);
            subqueryRoles.select(cb.literal(1L));
            SetJoin<Group, Role> joinGrpRole = subqueryRoles.from(Group.class).join(Group_.roles);
            subqueryRoles.where(broadLike(cb, joinGrpRole.get(Role_.name), r));
            lstWherePredicates.add(cb.exists(subqueryRoles));
        });
        fltrIsMemeber.ifPresent(m -> {
            Predicate pmem;
            if (m) {
                pmem = cb.isTrue(colMember);
            } else {
                pmem = cb.isFalse(colMember);
            }
            lstWherePredicates.add(pmem);
        });
        Predicate[] arrWherePredicates = lstWherePredicates.toArray(new Predicate[lstWherePredicates.size()]);
        if (arrWherePredicates.length > 0) {
            cqg.where(arrWherePredicates);
        }
        sortOrder.ifPresent(so -> {
            Optional<Order> order = Optional.empty();
            boolean asc = so.equalsIgnoreCase("asc");
            if (!asc && !so.equalsIgnoreCase("desc")) {
                throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
            }
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            String sp = sortProperty.get();
            if (sp.equals("name")) {
                order = Optional.of(asc ? cb.asc(colName) : cb.desc(colName));
            } else if (sp.equals("isMember")) {
                // I don't find a better way how to order a query result by calculated column.
                Expression<Integer> refOnColMember = cb.literal(2);
                order = Optional.of(asc ? cb.asc(refOnColMember) : cb.desc(refOnColMember));
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
        grpRoot = cq.from(Group.class);
        if (userId != null) {
            ListJoin<Group, UserGroup> usrGrpRoot2 = grpRoot.join(Group_.userGroups, LEFT);
            usrGrpRoot2.on(cb.equal(usrGrpRoot2.get(UserGroup_.user).get(User_.id), userId));
        }
        cq.select(cb.count(grpRoot));
        if (arrWherePredicates.length > 0) {
            cq.where(arrWherePredicates);
        }
        TypedQuery<Long> cntQuery = em.createQuery(cq);
        long total = cntQuery.getSingleResult();
        return new Page<>(total, recs);
    }

}
