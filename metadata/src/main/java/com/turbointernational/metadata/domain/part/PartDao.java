package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.AbstractDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

/**
 *
 * @author jrodriguez
 */
@Repository
public class PartDao extends AbstractDao<Part> {
    
    private static final Logger log = LoggerFactory.getLogger(PartDao.class);
    
    /**
     * Contains the date when we started the BOM rebuild, or null if not currently rebuilding.
     */
    public static volatile Date bomRebuildStart = null;

    public static final Date getBomRebuildStart() {
        return bomRebuildStart;
    }

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

    @Async("bomRebuildExecutor")
    @Transactional(propagation = REQUIRES_NEW)
    public void rebuildBomDescendancy() {
        try {
            bomRebuildStart = new Date();
            log.info("Rebuilding BOM descendancy.");
            em.createNativeQuery("CALL RebuildBomDescendancy()").executeUpdate();
            em.clear();
            log.info("BOM descendancy rebuild completed.");
        } finally {
            bomRebuildStart = null;
        }
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

}
