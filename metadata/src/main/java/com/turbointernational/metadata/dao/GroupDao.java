package com.turbointernational.metadata.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.Group;
import com.turbointernational.metadata.entity.Group_;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author jrodriguez
 */
@Repository
public class GroupDao extends AbstractDao<Group> {

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
    public Page<Group> filter(String fltrName, String fltrRole, String sortProperty, String sortOrder, Integer offset,
            Integer limit) {

        // select /*distinct*/ g.id, g.name, ug.user_id is not null
        // from
        //   groups as g left outer join user_group as ug on g.id = ug.group_id and ug.user_id = 4
        // where
        //   exists(select 1 from group_role as gr join role as r on gr.role_id=r.id where lower(r.name) like '%rb%')
        // order by 2

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Group> cqg = cb.createQuery(Group.class);
        Root<Group> grpRoot = cqg.from(Group.class);
        Path<String> colName = grpRoot.get(Group_.name);
        List<Predicate> lstWherePredicates = new ArrayList<>(2);
        if (fltrName != null) {
            cb.equal(cb.lower(colName), "%" + fltrName.toLowerCase() + "%");
        }
        Predicate[] arrWherePredicates = lstWherePredicates.toArray(new Predicate[lstWherePredicates.size()]);
        cqg.where(arrWherePredicates);
        Order order = null;
        if (sortOrder != null) {
            boolean asc = sortOrder.equalsIgnoreCase("asc");
            if (!asc && !sortOrder.equalsIgnoreCase("desc")) {
                throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
            }
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            if (sortProperty.equals("name")) {
                order = asc ? cb.asc(colName) : cb.desc(colName);
            } else {
                throw new IllegalArgumentException("Invalid sort property: " + sortProperty);
            }
        }
        if (order != null) {
            cqg.orderBy(order);
        }
        TypedQuery<Group> tq = em.createQuery(cqg);
        if (offset != null) {
            tq.setFirstResult(offset);
        }
        if (limit != null) {
            tq.setMaxResults(limit);
        }
        List<Group> recs = tq.getResultList();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(grpRoot));
        cq.where(arrWherePredicates);
        TypedQuery<Long> cntQuery = em.createQuery(cq);
        long total = cntQuery.getSingleResult();
        return new Page<>(total, recs);

    }

}
