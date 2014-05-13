<?php

class Amasty_Xlanding_Model_Page extends Mage_Core_Model_Abstract
{
    /**
     * Page's Statuses
     */
    const STATUS_ENABLED = 1;
    const STATUS_DISABLED = 0;

    const ON_SALE_YES = 2;
    const ON_SALE_NO = 1;

    const IS_NEW_YES = 2;
    const IS_NEW_NO = 1;

    const IS_INSTOCK_YES = 2;

    protected $_attributeCache;


    /**
     * Initialize resource model
     *
     */
    protected function _construct()
    {
        $this->_init('amlanding/page');
    }

    /**
     * Check if page identifier exist for specific store
     * return page id if page exists
     *
     * @param string $identifier
     * @param int $storeId
     * @return int
     */
    public function checkIdentifier($identifier, $storeId)
    {
        return $this->_getResource()->checkIdentifier($identifier, $storeId);
    }

    public function getAttributesAsArray()
    {
    	$array = array();
    	$attributes = $this->getData('attributes');
    	if (!empty($attributes)) {
    		$array = unserialize($attributes);
    	}
    	return $array;
    }

    public function applyPageRules()
    {
    	$layer = Mage::getSingleton('catalog/layer');

        $collection = $layer->getProductCollection();  
        
        $this->prepareCollection($collection);

        if (isset($_GET['xlanding_debug_page'])) {
            echo $collection->getSelect();
        }
    }
    
    public function prepareCollection(&$collection){
        $collection->distinct(true);
                
        $collection->addStoreFilter();
        
        $this->applyCategoryFilter($collection);

        $this->applyAttributesFilter($collection);

        $this->applyStockStatusFilter($collection);

        $this->applyNewCriteriaFilter($collection);

        $this->applyIsSaleFilter($collection);
    }

    function applyCategoryFilter(&$collection){
        if ($this->getCategory()) {
            $fromPart = $collection->getSelect()->getPart(Zend_Db_Select::FROM);
            if (isset($fromPart["cat_index"]) && isset($fromPart["cat_index"]['joinCondition'])){
                $fromPart["cat_index"]['joinCondition'] = 'cat_index.product_id=e.entity_id AND cat_index.store_id=' . Mage::app()->getStore()->getId() . ' AND cat_index.visibility IN(2, 4) AND cat_index.category_id = ' . $this->getCategory();
                $collection->getSelect()->setPart(Zend_Db_Select::FROM, $fromPart);
            }            
        }
    }

    function applyAttributesFilter(&$collection){
        $attributes = $this->getAttributesAsArray();
        if ($attributes){
            foreach ($attributes as $value) {
                $this->applyAttributeFilter($value, $collection);
            }
        }
    }

    function applyNewCriteriaFilter(&$collection){
        $newCriteriaDays = Mage::getStoreConfig('amlanding/advanced/new_criteria');
        if ($isNew = $this->getIsNew()) {
            if ($isNew == self::IS_NEW_YES) {
                if ($newCriteriaDays) {
                    $threshold = Mage::getStoreConfig('amlanding/advanced/new_threshold');
                    $collection->getSelect()->where('datediff(now(), created_at) < ?', $threshold);
                } else {
                    $todayDate  = Mage::app()->getLocale()->date()->toString(Varien_Date::DATE_INTERNAL_FORMAT);
                    $collection
                            ->addAttributeToFilter('news_from_date',
                                    array(

                                    'or' => array(
                            0 => array('date' => false, 'to' => $todayDate),
                                            1 => array('is' => new Zend_Db_Expr('null'))
                                    )), 'left')
                            ->addAttributeToFilter('news_to_date',
                                    array(
                                    'or'=> array(
                            0 => array('date' => false, 'from' => $todayDate),
                            1 => array('is' => new Zend_Db_Expr('null'))
                                    )), 'left');


                    $collection->getSelect()->where('NOT (IF(at_news_from_date.value_id > 0, at_news_from_date.value, at_news_from_date_default.value) IS NULL AND IF(at_news_to_date.value_id > 0, at_news_to_date.value, at_news_to_date_default.value)IS NULL)');

                }
            }

            if ($isNew == self::IS_NEW_NO) {
                if ($newCriteriaDays) {
                    $threshold = Mage::getStoreConfig('amlanding/advanced/new_threshold');
                    $collection->getSelect()->where('datediff(now(), created_at) > ?', $threshold);
                } else {
                    $todayDate  = Mage::app()->getLocale()->date()->toString(Varien_Date::DATE_INTERNAL_FORMAT);
                    $collection
                            ->addAttributeToFilter('news_from_date',
                                    array(
                                    'or' => array(
                            0 => array('date' => false, 'from' => $todayDate),
                                            1 => array('is' => new Zend_Db_Expr('null'))
                                    )), 'left')
                            ->addAttributeToFilter('news_to_date',
                                    array('or'=> array(
                            0 => array('date' => false, 'to' => $todayDate),
                            1 => array('is' => new Zend_Db_Expr('null'))
                                    )), 'left');

                }
            }
        }
    }

    function applyIsSaleFilter(&$collection){
        if ($sale = $this->getIsSale()){
            if ($sale == self::ON_SALE_YES) {
                $collection
                    ->addAttributeToFilter('special_price', array('gt' => 0));
                            $todayDate  = Mage::app()->getLocale()->date()->toString(Varien_Date::DATE_INTERNAL_FORMAT);
                    $collection
                            ->addAttributeToFilter('special_from_date',
                                    array(
                                    'or' => array(
                            0 => array('date' => false, 'to' => $todayDate),
                                            1 => array('is' => new Zend_Db_Expr('null'))
                                    )), 'left')
                            ->addAttributeToFilter('special_to_date',
                                    array('or'=> array(
                            0 => array('date' => false, 'from' => $todayDate),
                            1 => array('is' => new Zend_Db_Expr('null'))
                                    )), 'left');
$collection->getSelect()->where('NOT (IF(at_special_from_date.value_id > 0, at_special_from_date.value, at_special_from_date_default.value) IS NULL AND IF(at_special_to_date.value_id > 0, at_special_to_date.value, at_special_to_date_default.value)IS NULL)');


            }

            if ($sale == self::ON_SALE_NO) {
                $collection->addAttributeToFilter('special_price', array('null'=>'special_price'), 'left');
            }
        }
    }

    function applyStockStatusFilter(&$collection){
        if ($stock = $this->getStockStatus()){
            if ($stock == self::IS_INSTOCK_YES) {
                Mage::getSingleton('cataloginventory/stock')->addInStockFilterToCollection($collection);
            }
        }
    }
        
    function applyAttributeFilter($param, $collection)
    {
    	$code  = $param['code'];
    	$value = $param['value'];
    	if (is_array($value)){
    	   $value = $value[0];
    	}
    	$cond  = $param['cond'];
    	
    	if (!isset($this->_attributeCache[$code])) {
    		$attribute = Mage::getModel('catalog/product')->getResource()->getAttribute($code);
    		$this->_attributeCache[$code] = $attribute;
    	}

    	$attribute = $this->_attributeCache[$code];
    	$filterable = $attribute->getIsFilterable();

       
            
    	/*
    	 * Filterable, exists in index table
    	 */
    	if ($filterable && !Mage::helper('amlanding')->isPrice($code)) {

	    	$alias = $code . $cond . '_idx';
			$connection = $this->_getResource()->getReadConnection();
			$conditions = array(
				"{$alias}.entity_id = e.entity_id",
			    $connection->quoteInto("{$alias}.attribute_id = ?", $attribute->getAttributeId()),
			    $connection->quoteInto("{$alias}.store_id = ?",     $collection->getStoreId()),
			);

			$condSql = array(
				'eq' => ' = ?',
				'in' => ' in (?)',
				'nin' => ' not in (?)',
			);

			$conditions[] = $connection->quoteInto("{$alias}.value " . $condSql[$cond], $value);

			$collection->getSelect()->join(
				array($alias => Mage::getResourceModel('catalog/layer_filter_attribute')->getMainTable()),
				join(' AND ', $conditions),
			    array()
	        );
    	} 
    	else {
            if ($attribute->getFrontendInput() == "multiselect"){
                if ($cond == "in" || $cond == "eq"){
                    $filter = array();
                    if (is_array($value)){
                        foreach($value as $v){
                            $filter[] = array(
                                'like' => '%' . $v . '%'
                            );
                        }
                        $collection->addFieldToFilter($attribute, $filter);
                    } else {
                        $collection->addFieldToFilter($attribute, array(
                            "like" => '%' . $value . '%'
                        ));
                    }
                    
                }
            }
            else {
                if (strpos($cond, 'like') !== false) {
                    $value = '%' . $value . '%';
                }

                $code = $attribute->getAttributeCode();
                
                $collection->addAttributeToFilter(array(array(
                    "attribute" => $code,
                    $cond => $value
                )), null, 'inner');
                
            }
    	}
    }

  	public function massChangeStatus($ids, $status)
    {
        return $this->getResource()->massChangeStatus($ids, $status);
    }
    
    public function getUploadPath()
    {
        return  'amasty' . DS .'amxlanding';
    }
    
    public function getLayoutFileUrl(){
        return Mage::getBaseUrl(Mage_Core_Model_Store::URL_TYPE_MEDIA) . DS . $this->getLayoutFile();
    }
    
    protected function _beforeSave(){
        
        if(isset($_FILES['layout_file']) &&
                $_FILES['layout_file']['name'] != '') {
        
            try{
                $uploader = new Varien_File_Uploader('layout_file');
                $uploader->setAllowedExtensions(array('jpg','jpeg','gif','png'));
                $uploader->setAllowRenameFiles(true);
                $uploader->setFilesDispersion(true);

                $this->setLayoutFileName($uploader->getCorrectFileName($_FILES['layout_file']['name']));

                $result = $uploader->save(
                    Mage::getBaseDir('media') . DS . $this->getUploadPath(), uniqid().".".$uploader->getFileExtension()
                );

                $this->setLayoutFile($this->getUploadPath() . $result['file']); 
            } catch (Exception $e){
                Mage::throwException($this->__('Invalid image format'));
            }
            
        } else {
            
            $layoutFile = $this->getLayoutFile();
            
            if (isset($layoutFile['delete']) &&
                    $layoutFile['delete'] == 1
            ) {
                $this->setLayoutFile(NULL);
            } else {
                $this->setLayoutFile($this->layout_file["value"]);
            }
            
        }
        return parent::_beforeSave(); 
    }
}
