package com.turbointernational.metadata.dao;

import static com.turbointernational.metadata.entity.Manufacturer.TI_ID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.Manufacturer;
import com.turbointernational.metadata.entity.ManufacturerType_;
import com.turbointernational.metadata.entity.Manufacturer_;
import com.turbointernational.metadata.web.dto.Page;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ManufacturerDao extends AbstractDao<Manufacturer> {

    public ManufacturerDao() {
        super(Manufacturer.class);
    }

    @com.googlecode.ehcache.annotations.Cacheable(cacheName = "manufacturers")
    public List<Manufacturer> findAllManufacturers() {
        return em.createQuery("SELECT o FROM Manufacturer o ORDER BY o.name", Manufacturer.class).getResultList();
    }

    @com.googlecode.ehcache.annotations.Cacheable(cacheName = "manufacturers")
    public Manufacturer findManufacturer(Long id) {
        if (id == null)
            return null;
        return em.find(Manufacturer.class, id);
    }

    @com.googlecode.ehcache.annotations.Cacheable(cacheName = "manufacturers")
    public Manufacturer TI() {
        return findManufacturer(TI_ID);
    }

    public Page<Manufacturer> filter(String fltrName, Long fltrManufacturerTypeId, Boolean notExternal,
            String sortProperty, String sortOrder, Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Manufacturer> mcq = cb.createQuery(Manufacturer.class);
        Root<Manufacturer> root = mcq.from(Manufacturer.class);
        List<Predicate> lstPredicates = new ArrayList<>(2);
        Path<String> colName = root.get(Manufacturer_.name);
        Path<Long> colType = root.get(Manufacturer_.type).get(ManufacturerType_.id);
        Path<Boolean> colNotExternal = root.get(Manufacturer_.notExternal);
        if (isNotBlank(fltrName)) {
            lstPredicates.add(cb.like(cb.lower(colName), "%" + fltrName.toLowerCase() + "%"));
        }
        if (fltrManufacturerTypeId != null) {
            lstPredicates.add(cb.equal(colType, fltrManufacturerTypeId));
        }
        if (notExternal != null) {
            lstPredicates.add(cb.equal(colNotExternal, notExternal));
        }
        Predicate[] arrPredicates = lstPredicates.toArray(new Predicate[lstPredicates.size()]);
        if (arrPredicates.length > 0) {
            mcq.where(arrPredicates);
        }
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
            } else if (sortProperty.equals("typeName")) {
                order = asc ? cb.asc(colType) : cb.desc(colType);
            } else if (sortProperty.equals("notExternal")) {
                order = asc ? cb.asc(colNotExternal) : cb.desc(colNotExternal);
            } else {
                throw new IllegalArgumentException("Invalid sort property: " + sortProperty);
            }
        }
        if (order != null) {
            mcq.orderBy(order);
        }
        TypedQuery<Manufacturer> tq = em.createQuery(mcq);
        if (offset != null) {
            tq.setFirstResult(offset);
        }
        if (limit != null) {
            tq.setMaxResults(limit);
        }
        List<Manufacturer> recs = tq.getResultList();
        // Calculate number of records.
        CriteriaQuery<Long> cmcq = cb.createQuery(Long.class);
        Root<Manufacturer> countRoot = cmcq.from(Manufacturer.class);
        cmcq.select(cb.count(countRoot));
        cmcq.where(arrPredicates);
        TypedQuery<Long> cntQuery = em.createQuery(cmcq);
        long total = cntQuery.getSingleResult();
        return new Page<>(total, recs);
    }

    /**
     * How many parts refer to a manufacturer with specified ID.
     *
     * @param manufacturerId
     * @return
     */
    public Long getRefCountFromParts(Long manufacturerId) {
        return em.createNamedQuery("numPartsOfManufacturer", Long.class).setParameter("manufacturerId", manufacturerId)
                .getSingleResult();
    }

    /**
     * How many turbo types refer to a manufacturer with specified ID.
     *
     * @param manufacturerId
     * @return
     */
    public Long getRefCountFromTurboTypes(Long manufacturerId) {
        return em.createNamedQuery("numTurboTypesOfManufacturer", Long.class)
                .setParameter("manufacturerId", manufacturerId).getSingleResult();
    }

    public Manufacturer findManufacurerByName(String name) {
        try {
            return em.createNamedQuery("findManufacurerByName", Manufacturer.class).setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
