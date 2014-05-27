<?php
/**
 * @copyright   Copyright (c) 2010 Amasty (http://www.amasty.com)
 */
class Amasty_Shopby_Model_Catalog_Layer_Filter_Stock extends Mage_Catalog_Model_Layer_Filter_Abstract
{

	const FILTER_OUT_OF_STOCK = 1;

    /**
     * Class constructor
     */
    public function __construct()
    {
        parent::__construct();
        $this->_requestVar = 'stock';
    }

    /**
     * Apply category filter to layer
     *
     * @param   Zend_Controller_Request_Abstract $request
     * @param   Mage_Core_Block_Abstract $filterBlock
     * @return  Mage_Catalog_Model_Layer_Filter_Category
     */
    public function apply(Zend_Controller_Request_Abstract $request, $filterBlock)
    {
        
        // Stop now if the filter is already applied
        if (Mage::registry('am_stock_filter')) {
            return $this;
        }
        
        // Update the registry value
        $filter = (int) $request->getParam($this->getRequestVar());
        if ($filter === self::FILTER_OUT_OF_STOCK) {
            
            if (is_null(Mage::registry('am_stock_filter'))) {
                $state = $this->_createItem(Mage::helper('amshopby')->__('Show All'), $filter)
                                ->setVar($this->_requestVar);

                $this->getLayer()->getState()->addFilter($state);

                Mage::register('am_stock_filter', true);
            }
            
            return $this;
        }
        
        $select = $this->getLayer()->getProductCollection()->getSelect();
        
        if (strpos($select, 'cataloginventory_stock_status') === false) {
        	Mage::getResourceModel('cataloginventory/stock_status')
                ->addStockStatusToSelect($select, Mage::app()->getWebsite());
        }
        
        $select->where('stock_status.stock_status = ?', 1);
            
        return $this;
    }


    /**
     * Get filter name
     *
     * @return string
     */
    public function getName()
    {
        return Mage::helper('amshopby')->__('Non-TI Parts');
    }

    /**
     * Get data array for building category filter items
     *
     * @return array
     */
    protected function _getItemsData()
    {
    	$data = array();
    	$status = $this->_getCount();
    	
    	$in_stock = array_keys($status);
    	
    	$data[] = array(
            'label' => Mage::helper('amshopby')->__('Show All'),
            'value' => self::FILTER_OUT_OF_STOCK,
            'count' => $in_stock[0],
		);
        return $data;
    }
    
    protected function _getCount()
    {
    	$select = clone $this->getLayer()->getProductCollection()->getSelect();
    	
    	if (strpos($select, 'cataloginventory_stock_status') === false) {
        	Mage::getResourceModel('cataloginventory/stock_status')
                ->addStockStatusToSelect($select, Mage::app()->getWebsite());
        } 
        
		$select->reset(Zend_Db_Select::ORDER);
        $select->reset(Zend_Db_Select::LIMIT_COUNT);
        $select->reset(Zend_Db_Select::LIMIT_OFFSET);
        $select->reset(Zend_Db_Select::WHERE);
       
    	$sql = 'select  SUM(stock.salable) as in_stock, COUNT(stock.salable) - SUM(stock.salable) as out_stock from (' . $select->__toString()  . ') as stock';    	
		$connection = Mage::getSingleton('core/resource')->getConnection('core_read');
		
        return $connection->fetchPairs($sql);            
    }
}
