package com.turbointernational.metadata.magento;

import au.com.bytecode.opencsv.CSVWriter;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import mas90magmi.ItemPricing;
import mas90magmi.Mas90Magmi;
import mas90magmi.PriceCalculator;
import mas90magmi.PriceCalculator.CalculatedPrice;
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
        "attribute_set",
        "type",
        "visibility",
        "status",
        "name",
        "description",
        "manufacturer",
        "manufacturer_part_number",
        "ti_part_sku",
        "categories",
        "bill_of_materials", // BOM
        "price",
        "quantity",
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Part Type Specifics">
        "application",
        "bearing_type",
        "bore_oe",
        "compressor_housing_diameter",
        "compressor_wheel_diameter",
        "cool_type",
        "design_features",
        "exduce_oa",
        "exducer_oc",
        "gasket_type_name",
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
        "kit_type",
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
        "seal_type",
        "secondary_diameter",
        "shaft_thread_f",
        "standard_size_id",
        "stem_oe",
        "style_compressor_wheel",
        "tip_height_b",
        "turbo_model_name",
        "water_ports",
        "width_max",
        "width_min",
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="MAS90 Price Levels">
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
            addErpPricing(columns, part.getManufacturerPartNumber());
        }

        // Map the column into a value array for the CSV writer
        String[] valueArray = new String[HEADERS.length];
        for (int i = 0; i < HEADERS.length; i++) {
            
            String header = HEADERS[i];
            
            valueArray[i] = StringUtils.defaultIfEmpty(columns.get(header), "");
        }
        
        return valueArray;
    }
    
    private void addErpPricing(Map<String, String> columns, String partNumber) throws IOException {
        ItemPricing itemPricing = mas90.getItemPricing(partNumber);
        PriceCalculator calculator = mas90.getCalculator();
        
        // Nothing to do!
        if (itemPricing.getStandardPrice() == null) {
            logger.log(Level.INFO, "No pricing info for TI part number: `{0}'", partNumber);
            return;
        }
        columns.put("price", itemPricing.getStandardPrice().toString());
        
        // Add column data for each price level
        for (String priceLevel : Mas90Magmi.getPriceLevels()) {
            String columnName = "tier_price:ERP_PL_" + priceLevel;
            
            // Get the price level pricing
            StringBuilder priceString = new StringBuilder();
            Iterator<CalculatedPrice> prices = calculator.calculatePriceBreaks(priceLevel, itemPricing);
            
            // Build the value string "quantity1:price1;quantityN:priceN"
            while (prices.hasNext()) {
                CalculatedPrice price = prices.next();
                
                if (price.getBreakLevel() > 0) {
                    priceString.append(";");
                }
                
                priceString.append(price.getQuantity());
                priceString.append(":");
                priceString.append(price.getPrice());
            }
            
            if (priceString.length() > 0) {
                columns.put(columnName, priceString.toString());
            }
            
            mas90.getCalculator().calculateLevelPrices(itemPricing);
        }
    }
}
