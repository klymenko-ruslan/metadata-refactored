
package com.turbointernational.metadata.magmi;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.ProductImage;
import com.turbointernational.metadata.magmi.dto.MagmiApplication;
import com.turbointernational.metadata.magmi.dto.MagmiBomItem;
import com.turbointernational.metadata.magmi.dto.MagmiInterchange;
import com.turbointernational.metadata.magmi.dto.MagmiProduct;
import com.turbointernational.metadata.magmi.dto.MagmiServiceKit;
import com.turbointernational.metadata.magmi.dto.MagmiTurbo;
import com.turbointernational.metadata.magmi.dto.MagmiUsage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
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
        final TreeMap<Long, MagmiProduct> productMap = new TreeMap<Long, MagmiProduct>();
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
        
        // Add TI CHRAs
        logger.log(Level.INFO, "Finding TI CHRA descendants.", applications.size());
        db.query(
            "SELECT DISTINCT\n" +
            "    p.id,\n" +
            "    CASE\n" +
            "      WHEN pt.manfr_part_num IS NOT NULL THEN 1\n" +
            "      ELSE 0\n" +
            "    END has_ti_chra\n" +
            "FROM\n" +
            "    part p\n" +
            "    LEFT JOIN (vbom_descendant bd \n" +
            "               INNER JOIN part pt ON bd.part_id_descendant = pt.id\n" +
            "                                  AND pt.manfr_id = 11\n" +
            "                                  AND pt.part_type_id = 2)\n" +
            "              ON p.id = bd.part_id_ancestor\n" +
            "WHERE p.id IN (?)", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                boolean hasTiChra = rs.getBoolean("has_ti_chra");
                long partId = rs.getLong("id");
                
                MagmiProduct product = productMap.get(partId);
                product.setHasTiChra(hasTiChra);
            }
        });
        
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
        ListMultimap<Long, MagmiBomItem> bom = findMagmiBom(productIds);
        
        for (Long ancestorPartId : bom.keySet()) {
            productMap.get(ancestorPartId)
                      .getBom()
                      .addAll(bom.get(ancestorPartId));
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
            + "  sku,\n"
            + "  kitSku,\n"
            + "  kitPartNumber,\n"
            + "  description,\n"
            + "  tiKitSku,\n"
            + "  tiKitPartNumber\n"
            + "FROM\n"
            + "  vmagmi_service_kits\n"
            + "WHERE\n"
            + "  sku in ("
            +     StringUtils.join(productIds, ',')
            + ")\n"
            + "GROUP BY sku, kitSku\n"
            + "ORDER BY sku, kitSku, tiKitSku",
            new BeanPropertyRowMapper(MagmiServiceKit.class));
    }
    
    List<MagmiInterchange> findMagmiInterchanges(Collection<Long> productIds) {
        return Part.entityManager().createQuery(
                "SELECT DISTINCT NEW"
              + "  com.turbointernational.metadata.magmi.dto.MagmiInterchange("
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

    ListMultimap<Long, MagmiBomItem> findMagmiBom(Collection<Long> productIds) {
        return db.query(
                "SELECT\n"
              + "  ancestor_sku,\n"
              + "  descendant_sku,\n"
              + "  quantity,\n"
              + "  distance,\n"
              + "  part_type_parent,\n"
              + "  has_bom,\n"
              + "  alt_sku,\n"
              + "  alt_mfr_id,\n"
              + "  int_sku\n"
              + "FROM vmagmi_bom\n"
              + "WHERE\n"
              + "  ancestor_sku IN ("
              +   StringUtils.join(productIds, ',')
              + ")",
            new ResultSetExtractor<ListMultimap<Long, MagmiBomItem>>() {

            @Override
            public ListMultimap<Long, MagmiBomItem> extractData(ResultSet rs) throws SQLException, DataAccessException {
                
                // Ancestor rows, descendant columns
                Table<Long, Long, MagmiBomItem> bomTable = TreeBasedTable.create();
                
                while (rs.next()) {
                    
                    // Get the values we need from the result set
                    long ancestorSku      = rs.getLong("ancestor_sku");
                    long descendantSku    = rs.getLong("descendant_sku");
                    int quantity          = rs.getInt("quantity");
                    int distance          = rs.getInt("distance");
                    boolean hasBom        = rs.getBoolean("has_bom");
                    String partTypeParent = rs.getString("part_type_parent");
                    
                    // Get the BOM item so we can roll up any alts and interchanges
                    MagmiBomItem bomItem = bomTable.get(ancestorSku, descendantSku);
                    if (bomItem == null) {
                        bomItem = new MagmiBomItem(descendantSku, quantity, distance, hasBom, partTypeParent);
                        bomTable.put(ancestorSku, descendantSku, bomItem);
                    }
                    
                    // BOM alternate rollup
                    long altSku = rs.getLong("alt_sku");
                    if (altSku > 0) {
                      bomItem.getAltSku().add(altSku);
                      
                      // Add it to TI Part skus if the manufacturer matches
                      if (rs.getLong("alt_mfr_id") == Manufacturer.TI_ID) {
                          bomItem.getTiPartSku().add(altSku);
                      }
                    }
                    
                    // TI Part SKU Rollup
                    long tiSku = rs.getLong("int_sku");
                    if (tiSku > 0) {
                        bomItem.getTiPartSku().add(tiSku);
                    }
                }
                
                ListMultimap<Long, MagmiBomItem> result = ArrayListMultimap.create();
                
                for (Long productId : bomTable.rowKeySet()) {
                    result.putAll(productId, bomTable.row(productId).values());
                }
                
                return result;
            }
        });
    }
    
}
