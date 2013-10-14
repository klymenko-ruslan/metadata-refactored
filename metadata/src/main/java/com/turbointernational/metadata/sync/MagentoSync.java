package com.turbointernational.metadata.sync;

import com.google.code.magja.model.product.ProductLink;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.magento.rest.MagentoRest;
import com.turbointernational.metadata.magento.soap.MagentoSoap;
import java.util.Iterator;
import java.util.Set;
import net.sf.jsog.JSOG;
import net.sf.jsog.client.UrlBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.scribe.model.Token;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import static com.google.code.magja.model.product.ProductLink.LinkType.*;
@Controller
@RequestMapping("/other/sync")
public class MagentoSync {
    
    private static JSOG partToProduct(Part part) {
        
        // Serialize to JSOG
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
        
        return partJsog;
    }
    
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
    
    @Transactional
    @RequestMapping(value="/part")
    @ResponseBody
    public String syncPart(@RequestParam long id) {
        Part part = Part.findPart(id);
        
        // Get the existing product if it exists
        JSOG productJsog = rest().getProductBySku(part.getId().toString());
        
        // Create or update the product
        if (productJsog == null) {
            
            // Create the product
            productJsog = partToProduct(part);
            int productId = rest().createProduct(productJsog);
            part.setMagentoProductId(productId);
            part.merge();
            part.flush();
            
        } else {
        
            // Set the magento product ID it hasn't been
            if (part.getMagentoProductId() == null) {
                int productId = productJsog.get("entity_id").getIntegerValue();
                part.setMagentoProductId(productId);
                part.merge();
                part.flush();
            }
            
            // Get the current product version
            Integer productVersion = null;
            if (productJsog.hasKey("version")) {
                productJsog.get("version").getIntegerValue();
            }

            // Update if it's different from the current part version
            if (!ObjectUtils.equals(part.getVersion(), productVersion)) {
                productJsog.merge(partToProduct(part));
                rest().updateProduct(part.getMagentoProductId(), productJsog);
            }
        }
        
        // Update the product category
        setProductCategory(part);
        
        // Update interchange parts
        updateProductLinks(part);
        
        return part.getMagentoProductId().toString();
    }

    public JSOG getProduct(Part part) {
        return rest().getProductById(part.getMagentoProductId());
    }
    
    private void setProductCategory(Part part) {
        Integer partMagentoCategoryId = part.getPartType().getMagentoCategoryId();
        
        // Handle null category ID
        if (partMagentoCategoryId == null) {
            throw new IllegalStateException("No magento category id assigned for part #" + part.getId());
        }
        
        // Get the categories currently on the product
        Set<Integer> currentCategories = rest().getCategories(part.getMagentoProductId());
        
        // Delete any wrong ones
        Iterator<Integer> categoryIt = currentCategories.iterator();
        while (categoryIt.hasNext()) {
            Integer categoryId = categoryIt.next();
            
            if (ObjectUtils.notEqual(partMagentoCategoryId, categoryId)) {
                rest().deleteCategory(part.getMagentoProductId(), categoryId);
                categoryIt.remove();
            }
        }
        
        // Add the category if we need to
        if (currentCategories.isEmpty()) {
            rest().addCategory(part.getMagentoProductId(), partMagentoCategoryId);
        }
    }
    
    private void updateProductLinks(Part part) {
        Manufacturer mfrTi = Manufacturer.findByName("Turbo International");
        
        Set<Part> interchangeParts = part.getInterchangePartsForManufacturer(mfrTi);
        
        // Get the existing product links
        linkLoop: for (ProductLink link : soap().getProductLinks(part.getMagentoProductId())) {
            System.out.println(ToStringBuilder.reflectionToString(link));
            switch (link.getLinkType()) {
                
                // Cross-sells are used for interchangeable parts
                case CROSS_SELL: {
                    
                    // Remove parts from interchangeParts if a link already exists
                    Iterator<Part> interchangeIt = interchangeParts.iterator();
                    while (interchangeIt.hasNext()) {
                        Part interchangePart = interchangeIt.next();
                        
                        if (ObjectUtils.equals(interchangePart.getId().toString(), link.getSku())) {
                            interchangeIt.remove();
                            break linkLoop;
                        }
                    }
                    
                    // The link is no longer valid
                    soap().deleteProductLink(part.getMagentoProductId(), link.getId(), ProductLink.LinkType.CROSS_SELL);
                }
            }
        }
        
        // Create any missing interchange links
        for (Part interchangePart : interchangeParts) {
            soap().addProductLink(part.getMagentoProductId(), interchangePart.getMagentoProductId(), CROSS_SELL);
        }
    }
    
//    @RequestMapping(value="/parts", headers = "Accept=application/json")
//    @ResponseBody
//    public void addAllParts() {
//        for (Part part : Part.findAll()) {
//            addProduct(part);
//        }
//    }
    
    

//    public void synchronize(Date lastUpdated) {
//        int pageSize = 1000;
//        int page = 0;
//        List<Part> parts;
//        do {
//            parts = Part.getPartsUpdatedAfter(lastUpdated, page * pageSize, pageSize);
//
//            for (Part part : parts) {
//                
//                // Remove part if non-existent or inactive
//                if (part.getInactive() || part == null) {
//                    deleteProduct(part);
//                
//                    // Otherwise update part
//                } else {
//                    updateProduct(part);
//                }
//            }
//
//            page++;
//
//        } while (parts.size() >= pageSize);
//    }
}
