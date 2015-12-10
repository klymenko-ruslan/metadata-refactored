package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.magmi.MagmiDataFinder;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

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

    @Override
    public Part findOne(long id) {
        return em.createQuery("SELECT DISTINCT p FROM Part p LEFT JOIN p.interchange i WHERE p.id = :id", Part.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Async("bomRebuildExecutor")
    public void rebuildBomDescendancy() {
        try {
            bomRebuildStart = new Date();
            new TransactionTemplate(txManager).execute(new TransactionCallback() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    log.info("Rebuilding BOM descendancy.");
                    em.createNativeQuery("CALL RebuildBomDescendancy()").executeUpdate();
                    em.clear();
                    log.info("BOM descendancy rebuild completed.");
                    return null;
                }
            });
        } finally {
            bomRebuildStart = null;
        }
    }

    public List<ProductImage> findProductImages(Collection<Long> productIds, MagmiDataFinder magmiDataFinder) {
        return em.createQuery("SELECT DISTINCT pi\n"
                + "FROM ProductImage pi\n"
                + "WHERE\n"
                + "  pi.part.id IN ("
                + StringUtils.join(productIds, ',')
                + ")\n"
                + "ORDER BY pi.id", ProductImage.class).getResultList();
    }

    public Part findByPartNumber(String partNumber) throws NoResultException {
        return em.createQuery("FROM Part p WHERE p.manufacturerPartNumber = :partNumber", Part.class)
                .setParameter("partNumber", partNumber)
                .getSingleResult();
    }
}
