package com.turbointernational.metadata.magento;

import au.com.bytecode.opencsv.CSVWriter;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.util.dto.MagmiBasicProduct;
import com.turbointernational.metadata.util.dto.MagmiBomItem;
import com.turbointernational.metadata.util.dto.MagmiInterchange;
import com.turbointernational.metadata.util.dto.MagmiProduct;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
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
import net.sf.jsog.JSOG;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping(value={"/magmi", "/metadata/magmi"})
public class Magmi {
    private static final Logger logger = Logger.getLogger(Magmi.class.toString());
    
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
        "ti_part_sku",       // Interchangeable parts by TI
        "interchanges",      // Interchangeable parts
        "bill_of_materials", // BOM
        "price",
        "quantity",
        "turbo_model",      // Turbo Models
        "turbo_type",       // Turbo Types
        "image",            // Product image
        "media_gallery",    // Extra images
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Types">
        "kit_type",
        "gasket_type",
        "seal_type",
        "cool_type",
        "turbo_model_name",
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
        "finder:" + MagmiProduct.FINDER_ID_TURBO
    };

    @Value("${mas90.db.path}")
    String mas90DbPath;
    
    @RequestMapping("/products")
    @ResponseBody   
    @Transactional
    public void products(
            @RequestParam(required = false) Long id,
            HttpServletResponse response, OutputStream out) throws Exception {
        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Content-Disposition: attachment; filename=products.csv", null);
        
        Mas90Prices mas90 = new Mas90Prices(new File(mas90DbPath));
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(out), ',', '"');
        
        // Write the header row
        writer.writeNext(HEADERS);
        
        // Get the product IDs to retrieve
        int position = 0;
        int pageSize = 100;
        List<Long> productIds;
        do {
            
            // Get the next batch of part IDs
            productIds = Part.findPartIds(position, pageSize);

            // Process each product
            for (MagmiProduct product : findMagmiProducts(productIds).values()) {
                try {
                    writer.writeNext(magmiProductToCsvRow(mas90, product));
                } catch (NoPriceException e) {
                    logger.log(Level.WARNING, e.getMessage());
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Could not write magmi part.", e);
                }
            }
            
            // Update the position
            position += productIds.size();
        } while (productIds.size() >= pageSize);
        
        
        writer.flush();
        writer.close();
        
    }
    
    private String[] magmiProductToCsvRow(Mas90Prices mas90, MagmiProduct product) throws IOException, NoPriceException {
        Map<String, String> columns = product.getCsvColumns();
        
        // Only TI parts get this info
        if (product.getManufacturerId() == Manufacturer.TI_ID) {

            // Default to quantity 1
            columns.put("quantity", "1");
            
            // ERP Prices
            try {
                if (StringUtils.isNotBlank(product.getPartNumber())) {
                    addErpPrices(mas90, columns, product.getPartNumber());
                }
            } catch (EmptyResultDataAccessException e) {
                throw new NoPriceException(product.getPartNumber());
            }
        }

        // Map the column into a value array for the CSV writer
        String[] valueArray = new String[HEADERS.length];
        for (int i = 0; i < HEADERS.length; i++) {
            
            String header = HEADERS[i];
            
            valueArray[i] = StringUtils.defaultIfEmpty(columns.get(header), "");
        }
        
        return valueArray;
    }
    
    private void addErpPrices(Mas90Prices mas90, Map<String, String> columns, String partNumber) throws IOException {
        ItemPricing itemPricing = mas90.getItemPricing(partNumber);
        
        // Nothing to do!
        if (itemPricing.getStandardPrice() == null) {
            logger.log(Level.INFO, "No pricing info for TI part number: {0}", partNumber);
            return;
        }
        columns.put("price", itemPricing.getStandardPrice().toString());
        
        addErpCustomerPrices(mas90, itemPricing, columns);
        addErpGroupPrices(mas90, itemPricing, columns);
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
    
    public static TreeMap<Long, MagmiProduct> findMagmiProducts(List<Long> productIds) {
        long startTime = System.currentTimeMillis();
        
        TreeMap<Long, MagmiProduct> productMap = new TreeMap<Long, MagmiProduct>();
        
        List<MagmiBasicProduct> basicProducts = findMagmiBasicProducts(productIds);
        
        List<MagmiInterchange> interchanges = findMagmiInterchanges(productIds);
        
        List<MagmiBomItem> bom = findMagmiBom(productIds);
        
        // Transform the basic product rows in the aggregated product data
        for (MagmiBasicProduct basicProduct : basicProducts) {
            Long sku = basicProduct.getSku();
            
            // Get the product
            MagmiProduct magmiProduct = productMap.get(sku);
            
            // Create it or update it
            if (magmiProduct == null) {
                magmiProduct = new MagmiProduct(basicProduct);
                productMap.put(sku, magmiProduct);
            } else {
                magmiProduct.addBasicProductCollections(basicProduct);
            }
        }
        
        // Add the interchanges
        for (MagmiInterchange interchange : interchanges) {
            MagmiProduct magmiProduct = productMap.get(interchange.getSku());
            magmiProduct.addInterchange(interchange);
        }
        
        // Add the bom items
        for (MagmiBomItem bomItem : bom) {
            MagmiProduct magmiProduct = productMap.get(bomItem.getParentSku());
            magmiProduct.addBomItem(bomItem);
        }
        
        logger.log(Level.INFO, "Got {0} basic, {1} interchange, {2} bom records for {3} product ids in {4}ms",
                new Object[] {basicProducts.size(), interchanges.size(), bom.size(), productIds.size(), System.currentTimeMillis() - startTime});
        
        return productMap;
    }
    
    public static List<MagmiBasicProduct> findMagmiBasicProducts(List<Long> productIds) {
        return Part.entityManager().createQuery(
              "SELECT DISTINCT new com.turbointernational.metadata.util.dto.MagmiBasicProduct("
                + "  p.id AS sku,\n"
                + "  p.name AS name,\n"
                + "  p.description AS description,\n"
                + "  pt.magentoAttributeSet AS attribute_set,\n"
                + "  pt.name AS part_type,\n"
                + "  ptp.name AS part_type_parent,\n"
                + "  m.id AS manufacturerId,\n"
                + "  m.name AS manufacturer,\n"
                + "  p.manufacturerPartNumber AS part_number,\n"
                + "  i.filename AS imageFile,\n" // Collection
                + "  tt.name AS turbo_type,\n" // Collection
                + "  tm.name AS turbo_model,\n" // Collection
                + "  CONCAT(tman.name, '!!', tt.name, '!!', tm.name, '!!', t.manufacturerPartNumber) AS finder_turbo,\n" // Collection
                + "  CONCAT(cmake.name, '!!', cyear.name, '!!', cmodel.name) AS finder_application\n" // Collection
                + ")\n"
                + "FROM Part p\n"
                + "  JOIN p.partType pt\n"
                + "  JOIN p.manufacturer m\n"
                + "  LEFT JOIN pt.parent ptp\n"
                + "  LEFT JOIN p.productImages i\n"
                + "  LEFT JOIN p.turbos t\n"
                + "  LEFT JOIN t.manufacturer tman\n"
                + "  LEFT JOIN t.turboModel tm\n"
                + "  LEFT JOIN tm.turboType tt\n"
                + "  LEFT JOIN t.cars c\n"
                + "  LEFT JOIN c.model cmodel\n"
                + "  LEFT JOIN cmodel.make cmake\n"
                + "  LEFT JOIN c.year cyear\n"
                + "WHERE\n"
                + "  p.id IN (" + StringUtils.join(productIds, ',') + ") \n"
                + "ORDER BY p.id", MagmiBasicProduct.class)
            .getResultList();
    }
    
    private static List<MagmiInterchange> findMagmiInterchanges(List<Long> productIds) {
        return Part.entityManager().createQuery(
                "SELECT DISTINCT NEW"
              + "  com.turbointernational.metadata.util.dto.MagmiInterchange("
              + "    p.id AS sku,"
              + "    ip.id AS interchangePartSku,"
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

    private static List<MagmiBomItem> findMagmiBom(List<Long> productIds) {
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
