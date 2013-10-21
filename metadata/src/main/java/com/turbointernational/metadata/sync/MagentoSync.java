package com.turbointernational.metadata.sync;

import com.google.code.magja.model.product.ProductLink;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.magento.rest.MagentoRest;
import com.turbointernational.metadata.magento.soap.MagentoSoap;
import java.util.Iterator;
import java.util.Set;
import net.sf.jsog.JSOG;
import net.sf.jsog.client.UrlBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.scribe.model.Token;
import org.springframework.transaction.annotation.Transactional;
import static com.google.code.magja.model.product.ProductLink.LinkType.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class MagentoSync {
    
    private static final Logger logger = Logger.getLogger(MagentoSync.class.toString());
    
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
    String restApiKey = "4wy124nq41hquo0z3hm8mm3uzaqrmte2";
    
    String restApiSecret = "m41dv0jtygqrl3cne1731pe5pglh5pel";
    
    String restToken = "8zp75lkc8zrm04jscylo0s3nbojae057";
    
    String restTokenSecret = "4y791olryopwe8cfebxn6jij8apoerck";
    
    UrlBuilder restApiUrl = new UrlBuilder("http://ec2-184-73-132-150.compute-1.amazonaws.com/api/rest/{0}").set("type", "rest");
    
    //
    // SOAP
    //
    String soapApiUrl = "http://ec2-184-73-132-150.compute-1.amazonaws.com/index.php/api/soap";
    
    String soapApiUsername = "metadata";
    
    String soapApiPassword = "9l8t6QCihtX4";
    
    protected MagentoRest rest() {
        return new MagentoRest(
                restApiUrl,
                new Token(restToken, restTokenSecret),
                restApiKey, restApiSecret);
    }

    protected MagentoSoap soap() {
        logger.log(Level.INFO, "Creating soap client.");
        return new MagentoSoap(soapApiUsername, soapApiPassword, soapApiUrl);
    }
    
    @Transactional(readOnly = true)
    public int syncPart(long id) {
        logger.log(Level.INFO, "Synchronizing part #{0}", id);
        
        MagentoRest rest = rest();
        try {
            Part part = Part.findPart(id);

            // Get the existing product
            JSOG productJsog = rest.getProductBySku(part.getId().toString());

            // Create or update the product
            if (productJsog == null) {
                logger.log(Level.INFO, "Creating magento product for part #{0}", id);

                // Create the product
                productJsog = partToProduct(part);
                int productId = rest.createProduct(productJsog);
                part.updateMagentoProductId(productId);
                
                logger.log(Level.INFO, "Created magento product #{0}", productId);
            } else {
                logger.log(Level.INFO, "Updating magento product for part #{0}", id);

                // Set the magento product ID it hasn't been
                if (part.getMagentoProductId() == null) {
                    int productId = productJsog.get("entity_id").getIntegerValue();
                    part.updateMagentoProductId(productId);
                }

                // Get the current product version
                Integer productVersion = null;
                if (productJsog.hasKey("version")) {
                    productJsog.get("version").getIntegerValue();
                }

                // Update if it's different from the current part version
                if (!ObjectUtils.equals(part.getVersion(), productVersion)) {
                    productJsog.merge(partToProduct(part));
                    rest.updateProduct(part.getMagentoProductId(), productJsog);
                    logger.log(Level.INFO, "Updating magento product #{0} for part #{1}", new Object[] {part.getMagentoProductId(), id});
                }
                
                logger.log(Level.INFO, "Updated magento product for part #{0}", id);
            }

            // Update the product category
            setProductCategory(part);

            // Update interchange parts
            updateProductLinks(part);
            
            
            logger.log(Level.INFO, "Synchronized part #{0}", id);

            return part.getMagentoProductId();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Failed to synchronize", t);
            throw new RuntimeException(t);
        }
    }

    public JSOG getProduct(Part part) {
        return rest().getProductById(part.getMagentoProductId());
    }
    
    private void setProductCategory(Part part) {
        logger.log(Level.INFO, "Checking product category for part #{0}", part.getId());
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
    
    public static ProductLink getLinkBySku(Collection<ProductLink> links, String sku) {
        for (ProductLink link : links) {
            if (ObjectUtils.equals(sku, link.getSku())) {
                return link;
            }
        }
        
        return null;
    }
    
    public void updateInterchanges(MagentoSoap soap, Part part, Collection<ProductLink> links) {
        logger.log(Level.INFO, "Updating interchanges for part #{0}", part.getId());
        
        // Clone the list of links so we can use it for updating
        links = new ArrayList<ProductLink>(links);
        
        // Add missing links and prepare links for removal
        for (Part interchangePart : part.getTIInterchanges()) {
            ProductLink link = getLinkBySku(links, interchangePart.getId().toString());
            
            // Create the link or remove it from the links list so we don't remove it later
            if (link == null) {
                
                if (interchangePart.getMagentoProductId() != null) {
                logger.log(Level.INFO, "Adding interchange link for part #{0}", part.getId());

                    // Add missing links
                    soap.addProductLink(
                            part.getMagentoProductId(), interchangePart.getId(),
                            RELATED, null, null);
                }
            } else {
                
                // Active link, don't remove
                links.remove(link);
            }
        }
        
        // Delete the links that weren't removed 
        for (ProductLink link : links) {
            logger.log(Level.INFO, "Deleting interchange link for part #{0}", part.getId());
            soap.deleteProductLink(part.getMagentoProductId(), link.getSku(), link.getLinkType());
        }
    }
    
    public void updateBOM(MagentoSoap soap, Part part, Collection<ProductLink> links) {
        logger.log(Level.INFO, "Updating BOM links for part #{0}", part.getId());
        
        // Clone the list of links so we can use it for updating
        links = new ArrayList<ProductLink>(links);
        
        // Add missing links and prepare links for removal
        for (BOMItem item : part.getBom()) {
            logger.log(Level.INFO, "Updating BOM item #{0} for part #{1}", new Object[] {item.getId(), part.getId()});
            
            // If the child doesn't have a magento product ID, skip the link for now
            if (item.getChild().getMagentoProductId() == null) {
                continue;
            }
        
            ProductLink link = getLinkBySku(links, item.getChild().getId().toString());
            
            // Create the link or remove it from the links list so we don't remove it later
            if (link == null) {
                
                // Get the BOM atlernate part if there is one
                Part tiPart = null;
                List<Part> tiAlternates = item.getTIAlternates();
                
                if (!tiAlternates.isEmpty()) {
                    
                    // Get the TI alternate
                    tiPart = tiAlternates.get(0);
                } else {
                    
                    // No BOM alternates, get the TI interchange
                    List<Part> tiInterchanges = item.getChild().getTIInterchanges();
                    
                    if (!tiInterchanges.isEmpty()) {
                        tiPart = tiInterchanges.get(0);
                    }
                }
                
                // Add the TI alternate attributes
                Map<String, String> attributes = Maps.newHashMap();
                
                if (tiPart != null && tiPart.getMagentoProductId() != null) {    
                    attributes.put("ti_product_id", tiPart.getMagentoProductId().toString());
                    attributes.put("ti_product_sku", tiPart.getId().toString());
                }

                // Add missing link
                logger.log(Level.INFO, "Adding BOM item link for part #{0}", part.getId());
                soap.addProductLink(    
                        part.getMagentoProductId(), item.getChild().getId(),
                        GROUPED, item.getQuantity().doubleValue(), attributes);
            } else {
                
                // Active link, don't remove
                links.remove(link);
            }
        }
        
        // Delete the links that weren't removed 
        for (ProductLink link : links) {
            soap.deleteProductLink(part.getMagentoProductId(), link.getSku(), link.getLinkType());
        }
    }
    
    private void updateProductLinks(Part part) {
        logger.log(Level.INFO, "Updating product links for part #{0}", part.getId());
        MagentoSoap soap = soap();
        
        Multimap<ProductLink.LinkType, ProductLink> links = soap.getProductLinks(part.getMagentoProductId());
        
        updateInterchanges(soap, part, links.get(RELATED));
        
        updateBOM(soap, part, links.get(GROUPED));
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
