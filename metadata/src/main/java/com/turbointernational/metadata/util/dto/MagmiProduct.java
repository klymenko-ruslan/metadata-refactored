package com.turbointernational.metadata.util.dto;

import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jrodriguez
 */
public class MagmiProduct {

    public static final String FINDER_ID_APPLICATION = "1";

    public static final String FINDER_ID_TURBO = "2";
    
    private Part part;
    
    private TreeSet<String> imageFiles = new TreeSet<String>();

    private Set<String> turboType = new TreeSet<String>();

    private Set<String> turboModel = new TreeSet<String>();

    private Set<String> finderTurbo = new TreeSet<String>();

    private Set<String> finderApplication = new TreeSet<String>();

    private Set<Long> interchanges = new TreeSet<Long>();

    private Set<Long> tiInterchanges = new TreeSet<Long>();
    
    private JSOG bom = JSOG.array();

    private int rowCount = 0;

    public MagmiProduct(Part part) {
        this.part = part;
    }
    
    public Long getSku() {
        return part.getId();
    }

    public Long getManufacturerId() {
        return part.getManufacturer().getId();
    }

    public String getPartNumber() {
        return part.getManufacturerPartNumber();
    }

    public final void addBasicProductCollections(MagmiBasicProduct basicProduct) {
        rowCount++;

        if (StringUtils.isNotEmpty(basicProduct.getImageFile())) {
            imageFiles.add(basicProduct.getImageFile());
        }

        if (StringUtils.isNotEmpty(basicProduct.getTurboType())) {
            turboType.add(basicProduct.getTurboType());
        }

        if (StringUtils.isNotEmpty(basicProduct.getTurboModel())) {
            turboModel.add(basicProduct.getTurboModel());
        }

        if (StringUtils.isNotEmpty(basicProduct.getFinderTurbo())) {
            finderTurbo.add(basicProduct.getFinderTurbo());
        }

        if (StringUtils.isNotEmpty(basicProduct.getFinderApplication())) {
            finderApplication.add(basicProduct.getFinderApplication());
        }
    }

    public void addInterchange(MagmiInterchange interchange) {
        interchanges.add(interchange.getInterchangePartSku());

        // TI interchanges
        if (interchange.getInterchangePartManufacturerId() == Manufacturer.TI_ID) {
            tiInterchanges.add(interchange.getInterchangePartSku());
        }
    }
    
    private JSOG getBomItemBySku(Long sku) {
        
        // Find the item, if it exists
        for (JSOG candidate : bom.arrayIterable()) {
            if (StringUtils.equals(candidate.get("sku").getStringValue(), sku.toString())) {
                return candidate;
            }
        }
        
        // Create the item if it doesn't
        JSOG bomItem = JSOG.object();
        bom.add(bomItem);
        bomItem.put("alt_part_sku", JSOG.array());
        bomItem.put("ti_part_sku", JSOG.array());
        
        return bomItem;
    }
    
    public static void addSkuToBomItemCollection(JSOG jsogItem, String key, Long sku) {
        if (sku == null) {
            return;
        }
        
        // Get the array
        JSOG skuArray = jsogItem.get(key);
        if (!skuArray.isArray()) {
            skuArray.add(sku);
        } else if (!skuArray.contains(sku)) {
            skuArray.add(sku);
        }
    }
    
    public void addBomItem(MagmiBomItem bomItem) {
        JSOG jsogItem = getBomItemBySku(bomItem.getChildSku());
        
        // Set the quantity
        jsogItem.put("sku", bomItem.getChildSku());
        jsogItem.put("quantity", bomItem.getQuantity());
        
        // Add the alternates
        addSkuToBomItemCollection(jsogItem, "alt_part_sku", bomItem.getAltSku());

        // Add the alternate and interchange TI skus
        if (bomItem.getAltSkuMfrId() == Manufacturer.TI_ID) {
            addSkuToBomItemCollection(jsogItem, "ti_part_sku", bomItem.getAltSku());
        }
        
        if (bomItem.getIntSkuMfrId() == Manufacturer.TI_ID) {
            addSkuToBomItemCollection(jsogItem, "ti_part_sku", bomItem.getIntSku());
        }
    }

    public final Map<String, String> getCsvColumns() {

        // CSV column map
        Map<String, String> columns = new HashMap<String, String>();
        
        // Part data
        part.csvColumns(columns);

        // type
        columns.put("type", "simple");

        // visibility
        columns.put("visibility", "Catalog, Search"); // See magmi genericmapper visibility.csv

        // type
        columns.put("status", "Enabled"); // See magmi genericmapper status.csv

        // categories
        StringBuilder categories = new StringBuilder()
                .append("Manufacturer/")
                .append(part.getManufacturer().getName())
                .append(";;Part Type/");
        
        if (part.getPartType().getParent() != null) {
            categories.append(part.getPartType().getParent().getName()).append("/");
        }
        
        categories.append(part.getPartType().getName());

        columns.put("categories", categories.toString());
        
        // Interchanges / TI Interchanges
        columns.put("interchanges", StringUtils.join(interchanges, ','));
        columns.put("ti_part_sku", StringUtils.join(tiInterchanges, ','));


        // Images
        if (!imageFiles.isEmpty()) {

            // Get the first image
            Iterator<String> it = imageFiles.iterator();
            String firstImage = it.next();

            columns.put("image", firstImage);

            // Additional images
            StringBuilder galleryString = new StringBuilder();
            while (it.hasNext()) {
                String additionalImage = it.next();

                // Add a separator if this isn't the first additional image
                if (galleryString.length() > 0) {
                    galleryString.append(';');
                }

                // Add the filename
                galleryString.append(additionalImage);
            }

            // Add the column
            columns.put("media_gallery", galleryString.toString());
        }
        
        columns.put("turbo_model", org.apache.commons.lang3.StringUtils.join(turboModel, ','));
        columns.put("turbo_type", org.apache.commons.lang3.StringUtils.join(turboType, ','));

        columns.put("finder:" + FINDER_ID_TURBO, StringUtils.join(finderTurbo, "||"));

        columns.put("finder:" + FINDER_ID_APPLICATION, StringUtils.join(finderApplication, "||"));

        columns.put("bill_of_materials", bom.toString());
        
        
        
//        // Backplate
//        if (getSealType() != null) {
//            columns.put("seal_type", org.apache.commons.lang.ObjectUtils.toString(getSealType().getName()));
//        }
//
//        columns.put("overall_diameter", org.apache.commons.lang.ObjectUtils.toString(getOverallDiameter()));
//        columns.put("compressor_wheel_diameter", org.apache.commons.lang.ObjectUtils.toString(getCompressorWheelDiameter()));
//        columns.put("piston_ring_diameter", org.apache.commons.lang.ObjectUtils.toString(getPistonRingDiameter()));
//        columns.put("compressor_housing_diameter", org.apache.commons.lang.ObjectUtils.toString(getCompressorHousingDiameter()));
//        columns.put("secondary_diameter", org.apache.commons.lang.ObjectUtils.toString(getSecondaryDiameter()));
//        columns.put("overall_height", org.apache.commons.lang.ObjectUtils.toString(getOverallHeight()));
//        columns.put("style_compressor_wheel", org.apache.commons.lang.ObjectUtils.toString(getStyleCompressorWheel()));
//        
//        // Bearing Housing
//        columns.put("oil_inlet", org.apache.commons.lang.ObjectUtils.toString(getOilInlet()));
//        columns.put("oil_outlet", org.apache.commons.lang.ObjectUtils.toString(getOilOutlet()));
//        columns.put("oil", org.apache.commons.lang.ObjectUtils.toString(getOil()));
//        columns.put("outlet_flange_holes", org.apache.commons.lang.ObjectUtils.toString(getOutletFlangeHoles()));
//        columns.put("water_ports", org.apache.commons.lang.ObjectUtils.toString(getWaterPorts()));
//        columns.put("design_features", org.apache.commons.lang.ObjectUtils.toString(getDesignFeatures()));
//        columns.put("bearing_type", org.apache.commons.lang.ObjectUtils.toString(getBearingType()));
//
//        if (getCoolType() != null) {
//            columns.put("cool_type", org.apache.commons.lang.ObjectUtils.toString(getCoolType().getName()));
//        }
//        
//        
//        // Bearing Spacer
//        columns.put("outside_diameter_min", org.apache.commons.lang.ObjectUtils.toString(getOutsideDiameterMin()));
//        columns.put("outside_diameter_max", org.apache.commons.lang.ObjectUtils.toString(getOutsideDiameterMax()));
//        columns.put("inside_diameter_min", org.apache.commons.lang.ObjectUtils.toString(getInsideDiameterMin()));
//        columns.put("inside_diameter_max", org.apache.commons.lang.ObjectUtils.toString(getInsideDiameterMax()));
//
//        if (getStandardSize() != null) {
//            columns.put("standard_size_id", org.apache.commons.lang.ObjectUtils.toString(getStandardSize().getId()));
//        }
//
//        if (getOversize() != null) {
//            columns.put("oversize_id", org.apache.commons.lang.ObjectUtils.toString(getOversize().getId()));
//        }
//        
//        // Compressor Wheel
//        
//        columns.put("inducer_oa", ObjectUtils.toString(getInducerOa()));
//        columns.put("tip_height_b", org.apache.commons.lang.ObjectUtils.toString(getTipHeightB()));
//        columns.put("exducer_oc", org.apache.commons.lang.ObjectUtils.toString(getExducerOc()));
//        columns.put("hub_length_d", org.apache.commons.lang.ObjectUtils.toString(getHubLengthD()));
//        columns.put("bore_oe", org.apache.commons.lang.ObjectUtils.toString(getBoreOe()));
//        columns.put("number_of_blades", org.apache.commons.lang.ObjectUtils.toString(getNumberOfBlades()));
//        columns.put("application", org.apache.commons.lang.ObjectUtils.toString(getApplication()));
//        
//        // Gasket
//        if (getGasketType() != null) {
//            columns.put("gasket_type", org.apache.commons.lang.ObjectUtils.toString(getGasketType().getName()));
//        }
//        
//        // Heatshield
//        columns.put("overall_diameter", org.apache.commons.lang.ObjectUtils.toString(getOverallDiameter()));
//        columns.put("inside_diameter", org.apache.commons.lang.ObjectUtils.toString(getInsideDiameter()));
//        columns.put("inducer_diameter", org.apache.commons.lang.ObjectUtils.toString(getInducerDiameter()));
//        
//        // Journal Bearing
//        columns.put("outside_diameter_min", org.apache.commons.lang.ObjectUtils.toString(getOutsideDiameterMin()));
//        columns.put("outside_diameter_max", org.apache.commons.lang.ObjectUtils.toString(getOutsideDiameterMax()));
//        columns.put("inside_diameter_min", org.apache.commons.lang.ObjectUtils.toString(getInsideDiameterMin()));
//        columns.put("inside_diameter_max", org.apache.commons.lang.ObjectUtils.toString(getInsideDiameterMax()));
//
//        if (getStandardSize() != null) {
//            columns.put("standard_size_id", org.apache.commons.lang.ObjectUtils.toString(getStandardSize().getId()));
//        }
//
//        if (getOversize() != null) {
//            columns.put("oversize_id", org.apache.commons.lang.ObjectUtils.toString(getOversize().getId()));
//        }
//        
//        // Kit
//        if (kitType != null) {
//            columns.put("kit_type", kitType.getName());
//        }
//        
//        // Piston Ring
//        columns.put("outside_dim_min", org.apache.commons.lang.ObjectUtils.toString(getOutsideDiameterMin()));
//        columns.put("outside_dim_max", org.apache.commons.lang.ObjectUtils.toString(getOutsideDiameterMax()));
//        columns.put("width_min", org.apache.commons.lang.ObjectUtils.toString(getWidthMin()));
//        columns.put("width_max", org.apache.commons.lang.ObjectUtils.toString(getWidthMax()));
//        columns.put("i_gap_min", org.apache.commons.lang.ObjectUtils.toString(getInstalledGapMin()));
//        columns.put("i_gap_max", org.apache.commons.lang.ObjectUtils.toString(getInstalledGapMax()));
//        
//        // Turbine Wheel
//        columns.put("exduce_oa", org.apache.commons.lang.ObjectUtils.toString(getExducerDiameterA()));
//        columns.put("tip_height_b", org.apache.commons.lang.ObjectUtils.toString(getTipHeightB()));
//        columns.put("inducer_oc", org.apache.commons.lang.ObjectUtils.toString(getInducerDiameterC()));
//        columns.put("journal_od", org.apache.commons.lang.ObjectUtils.toString(getJournalDiameterD()));
//        columns.put("stem_oe", org.apache.commons.lang.ObjectUtils.toString(getStemDiameterE()));
//        columns.put("shaft_thread_f", org.apache.commons.lang.ObjectUtils.toString(getShaftThreadF()));
//        columns.put("number_of_blades", org.apache.commons.lang.ObjectUtils.toString(getNumberOfBlades()));
//        
//        // Turbo
//        if (getTurboModel() != null) {
//            columns.put("turbo_model_name", org.apache.commons.lang.ObjectUtils.toString(getTurboModel().getName()));
//        }
//
//        if (getCoolType() != null) {
//            columns.put("cool_type", org.apache.commons.lang.ObjectUtils.toString(getCoolType().getName()));
//        }
        
        
        return columns;
    }
}
