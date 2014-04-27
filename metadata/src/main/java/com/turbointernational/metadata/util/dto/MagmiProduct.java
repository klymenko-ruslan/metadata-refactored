package com.turbointernational.metadata.util.dto;

import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.ProductImage;
import com.turbointernational.metadata.util.ImageResizer;
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
    
    private final Part part;
    
    private final TreeSet<Long> imageIds = new TreeSet<Long>();

    private final Set<String> turboType = new TreeSet<String>();

    private final Set<String> turboModel = new TreeSet<String>();

    private final Set<String> finderTurbo = new TreeSet<String>();

    /**
     * make!!year!!model
     */
    private final Set<String> finderApplication = new TreeSet<String>();

    /**
     * make!!model!!year!!engine!!fuel
     */
    private final Set<String> applicationDetail = new TreeSet<String>();

    private final Set<Long> interchanges = new TreeSet<Long>();

    private final Set<Long> tiInterchanges = new TreeSet<Long>();
    
    private String tiPartNumber = "";
    
    private final JSOG bom = JSOG.array();
    
    private final JSOG serviceKits = JSOG.array();
    
    private final JSOG usages = JSOG.object();

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

    private String getCategoriesString() {
        
        // categories
        StringBuilder categories = new StringBuilder()
            .append("Manufacturer/")
            .append(part.getManufacturer().getName())
            .append(";;Part Type/");
        
        if (part.getPartType().getParent() != null) {
            categories.append(part.getPartType().getParent().getName()).append("/");
        }
        
        categories.append(part.getPartType().getName());
        
        return categories.toString();
    }
    
    public boolean hasTiPart() {
        return getManufacturerId() == Manufacturer.TI_ID
            || StringUtils.isNotBlank(tiPartNumber);
    }
    
    public final void addApplication(MagmiApplication application) {
        if (StringUtils.isNotEmpty(application.finder)) {
            finderApplication.add(application.finder);
        }

        if (StringUtils.isNotEmpty(application.detail)) {
            applicationDetail.add(application.detail);
        }
    }

    public final void addTurbo(MagmiTurbo magmiTurbo) {

        if (StringUtils.isNotEmpty(magmiTurbo.turboType)) {
            turboType.add(magmiTurbo.turboType);
        }

        if (StringUtils.isNotEmpty(magmiTurbo.partTurboType)) {
            turboType.add(magmiTurbo.partTurboType);
        }

        if (StringUtils.isNotEmpty(magmiTurbo.turboModel)) {
            turboModel.add(magmiTurbo.turboModel);
        }

        if (StringUtils.isNotEmpty(magmiTurbo.finder)) {
            finderTurbo.add(magmiTurbo.finder);
        }
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
    
    public void addImageId(Long imageId) {
        imageIds.add(imageId);
    }

    public void addInterchange(MagmiInterchange interchange) {
        interchanges.add(interchange.getInterchangePartSku());

        // TI interchanges
        if (interchange.getInterchangePartManufacturerId() == Manufacturer.TI_ID) {
            tiInterchanges.add(interchange.getInterchangePartSku());
            
            // Save the part number if this is the first interchange part
            if (tiInterchanges.size() == 1) {
                tiPartNumber = interchange.getInterchangePartNumber();
            }
        }
    }
    
    public void addServiceKit(MagmiServiceKit sk) {
        JSOG jsog = JSOG.object()
                        .put("sku", sk.kitSku)
                        .put("part_number", sk.kitPartNumber)
                        .put("type", sk.type)
                        .put("ti_part_sku", sk.tiKitSku)
                        .put("ti_part_number", sk.tiKitPartNumber);
        
        serviceKits.add(jsog);
    }
    
    public void addUsage(MagmiUsages usage) {
        
        // Look for a previous usage we need to add to
        JSOG jsogUsage = usages.get(usage.sku.toString());
        if (jsogUsage.isNull()) {
            jsogUsage.put("sku", usage.sku);
            jsogUsage.put("manufacturer", usage.manufacturer);
            jsogUsage.put("partNumber", usage.partNumber);
            jsogUsage.put("tiSku", usage.tiSku);
            jsogUsage.put("tiPartNumber", usage.tiPartNumber);
            jsogUsage.put("partType", usage.partType);
            jsogUsage.put("turboType", usage.turboType);
        }
        
        jsogUsage.get("turboPartNumbers").add(usage.turboPartNumber);
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

        columns.put("categories", getCategoriesString());
        
        // Interchanges / TI Interchanges
        columns.put("interchanges", StringUtils.join(interchanges, ','));
        columns.put("ti_part_sku", StringUtils.join(tiInterchanges, ','));
        columns.put("ti_part_number", tiPartNumber);
        
        columns.put("turbo_model", StringUtils.join(turboModel, ','));
        columns.put("turbo_type", StringUtils.join(turboType, ','));
        
        columns.put("applicationDetail", StringUtils.join(applicationDetail, "||"));

        columns.put("bill_of_materials", bom.toString());
        
        columns.put("service_kits", serviceKits.toString());
        
        columns.put("where_used", usages.toString());
        
        return columns;
    }
    
    public final void csvFinderColumns(Map<String, String> columns, String applicationId, String turboId) {

        columns.put("finder:" + applicationId, StringUtils.join(finderApplication, "||"));

        columns.put("finder:" + turboId, StringUtils.join(finderTurbo, "||"));
    }

    public void csvImageColumns(Map<String, String> columns, ImageResizer resizer) {
        
        // Stop now if there aren't any images
        if (imageIds.isEmpty()) {
            return;
        }
            
        // Get the first image
        Iterator<Long> it = imageIds.iterator();
        Long firstImage = it.next();

        columns.put("image", ProductImage.getResizedFilename(part.getId(), firstImage, 1000));
        columns.put("small_image", ProductImage.getResizedFilename(part.getId(), firstImage, 135));
        columns.put("thumbnail", ProductImage.getResizedFilename(part.getId(), firstImage, 50));

        // Additional images
        StringBuilder galleryString = new StringBuilder();
        while (it.hasNext()) {
            Long additionalImage = it.next();
            

            // Add a separator if this isn't the first additional image
            if (galleryString.length() > 0) {
                galleryString.append(';');
            }

            // Add the filename
            galleryString.append(ProductImage.getResizedFilename(part.getId(), additionalImage, 1000));
        }

        // Add the column
        columns.put("media_gallery", galleryString.toString());
    }

}
