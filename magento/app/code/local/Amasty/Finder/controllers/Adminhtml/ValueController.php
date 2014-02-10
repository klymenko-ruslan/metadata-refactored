<?php
/**
 * @copyright   Copyright (c) 2009-2012 Amasty (http://www.amasty.com)
 */  
class Amasty_Finder_Adminhtml_ValueController extends Mage_Adminhtml_Controller_Action
{
    protected $_title     = 'Cortages';
    protected $_modelName = 'value';
    
    
    public function newAction() 
    {
        $this->editAction();
    }
    
    public function editAction() 
    {
        $newId     = (int) $this->getRequest()->getParam('id');
		$id = Mage::getModel('amfinder/finder')->newSetterId($newId);
		
        $model  = Mage::getModel('amfinder/' . $this->_modelName)->load($id);
        if ($id && !$model->getId()) {
            Mage::getSingleton('adminhtml/session')->addError(Mage::helper('amfinder')->__('Record does not exist'));
            $this->_redirect('adminhtml/finder/*');
            return;
        }
        $settingData = array();
        $labelData   = array();
        $dropdownId = $model->getDropdownId();
        
        $currentId = $id;

        
        Mage::register('amfinder_' . $this->_modelName, $model);

        $this->loadLayout();
        
        $this->_setActiveMenu('catalog/amfinder');
        $this->_title($this->__('Edit'));
        
        $this->_addContent($this->getLayout()->createBlock('amfinder/adminhtml_' . $this->_modelName . '_edit'));
        $this->renderLayout();
    }
    
    public function saveAction() 
    {
        $newId     = $this->getRequest()->getParam('id');
        $finder = $this->getRequest()->getParam('finder');
                   
        $data = $this->getRequest()->getPost();
		$id = Mage::getModel('amfinder/finder')->newSetterId($newId);
        $currentId = $id;

        if ($id) {
            $model  = Mage::getModel('amfinder/value')->load($id);
            $dropdownId = $model->getDropdownId();
            $dropdown = Mage::getModel('amfinder/dropdown')->load($dropdownId);
            $finderId = $dropdown->getFinderId();
            
            try {
                $model->saveAfterEdit($newId,$id,$data['sku']);
                while ($currentId){
                    $label_alias = 'label_'.$currentId;
                    $dataNew['name'] = $data[$label_alias];
                    $model->setData($dataNew)->setId($currentId);
                    $model->save();            
                    $value = Mage::getModel('amfinder/value')->load($currentId);
                    $currentId = $value->getParentId();
                }
                
                Mage::getSingleton('adminhtml/session')->setFormData(false);
                
                $msg = Mage::helper('amfinder')->__('Record have been successfully saved');
                Mage::getSingleton('adminhtml/session')->addSuccess($msg);

                
                $this->_redirect('adminhtml/finder/edit', array('id'=>$finderId, 'active_tab'=>'products'));
               
                
            } 
            catch (Exception $e) {
                Mage::getSingleton('adminhtml/session')->addError($e->getMessage());
                Mage::getSingleton('adminhtml/session')->setFormData($data);
                $this->_redirect('adminhtml/value/edit', array('id' => $id));
            }              
        }
        
        if (isset($data['new_finder'])) {

            try {
                $model  = Mage::getModel('amfinder/value');
                $finderId = $model->saveNewFinder($data);
                
                Mage::getSingleton('adminhtml/session')->setFormData(false);
                
                $msg = Mage::helper('amfinder')->__('Record have been successfully saved');
                Mage::getSingleton('adminhtml/session')->addSuccess($msg);

                $this->_redirect('adminhtml/finder/edit', array('id'=>$finderId, 'active_tab'=>'products'));
               
                
            } 
            catch (Exception $e) {
                Mage::getSingleton('adminhtml/session')->addError($e->getMessage());
                Mage::getSingleton('adminhtml/session')->setFormData($data);
                $this->_redirect('adminhtml/value/new');
            } 
        }    
  
    }       
    
}    