package com.turbointernational.metadata.magmi.dto;

import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.ProductImage;
import com.turbointernational.metadata.domain.part.types.Turbo;
import com.turbointernational.metadata.util.ImageResizer;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.StringUtils;

import java.util.*;

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
    
    private Boolean hasTiChra = false;
    
    private final List<MagmiBomItem> bom = new ArrayList<MagmiBomItem>();
    
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
    
    public boolean hasTiChra() {
        return hasTiChra;
    }

    public void setHasTiChra(Boolean hasTiChra) {
        this.hasTiChra = hasTiChra;
    }
    
    public final void addApplication(MagmiApplication application) {
        if (StringUtils.isNotEmpty(application.getFinder())) {
            finderApplication.add(application.getFinder());
        }

        if (StringUtils.isNotEmpty(application.getDetail())) {
            applicationDetail.add(application.getDetail());
        }
    }

    public final void addTurbo(MagmiTurbo magmiTurbo) {

        if (StringUtils.isNotEmpty(magmiTurbo.getTurboType())) {
            turboType.add(magmiTurbo.getTurboType());
        }

        if (StringUtils.isNotEmpty(magmiTurbo.getPartTurboType())) {
            turboType.add(magmiTurbo.getPartTurboType());
        }

        if (StringUtils.isNotEmpty(magmiTurbo.getTurboModel())) {
            turboModel.add(magmiTurbo.getTurboModel());
        }

        if (StringUtils.isNotEmpty(magmiTurbo.getFinder())) {
            finderTurbo.add(magmiTurbo.getFinder());
        }
    }

    public List<MagmiBomItem> getBom() {
        return bom;
    }
    
    public void addImageId(Long imageId) {
        imageIds.add(imageId);
    }

    public void addInterchange(MagmiInterchange interchange) {
        interchanges.add(interchange.getInterchangePartSku());

        // TI interchanges
        if (Manufacturer.TI_ID.equals(interchange.getInterchangePartManufacturerId())) {
            tiInterchanges.add(interchange.getInterchangePartSku());
            
            // Save the part number if this is the first interchange part
            if (tiInterchanges.size() == 1) {
                tiPartNumber = interchange.getInterchangePartNumber();
            }
        }
    }
    
    public void addServiceKit(MagmiServiceKit sk) {
        JSOG jsog = JSOG.object()
                        .put("sku", sk.getKitSku())
                        .put("partNumber", sk.getKitPartNumber())
                        .put("description", sk.getDescription())
                        .put("tiSku", sk.getTiKitSku())
                        .put("tiPartNumber", sk.getTiKitPartNumber());
        
        serviceKits.add(jsog);
    }
    
    public void addUsage(MagmiUsage usage) {
        
        // Look for a previous usage we need to add to
        JSOG jsogUsage = usages.get(usage.getSku().toString());
        if (jsogUsage.isNull()) {
            jsogUsage.put("sku", usage.getSku());
            jsogUsage.put("manufacturer", usage.getManufacturer());
            jsogUsage.put("partNumber", usage.getPartNumber());
            jsogUsage.put("tiSku", usage.getTiSku());
            jsogUsage.put("tiPartNumber", usage.getTiPartNumber());
            jsogUsage.put("partType", usage.getPartType());
            jsogUsage.put("turboType", usage.getTurboType());
            jsogUsage.put("turboPartNumbers", JSOG.array());
        }
        
        if (StringUtils.isNotBlank(usage.getTurboPartNumber())) {
            jsogUsage.get("turboPartNumbers").add(usage.getTurboPartNumber());
        }
    }

    public final Map<String, String> getCsvColumns() {

        // CSV column map
        Map<String, String> columns = new HashMap<String, String>();
        
        // Part data
        part.csvColumns(columns);

        // description
        columns.put("type", "simple");
        
        // OEM SKU (For showing OEM p/n in cart)
        if (Manufacturer.TI_ID.equals(getManufacturerId())) {
            
            // Default empty value
            // See: http://sourceforge.net/apps/mediawiki/magmi/index.php?title=Custom_Options
            columns.put("OEMSKU:field:0", ":"); 
        }

        // visibility
        // magento/magmi/plugins/base/itemprocessors/genericmapper/mappings/default/visibility.csv
        columns.put("visibility", "Catalog, Search");

        // status
        // magento/magmi/plugins/base/itemprocessors/genericmapper/mappings/default/status.csv
        if (part.getInactive()) {
            columns.put("status", "Disabled");
        } else {
            columns.put("status", "Enabled");
        }

        columns.put("categories", getCategoriesString());
        
        // Interchanges / TI Interchanges
        columns.put("interchanges", StringUtils.join(interchanges, ','));
        columns.put("ti_part_sku", StringUtils.join(tiInterchanges, ','));
        columns.put("ti_part_number", tiPartNumber);
        columns.put("has_ti_chra", Boolean.toString(hasTiChra));
        
        // Don't overwrite turbo values
        if (!(part instanceof Turbo)) {
            columns.put("turbo_model", StringUtils.join(turboModel, ','));
            columns.put("turbo_type", StringUtils.join(turboType, ','));
        }
        
        columns.put("application_detail", StringUtils.join(applicationDetail, "||"));

        columns.put("bill_of_materials", MagmiBomItem.toJsonArray(bom));
        
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
