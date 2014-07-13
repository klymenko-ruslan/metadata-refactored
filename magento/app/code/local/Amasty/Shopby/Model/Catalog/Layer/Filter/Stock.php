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

        //define where clause for stock status filter
        $where_str = "stock_status.stock_status = 1";

        // get where clause for turbo override
        $turbo_where_str = $this->_prepareTurboOverrideStockFilter();

        // if "where clause" from turbo override is not empty then add to existing where string
        if ($turbo_where_str != '') {
            $where_str = "stock_status.stock_status = 1 or " . $turbo_where_str;
        }

        $select->where($where_str);

        return $this;
    }

    /**
     * Prepare where clause to show always turbos when stock filter is applied
     *
     * @return string
     */
    protected function _prepareTurboOverrideStockFilter()
    {
        $collection = $this->getLayer()->getProductCollection();
        $alias      = "part_type_turbo_override";

        // get attribute id for part type.  current value = 190
        $eavAttribute = new Mage_Eav_Model_Mysql4_Entity_Attribute();
        $attributeId = $eavAttribute->getIdByCode('catalog_product', 'part_type');

        // define part type id for turbo. current value is 6610
        $turbo_part_type_id = "6610";

		$connection = Mage::getSingleton('core/resource')->getConnection('core_read');

        // generate array that will be applied to select sql
        $conditions = array(
            "{$alias}.entity_id = e.entity_id",
            $connection->quoteInto("{$alias}.attribute_id = ?", $attributeId),
            $connection->quoteInto("{$alias}.store_id = ?",     $collection->getStoreId()),
            #$connection->quoteInto("{$alias}.value NOT IN(?)",      $turbo_part_type_id)
        );

        // check to see if filter table has already been added to select sql statement and skip if it exists
        if (strpos($collection->getSelect()->__toString(), $alias) === false) {

            // apply array to select sql
            $collection->getSelect()->join(
                array($alias => 'catalog_product_index_eav'),
                join(' AND ', $conditions),
                array()
            );          
        }

        // create where clause that will be combined with stock filter where clause
        $where_str = $connection->quoteInto("{$alias}.value IN (?)", $turbo_part_type_id);

        return $where_str;
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
