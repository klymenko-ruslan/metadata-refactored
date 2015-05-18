package com.turbointernational.metadata.web;

import au.com.bytecode.opencsv.CSVWriter;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.type.GasketType;
import com.turbointernational.metadata.domain.type.KitType;
import com.turbointernational.metadata.domain.type.ManufacturerType;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.domain.type.SealType;
import com.turbointernational.metadata.magmi.MagmiDataFinder;
import com.turbointernational.metadata.util.ImageResizer;
import com.turbointernational.metadata.magmi.dto.MagmiProduct;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import com.turbointernational.metadata.mas90.pricing.ItemPricing;
import com.turbointernational.metadata.mas90.pricing.CalculatedPrice;
import com.turbointernational.metadata.mas90.Mas90;
import com.turbointernational.metadata.mas90.pricing.UnknownDiscountCodeException;
import java.util.Arrays;
import java.util.SortedSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jrodriguez
 */
@Controller
@RequestMapping(value={"/magmi", "/metadata/magmi"})
public class MagmiController {
    private static final Logger logger = Logger.getLogger(MagmiController.class.toString());
    
    public String[] getCsvHeaders(SortedSet<String> priceLevels) {
        List<String> headers = new ArrayList<String>();
        
        //<editor-fold defaultstate="collapsed" desc="Basic">
        headers.addAll(Arrays.asList(
            "sku",
            "magmi:delete",
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
            "where_used",        // Usages
            "has_ti_chra",       // Has TI CHRA descendant
            "price",
            "qty",
            "turbo_model",      // Turbo Models
            "turbo_type",       // Turbo Types

            // Product images
            "image",
            "small_image",
            "thumbnail",
            "media_gallery",
            "standard_oversize_part"
        ));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Types">
        headers.addAll(Arrays.asList(
            "kit_type",
            "gasket_type",
            "seal_type",
            "cool_type"
        ));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Part Type Specifics">
        headers.addAll(Arrays.asList(
             // Cartridge
            "service_kits",

            // Other
            "bearing_type",
            "bore_oe",
            "compressor_housing_diameter",
            "compressor_wheel_diameter",
            "design_features",
            "exducer_oa",
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
            "piston_ring_diameter",
            "secondary_diameter",
            "shaft_thread_f",
            "stem_oe",
            "style_compressor_wheel",
            "tip_height_b",
            "water_ports",
            "width_max",
            "width_min"
        ));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="MAS90 Prices">
        headers.add("customerprice");
        
        // Group Prices
        for (String priceLevel: priceLevels) {
            headers.add("group_price:ERP_PL_" + priceLevel);
        }
        
        // Tier Prices
        for (String priceLevel: priceLevels) {
            headers.add("tier_price:ERP_PL_" + priceLevel);
        }
        //</editor-fold>
        

        headers.addAll(Arrays.asList(

            // Make,Year,Model
            "finder:" + finderIdApplication,

            // Manufacturer,TurboType,TurboModel
            "finder:" + finderIdTurbo,

            // Make!!Model!!Year!!Displacement!!Fuel||...
            "application_detail",
            
            // OEM SKU (custom option, used to show OEM part in cart)
            "OEMSKU:field:0"
        ));
        
        return headers.toArray(new String[0]);
    }

    @Value("${mas90.db.path}")
    String mas90DbPath;

    @Value("${magmi.batch.size}")
    Integer magmiBatchSize;

    @Value("${magmi.finderId.application}")
    String finderIdApplication;
    
    @Value("${magmi.finderId.turbo}")
    String finderIdTurbo;
    
    @Autowired(required=true)
    ImageResizer imageResizer;
    
    @Autowired(required=true)
    MagmiDataFinder magmiDataFinder;
    
    @Autowired(required=true)
    JdbcTemplate db;
    
    @RequestMapping("/products")
    @ResponseBody   
    @Transactional
    public void products(HttpServletResponse response, OutputStream out, @RequestParam(defaultValue="30", required=false) int days) throws Exception {
        logger.log(Level.INFO, "Magmi export started.");
        
        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Content-Disposition: attachment; filename=products.csv", null);
        
        long startTime = System.currentTimeMillis();
        
        Mas90 mas90 = new Mas90(new File(mas90DbPath));
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(out), ',', '\'', '\\');
        
        // Write the header row
        writer.writeNext(getCsvHeaders(mas90.getPriceLevels()));
        
        // Get the product IDs to retrieve
        int position = 0;
        Long lastSuccessfulSku = null;
        List<Part> parts = Collections.emptyList();
        do {
            try {
                
                // Clear Hibernate
                Part.entityManager().clear();
                
                // Pre-cache our frequently-used entities
                ManufacturerType.findAllManufacturerTypes();
                Manufacturer.findAllManufacturers();
                PartType.findAllPartTypes();
                CoolType.findAllCoolTypes();
                GasketType.findAllGasketTypes();
                KitType.findAllKitTypes();
                SealType.findAllSealTypes();

                // Get the next batch of part IDs
                parts = Part.findPartEntries(position, magmiBatchSize);

                // Process each product
                for (MagmiProduct product : magmiDataFinder.findMagmiProducts(parts).values()) {
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
                            + ", batch size: " + magmiBatchSize
                            + ", lastSuccessfulSku: " + lastSuccessfulSku,
                    e);
            }
        } while (parts.size() >= magmiBatchSize);
        
        
        // Deleted products
        List<String> deletedIds = db.queryForList(
                  "SELECT"
                + "  `id`"
                + "  FROM `deleted_parts`"
                + "  WHERE dt > DATE_SUB(NOW(), INTERVAL ? DAY)",
                String.class, days);
        
        for (String id : deletedIds) {
            writer.writeNext(new String[] {id, "1"});
        }
        
        
        writer.flush();
        writer.close();
        
        logger.log(Level.INFO, "Exported {0} products in {1}ms",
                new Object[] {position, System.currentTimeMillis() - startTime});
    }
    
    @RequestMapping("/product/{partId}")
    @ResponseBody   
    @Transactional
    public void product(HttpServletResponse response, OutputStream out, @PathVariable Long partId) throws Exception {
        
        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Content-Disposition: attachment; filename=products.csv", null);
        
        Mas90 mas90 = new Mas90(new File(mas90DbPath));
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(out), ',', '\'', '\\');
        
        // Write the header row
        writer.writeNext(getCsvHeaders(mas90.getPriceLevels()));
        
        Part part = Part.findPart(partId);
        List<Part> parts = new ArrayList<Part>();
        parts.add(part);
        
        Iterator<MagmiProduct> it = magmiDataFinder.findMagmiProducts(parts).values().iterator();
        while (it.hasNext()) {
            writer.writeNext(magmiProductToCsvRow(mas90, it.next()));
        }
        
        writer.flush();
        writer.close();
    }
    
    private String[] magmiProductToCsvRow(Mas90 mas90, MagmiProduct product) {
        Map<String, String> columns = product.getCsvColumns();
        product.csvFinderColumns(columns, finderIdApplication, finderIdTurbo);
        product.csvImageColumns(columns, imageResizer);
        
        // Only TI parts get this info
        if (product.hasTiPart()) {

            // Default to 1
            columns.put("qty", "1");
            
            // ERP Prices
            if (product.getManufacturerId() == Manufacturer.TI_ID) {
                addErpPrices(mas90, columns, product);
            }
        } else {
            columns.put("qty", "0");
        }

        // Map the column into a value array for the CSV writer
        String[] csvHeaders = getCsvHeaders(mas90.getPriceLevels());
        String[] valueArray = new String[csvHeaders.length];
        for (int i = 0; i < csvHeaders.length; i++) {
            
            String header = csvHeaders[i];
            
            valueArray[i] = StringUtils.defaultIfEmpty(columns.get(header), "");
        }
        
        return valueArray;
    }
    
    private void addErpPrices(Mas90 mas90, Map<String, String> columns, MagmiProduct product) {
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
        } catch (UnknownDiscountCodeException e) {
                logger.log(Level.WARNING, "Unknown discount code {0} for product {1}", new Object[] {e.getCode(), product.getPartNumber()});
        } catch (EmptyResultDataAccessException e) {
            logger.log(Level.WARNING, "Missing prices for product: {0}", product.getPartNumber());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error getting prices from MAS90 db {0}", e);
        }
    }
    
    // bob@example.com;0:$1.00;10:$0.95;20:$0.90|jim@example.com....
    private void addErpCustomerPrices(Mas90 mas90, ItemPricing itemPricing, Map<String, String> columns) throws IOException {
        
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
    
    private void addErpGroupPrices(Mas90 mas90, ItemPricing itemPricing, Map<String, String> columns) throws IOException {
        
        // Add column data for each price level, group and tier prices
        for (String priceLevel : mas90.getPriceLevels()) {
            
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
    
}
