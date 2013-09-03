//package com.turbointernational.metadata.magento;
//
//import com.google.code.magja.model.product.Product;
//import com.google.code.magja.model.product.ProductAttributeSet;
//import com.google.code.magja.model.product.ProductType;
//import com.google.code.magja.service.RemoteServiceFactory;
//import com.google.code.magja.service.ServiceException;
//import com.google.code.magja.service.product.ProductAttributeRemoteService;
//import com.google.code.magja.service.product.ProductRemoteService;
//import com.google.code.magja.soap.MagentoSoapClient;
//import com.google.code.magja.soap.SoapConfig;
//import com.turbointernational.metadata.domain.part.Part;
//import java.util.Collections;
//import java.util.Map;
//import java.util.TreeMap;
//
///**
// *
// * @author jrodriguez
// */
//public class StoreSync {
//    public static void main(String[] args) throws Exception {
//        
//        
//
//        SoapConfig config = new SoapConfig("metadata", "9l8t6QCihtX4", "http://ec2-184-73-132-150.compute-1.amazonaws.com/index.php/api/soap");
//        MagentoSoapClient client = MagentoSoapClient.getInstance(config);
//        RemoteServiceFactory rsf = new RemoteServiceFactory(client);
////        
////        StoreSync sync = new StoreSync(rsf);
//////        sync.deleteAllProducts();
//////        sync.test();
////        sync.test();
//        
////        ProductAttributeRemoteService productAttributeApi = rsf.getProductAttributeRemoteService();
////        for (ProductAttributeSet set : productAttributeApi.listAllProductAttributeSet()) {
////            System.out.println(set.getId() + ": " + set.getName());
////        }
////        
////        System.out.println(productsApi.listAllProductTypes());
////        System.out.println(productsApi.listAll());
////        sync(productApi);
//    }
//    
//    private final RemoteServiceFactory magentoApiFactory;
//    
//    private ProductRemoteService productApi;
//    
//    private ProductAttributeRemoteService productAttributeApi;
//    
//    private Map<String, ProductAttributeSet> productAttributeSets = Collections.EMPTY_MAP;
//    
//
//    public StoreSync(RemoteServiceFactory magentoApiFactory) {
//        this.magentoApiFactory = magentoApiFactory;
//        this.productApi = magentoApiFactory.getProductRemoteService();
//        this.productAttributeApi = magentoApiFactory.getProductAttributeRemoteService();
//    }
//    
//    public void test() throws ServiceException {
//        Product product = null; getProductById(6);
//        
//        if (product == null) {
//            System.out.println("No product found with ID: 1");
//            product = new Product();
//        }
//        
//        product.setId(6);
//        product.setSku("125");
//        product.setName("my name");
//        product.setDescription("my desc");
//        product.setEnabled(true);
//        product.setWebsites(new Integer[] {1});
//        product.setType(new ProductType("Simple", "simple"));
//        product.setAttributeSet(new ProductAttributeSet(12, "Backplate"));
//        
////        
//////        product.set("seal_type", "Test Seal Type");
//        product.set("compressor_wheel_diameter", "3.14");
//        product.set("overall_diameter", "6.021023");
//        
//        productApi.save(product);
//    }
//    
//    private Product getProductById(Integer id) throws ServiceException {
//        try {
//            return productApi.getById(id);
//        } catch (ServiceException e) {
//            
//            // Null if the product doesn't exist doesn't exist
//            if (!e.getMessage().equals("Product not exists.")) {
//                throw e;
//            }
//            
//            return null;
//        }
//    }
//    
//    private Product getProductBySku(String sku) throws ServiceException {
//        try {
//            return productApi.getBySku("123");
//        } catch (ServiceException e) {
//            
//            // Null if the product doesn't exist doesn't exist
//            if (!e.getMessage().equals("Product not exists.")) {
//                throw e;
//            }
//            
//            return null;
//        }
//    }
//    
//    private void deleteAllProducts() throws Exception {
//        productApi.deleteAll();
//    }
//    
//    private Map<String, ProductAttributeSet> getProductAttributeSets() throws ServiceException {
//        Map<String, ProductAttributeSet> sets = new TreeMap();
//        
//        for (ProductAttributeSet set : productAttributeApi.listAllProductAttributeSet()) {
//            sets.put(set.getName(), set);
//        }
//        
//        return sets;
//    }
//    
//    private ProductAttributeSet getProductAttributeSet(String name) throws ServiceException {
//        if (!productAttributeSets.containsKey(name)) {
//            this.productAttributeSets = getProductAttributeSets();
//        }
//        
//        return  productAttributeSets.get(name);
//    }
//    
//    
//    public void syncPart(Part part) throws ServiceException {
//        Product product = getProductBySku("125");
//
//        if (product == null) {
//            System.out.println("Creating product");
//            product = new Product();
//        }
//    }
//    
//    
//    
//    public static void sync(ProductRemoteService products) throws Exception {
//    }
//
//    private void deleteProductBySku(String string) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    
//}