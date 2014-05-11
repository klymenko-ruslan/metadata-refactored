<?php

//require_once 'Mage/Checkout/Model/Cart.php';

class TurboInternational_Checkout_Model_Cart extends Mage_Checkout_Model_Cart {

    public function addProduct($productInfo, $requestInfo = null) {
        
        // Load the TI product
        $tiProductId = $this->_getProduct($productInfo)->getTiProduct()->getId();
        
        // Workaround for a stupid magento bug: https://stackoverflow.com/questions/10929563/magento-product-load-difference-between-loadbyattribute-and-load-methods
        $tiProduct = Mage::getModel('catalog/product')->load($tiProductId);

        // Follow the normal add to cart procedure with this product
        return parent::addProduct($tiProduct, $requestInfo);
    }

}
