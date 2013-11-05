package com.turbointernational.metadata.magento;

import au.com.bytecode.opencsv.CSVWriter;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.types.Backplate;
import com.turbointernational.metadata.domain.part.types.BearingHousing;
import com.turbointernational.metadata.domain.part.types.BearingSpacer;
import com.turbointernational.metadata.domain.part.types.CompressorWheel;
import com.turbointernational.metadata.domain.part.types.Gasket;
import com.turbointernational.metadata.domain.part.types.Heatshield;
import com.turbointernational.metadata.domain.part.types.JournalBearing;
import com.turbointernational.metadata.domain.part.types.Kit;
import com.turbointernational.metadata.domain.part.types.PistonRing;
import com.turbointernational.metadata.domain.part.types.TurbineWheel;
import com.turbointernational.metadata.domain.part.types.Turbo;
import com.turbointernational.metadata.domain.type.PartType;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    
    @Autowired(required=true)
    private JdbcTemplate metadataDb;
    
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
        csvColumnsBasic(columns, part);
        
        csvColumnsPartType(columns, part);

        // Map the column into a value array for the CSV writer
        String[] valueArray = new String[HEADERS.length];
        for (int i = 0; i < HEADERS.length; i++) {
            
            String header = HEADERS[i];
            
            valueArray[i] = StringUtils.defaultIfEmpty(columns.get(header), "");
        }
        
        return valueArray;
    }
     
    void csvColumnsBasic(Map<String, String> columns, Part part) {
        
        // sku
        columns.put("sku", part.getId().toString());
        
        // attribute_set
        columns.put("attribute_set", part.getPartType().getMagentoAttributeSet());
        
        // type
        columns.put("type", "simple");
        
        // visibility
        columns.put("visibility", "Catalog, Search"); // See magmi genericmapper visibility.csv
        
        // type
        columns.put("status", "Enabled"); // See magmi genericmapper status.csv
        
        // name
        columns.put("name", ObjectUtils.toString(part.getName()));
        
        // description
        columns.put("description", ObjectUtils.toString(part.getDescription()));
        
        // manufacturer
        columns.put("manufacturer", ObjectUtils.toString(part.getManufacturer().getName()));
        
        // manufacturer_part_number
        columns.put("manufacturer_part_number", ObjectUtils.toString(part.getManufacturerPartNumber()));
        
        // ti_part_sku
        List<Part> tiInterchanges = part.getTIInterchanges();
        if (!tiInterchanges.isEmpty()) {
            columns.put("ti_part_sku", tiInterchanges.get(0).getId().toString());
        }
        
        // categories
        String categories = partTypeToCategories(part);
        
        if (StringUtils.isNotBlank(categories)) {
            columns.put("categories", categories);
        }
        
        // bill_of_materials
        if (!part.getBom().isEmpty()) {
            JSOG bom = JSOG.array();
            
            for (BOMItem item : part.getBom()) {
                
                JSOG jsogItem = JSOG.object();
                jsogItem.put("sku", item.getChild().getId());
                jsogItem.put("quantity", item.getQuantity());
                
                JSOG tiParts = JSOG.array();
                jsogItem.put("ti_part_sku", tiParts);
                
                for (Part tiAlt : item.getTIAlternates()) {
                    tiParts.add(tiAlt.getId());
                }
                
                for (Part tiInterchange : item.getChild().getTIInterchanges()) {
                    tiParts.add(tiInterchange.getId());
                }
                
                if (tiParts.size() > 0) {
                    bom.add(jsogItem);
                }
            }

            if (bom.size() > 0) {
                columns.put("bill_of_materials", bom.toString());
            }
        }
        
        // HACK: We should be getting this through hibernate. It's giving me grief ATM, so moving on...
        List<String> priceList = metadataDb.queryForList(
                "SELECT StdPrice FROM mas90_std_price WHERE ItemNumber = ?",
                String.class, part.getManufacturerPartNumber());
        
        if (!priceList.isEmpty()) {
            columns.put("price", priceList.get(0));
        }
        
        if (part.getManufacturer().getId() == Manufacturer.TI_ID) {
            columns.put("quantity", "1");
        }
    }
    
    void csvColumnsPartType(Map<String, String> columns, Part part) {
        if (part instanceof Backplate) {
            csvColumns((Backplate) part, columns);
        } else if (part instanceof BearingHousing) {
            csvColumns((BearingHousing) part, columns);
        } else if (part instanceof BearingSpacer) {
            csvColumns((BearingSpacer) part, columns);
        } else if (part instanceof CompressorWheel) {
            csvColumns((CompressorWheel) part, columns);
        } else if (part instanceof Gasket) {
            csvColumns((Gasket) part, columns);
        } else if (part instanceof Heatshield) {
            csvColumns((Heatshield) part, columns);
        } else if (part instanceof JournalBearing) {
            csvColumns((JournalBearing) part, columns);
        } else if (part instanceof Kit) {
            csvColumns((Kit) part, columns);
        } else if (part instanceof PistonRing) {
            csvColumns((PistonRing) part, columns);
        } else if (part instanceof TurbineWheel) {
            csvColumns((TurbineWheel) part, columns);
        } else if (part instanceof Turbo) {
            csvColumns((Turbo) part, columns);
        }
    }
    
    private void csvColumns(Backplate castPart, Map<String, String> columns) {
        if (castPart.getSealType() != null) {
            columns.put("seal_type", ObjectUtils.toString(castPart.getSealType().getName()));
        }

        columns.put("overall_diameter", ObjectUtils.toString(castPart.getOverallDiameter()));
        columns.put("compressor_wheel_diameter", ObjectUtils.toString(castPart.getCompressorWheelDiameter()));
        columns.put("piston_ring_diameter", ObjectUtils.toString(castPart.getPistonRingDiameter()));
        columns.put("compressor_housing_diameter", ObjectUtils.toString(castPart.getCompressorHousingDiameter()));
        columns.put("secondary_diameter", ObjectUtils.toString(castPart.getSecondaryDiameter()));
        columns.put("overall_height", ObjectUtils.toString(castPart.getOverallHeight()));
        columns.put("style_compressor_wheel", ObjectUtils.toString(castPart.getStyleCompressorWheel()));
    }

    private void csvColumns(BearingHousing castPart, Map<String, String> columns) {
        columns.put("oil_inlet", ObjectUtils.toString(castPart.getOilInlet()));
        columns.put("oil_outlet", ObjectUtils.toString(castPart.getOilOutlet()));
        columns.put("oil", ObjectUtils.toString(castPart.getOil()));
        columns.put("outlet_flange_holes", ObjectUtils.toString(castPart.getOutletFlangeHoles()));
        columns.put("water_ports", ObjectUtils.toString(castPart.getWaterPorts()));
        columns.put("design_features", ObjectUtils.toString(castPart.getDesignFeatures()));
        columns.put("bearing_type", ObjectUtils.toString(castPart.getBearingType()));

        if (castPart.getCoolType() != null) {
            columns.put("cool_type", ObjectUtils.toString(castPart.getCoolType().getName()));
        }
    }

    private void csvColumns(BearingSpacer castPart, Map<String, String> columns) {
        columns.put("outside_diameter_min", ObjectUtils.toString(castPart.getOutsideDiameterMin()));
        columns.put("outside_diameter_max", ObjectUtils.toString(castPart.getOutsideDiameterMax()));
        columns.put("inside_diameter_min", ObjectUtils.toString(castPart.getInsideDiameterMin()));
        columns.put("inside_diameter_max", ObjectUtils.toString(castPart.getInsideDiameterMax()));

        if (castPart.getStandardSize() != null) {
            columns.put("standard_size_id", ObjectUtils.toString(castPart.getStandardSize().getId()));
        }

        if (castPart.getOversize() != null) {
            columns.put("oversize_id", ObjectUtils.toString(castPart.getOversize().getId()));
        }
    }

    private void csvColumns(CompressorWheel castPart, Map<String, String> columns) {
        columns.put("inducer_oa", ObjectUtils.toString(castPart.getInducerOa()));
        columns.put("tip_height_b", ObjectUtils.toString(castPart.getTipHeightB()));
        columns.put("exducer_oc", ObjectUtils.toString(castPart.getExducerOc()));
        columns.put("hub_length_d", ObjectUtils.toString(castPart.getHubLengthD()));
        columns.put("bore_oe", ObjectUtils.toString(castPart.getBoreOe()));
        columns.put("number_of_blades", ObjectUtils.toString(castPart.getNumberOfBlades()));
        columns.put("application", ObjectUtils.toString(castPart.getApplication()));
    }

    private void csvColumns(Gasket castPart, Map<String, String> columns) {
        if (castPart.getGasketType() != null) {
            columns.put("gasket_type", ObjectUtils.toString(castPart.getGasketType().getName()));
        }
    }

    private void csvColumns(Heatshield castPart, Map<String, String> columns) {
        columns.put("overall_diameter", ObjectUtils.toString(castPart.getOverallDiameter()));
        columns.put("inside_diameter", ObjectUtils.toString(castPart.getInsideDiameter()));
        columns.put("inducer_diameter", ObjectUtils.toString(castPart.getInducerDiameter()));
    }

    private void csvColumns(JournalBearing castPart, Map<String, String> columns) {
        columns.put("outside_diameter_min", ObjectUtils.toString(castPart.getOutsideDiameterMin()));
        columns.put("outside_diameter_max", ObjectUtils.toString(castPart.getOutsideDiameterMax()));
        columns.put("inside_diameter_min", ObjectUtils.toString(castPart.getInsideDiameterMin()));
        columns.put("inside_diameter_max", ObjectUtils.toString(castPart.getInsideDiameterMax()));

        if (castPart.getStandardSize() != null) {
            columns.put("standard_size_id", ObjectUtils.toString(castPart.getStandardSize().getId()));
        }

        if (castPart.getOversize() != null) {
            columns.put("oversize_id", ObjectUtils.toString(castPart.getOversize().getId()));
        }
    }

    private void csvColumns(Kit castPart, Map<String, String> columns) {
        if (castPart.getKitType() != null) {
            columns.put("kit_type", ObjectUtils.toString(castPart.getKitType().getName()));
        }
    }

    private void csvColumns(PistonRing castPart, Map<String, String> columns) {
        columns.put("outside_dim_min", ObjectUtils.toString(castPart.getOutsideDiameterMin()));
        columns.put("outside_dim_max", ObjectUtils.toString(castPart.getOutsideDiameterMax()));
        columns.put("width_min", ObjectUtils.toString(castPart.getWidthMin()));
        columns.put("width_max", ObjectUtils.toString(castPart.getWidthMax()));
        columns.put("i_gap_min", ObjectUtils.toString(castPart.getInstalledGapMin()));
        columns.put("i_gap_max", ObjectUtils.toString(castPart.getInstalledGapMax()));
    }

    private void csvColumns(TurbineWheel castPart, Map<String, String> columns) {
        columns.put("exduce_oa", ObjectUtils.toString(castPart.getExducerDiameterA()));
        columns.put("tip_height_b", ObjectUtils.toString(castPart.getTipHeightB()));
        columns.put("inducer_oc", ObjectUtils.toString(castPart.getInducerDiameterC()));
        columns.put("journal_od", ObjectUtils.toString(castPart.getJournalDiameterD()));
        columns.put("stem_oe", ObjectUtils.toString(castPart.getStemDiameterE()));
        columns.put("shaft_thread_f", ObjectUtils.toString(castPart.getShaftThreadF()));
        columns.put("number_of_blades", ObjectUtils.toString(castPart.getNumberOfBlades()));
    }

    private void csvColumns(Turbo castPart, Map<String, String> columns) {
        if (castPart.getTurboModel() != null) {
            columns.put("turbo_model_name", ObjectUtils.toString(castPart.getTurboModel().getName()));
        }

        if (castPart.getCoolType() != null) {
            columns.put("cool_type", ObjectUtils.toString(castPart.getCoolType().getName()));
        }
    }

    /**
     * Create a category of part types:
     * parent/child/grandchild
     */
    private String partTypeToCategories(Part part) {
        
        StringBuilder categoriesSB = new StringBuilder("Part Type");
        
        // Get the first part type
        PartType partType = part.getPartType();
        
        while (partType != null) {
            categoriesSB.append('/');
            
            // Append the category name
            categoriesSB.append(partType.getName());

            // Setup next iteration
            partType = partType.getParent();
        }

        return categoriesSB.toString();
    }
    
}
