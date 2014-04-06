package com.turbointernational.metadata.web;

import au.com.bytecode.opencsv.CSVWriter;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.util.ImageResizer;
import com.turbointernational.metadata.util.dto.MagmiBasicProduct;
import com.turbointernational.metadata.util.dto.MagmiBomItem;
import com.turbointernational.metadata.util.dto.MagmiInterchange;
import com.turbointernational.metadata.util.dto.MagmiProduct;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import mas90magmi.ItemPricing;
import mas90magmi.CalculatedPrice;
import mas90magmi.Mas90Prices;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping(value={"/magmi", "/metadata/magmi"})
public class MagmiController {
    private static final Logger logger = Logger.getLogger(MagmiController.class.toString());
    
    public static final String[] HEADERS = {
        
        //<editor-fold defaultstate="collapsed" desc="Basic">
        "sku",
        "part_type",
        "attribute_set",
        "type",
        "visibility",
        "status",
        "name",
        "description",
        "manufacturer",
        "part_number",
        "part_number_short",
        "categories",
        "ti_part_number",    // First interchangeable TI part number
        "ti_part_sku",       // Interchangeable parts by TI
        "interchanges",      // Interchangeable parts
        "bill_of_materials", // BOM
        "price",
        "quantity",
        "turbo_model",      // Turbo Models
        "turbo_type",       // Turbo Types
        
        // Product images
        "image",
        "small_image",
        "thumbnail",
        "media_gallery",
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Types">
        "kit_type",
        "gasket_type",
        "seal_type",
        "cool_type",
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Part Type Specifics">
        "bearing_type",
        "bore_oe",
        "compressor_housing_diameter",
        "compressor_wheel_diameter",
        "design_features",
        "exduce_oa",
        "exducer_oc",
        "hub_length_d",
        "i_gap_max",
        "i_gap_min",
        "inducer_diameter",
        "inducer_oa",
        "inducer_oc",
        "inside_diameter",
        "inside_diameter_max",
        "inside_diameter_min",
        "journal_od",
        "number_of_blades",
        "oil",
        "oil_inlet",
        "oil_outlet",
        "outlet_flange_holes",
        "outside_diameter_max",
        "outside_diameter_min",
        "outside_dim_max",
        "outside_dim_min",
        "overall_diameter",
        "overall_height",
        "oversize_id",
        "piston_ring_diameter",
        "secondary_diameter",
        "shaft_thread_f",
        "standard_size_id",
        "stem_oe",
        "style_compressor_wheel",
        "tip_height_b",
        "water_ports",
        "width_max",
        "width_min",
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="MAS90 Prices">
        "customerprice",
        
        "group_price:ERP_PL_0",
        "group_price:ERP_PL_1",
        "group_price:ERP_PL_2",
        "group_price:ERP_PL_3",
        "group_price:ERP_PL_4",
        "group_price:ERP_PL_5",
        "group_price:ERP_PL_E",
        "group_price:ERP_PL_R",
        "group_price:ERP_PL_W",
        
        "tier_price:ERP_PL_0",
        "tier_price:ERP_PL_1",
        "tier_price:ERP_PL_2",
        "tier_price:ERP_PL_3",
        "tier_price:ERP_PL_4",
        "tier_price:ERP_PL_5",
        "tier_price:ERP_PL_E",
        "tier_price:ERP_PL_R",
        "tier_price:ERP_PL_W",
        //</editor-fold>
        
        // Make,Year,Model
        "finder:" + MagmiProduct.FINDER_ID_APPLICATION,
        
        // Manufacturer,TurboType,TurboModel
        "finder:" + MagmiProduct.FINDER_ID_TURBO,
        
        // Make!!Model!!Year!!Displacement!!Fuel||...
        "application_detail"
    };

    @Value("${mas90.db.path}")
    String mas90DbPath;

    @Value("${magmi.batch.size}")
    int magmiBatchSize = 1000;
    
    @Autowired(required=true)
    ImageResizer imageResizer;
    
    @RequestMapping("/products")
    @ResponseBody   
    @Transactional
    @Secured("ROLE_MAGMI")
    public void products(HttpServletResponse response, OutputStream out) throws Exception {
        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Content-Disposition: attachment; filename=products.csv", null);
        
        long startTime = System.currentTimeMillis();
        
        Mas90Prices mas90 = new Mas90Prices(new File(mas90DbPath));
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(out), ',', '"');
        
        // Write the header row
        writer.writeNext(HEADERS);
        
        // Get the product IDs to retrieve
        int position = 0;
        int pageSize = magmiBatchSize;
        Long lastSuccessfulSku = null;
        List<Part> parts = null;
        do {
            try {
            
                // Clear Hibernate
                Part.entityManager().clear();

                // Get the next batch of part IDs
                parts = Part.findPartEntries(position, pageSize);

                // Process each product
                for (MagmiProduct product : findMagmiProducts(parts).values()) {
                    try {
                        writer.writeNext(magmiProductToCsvRow(mas90, product));
                        
                        // Debugging variable
                        lastSuccessfulSku = product.getSku();
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Could not write magmi part " + product.getSku(), e);
                    }
                }

                // Update the position
                position += parts.size();
            } catch (Exception e) {
                logger.log(Level.SEVERE,
                    "Batch failed! position: " + position
                            + ", page size: " + pageSize
                            + ", lastSuccessfulSku: " + lastSuccessfulSku,
                    e);
            }
        } while (parts.size() >= pageSize);
        
        
        writer.flush();
        writer.close();
        
        logger.log(Level.INFO, "Exported {0} products in {1}ms",
                new Object[] {position, System.currentTimeMillis() - startTime});
    }
    
    private String[] magmiProductToCsvRow(Mas90Prices mas90, MagmiProduct product) {
        Map<String, String> columns = product.getCsvColumns(imageResizer);
        
        // Only TI parts get this info
        if (product.getManufacturerId() == Manufacturer.TI_ID) {

            // Default to quantity 1
            columns.put("quantity", "1");
            
            // ERP Prices
            addErpPrices(mas90, columns, product);
        }

        // Map the column into a value array for the CSV writer
        String[] valueArray = new String[HEADERS.length];
        for (int i = 0; i < HEADERS.length; i++) {
            
            String header = HEADERS[i];
            
            valueArray[i] = StringUtils.defaultIfEmpty(columns.get(header), "");
        }
        
        return valueArray;
    }
    
    private void addErpPrices(Mas90Prices mas90, Map<String, String> columns, MagmiProduct product) {
        try {
            
            // Stop now if there's no part number
            if (StringUtils.isBlank(product.getPartNumber())) {
                return;
            }
            
            // Get the pricing info
            ItemPricing itemPricing = mas90.getItemPricing(product.getPartNumber());

            // Stop if there's no standard price
            if (itemPricing.getStandardPrice() == null) {
                logger.log(Level.INFO, "Missing standard price product: {0}", product.getPartNumber());
                return;
            }
            
            columns.put("price", itemPricing.getStandardPrice().toString());

            addErpCustomerPrices(mas90, itemPricing, columns);
            addErpGroupPrices(mas90, itemPricing, columns);
            
        } catch (EmptyResultDataAccessException e) {
            logger.log(Level.WARNING, "Missing prices for product: {0}", product.getPartNumber());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error getting prices from MAS90 db {0}", e);
        }
    }
    
    // bob@example.com;0:$1.00;10:$0.95;20:$0.90|jim@example.com....
    private void addErpCustomerPrices(Mas90Prices mas90, ItemPricing itemPricing, Map<String, String> columns) throws IOException {
        
        // Build the customer price string
        StringBuilder priceString = new StringBuilder();
        for (Entry<String, List<CalculatedPrice>> entry : itemPricing.calculateCustomerSpecificPrices().entrySet()) {
            
            // Get the entry info
            String customerEmail = entry.getKey();
            List<CalculatedPrice> prices = entry.getValue();
            
            if (StringUtils.isBlank(customerEmail)) {
                continue;
            }
            
            // Add a separator is this isn't the first customer
            if (priceString.length() > 0) {
                priceString.append('|');
            }
            
            priceString.append(customerEmail);
            
            // MAS90's uses "up to this quantity", Magento is "this quantity and up"
            // Keep track of the previous quantity so we can use the proper quantity in Magento
            int previousQuantity = 0;
            
            // Build the value string "quantity1:price1;quantityN:priceN"
            for (CalculatedPrice price : prices) {
                priceString.append(";");
                
                priceString.append(previousQuantity + 1);
                priceString.append(":");
                priceString.append(price.getPrice());
                
                // Update the previous quantity
                previousQuantity = price.getQuantity();
            }
        }
        
        columns.put("customerprice", priceString.toString());
    }
    
    private void addErpGroupPrices(Mas90Prices mas90, ItemPricing itemPricing, Map<String, String> columns) throws IOException {
        
        // Add column data for each price level, group and tier prices
        for (String priceLevel : Mas90Prices.getPriceLevels()) {
            
            // Get the price level pricing
            StringBuilder priceString = new StringBuilder();
            List<CalculatedPrice> prices = mas90.calculatePriceLevelPrices(priceLevel, itemPricing);
            
            // MAS90's uses "up to this quantity", Magento is "this quantity and up"
            // Keep track of the previous quantity so we can use the proper quantity in Magento
            int previousQuantity = 0;
            
            // Build the value string "quantity1:price1;quantityN:priceN"
            for (CalculatedPrice price : prices) {
                if (price.getBreakLevel() > 0) {
                    priceString.append(";");
                }
                
                priceString.append(previousQuantity + 1);
                priceString.append(":");
                priceString.append(price.getPrice());
                
                // Update the previous quantity
                previousQuantity = price.getQuantity();
            }
            
            // Add the tier price column if we have more than a base price
            if (prices.size() > 1) {
                columns.put("tier_price:ERP_PL_" + priceLevel, priceString.toString());
            }
            
            // Add the group price column
            if (!prices.isEmpty()) {
                columns.put("group_price:ERP_PL_" + priceLevel, prices.get(0).getPrice().toString());
            }
        }
    }
    
    public static TreeMap<Long, MagmiProduct> findMagmiProducts(List<Part> parts) {
        long startTime = System.currentTimeMillis();
        
        // Build a product map from the parts
        TreeMap<Long, MagmiProduct> productMap = new TreeMap<Long, MagmiProduct>();
        for (Part part : parts) {
            productMap.put(part.getId(), new MagmiProduct(part));
        }
        
        List<Long> productIds = new ArrayList<Long>(productMap.keySet());
        
        
        // Aggregate basic product data
        List<MagmiBasicProduct> basicProducts = findMagmiBasicProducts(productIds);
        
        for (MagmiBasicProduct basicProduct : basicProducts) {
            productMap.get(basicProduct.getSku())
                    .addBasicProductCollections(basicProduct);
        }
        
        // Add the interchanges
        List<MagmiInterchange> interchanges = findMagmiInterchanges(productIds);
        
        for (MagmiInterchange interchange : interchanges) {
            productMap.get(interchange.getSku())
                    .addInterchange(interchange);
        }
        
        // Add the bom items
        List<MagmiBomItem> bom = findMagmiBom(productIds);
        
        for (MagmiBomItem bomItem : bom) {
            productMap.get(bomItem.getParentSku())
                    .addBomItem(bomItem);
        }
        
        logger.log(Level.INFO, "Got {0} basic, {1} interchange, {2} bom records for {3} parts in {4}ms; first/last id {5}/{6}",
                new Object[] {basicProducts.size(), interchanges.size(), bom.size(), parts.size(), System.currentTimeMillis() - startTime, productIds.get(0), productIds.get(productIds.size() - 1)});
        
        return productMap;
    }
    
    public static List<MagmiBasicProduct> findMagmiBasicProducts(Collection<Long> productIds) {
        return Part.entityManager().createQuery(
              "SELECT DISTINCT new com.turbointernational.metadata.util.dto.MagmiBasicProduct("
                + "  p.id AS sku,\n"
                + "  i.id AS imageId,\n"
                + "  tt.name AS turbo_type,\n"
                + "  tm.name AS turbo_model,\n"
                + "  CONCAT(tman.name, '!!', tt.name, '!!', tm.name) AS finder_turbo,\n"
                + "  CONCAT(cmake.name, '!!', COALESCE(cyear.name, 'not specified'), '!!', cmodel.name) AS finder_application,\n"
                + "  CONCAT("
                + "   cmake.name, '!!',"
                + "   cmodel.name, '!!',"
                + "   COALESCE(cyear.name, 'not specified'), '!!',"
                + "   COALESCE(cengine.engineSize, ''), '!!',"
                + "   COALESCE(cfuel.name, '')"
                + "  ) AS application_detail\n"
                + ")\n"
                + "FROM Part p\n"
                + "  LEFT JOIN p.productImages i\n"
                + "  LEFT JOIN p.turbos t\n"
                + "  LEFT JOIN t.manufacturer tman\n"
                + "  LEFT JOIN t.turboModel tm\n"
                + "  LEFT JOIN tm.turboType tt\n"
                + "  LEFT JOIN t.cars c\n"
                + "  LEFT JOIN c.model cmodel\n"
                + "  LEFT JOIN c.engine cengine\n"
                + "  LEFT JOIN c.year cyear\n"
                + "  LEFT JOIN cmodel.make cmake\n"
                + "  LEFT JOIN cengine.fuelType cfuel\n"
                + "WHERE\n"
                + "  p.id IN (" + StringUtils.join(productIds, ',') + ")\n"
                + "ORDER BY p.id", MagmiBasicProduct.class)
            .getResultList();
    }
    
    private static List<MagmiInterchange> findMagmiInterchanges(Collection<Long> productIds) {
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

    private static List<MagmiBomItem> findMagmiBom(Collection<Long> productIds) {
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
