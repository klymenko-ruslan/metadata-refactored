package com.turbointernational.metadata.sync;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.magento.rest.MagentoRest;
import com.turbointernational.metadata.magento.soap.MagentoSoap;
import java.util.Date;
import java.util.List;
import net.sf.jsog.JSOG;
import net.sf.jsog.client.UrlBuilder;
import org.apache.commons.lang3.StringUtils;
import org.scribe.model.Token;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/other/sync")
public class MagentoSync {
    
    //
    // REST
    //
    public static final String BASE_URL = "http://ec2-184-73-132-150.compute-1.amazonaws.com/";
    
    String restApiKey = "4wy124nq41hquo0z3hm8mm3uzaqrmte2";
    
    String restApiSecret = "m41dv0jtygqrl3cne1731pe5pglh5pel";
    
    String restToken = "8zp75lkc8zrm04jscylo0s3nbojae057";
    
    String restTokenSecret = "4y791olryopwe8cfebxn6jij8apoerck";
    
    UrlBuilder restApiUrl = new UrlBuilder(BASE_URL + "api/rest/{0}").set("type", "rest");
    
    //
    // SOAP
    //
    
    String soapApiUrl = BASE_URL + "index.php/api/soap";
    
    String soapApiUsername = "metadata";
    
    String soapApiPassword = "9l8t6QCihtX4";
    
    protected MagentoRest rest() {
        return new MagentoRest(
                restApiUrl,
                new Token(restToken, restTokenSecret),
                restApiKey, restApiSecret);
    }

    protected MagentoSoap soap() {
        return new MagentoSoap(soapApiUsername, soapApiPassword, soapApiUrl);
    }

    public void synchronize(Date lastUpdated) {
        int pageSize = 1000;
        int page = 0;
        List<Part> parts;
        do {
            parts = Part.getPartsUpdatedAfter(lastUpdated, page * pageSize, pageSize);

            for (Part part : parts) {
                
                // Remove part if non-existent or inactive
                if (part.getInactive() || part == null) {
                    deleteProduct(part);
                
                    // Otherwise update part
                } else {
                    updateProduct(part);
                }
            }

            page++;

        } while (parts.size() >= pageSize);
    }
    
    @RequestMapping(value="/part", headers = "Accept=application/json")
    @ResponseBody
    public String addPart(@RequestParam long id) {
        return addProduct(Part.findPart(id));
    }
    
    @RequestMapping(value="/parts", headers = "Accept=application/json")
    @ResponseBody
    public void addAllParts() {
        for (Part part : Part.findAll()) {
            addProduct(part);
        }
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
            rest().updateProduct(originalProduct.get("id").getIntegerValue(), partJsog);
        }

    }

    private String addProduct(Part part) {
        
        // Serialize to JSOG as in part update
        JSOG partJsog = part.toJsog()
        .put("sku", part.getId())
        .put("attribute_set_id", part.getPartType().getMagentoAttributeSetId())
        .put("short_description", part.getDescription()) // Required
        .put("type_id","simple") // Required
        .put("status", "1") // Required
        .put("visibility","4") // Required
        ;
        
        if (StringUtils.isBlank(part.getName())) {
            partJsog.put("name", part.getManufacturer().getName() + " " + part.getManufacturerPartNumber());
        }
        
        if (StringUtils.isBlank(part.getDescription())) {
            partJsog.put("description", "no description given");
        }
                
//        .put("name", "my name") // Required
//        .put("description","my desc") // Required
                                
                                
//        .put("tax_class_id","0") // Required
//        .put("weight", "0.0") // Required
//        .put("price", "42.00") // Required

        
        // POST request to create product
        return rest().createProduct(partJsog).toString();
    }

    private void deleteProduct(Part part) {
        // Get the part ID
        int partId = part.getMagentoProductId();
        // Send DELETE request for the part
        rest().deleteProduct(partId);
    }

    public JSOG getProduct(Part part) {
        return rest().getProductById(part.getMagentoProductId());
    }
}
