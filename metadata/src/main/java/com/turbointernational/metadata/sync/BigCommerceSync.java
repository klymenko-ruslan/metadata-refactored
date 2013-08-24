package com.turbointernational.metadata.sync;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PistonRing;
import java.util.Date;
import java.util.List;
import net.sf.jsog.JSOG;
import net.sf.jsog.client.JsogClient;

/**
 *
 * @author Akouvi
 */
public class BigCommerceSync {
    
    JsogClient client;
    
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
        
        if (part instanceof PistonRing) {
            piston ring specific stuff
        }
        JSOG partJsog = JSOG.object();
        part.toJson(partJsog);
        
        // Get a list of brands via REST
        JSOG brands = getBrands(); // WRITE ME
        
        // Get the product via REST.
        JSOG originalProduct = getProduct(long id);
        
        // Get the brand out of the brands list, if it doesn't exist call createBrand()
        // If it does exist, compare the brand IDs and if they're different, update the product
        if (brand  == originalProduct.get("brand_id"))
        
        // Check what values are different based on the JSOG, check the part class for details.
        
        // Update if any values have changed. (PUT = update) https://developer.bigcommerce.com/docs/api/v2/resources/brands#put-brandsidjson
                
        
        // Get the custom values on the product
        
        // Update any that are different, added, or removed
        
        // Make sure the category is setup properly
        
        // 
        
    }
    
    public JSOG getBrand(long id) {
        return client.getJsog("/brands/" + id + ".json");
    }
    
    public JSOG updateBrand(long id, JSOG brand) {
        return client.putJsog("/brands/" + id + ".json");
    }
    
    public JSOG createBrand(long id, JSOG brand) {
        return client.postJsog("/brands.json", brand);
    }
}
