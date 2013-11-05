package com.turbointernational.metadata.magento;

import au.com.bytecode.opencsv.CSVWriter;
import com.turbointernational.metadata.domain.part.Part;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
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
            
    };
    
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
            
            // Get the next batch of parts
            parts = Part.findPartEntries(start, count);
            start += parts.size();
            
            // Write each part
            for (Part part : parts) {
                try {
                    writer.writeNext(partToProductCsvRow(part));
                } catch (Exception e) {
                    logger.log(Level.INFO, "Failed to synchronize part " + part.getId(), e);
                    throw e;
                }
            }
        } while (parts.size() > 0);
        
        return start;
    }
    
    private String[] partToProductCsvRow(Part part) {
        
        // Get the part's column values
        Map<String, String> columns = new HashMap<String, String>();
        part.csvColumns(columns);

        // Map the column into a value array for the CSV writer
        String[] valueArray = new String[HEADERS.length];
        for (int i = 0; i < HEADERS.length; i++) {
            
            String header = HEADERS[i];
            
            valueArray[i] = StringUtils.defaultIfEmpty(columns.get(header), "");
        }
        
        return valueArray;
    }
}
