package com.turbointernational.metadata.sync;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.magento.rest.MagentoRest;
import com.turbointernational.metadata.magento.soap.MagentoSoap;
import java.util.Date;
import java.util.List;
import net.sf.jsog.JSOG;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/other/sync")
public class MagentoSync {

    MagentoRest rest;
    MagentoSoap soap;

    public void synchronize(Date lastUpdated) {
        int pageSize = 1000;
        int page = 0;
        List<Part> parts;
        do {
            parts = Part.getPartsUpdatedAfter(lastUpdated, page * pageSize, pageSize);

            for (Part part : parts) {
                
                // Remove part if non-existent or inactive
                if (part.getInactive() || part == null) {
                    deletePart(part);
                
                    // Otherwise update part
                } else {
                    updateProduct(part);
                }
            }

            page++;

        } while (parts.size() >= pageSize);
    }

    
    private void updateProduct(Part part) {

        // Get the part as JSOG, delete/transform the values for Magento
        JSOG partJsog = part.toJsog();
        partJsog.remove("id");

        // Get the current product
        JSOG originalProduct = getProduct(part);

        // Merge in the current part's properties
        JSOG updatedProduct = originalProduct.clone().merge(partJsog);

        // If they're different, update the part
        if (!originalProduct.equals(updatedProduct)) {
            rest.updateProduct(originalProduct.get("id").getIntegerValue(), partJsog);
        }

    }

    private void addPart(Part part) {
        // Serialize to JSOG as in part update
        JSOG partJsog = part.toJsog();
        // POST request to create product
        rest.createProduct(partJsog);
    }

    private void deletePart(Part part) {
        // Get the part ID
        int partId = part.getMagentoProductId();
        // Send DELETE request for the part
        rest.deleteProduct(partId);
    }

    private void addPart(Part part) {
        // Serialize to JSOG as in part update
        JSOG partJsog = part.toJsog();
        // POST request to create product
        rest.createProduct(partJsog);
    }

    private void deletePart(Part part) {
        // Get the part ID
        int partId = part.getMagentoProductId();
        // Send DELETE request for the part
        rest.deleteProduct(partId);
    }

    private void addProduct(Part part) {
        
        // Serialize to JSOG as in part update
        JSOG partJsog = part.toJsog();
        
        // POST request to create product
        rest.createProduct(partJsog);
    }

    private void deletePart(Part part) {
        // Get the part ID
        int partId = part.getMagentoProductId();
        // Send DELETE request for the part
        rest.deleteProduct(partId);
    }

    private void addPart(Part part) {
        // Serialize to JSOG as in part update
        JSOG partJsog = part.toJsog();
        // POST request to create product
        rest.createProduct(partJsog);
    }

    private void deletePart(Part part) {
        // Get the part ID
        int partId = part.getMagentoProductId();
        // Send DELETE request for the part
        rest.deleteProduct(partId);
    }

    public JSOG getProduct(Part part) {
        return rest.getProductById(part.getMagentoProductId());
    }
}
