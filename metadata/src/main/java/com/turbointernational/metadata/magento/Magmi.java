package com.turbointernational.metadata.magento;

import au.com.bytecode.opencsv.CSVWriter;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import mas90magmi.Customer;
import mas90magmi.ItemPricing;
import mas90magmi.Mas90Magmi;
import mas90magmi.PriceCalculator;
import mas90magmi.CalculatedPrice;
import mas90magmi.Pricing;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/magmi")
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
        "manufacturer_part_number",
        "categories",
        "ti_part_sku",       // Interchangeable parts by TI
        "interchanges",      // Interchangeable parts
        "bill_of_materials", // BOM
        "price",
        "quantity",
        "turbo_model",       // Turbo Models
        "turbo_type",       // Turbo Types
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Types">
        "kit_type",
        "gasket_type",
        "seal_type",
        "cool_type",
        "turbo_model_name",
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Part Type Specifics">
        "application",
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
        "tier_price:ERP_PL_W"
        //</editor-fold>
            
    };
    
    @Autowired(required=true)
    Mas90Magmi mas90;
    
    @RequestMapping("/products")
    @ResponseBody
    @Transactional
    public void products(
            @RequestParam(required = false) Long id,
            HttpServletResponse response, OutputStream out) throws Exception {
        
        long runtimeStart = System.currentTimeMillis();
        
        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Content-Disposition: attachment; filename=products.csv", null);
        
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(out), ',', '"');
        
        // Write the header row
        writer.writeNext(HEADERS);
        
        if (id == null) {
            
            // Write the non-bom parts, then bom parts
            int count = writeAllParts(writer);
            
            logger.log(Level.INFO, "Magmi products exported, {0} parts in {1}ms",
                new Object[] {count, System.currentTimeMillis() - runtimeStart});
        } else {
            writer.writeNext(partToProductCsvRow(Part.findPart(id)));
        }
        
        writer.flush();
        writer.close();
        
    }

    private int writeAllParts(CSVWriter writer) throws Exception {
        
        // Write a CSV for each part
        List<Part> parts;
        int start = 0;
        int count = 100;
        
        do {
            logger.log(Level.INFO, "Writing parts {0}-{1}", new Object[]{start, start+count});
            
            // Get the next batch of parts
            parts = Part.findPartEntries(start, count);
            start += parts.size();
            
            // Write each part
            for (Part part : parts) {
                try {
                    writer.writeNext(partToProductCsvRow(part));
                } catch (Exception e) {
                    logger.log(Level.INFO, "Failed to synchronize part " + part.getId(), e);
                    continue;
                }
            }
        } while (parts.size() > 0);
        
        return start;
    }
    
    private String[] partToProductCsvRow(Part part) throws IOException {
        
        // Get the part's column values
        Map<String, String> columns = new HashMap<String, String>();
        part.csvColumns(columns);
        
        // Add ERP pricing details if this is a TI part
        if (Manufacturer.TI_ID.equals(part.getManufacturer().getId())) {
            addErpPrices(columns, part.getManufacturerPartNumber());
        }

        // Map the column into a value array for the CSV writer
        String[] valueArray = new String[HEADERS.length];
        for (int i = 0; i < HEADERS.length; i++) {
            
            String header = HEADERS[i];
            
            valueArray[i] = StringUtils.defaultIfEmpty(columns.get(header), "");
        }
        
        return valueArray;
    }
    
    private void addErpPrices(Map<String, String> columns, String partNumber) throws IOException {
        ItemPricing itemPricing = mas90.getItemPricing(partNumber);
        PriceCalculator calculator = mas90.getCalculator();
        
        // Nothing to do!
        if (itemPricing.getStandardPrice() == null) {
            logger.log(Level.INFO, "No pricing info for TI part number: {0}", partNumber);
            return;
        }
        columns.put("price", itemPricing.getStandardPrice().toString());
        
        addErpCustomerPrices(itemPricing, calculator, columns);
        addErpGroupPrices(itemPricing, calculator, columns);
    }
    
    // bob@example.com;0:$1.00;10:$0.95;20:$0.90|jim@example.com....
    private void addErpCustomerPrices(ItemPricing itemPricing, PriceCalculator calculator, Map<String, String> columns) throws IOException {
        
        // Build the customer price string
        StringBuilder priceString = new StringBuilder();
        for (Entry<Customer, List<CalculatedPrice>> entry : calculator.calculateCustomerSpecificPrices(itemPricing).entrySet()) {
            
            // Get the entry info
            Customer customer = entry.getKey();
            String customerEmail = mas90.getCustomerEmail(customer);
            List<CalculatedPrice> prices = entry.getValue();
            
            if (StringUtils.isBlank(customerEmail)) {
                logger.log(Level.WARNING, "No email for customer {0} {1}", new Object[]{customer.getDivision(), customer.getCustomerNumber()});
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
    
    private void addErpGroupPrices(ItemPricing itemPricing, PriceCalculator calculator, Map<String, String> columns) throws IOException {
        
        // Add column data for each price level, group and tier prices
        for (String priceLevel : Mas90Magmi.getPriceLevels()) {
            
            // Get the price level pricing
            StringBuilder priceString = new StringBuilder();
            List<CalculatedPrice> prices = calculator.getPriceLevelPrices(priceLevel, itemPricing);
            
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
