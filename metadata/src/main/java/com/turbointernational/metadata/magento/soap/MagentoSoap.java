package com.turbointernational.metadata.magento.soap;

import com.google.code.magja.model.product.Product;
import com.google.code.magja.model.product.ProductLink;
import com.google.code.magja.model.product.ProductLink.LinkType;
import com.google.code.magja.service.RemoteServiceFactory;
import com.google.code.magja.service.ServiceException;
import com.google.code.magja.soap.MagentoSoapClient;
import com.google.code.magja.soap.SoapConfig;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

/**
 *
 * @author jrodriguez
 */
public class MagentoSoap {
    
    public static final String BASE_URL = "http://ec2-184-73-132-150.compute-1.amazonaws.com/";
    
    public static void main(String[] args) throws Exception {
        
        MagentoSoap magento = new MagentoSoap("metadata", "9l8t6QCihtX4", BASE_URL + "index.php/api/soap");
        System.out.println("Pre-delete: " + magento.getProductLinks(6));
        
        magento.deleteProductLink(6, 7, LinkType.RELATED);
        
        System.out.println("Post-delete, pre-add: " + magento.getProductLinks(6));
        
        magento.addProductLink(6, 7, LinkType.RELATED);
        
        System.out.println("Post-add: " + magento.getProductLinks(6));
    }
    
    private RemoteServiceFactory rsf;

    public MagentoSoap(String user, String password, String url) {
        SoapConfig config = new SoapConfig(user, password, url);
        MagentoSoapClient client = MagentoSoapClient.getInstance(config);
        rsf = new RemoteServiceFactory(client);
    }
    
    public void addProductLink(int productId, int linkedProductId, LinkType type) throws MagentoSoapException {
        Product parent = new Product();
        parent.setId(productId);
        
        ProductLink productLink = new ProductLink();
        productLink.setId(linkedProductId);
        productLink.setLinkType(type);
        
        try {
            rsf.getProductLinkRemoteService().assign(parent, productLink);
        } catch (ServiceException e) {
            throw new MagentoSoapException("Could not link products.", e);
        }
    }
    
    public Multimap<LinkType, Integer> getProductLinks(int productId) {
        Product product = new Product();
        product.setId(productId);
        
        
        Multimap<LinkType, Integer> linkedProducts = LinkedListMultimap.create();
        try {
            for (ProductLink link : rsf.getProductLinkRemoteService().list(product)) {
                linkedProducts.put(link.getLinkType(), link.getId());
            
            }
        } catch (ServiceException e) {
            throw new MagentoSoapException("Failed to fetch product links.", e);
        }
        
        return linkedProducts;
    }
    
    public void deleteProductLink(int productId, int linkedProductId, LinkType type) throws MagentoSoapException {
        Product parent = new Product();
        parent.setId(productId);
        
        ProductLink productLink = new ProductLink();
        productLink.setId(linkedProductId);
        productLink.setLinkType(type);
        
        try {
            rsf.getProductLinkRemoteService().remove(parent, productLink);
        } catch (ServiceException e) {
            throw new MagentoSoapException("Could not link products.", e);
        }
    }
}
