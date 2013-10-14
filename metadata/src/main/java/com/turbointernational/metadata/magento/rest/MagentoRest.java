package com.turbointernational.metadata.magento.rest;

import com.google.common.base.Function;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsog.JSOG;
import net.sf.jsog.client.UrlBuilder;
import org.apache.commons.lang.StringUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 *
 * @author jrodriguez
 */
public class MagentoRest {
    private static final Logger logger = Logger.getLogger(MagentoRest.class.toString());
    
    public static final String BASE_URL = "http://ec2-184-73-132-150.compute-1.amazonaws.com/";

    public static class MyMagento17Api extends Magento17Api {
        @Override
        public String getBaseUrl() {
            return BASE_URL.toString();
        }
    }
    
    private static JSOG getFirstObjectValue(JSOG result) {
        if (result != null && result.isObject() && result.size() == 1) {
            return result.objectIterator().next().getValue();
        }
        
        return null;
    }
    
    private static void checkForAndThrowErrors(JSOG response) {
        
        // Return if the response is null
        if (response == null
                || !response.isObject()
                || !response.hasKey("messages")
                || !response.get("messages").hasKey("error")) {
            return;
        }
        
        // Get the error message, return if there isn't one
        JSOG errorMessage = response.path("$.messages.error[0].message");
        if (errorMessage == null || errorMessage.isNull()) {
            return;
        }
        
        throw new RuntimeException("Magento error: " + errorMessage.getStringValue());
    }
    
    private static JSOG getResponseJsog(Response response) {
        
        // Parse the response as JSOG
        String responseString = response.getBody();
        logger.log(Level.FINE, "Response: {0}", responseString);

        JSOG responseJsog;
        try {
            responseJsog = JSOG.parse(responseString);
        } catch (IOException e) {
            throw new MagentoRestException("Could not parse response: " + responseString, e);
        }
        
        return responseJsog;
    }
    
    public static void main(String[] args) throws Exception {
        MagentoRest magento = new MagentoRest(
                new UrlBuilder(BASE_URL + "api/rest/{0}").set("type", "rest"),
                new Token("8zp75lkc8zrm04jscylo0s3nbojae057", "4y791olryopwe8cfebxn6jij8apoerck"),
                "4wy124nq41hquo0z3hm8mm3uzaqrmte2", "m41dv0jtygqrl3cne1731pe5pglh5pel");
        
        // Update
//        JSOG product6 = magento.getProductById(6);
//        System.out.println(product6);
//
//
//        product6.put("sku", "126")
//                .put("overall_diameter", "123");
//
//        System.out.println(product6);
//        System.out.println(magento.updateProduct(6, product6));
//        product6 = magento.getProductById(6);
//        System.out.println(product6);
        
        // Create
//        JSOG product = JSOG.object()
//            .put("sku", "125")
//            .put("attribute_set_id", "12") // Attribute set
//            .put("type_id","simple") // Required
//            .put("name", "my name") // Required
//            .put("short_description", "my short description") // Required
//            .put("description","my desc") // Required
//            .put("overall_diameter","6.021023")
//            .put("compressor_wheel_diameter","3.14")
//            .put("price", "42.00") // Required
//            .put("weight", "0.0") // Required
//            .put("tax_class_id","0") // Required
//            .put("status", "1") // Required
//            .put("visibility","4") // Required
//            ;
//        
//        // Create the product
//        try {
//            System.out.println(magento.createProduct(product));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
        // Delete
//        magento.deleteProductById(7);
        
        // Read
//        System.out.println(magento.getProducts());
//        System.out.println(magento.getProductById(6));
//        JSOG productJsog = magento.getProductBySku("125");
//        
//        if (productJsog == null) {
//            System.out.println("No such product.");
//            return;
//        }
//        
//        System.out.println("Product:\n" + productJsog);
//        int productId = productJsog.get("entity_id").getIntegerValue();
//        magento.deleteProduct(productId);
        
//        System.out.println("Categories: \n" + magento.getCategories(productId));
//        
//        System.out.println("Delete Category Cartridge:\n" + magento.deleteCategory(productId, 6));
//        System.out.println("Categories: \n" + magento.getCategories(productId));
//        
//        System.out.println("Add category Cartridge: \n" + magento.addCategory(productId, 6));
//        System.out.println("Categories: \n" + magento.getCategories(productId));
    }

    private UrlBuilder baseUrl;
    private OAuthService oauth;
    private Token accessToken;
    
    public MagentoRest(final UrlBuilder baseUrl, Token accessToken, String apiKey, String apiSecret) {
        
        // Create the oauth service
        oauth = new ServiceBuilder()
                .provider(MyMagento17Api.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
        
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
    }
    
    private JSOG call(MagentoRequest magentoRequest) throws MagentoRestException {
        return call(magentoRequest, new Function<Response, JSOG>() {

            @Override
            public JSOG apply(Response response) {
                JSOG responseJsog = getResponseJsog(response);
                
                checkForAndThrowErrors(responseJsog);
                
                return responseJsog;
            }
        });
    }
    
    private <T> T call(MagentoRequest magentoRequest, Function<Response, T> callback) {
        
        // Build the OAuthRequest and sign it with our access token
        OAuthRequest request = magentoRequest.getOAuthRequest();
        request.addHeader("Content-type", "application/json");
        request.addHeader("Accept", "*/*");
        logger.log(Level.INFO, "{0} {1}", new Object[] {request.getVerb(), request.getUrl()});
        
        oauth.signRequest(accessToken, request);
        
        // Send the request
        Response response = request.send();
        
        return callback.apply(response);
    }
    
    public int createProduct(JSOG product) throws MagentoRestException {
        
        // Create the request
        MagentoRequest request = new MagentoRequest(Verb.POST, baseUrl, "products");
        request.setPayload(product);
        
        // Issue the request
        return call(request, new Function<Response, Integer>() {
            @Override
            public Integer apply(Response response) {
                
                // Response handling basics
                JSOG responseJsog = getResponseJsog(response);
                
                checkForAndThrowErrors(responseJsog);
                
                // Get the created product ID out of the 'Location' header
                String location = response.getHeader("Location");
                
                if (StringUtils.isBlank(location)) {
                    throw new RuntimeException("No location header found. Response: " + responseJsog);
                }
                
                logger.log(Level.FINER, "Location: {0}", location);
                String productId = location.substring(location.lastIndexOf('/') + 1);
                
                return Integer.parseInt(productId);
            }
        });
    }
    
    public void updateProduct(int id, JSOG product) throws MagentoRestException {
        MagentoRequest request = new MagentoRequest(Verb.PUT, baseUrl, "products/" + id);
        request.setPayload(product);
        call(request);
    }
    
    public JSOG getProducts() throws MagentoRestException {
        MagentoRequest request = new MagentoRequest(Verb.GET, baseUrl, "products");
        return call(request);
    }
    
    public JSOG getProductById(int id) throws MagentoRestException {
        MagentoRequest request = new MagentoRequest(Verb.GET, baseUrl, "products");
        
        request.addFilter("entity_id", FilterType.in)
               .addParameter(Integer.toString(id));
        
        return getFirstObjectValue(call(request));
        
    }
    
    public JSOG getProductBySku(String sku) throws MagentoRestException {
        MagentoRequest request = new MagentoRequest(Verb.GET, baseUrl, "products");
        
        request.addFilter("sku", FilterType.in)
               .addParameter(sku);
        
        return getFirstObjectValue(call(request));
    }
    
    public JSOG addCategory(int productId, int categoryId) {
        MagentoRequest request = new MagentoRequest(Verb.POST, baseUrl, "products/" + productId + "/categories");
        
        request.setPayload(JSOG.object("category_id", categoryId));
        
        return call(request);
    }
    
    public Set<Integer> getCategories(int productId) {
        MagentoRequest request = new MagentoRequest(Verb.GET, baseUrl, "products/" + productId + "/categories");
        
        Set<Integer> categoryIds = new TreeSet<Integer>();
        
        JSOG response = call(request);
        for (JSOG catObj : response.arrayIterable()) {
            categoryIds.add(catObj.get("category_id").getIntegerValue());
        }
        
        return categoryIds;
    }
    
    public JSOG deleteCategory(int productId, int categoryId) {
        MagentoRequest request = new MagentoRequest(Verb.DELETE, baseUrl, "products/" + productId + "/categories?category_id=" + categoryId);
        
        return call(request);
    }

    public void deleteProduct(int id) throws MagentoRestException {
        MagentoRequest request = new MagentoRequest(Verb.DELETE, baseUrl, "products/" + id);
        call(request);
    }
    
    public void deleteAllProducts() throws MagentoRestException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}