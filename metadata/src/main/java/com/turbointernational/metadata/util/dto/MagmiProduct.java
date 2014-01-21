package com.turbointernational.metadata.util.dto;

import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.types.Turbo;
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
        
        // Turbo-specifics
        if (StringUtils.equals("Turbo", part.getPartType().getTypeName())) {
            Turbo turbo = (Turbo) part;
            
            // Turbo type/model
            turboModel.add(turbo.getTurboModel().getName());
            turboType.add(turbo.getTurboModel().getTurboType().getName());
            
            // Turbo finder
            finderTurbo.add(
                    turbo.getManufacturer().getName()
                    + "!!" + turbo.getTurboModel().getName()
                    + "!!" + turbo.getTurboModel().getTurboType().getName());
            
            // Application finder
            for (CarModelEngineYear application : turbo.getCars()) {
                
                // Make sure each component exists
                if (application.getModel() != null
                        && application.getModel().getMake() != null
                        && application.getYear() != null) {
                    finderApplication.add(
                        application.getModel().getMake().getName()
                        + "!!" + application.getYear().getName()
                        + "!!" + application.getModel().getName());
                }
            }
        }
        
        columns.put("turbo_model", StringUtils.join(turboModel, ','));
        columns.put("turbo_type", StringUtils.join(turboType, ','));

        columns.put("finder:" + FINDER_ID_TURBO, StringUtils.join(finderTurbo, "||"));

        columns.put("finder:" + FINDER_ID_APPLICATION, StringUtils.join(finderApplication, "||"));

        columns.put("bill_of_materials", bom.toString());
        
        return columns;
    }
}
