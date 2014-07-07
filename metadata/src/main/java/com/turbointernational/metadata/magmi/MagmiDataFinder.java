
package com.turbointernational.metadata.magmi;

import com.google.common.collect.Lists;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.ProductImage;
import com.turbointernational.metadata.util.dto.MagmiApplication;
import com.turbointernational.metadata.util.dto.MagmiBomItem;
import com.turbointernational.metadata.util.dto.MagmiInterchange;
import com.turbointernational.metadata.util.dto.MagmiProduct;
import com.turbointernational.metadata.util.dto.MagmiServiceKit;
import com.turbointernational.metadata.util.dto.MagmiTurbo;
import com.turbointernational.metadata.util.dto.MagmiUsage;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author jrodriguez
 */
@Service
public class MagmiDataFinder {
    private static final Logger logger = Logger.getLogger(MagmiDataFinder.class.toString());
    
    public static TreeMap<Long, MagmiProduct> findMagmiProducts(List<Part> parts) {
        long startTime = System.currentTimeMillis();
        
        // Build a product map from the parts
        TreeMap<Long, MagmiProduct> productMap = new TreeMap<Long, MagmiProduct>();
        for (Part part : parts) {
            productMap.put(part.getId(), new MagmiProduct(part));
        }
        
        List<Long> productIds = new ArrayList<Long>(productMap.keySet());
        
        // Add the applications
        List<MagmiApplication> applications = findMagmiApplications(productIds);
        
        for (MagmiApplication application : applications) {
            productMap.get(application.sku)
                      .addApplication(application);
        }
        
        logger.log(Level.INFO, "Found {0} applications", applications.size());
        
        // Add the images
        List<ProductImage> images = findProductImages(productIds);
        
        for (ProductImage image : images) {
            productMap.get(image.getPart().getId())
                      .addImageId(image.getId());
        }
        
        logger.log(Level.INFO, "Found {0} images.", images.size());
        
        // Add the turbos
        List<MagmiTurbo> turbos = findMagmiTurbos(productIds);
        
        for (MagmiTurbo magmiTurbo : turbos) {
            productMap.get(magmiTurbo.sku)
                    .addTurbo(magmiTurbo);
        }
        
        logger.log(Level.INFO, "Found {0} turbos.", turbos.size());
        
        // Add the interchanges
        List<MagmiInterchange> interchanges = findMagmiInterchanges(productIds);
        
        for (MagmiInterchange interchange : interchanges) {
            productMap.get(interchange.getSku())
                    .addInterchange(interchange);
        }
        
        // Add usages
        List<MagmiUsage> usages = findMagmiUsages(productIds);

        for (MagmiUsage usage : usages) {
            productMap.get(usage.principalId)
                    .addUsage(usage);
        }

        logger.log(Level.INFO, "Found {0} usages.", usages.size());
        
        // Add the bom items
        List<MagmiBomItem> bom = findMagmiBom(productIds);
        
        for (MagmiBomItem bomItem : bom) {
            productMap.get(bomItem.getParentSku())
                    .addBomItem(bomItem);
        }
        
        logger.log(Level.INFO, "Found {0} BOM items.", bom.size());
        
        // Add the service kits
        List<MagmiServiceKit> serviceKits = findMagmiServiceKits(productIds);
        
        for (MagmiServiceKit sk : serviceKits) {
            productMap.get(sk.sku)
                    .addServiceKit(sk);
        }
        
        logger.log(Level.INFO, "Found {0} service kits.", serviceKits.size());
        
        logger.log(Level.INFO, "Got {0} products in {1}ms",
                new Object[] {productMap.size(), System.currentTimeMillis() - startTime});
        
        return productMap;
    }
    
    public static List<ProductImage> findProductImages(Collection<Long> productIds) {
        return Part.entityManager().createQuery(
                  "SELECT DISTINCT pi\n"
                + "FROM ProductImage pi\n"
                + "WHERE\n"
                + "  pi.part.id IN (" + StringUtils.join(productIds, ',') + ")\n"
                + "ORDER BY pi.id", ProductImage.class)
            .getResultList();
    }
    
    static List<MagmiApplication> findMagmiApplications(Collection<Long> productIds) {
        return Part.entityManager().createQuery(
              "SELECT DISTINCT new com.turbointernational.metadata.util.dto.MagmiApplication("
                + "  p.id AS sku,\n"
                + "  CONCAT(cmake.name, '!!', COALESCE(cyear.name, 'not specified'), '!!', cmodel.name) AS finder,\n"
                + "  CONCAT("
                + "   cmake.name, '!!',"
                + "   cmodel.name, '!!',"
                + "   COALESCE(cyear.name, 'not specified'), '!!',"
                + "   COALESCE(cengine.engineSize, ''), '!!',"
                + "   COALESCE(cfuel.name, '')"
                + "  ) AS detail\n"
                + ")\n"
                + "FROM Part p\n"
                + "  LEFT JOIN p.turbos t\n"
                + "  LEFT JOIN t.cars c\n"
                + "  LEFT JOIN c.model cmodel\n"
                + "  LEFT JOIN c.engine cengine\n"
                + "  LEFT JOIN c.year cyear\n"
                + "  LEFT JOIN cmodel.make cmake\n"
                + "  LEFT JOIN cengine.fuelType cfuel\n"
                + "WHERE\n"
                + "  p.id IN (" + StringUtils.join(productIds, ',') + ")\n"
                + "ORDER BY p.id", MagmiApplication.class)
            .getResultList();
    }
    
    static List<MagmiTurbo> findMagmiTurbos(Collection<Long> productIds) {
        return Part.entityManager().createQuery(
              "SELECT DISTINCT new com.turbointernational.metadata.util.dto.MagmiTurbo("
                + "  p.id AS sku,\n"
                + "  ptt.name AS part_turbo_type,\n"
                + "  tt.name AS turbo_type,\n"
                + "  tm.name AS turbo_model,\n"
                + "  CONCAT(tman.name, '!!', tt.name, '!!', tm.name) AS finder\n"
                + ")\n"
                + "FROM Part p\n"
                + "  LEFT JOIN p.turbos t\n"
                + "  LEFT JOIN t.manufacturer tman\n"
                + "  LEFT JOIN t.turboModel tm\n"
                + "  LEFT JOIN tm.turboType tt\n"
                + "  LEFT JOIN p.turboTypes ptt\n"
                + "WHERE\n"
                + "  p.id IN (" + StringUtils.join(productIds, ',') + ")", MagmiTurbo.class)
            .getResultList();
    }
    
    static List<MagmiServiceKit> findMagmiServiceKits(Collection<Long> productIds) {
        List<Object[]> results = Part.entityManager().createNativeQuery(
            "SELECT DISTINCT\n"
            + "  p.id               AS sku,\n"
            + "  k.id               AS kitSku,\n"
            + "  k.manfr_part_num   AS partNumber,\n"
            + "  k.description      AS description,\n"
            + "  kti.id             AS tiPartSku,\n"
            + "  kti.manfr_part_num AS tiPartNumber\n"
            + "FROM\n"
            + "  part p\n"
            + "  JOIN vpart_turbotype_kits vpttk ON p.id        = vpttk.part_id\n"
            + "  JOIN part                 k     ON k.id        = vpttk.kit_id\n"
            + "  LEFT JOIN vint_ti         iti   ON iti.part_id = k.id\n"
            + "  LEFT JOIN part            kti   ON kti.id      = iti.ti_part_id\n"
            + "WHERE p.id in (" + StringUtils.join(productIds, ',') + ")\n"
            + "GROUP BY p.id, k.id\n"
            + "ORDER BY p.id, k.id, kti.id")
            .getResultList();

        // Create the objects
        List<MagmiServiceKit> serviceKits = Lists.newLinkedList();
        
        for (Object[] row : results) {
            MagmiServiceKit serviceKit = new MagmiServiceKit(
                    ((BigInteger) row[0]).longValue(),
                    ((BigInteger) row[1]).longValue(),
                    (String) row[2],
                    (String) row[3],
                    row[4] == null ? null : ((BigInteger) row[4]).longValue(),
                    row[5] == null ? null : (String) row[5]
                );
            
            serviceKits.add(serviceKit);
        }
        
        return serviceKits;
    }
    
    static List<MagmiInterchange> findMagmiInterchanges(Collection<Long> productIds) {
        return Part.entityManager().createQuery(
                "SELECT DISTINCT NEW"
              + "  com.turbointernational.metadata.util.dto.MagmiInterchange("
              + "    p.id AS sku,"
              + "    ip.id AS interchangePartSku,"
              + "    ip.manufacturerPartNumber AS interchangePartNumber,"
              + "    ipm.id AS interchangePartManufacturerId\n"
              + ")\n"
              + "FROM Part p\n"
              + "  JOIN p.interchange i\n"
              + "  JOIN i.parts ip\n"
              + "  JOIN ip.manufacturer ipm\n"
              + "WHERE\n"
              + "  ip.id != p.id\n"
              + "  AND p.id IN (" + StringUtils.join(productIds, ',') + ")", MagmiInterchange.class)
                .getResultList();
    }
    
    static List<MagmiUsage> findMagmiUsages(Collection<Long> productIds) {
        List<Object[]> results = Part.entityManager()
            .createNativeQuery(
                "SELECT DISTINCT\n"
              + "  principal_id,\n"
              + "  sku,\n"
              + "  manufacturer,\n"
              + "  part_number,\n"
              + "  ti_sku,\n"
              + "  ti_part_number,\n"
              + "  part_type,\n"
              + "  turbo_type,\n"
              + "  turbo_part_number\n"
              + "FROM vwhere_used\n"
              + "WHERE principal_id IN (" + StringUtils.join(productIds, ',') + ")")
            .getResultList();
        
            

        // Create the objects
        List<MagmiUsage> usages = Lists.newLinkedList();
        
        for (Object[] row : results) {
            MagmiUsage usage = new MagmiUsage(
                    ((BigInteger) row[0]).longValue(),
                    ((BigInteger) row[1]).longValue(),
                    (String) row[2],
                    (String) row[3],
                    row[4] == null ? null : ((BigInteger) row[4]).longValue(),
                    (String) row[5],
                    (String) row[6],
                    (String) row[7],
                    (String) row[8]
                );
            
            usages.add(usage);
        }
        
        return usages;
    }

    static List<MagmiBomItem> findMagmiBom(Collection<Long> productIds) {
        return Part.entityManager().createQuery(
                "SELECT DISTINCT NEW com.turbointernational.metadata.util.dto.MagmiBomItem(\n"
              + "  b.parent.id as parent_sku,\n"
              + "  b.child.id as child_sku,\n"
              + "  b.quantity,\n"
                        
                // Alternates
              + "  alt.id AS alt_sku,\n"
              + "  alt.manufacturer.id AS alt_mfr_id,\n"
                        
                // Interchanges
              + "  int.id AS int_sku,\n"
              + "  int.manufacturer.id AS int_mfr_id\n"
              + ")\n"
              + "FROM BOMItem b\n" 
             + "  JOIN b.child bc\n"
              + "  LEFT JOIN bc.interchange bci\n"
              + "  LEFT JOIN bci.parts int\n"
              + "  LEFT JOIN b.alternatives balt\n"
              + "  LEFT JOIN balt.part alt\n"
              + "WHERE b.parent.id IN (" + StringUtils.join(productIds, ',') + ")", MagmiBomItem.class)
                .getResultList();
    }
    
    
}
