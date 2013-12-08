<?php

/**
 * Import customer prices for columns names called "customerprice"
 */

class CustomerpriceProcessor extends Magmi_ItemProcessor
{
    protected $_columnName = 'customerprice';

    public function getPluginInfo()
    {
        return array(
            'name'      => 'Customer Price Importer',
            'author'    => 'Jeff Rodriguez',
            'version'   => '0.0.1'
        );
    }

    public function processItemAfterId(&$item, $params = null)
    {
        $table_name = $this->tablename("customerprices_prices");
        $customer_table_name = $this->tablename("customer_entity");
        
        if (!array_key_exists($this->_columnName, $item)) {
            return true;
        }
        
        $sql = 'DELETE FROM ' . $table_name . ' WHERE product_id=?';
        $this->delete($sql, array($params['product_id']));
                
        // Build the insert
        $sql = 'INSERT INTO ' . $table_name . ' (product_id, customer_id, customer_email, qty, price) VALUES ';
        $inserts = array();
        $data = array();
        
        // Explode on pipes - email1;qty1:price1;qty2:price2|email2...
        foreach(explode("|", $item[$this->_columnName]) as $customer_price) {
            if (strlen($customer_price) == 0) {
                continue;
            }
            
            // Explode into a quantity:price array - email1;qty1:price1...
            // Strip the email off the quantity:price array, it's the first value
            $quantityPrices = explode(";", $customer_price);
            $customerEmail = array_shift($quantityPrices);
            
            if ($customerEmail)
            $customerId = $this->selectone(
                    'SELECT entity_id FROM ' . $customer_table_name . ' WHERE email=?',
                    $customerEmail, "entity_id");
            
            if ($customerId == null) {
                error_log("Skipping customer price; no customer ID for " . $customerEmail);
                continue;
            }
            
            // Now we have an array of qty:price strings
            foreach ($quantityPrices as $qtyPrice) {
                
                // Get the quantity and price
                $qtyPricePieces = explode(":", $qtyPrice);
                $quantity = $qtyPricePieces[0];
                $price = (float) $qtyPricePieces[1];
                
                $inserts[] = '(?,?,?,?,?)';
                $data[] = $params['product_id'];
                $data[] = $customerId;
                $data[] = $customerEmail;
                $data[] = $quantity;
                $data[] = $price;
            }
        }
        
        if(!empty($data)) {
            $sql .= implode(', ', $inserts);
            $this->insert($sql, $data);
        }

        return true;
    }

    public function initialize($params)
    {
        $sql = 'SELECT COUNT(store_id) as cnt FROM ' . $this->tablename('core_store') . ' WHERE store_id != 0';
        $ns = $this->selectOne($sql, array(), "cnt");
        $this->_singleStore = $ns == 1;

        /* Check price scope in a general config (0 = global, 1 = website) */
        $sql = 'SELECT value FROM ' . $this->tablename('core_config_data') . ' WHERE path = ?';
        $this->_priceScope = intval($this->selectone($sql, array('catalog/price/scope'), 'value'));
    }
}
