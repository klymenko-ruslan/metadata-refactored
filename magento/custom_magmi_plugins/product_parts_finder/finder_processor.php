<?php

/**
 * Import customer prices for columns names called "customerprice"
 */

class FinderProcessor extends Magmi_ItemProcessor {
    protected $_VALUE_SEPARATOR = "||";
    protected $_DROPDOWN_SEPARATOR = "!!";
    protected $_columnPrefix = 'finder:';
    protected $_finders = array(); // Finder ID -> array of Dropdown IDs

    public function finderColumnFilter($name) {
        return strpos($name, $this->_columnPrefix) == 0;
    }

    public function getPluginInfo() {
        return array(
            'name'      => 'Amasty Product Parts Finder',
            'author'    => 'Jeff Rodriguez',
            'version'   => '0.0.1'
        );
    }

    public function initialize($params) {

	// Get the finders and their dropdowns
	$dropdownTable = $this->tablename('am_finder_dropdown');
	$sql = "SELECT finder_id, dropdown_id\n" .
	       "FROM $dropdownTable\n" .
               "ORDER BY pos";

	foreach ($this->selectAll($sql) as $row) {
            $finderId      = $row['finder_id'];
            $dropdownId    = $row['dropdown_id'];

            // Create the finder's dropdown array if there isn't one
            if (!isset($this->_finders[$finderId])) {
                $this->_finders[$finderId] = array();
            }

            $this->_finders[$finderId][] = $dropdownId;
	}
    }


    /* CSV column looks like:
    finder:42
    Toyota,2004,Matrix|Toyota,2010,Matrix
    Chevrolet,2010,S10
    */
    public function processItemAfterId(&$item, $params = null) {
        
        // Look for "finder:" prefixed columns and process them
	foreach ($item as $columnName => $columnValue) {
            
            // Skip non-finder columns
            if (strpos($columnName, $this->_columnPrefix) !== 0) {
                continue;
            }

            // Strip off the column prefix to get the finder's ID
            $finderId = substr($columnName, strlen($this->_columnPrefix));
            
            // Get the pipe-delimited mappings from the CSV column and lookup the corresponding value IDs
            $valueIds = $this->getValueIdsForMappings($finderId, explode('||', $columnValue));
            
            // Update the value mappings for the product
            if (!empty($valueIds)) {
                $this->mapValues($params['product_id'], $item['sku'], $valueIds);
            
                error_log("Mapped " . count($valueIds) . " values for finder $finderId");
            }
	}

        return true;
    }

    public function getValueIdsForMappings($finderId, $mappings) {
        $valueIds = array();
        
        // Get the value ID for each mapping
        foreach ($mappings as $mapping) {
            
            $mapping = trim($mapping);

            // Get the values of the mapping, it's comma delimited
            // Toyota,2004,Matrix
            $values = explode('!!', $mapping);
            
            // Get the IDs of the final dropdown values
            if (count($values) > 0 && !empty($values[0])) {
                $valueIds[] = $this->getFinalDropdownValueId($finderId, $values);
            }
        }

        return $valueIds;
    }

    // Gets the ID of the final dropdown value, we'll be mapping to this value
    private function getFinalDropdownValueId($finderId, $values) {

        // Get the finder's dropdowns
        $dropdowns = $this->_finders[$finderId];
        if (!is_array($dropdowns)) {
            throw new Exception("No dropdowns for finder $finderId");
        }

        if (count($dropdowns) != count($values)) {
            throw new Exception("Expected " . count($dropdowns) . " values, got: " . join(',', $values));
        }
        
        // The easy way
        $finalValueId = $this->selectFinalDropdownValueId($finderId, $values, $dropdowns);
        
        // The hard way
        if (!$finalValueId) {
            $finalValueId = $this->createDropdownValues($finderId, $values, $dropdowns);
            error_log("Created finder $finderId mapping " . join(',', $values) . "$finalValueId");
        } else {
            error_log("Found finder $finderId mapping " . join(',', $values) . "$finalValueId");
        }
        
        return $finalValueId;
    }

    public function mapValues($productId, $sku, $valueIds) {
	$mapTable = $this->tablename('am_finder_map');

        // Create any missing mappings
        $mappingInsertPlaceholders = array();
        $mappingInsertData = array();
        
        foreach ($valueIds as $valueId) {
            $mappingInsertPlaceholders[] = "(?, ?, ?)";
            $mappingInsertData[] = $valueId;
            $mappingInsertData[] = $productId;
            $mappingInsertData[] = $sku;
//            error_log("Mapping valueId: $valueId, productId: $productId, sku: $sku");
        }

        $this->insert(
                "INSERT IGNORE INTO $mapTable (value_id, pid, sku) VALUES " . join(',', $mappingInsertPlaceholders),
                $mappingInsertData);

        // Delete old mappings
        $this->delete(
                "DELETE FROM $mapTable WHERE pid = ? AND value_id NOT IN (" . join(',', $valueIds) . ")",
                $productId);
    }
    
    public function createDropdownValues($finderId, $values, $dropdowns) {
	$valueTable = $this->tablename('am_finder_value');

        // Create the values if they don't exist
        $parentId = 0;
        for ($i = 0; $i < count($dropdowns); $i++) {
            $dropdown = $dropdowns[$i];
            $dropdownValue = $values[$i];

            // Make sure we're dealing with a valid value
            if (empty($dropdownValue)) {
                throw new Exception("Invalid dropdown value.");
            }

            // Create the value
            $this->insert(
                "INSERT IGNORE INTO $valueTable (parent_id, dropdown_id, name) VALUES (?, ?, ?)",
                array($parentId, $dropdown, $dropdownValue));

            // Get the value's ID, it's the parent for the next dropdown value
            $parentId = $this->selectone(
                "SELECT value_id FROM $valueTable WHERE parent_id = ? AND dropdown_id = ? and name = ?",
                array($parentId, $dropdown, $dropdownValue), "value_id");
            
            if ($parentId == null) {
                throw new Exception("Could not lookup value chain for finder $finderId mapping: " . join (',', $values));
            }
        }

        // This is the ID of the last dropdown value
        return $parentId;
    }

    // Builds a dynamic query for the final value
    public function selectFinalDropdownValueId($finderId, $values, $dropdowns) {
        $count = count($dropdowns);
	$valueTable = $this->tablename('am_finder_value');
        
        $query = "SELECT v" . ($count-1) . ".value_id AS finalValueId ";
        
        // Build the FROM/JOIN and WHERE clauses
        for ($i = 0; $i < $count; $i++) {
            $tableName = "v$i";
            
            if ($i == 0) {
                $query .= " FROM $valueTable AS $tableName ";
            } else {
                $parentTableName = "v" . ($i - 1);
                $query .= " JOIN $valueTable AS $tableName ON $tableName.parent_id = $parentTableName.value_id ";
            }
            
            // Add the where clauses for the dropdown ID and value
            $where[] = " $tableName.dropdown_id = {$dropdowns[$i]} AND $tableName.name = ? ";
        }
        
        $query .= " WHERE " . join(" AND ", $where);
        
//        error_log("optimistic select: " . $query . " values: " . print_r($values, true));
        
        return $this->selectone($query, $values, "finalValueId");
    }

}
?>
