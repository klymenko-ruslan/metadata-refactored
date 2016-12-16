package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.PartType;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.entity.part.types.Turbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.turbointernational.metadata.entity.PartType.PTID_TURBO;

/**
 *
 * @author jrodriguez
 */
@Repository
public class PartDao extends AbstractDao<Part> {
    
    private static final Logger log = LoggerFactory.getLogger(PartDao.class);

    public PartDao() {
        super(Part.class);
    }

    /**
     * Get list of parts ordered by 'id'.
     *
     * Ordering is important when parts are processed by batches (as in Magmi CSV export).
     * Unordered (sub)list can contain duplications or skip some rows.
     *
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<Part> findAllOrderedById(int firstResult, int maxResults) {
        return em.createNamedQuery("findAllPartsOrderedById", Part.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    public List<ProductImage> findProductImages(Collection<Long> productIds) {
        return em.createNamedQuery("findProductImagesForPart", ProductImage.class)
                .setParameter("productIds", productIds).getResultList();
    }

    public Part findByPartNumber(String partNumber) throws NoResultException {
        try {
            return em.createNamedQuery("findByPartNumber", Part.class)
                    .setParameter("partNumber", partNumber)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public Part findByPartNumberAndManufacturer(Long manufacturerId, String partNumber) {
        try {
            return em.createNamedQuery("findByPartNumberAndManufacturer", Part.class)
                    .setParameter("manufacturerId", manufacturerId)
                    .setParameter("partNumber", partNumber)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public List<Turbo> listTurbosLinkedToGasketKit(Long id) {
        try {
            return em.createQuery("FROM Turbo AS t WHERE t.partType.id=" + PTID_TURBO +
                    " AND t.gasketKit.id=:gasketKitId").setParameter("gasketKitId", id).getResultList();
        } catch(NoResultException e) {
            return new ArrayList<>();
        }
    }

}
