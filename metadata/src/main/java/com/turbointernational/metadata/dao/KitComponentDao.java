package com.turbointernational.metadata.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.Manufacturer_;
import com.turbointernational.metadata.entity.PartType_;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.Part_;
import com.turbointernational.metadata.entity.part.types.Kit;
import com.turbointernational.metadata.entity.part.types.Kit_;
import com.turbointernational.metadata.entity.part.types.kit.KitComponent;
import com.turbointernational.metadata.entity.part.types.kit.KitComponent_;
import com.turbointernational.metadata.web.dto.Page;

/**
 * @author jrodriguez
 */
@Repository
public class KitComponentDao extends AbstractDao<KitComponent> {

    public KitComponentDao() {
        super(KitComponent.class);
    }

    public Page<KitComponent> filter(Long kitId, Long partId, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<KitComponent> ecq = cb.createQuery(KitComponent.class);
        Root<KitComponent> root = ecq.from(KitComponent.class);
        Join<KitComponent, Kit> kitJoin = null;
        Join<KitComponent, Part> partJoin = null;
        ecq.select(root);
        int numPredicates = 0;
        List<Predicate> lstPredicates = new ArrayList<>(2);
        if (kitId != null) {
            kitJoin = root.join(KitComponent_.kit);
            lstPredicates.add(cb.equal(kitJoin.get(Kit_.id), kitId));
            numPredicates++;
        }
        if (partId != null) {
            partJoin = root.join(KitComponent_.part);
            lstPredicates.add(cb.equal(partJoin.get(Part_.id), partId));
            numPredicates++;
        }
        Predicate[] arrPredicates = lstPredicates.toArray(new Predicate[numPredicates]);
        ecq.where(arrPredicates);
        if (sortOrder != null) {
            if (sortProperty == null) {
                throw new NullPointerException("Parameter 'sortOrder' can't be null.");
            }
            Path<?> sortPath;
            if (sortProperty.equals("part.manufacturerPartNumber")) {
                if (partJoin == null) {
                    partJoin = root.join(KitComponent_.part);
                }
                sortPath = partJoin.get(Part_.manufacturerPartNumber);
            } else if (sortProperty.equals("part.partType.name")) {
                if (partJoin == null) {
                    partJoin = root.join(KitComponent_.part);
                }
                sortPath = partJoin.get(Part_.partType).get(PartType_.name);
            } else if (sortProperty.equals("part.manufacturer.name")) {
                if (partJoin == null) {
                    partJoin = root.join(KitComponent_.part);
                }
                sortPath = partJoin.get(Part_.manufacturer).get(Manufacturer_.name);
            } else if (sortProperty.equals("kit.manufacturerPartNumber")) {
                if (kitJoin == null) {
                    kitJoin = root.join(KitComponent_.kit);
                }
                sortPath = kitJoin.get(Kit_.manufacturerPartNumber);
            } else if (sortProperty.equals("kit.partType.name")) {
                if (kitJoin == null) {
                    kitJoin = root.join(KitComponent_.kit);
                }
                sortPath = kitJoin.get(Part_.partType).get(PartType_.name);
            } else if (sortProperty.equals("kit.manufacturer.name")) {
                if (kitJoin == null) {
                    kitJoin = root.join(KitComponent_.kit);
                }
                sortPath = kitJoin.get(Part_.manufacturer).get(Manufacturer_.name);
            } else if (sortProperty.equals("exclude")) {
                sortPath = root.get(KitComponent_.exclude);
            } else {
                throw new AssertionError("Unsupported sort property: " + sortProperty);
            }
            if (sortOrder.equalsIgnoreCase("asc")) {
                ecq.orderBy(cb.asc(sortPath));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                ecq.orderBy(cb.desc(sortPath));
            } else {
                throw new AssertionError("Unknown sort order: " + sortOrder);
            }
        }
        TypedQuery<KitComponent> q = em.createQuery(ecq);
        if (offset != null) {
            q.setFirstResult(offset);
        }
        if (limit != null) {
            q.setMaxResults(limit);
        }
        List<KitComponent> recs = q.getResultList();
        CriteriaQuery<Long> ccq = cb.createQuery(Long.class);
        Root<KitComponent> countRoot = ccq.from(KitComponent.class);
        if (kitId != null) {
            countRoot.join(KitComponent_.kit);
        }
        if (partId != null) {
            countRoot.join(KitComponent_.part);
        }
        ccq.select(cb.count(countRoot));
        ccq.where(arrPredicates);
        long total = em.createQuery(ccq).getSingleResult();
        return new Page<>(total, recs);
    }

}
