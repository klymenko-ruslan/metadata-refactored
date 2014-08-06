
package com.turbointernational.metadata.magmi;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.ProductImage;
import com.turbointernational.metadata.util.dto.MagmiApplication;
import com.turbointernational.metadata.util.dto.MagmiBomItem;
import com.turbointernational.metadata.util.dto.MagmiInterchange;
import com.turbointernational.metadata.util.dto.MagmiProduct;
import com.turbointernational.metadata.util.dto.MagmiServiceKit;
import com.turbointernational.metadata.util.dto.MagmiTurbo;
import com.turbointernational.metadata.util.dto.MagmiUsage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @author jrodriguez
 */
@Service
public class MagmiDataFinder {
    private static final Logger logger = Logger.getLogger(MagmiDataFinder.class.toString());
    
    @Autowired(required=true)
    JdbcTemplate db;
    
    public TreeMap<Long, MagmiProduct> findMagmiProducts(List<Part> parts) {
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
            productMap.get(application.getSku())
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
            productMap.get(magmiTurbo.getSku())
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
            productMap.get(usage.getPrincipalId())
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
            productMap.get(sk.getSku())
                    .addServiceKit(sk);
        }
        
        logger.log(Level.INFO, "Found {0} service kits.", serviceKits.size());
        
        logger.log(Level.INFO, "Got {0} products in {1}ms",
                new Object[] {productMap.size(), System.currentTimeMillis() - startTime});
        
        return productMap;
    }
    
    public List<ProductImage> findProductImages(Collection<Long> productIds) {
        return Part.entityManager().createQuery(
                  "SELECT DISTINCT pi\n"
                + "FROM ProductImage pi\n"
                + "WHERE\n"
                + "  pi.part.id IN (" + StringUtils.join(productIds, ',') + ")\n"
                + "ORDER BY pi.id", ProductImage.class)
            .getResultList();
    }
    
    List<MagmiApplication> findMagmiApplications(Collection<Long> productIds) {
        return db.query(
            "SELECT DISTINCT\n"
          + "  p.id AS sku,\n"
          + "  CONCAT(cmake.name, '!!', COALESCE(cyear.name, 'not specified'), '!!', cmodel.name) AS finder,\n"
          + "  CONCAT(\n"
          + "    cmake.name, '!!',\n"
          + "    cmodel.name, '!!',\n"
          + "    COALESCE(cyear.name, 'not specified'), '!!',\n"
          + "    COALESCE(cengine.engine_size, ''), '!!',\n"
          + "    COALESCE(cfuel.name, '')\n"
          + "  ) AS detail\n"
          + "FROM part p\n"
          + "  LEFT JOIN vpart_turbo vpt\n"
          + "         ON vpt.part_id = p.id\n"

          + "  LEFT JOIN turbo_car_model_engine_year tcmey\n"
          + "         ON tcmey.part_id = vpt.turbo_id\n"

          + "  LEFT JOIN car_model_engine_year c"
          + "         ON c.id = tcmey.car_model_engine_year_id\n"

          + "  LEFT JOIN car_model cmodel\n"
          + "         ON cmodel.id = c.car_model_id\n"

          + "  LEFT JOIN car_engine cengine\n"
          + "         ON cengine.id = c.car_engine_id\n"

          + "  LEFT JOIN car_year cyear\n"
          + "         ON cyear.id = c.car_year_id"

          + "  LEFT JOIN car_make cmake\n"
          + "         ON cmake.id = cmodel.car_make_id\n"

          + "  LEFT JOIN car_fuel_type cfuel\n"
          + "         ON cfuel.id = cengine.car_fuel_type_id\n"

          + "WHERE\n"
          + "  p.id IN (" + StringUtils.join(productIds, ',') + ")\n"
          + "ORDER BY p.id", new BeanPropertyRowMapper(MagmiApplication.class));
    }

    List<MagmiTurbo> findMagmiTurbos(Collection<Long> productIds) {
        return db.query(
              "SELECT DISTINCT"
                + "  p.id AS sku,\n"
                + "  p_tt.name AS part_turbo_type,\n"
                + "  t_tt.name AS turbo_type,\n"
                + "  t_tm.name AS turbo_model,\n"
                + "  CONCAT(tman.name, '!!', t_tt.name, '!!', t_tm.name) AS finder\n"
                  
                  // The part and it's turbo type
                + "FROM part p\n"
                + "  LEFT JOIN part_turbo_type ptt\n"
                + "         ON ptt.part_id = p.id\n"
                  
                + "  LEFT JOIN turbo_type p_tt\n"
                + "         ON p_tt.id = ptt.turbo_type_id\n"
                  
                  // Associated turbos
                + "  LEFT JOIN vpart_turbo vpt\n"
                + "         ON vpt.part_id = p.id\n"
                  
                + "  LEFT JOIN turbo t\n"
                + "         ON t.part_id = vpt.turbo_id\n"
                  // Turbo part
                + "  LEFT JOIN part tp\n"
                + "         ON tp.id = t.part_id\n"
                  
                + "  LEFT JOIN manfr tman\n"
                + "         ON tman.id = tp.manfr_id\n"
                  // Turbo's model
                + "  LEFT JOIN turbo_model t_tm\n"
                + "         ON t_tm.id = t.turbo_model_id\n"
                  
                  // Turbo's turbo type
                + "  LEFT JOIN turbo_type t_tt\n"
                + "         ON t_tt.id = t_tm.turbo_type_id\n"
                  
                + "WHERE\n"
                + "  p.id IN (" + StringUtils.join(productIds, ',') + ")",
            new BeanPropertyRowMapper(MagmiTurbo.class));
    }

    List<MagmiServiceKit> findMagmiServiceKits(Collection<Long> productIds) {
        return db.query(
            "SELECT DISTINCT\n"
            + "  p.id               AS sku,\n"
            + "  k.id               AS kitSku,\n"
            + "  k.manfr_part_num   AS kitPartNumber,\n"
            + "  k.description      AS description,\n"
            + "  kti.id             AS tiKitSku,\n"
            + "  kti.manfr_part_num AS tiKitPartNumber\n"
            + "FROM\n"
            + "  part p\n"
            + "  JOIN vpart_turbotype_kits vpttk ON p.id        = vpttk.part_id\n"
            + "  JOIN part                 k     ON k.id        = vpttk.kit_id\n"
            + "  LEFT JOIN vint_ti         iti   ON iti.part_id = k.id\n"
            + "  LEFT JOIN part            kti   ON kti.id      = iti.ti_part_id\n"
            + "WHERE p.id in (" + StringUtils.join(productIds, ',') + ")\n"
            + "GROUP BY p.id, k.id\n"
            + "ORDER BY p.id, k.id, kti.id", new BeanPropertyRowMapper(MagmiServiceKit.class));
    }
    
    List<MagmiInterchange> findMagmiInterchanges(Collection<Long> productIds) {
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
    
    List<MagmiUsage> findMagmiUsages(Collection<Long> productIds) {
        return db.query(
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
              + "WHERE principal_id IN (" + StringUtils.join(productIds, ',') + ")",
            new BeanPropertyRowMapper(MagmiUsage.class));
    }

    List<MagmiBomItem> findMagmiBom(Collection<Long> productIds) {
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
