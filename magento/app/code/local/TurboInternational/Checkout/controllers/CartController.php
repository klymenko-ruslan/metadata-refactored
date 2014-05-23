<?php
/**
 * Magento
 *
 * NOTICE OF LICENSE
 *
 * This source file is subject to the Open Software License (OSL 3.0)
 * that is bundled with this package in the file LICENSE.txt.
 * It is also available through the world-wide-web at this URL:
 * http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to
 * obtain it through the world-wide-web, please send an email
 * to license@magentocommerce.com so we can send you a copy immediately.
 *
 * DISCLAIMER
 *
 * Do not edit or add to this file if you wish to upgrade Magento to newer
 * versions in the future. If you wish to customize Magento for your
 * needs please refer to http://www.magentocommerce.com for more information.
 *
 * @category    Mage
 * @package     Mage_Checkout
 * @copyright   Copyright (c) 2012 Magento Inc. (http://www.magentocommerce.com)
 * @license     http://opensource.org/licenses/osl-3.0.php  Open Software License (OSL 3.0)
 */

require_once 'Mage/Checkout/controllers/CartController.php';

/**
 * Shopping cart controller
 */
class TurboInternational_Checkout_CartController extends Mage_Checkout_CartController
{
    /**
     * Add product to shopping cart action
     *
     * @return Mage_Core_Controller_Varien_Action
     * @throws Exception
     */
    public function addAction()
    {
        $cart   = $this->_getCart();
        $params = $this->getRequest()->getParams();
        $qty = 1;
        
        if (isset($params['qty'])) {
            $qty = $params['qty'];
            $filter = new Zend_Filter_LocalizedToNormalized(
                array('locale' => Mage::app()->getLocale()->getLocaleCode())
            );
            $params['qty'] = $filter->filter($params['qty']);
        }
        
        
        $productId=$this->getRequest()->getParam('product');
        
        $_product= Mage::getModel('catalog/product')->load($productId);
        
        // Get the TI product
        $tiProduct = $_product->getTiProduct();
            
        if (!$tiProduct || $tiProduct->getId() == $_product->getId()){
            parent::addAction();
            return;
        }
        
        // Workaround for a stupid magento bug:
        // https://stackoverflow.com/questions/10929563/magento-product-load-difference-between-loadbyattribute-and-load-methods
        $tiProduct = Mage::getModel('catalog/product')->load($tiProduct->getId());
            
        // Update the cart params to use the TI part, add the OEM sku
        $params['product'] = $tiProduct->getId();
        
        // Set the OEM sku
        $optionId = $tiProduct->getOemSkuCustomOptionId();
        if ($optionId) {
            
            // Make 'options' an array if it isn't already
            if (!isset($param['options'])) {
                $params['options'] = array();
            }
            
            if (!isset($params['options'][$optionId])) {
                $params['options'][$optionId] = $_product->getSku();
            }
        }

        // Add the (reloaded) TI product to the cart
        $cart->addProduct($tiProduct, $params);
        $cart->save();
   
        $this->_getSession()->setCartWasUpdated(true);

        Mage::dispatchEvent('checkout_cart_add_product_complete',
            array('product' => $tiProduct, 'request' => $this->getRequest(), 'response' => $this->getResponse())
        );

        if (!$this->_getSession()->getNoCartRedirect(true)) {
            if (!$cart->getQuote()->getHasError()) {
                $message = $this->__('%s was added to your shopping cart.', Mage::helper('core')->escapeHtml($tiProduct->getName()));
                $this->_getSession()->addSuccess($message);
            }
            $this->_goBack();
        }
    }
}
