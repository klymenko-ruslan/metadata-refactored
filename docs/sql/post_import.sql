ALTER TABLE `kit` DROP COLUMN `name`;


ALTER TABLE `part`
  DROP COLUMN `temp1_char`,
  DROP COLUMN `temp2_int`,
  DROP COLUMN `temp3_int`,
  DROP COLUMN `temp1_dec`,
  DROP COLUMN `temp2_dec`,
  DROP COLUMN `temp3_dec`,
  DROP COLUMN `temp4_dec`,
  DROP COLUMN `temp5_dec`,
  DROP COLUMN `temp6_dec`,
  DROP COLUMN `temp2_char`,
  DROP COLUMN `temp3_char`,
  DROP COLUMN `temp4_char`,
  DROP COLUMN `temp5_char`,
  DROP COLUMN `temp6_char`,
  DROP COLUMN `temp7_char`,
  DROP COLUMN `temp8_char`;

DROP TABLE IF EXISTS `part_attribute`;
DROP TABLE IF EXISTS `attribute_type`;
DROP TABLE IF EXISTS `bom_hierarchy`;
DROP TABLE IF EXISTS `sql server destination`;


-- Default
SET @partType = 'Part';
UPDATE `part_type` SET
    `DTYPE` = @partType,
    `magento_attribute_set`= @partType,
    `magento_category`= @partType;


-- Kit and Cartridge have BOM
SET @partType = 'Cartridge';
UPDATE `part_type` SET
    `DTYPE` = @partType,
    `magento_attribute_set`= @partType,
    `magento_category`= @partType
WHERE ID = 2;

SET @partType = 'Kit';
UPDATE `part_type` SET
    `DTYPE` = @partType,
    `magento_attribute_set`= @partType,
    `magento_category`= @partType
WHERE ID = 3;


-- One-word names
SET @partType = 'Turbo';
UPDATE `part_type` SET
    `DTYPE` = @partType,
    `magento_attribute_set`= @partType,
    `magento_category`= @partType
WHERE ID = 1;

SET @partType = 'Gasket';
UPDATE `part_type` SET
    `DTYPE` = @partType,
    `magento_attribute_set`= @partType,
    `magento_category`= @partType
WHERE ID = 6;

SET @partType = 'Backplate';
UPDATE `part_type` SET
    `DTYPE` = @partType,
    `magento_attribute_set`= @partType,
    `magento_category`= @partType
WHERE ID = 14;

SET @partType = 'Heatshield';
UPDATE `part_type` SET
    `DTYPE` = @partType,
    `magento_attribute_set`= @partType,
    `magento_category`= @partType
WHERE ID = 15;


-- Multiword names
UPDATE `part_type` SET DTYPE = "PistonRing" WHERE ID = 4;
UPDATE `part_type` SET DTYPE = "JournalBearing" WHERE ID = 5;
UPDATE `part_type` SET DTYPE = "BearingSpacer" WHERE ID = 7;
UPDATE `part_type` SET DTYPE = "CompressorWheel" WHERE ID = 11;
UPDATE `part_type` SET DTYPE = "TurbineWheel" WHERE ID = 12;
UPDATE `part_type` SET DTYPE = "BearingHousing" WHERE ID = 13;
UPDATE `part_type` SET DTYPE = "NozzleRing" WHERE ID = 16;

UPDATE `part_type` SET
    `magento_attribute_set`= 'Piston Ring',
    `magento_category`= 'Piston Ring'
WHERE `DTYPE`='PistonRing';

UPDATE `part_type` SET
    `magento_attribute_set`= 'Journal Bearing',
    `magento_category`= 'Journal Bearing'
WHERE `DTYPE`='JournalBearing';

UPDATE `part_type` SET
    `magento_attribute_set`= 'Bearing Spacer',
    `magento_category` = 'Bearing Spacer'
WHERE `DTYPE`='BearingSpacer';

UPDATE `part_type` SET
    `magento_attribute_set`= 'Compressor Wheel',
    `magento_category` = 'Compressor Wheel'
WHERE `DTYPE`='CompressorWheel';

UPDATE `part_type` SET
    `magento_attribute_set`= 'Turbine Wheel',
    `magento_category` = 'Turbine Wheel'
WHERE `DTYPE`='TurbineWheel';

UPDATE `part_type` SET
    `magento_attribute_set`= 'Bearing Housing',
    `magento_category` = 'Bearing Housing'
WHERE `DTYPE`='BearingHousing';

UPDATE `part_type` SET
    `magento_attribute_set`= 'Nozzle Ring',
    `magento_category` = 'Nozzle Ring'
WHERE `DTYPE`='NozzleRing';


-- Update part type discriminators, default part.dtype to 'Part'
UPDATE `part`
SET `part`.`dtype` = (
  SELECT
    COALESCE(`part_type`.`dtype`, 'Part')
    FROM `part_type`
    WHERE `id` = `part`.`part_type_id`
);

-- Reset BOM items with quantity 999 to 1
UPDATE `bom` SET quantity = 1 WHERE quantity = 999;

