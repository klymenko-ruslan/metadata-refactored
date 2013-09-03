package com.turbointernational.metadata.sync;

import com.turbointernational.metadata.domain.part.Part;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jsog.JSOG;
import net.sf.jsog.client.JsogClient;
import org.apache.commons.lang3.ObjectUtils;

/**
 *
 */
public class MagentoSync {
    
    public void synchronize(Date lastUpdated) {
        int pageSize = 1000;
        int page = 0;
        List<Part> parts = null;
        do {
            parts = Part.getPartsUpdatedAfter(lastUpdated, page * pageSize, pageSize);
            
            for (Part part: parts) {
                updatePart(part);
            }
            
            page++;
            
        } while (parts.size() >= pageSize);
        
    }

    private void updatePart(Part part) {
        JSOG partJsog = JSOG.object();
        part.toJson(partJsog);
        
        // Get the product via REST.
        JSOG originalProduct = getProduct(part.getId());
        JSOG updatedProduct = originalProduct.clone();
        // Get a list of brands via REST
        
        JSOG brands = getBrands(); // WRITE ME
        if (ObjectUtils.notEqual(originalProduct.get("brand_id").getLongValue(), part.getManufacturer().getId())) {
            updatedProduct.put("brand_id", part.getManufacturer().getId());
        }
        if (ObjectUtils.notEqual(originalProduct.get("name").getStringValue(), part.getManufacturer().getName())) {
            updatedProduct.put("name", part.getManufacturer().getName());
        }
        if (ObjectUtils.notEqual(originalProduct.get("description").getStringValue(), part.getManufacturer().getDescription())) {
            updatedProduct.put("description", part.getManufacturer().getDescription());
        }
        if (ObjectUtils.notEqual(originalProduct.get("manfr_part_num").getStringValue(), part.getManufacturer().getManufacturerPartNumber())) {
            updatedProduct.put("manfr_part_num", part.getManufacturer().getManufacturerPartNumber());
        }
        if (ObjectUtils.notEqual(originalProduct.get("ti_part_num").getStringValue(), part.getManufacturer().getTiPartNumber())) {
            updatedProduct.put("ti_part_num", part.getManufacturer().getTiPartNumber());
        }
        
        JSOG currentCustomFields = getCustomFields(part.getId());
        
        Map<String, String> partToProduct = new HashMap(); // TO DO: add originalProduct in here
        // Is something supposed to be added into partToProduct here?
        // I'm guessing it is supposed to be the information from the originalProduct
        for (String key : partJsog.keySet()) {
            partToProduct.put(key, originalProduct.get(key));
        }
        for (String key : customFields.keySet()) {
            if () { // Compare key to list of necessary information
                partToProduct.put(key, originalProduct.get(key));
            }
            // Doesn't exist, add it
            // Does exist, update
            // Does exist, delete it
        }
        
        // Get the brand out of the brands list, if it doesn't exist call createBrand()
        // If it does exist, compare the brand IDs and if they're different, update the product
        if (brand  == originalProduct.get("brand_id")) {
            // Check differences and update
            // updateBrand(id, brand);
        } else {
            // createBrand(id, brand);
        }
        
        // Check what values are different based on the JSOG, check the part class for details.
        
        // Update if any values have changed. (PUT = update) https://developer.bigcommerce.com/docs/api/v2/resources/brands#put-brandsidjson
                
        
        // Get the custom values on the product
        
        // Update any that are different, added, or removed
        
        // Make sure the category is setup properly
        
          }
    public JSOG getProduct(long id) {
        return client.getJsog("/products/" + id + ".json");
    }
    
    public JSOG getBrands() {
        return client.getJsog("/brands.json");
    }
    
   /* public JSOG postBrands(String name, String imageFile) {
        return client.postJsog("/brands" + id + ".json");
    } */
    
    public JSOG getBrand(long id) {
        return client.getJsog("/brands/" + id + ".json");
    }
    /*
    public JSOG updateBrand(long id, JSOG brand) {
        return client.putJsog("/brands/" + id + ".json");
    }
    
    public JSOG createBrand(long id, JSOG brand) {
        return client.postJsog("/brands.json", brand);  // Add + id + ?
    }
    */