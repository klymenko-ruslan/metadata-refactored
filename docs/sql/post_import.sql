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

ALTER TABLE `part_type` ADD COLUMN `magento_category_id` INTEGER;

DROP TABLE IF EXISTS `part_turbo_type`;
DROP TABLE IF EXISTS `part_attribute`;
DROP TABLE IF EXISTS `attribute_type`;
DROP TABLE IF EXISTS `bom_hierarchy`;
DROP TABLE IF EXISTS `sql server destination`;


UPDATE `part_type` SET DTYPE = "Turbo" WHERE ID = 1;
UPDATE `part_type` SET DTYPE = "Cartridge" WHERE ID = 2;
UPDATE `part_type` SET DTYPE = "Kit" WHERE ID = 3;
UPDATE `part_type` SET DTYPE = "PistonRing" WHERE ID = 4;
UPDATE `part_type` SET DTYPE = "JournalBearing" WHERE ID = 5;
UPDATE `part_type` SET DTYPE = "Gasket" WHERE ID = 6;
UPDATE `part_type` SET DTYPE = "BearingSpacer" WHERE ID = 7;
UPDATE `part_type` SET DTYPE = "CompressorWheel" WHERE ID = 11;
UPDATE `part_type` SET DTYPE = "TurbineWheel" WHERE ID = 12;
UPDATE `part_type` SET DTYPE = "BearingHousing" WHERE ID = 13;
UPDATE `part_type` SET DTYPE = "Backplate" WHERE ID = 14;
UPDATE `part_type` SET DTYPE = "Heatshield" WHERE ID = 15;
UPDATE `part_type` SET DTYPE = "NozzleRing" WHERE ID = 16;

-- Magento part type stuff
UPDATE `part_type` SET
    `magento_attribute_set_id`= 10,
    `magento_category_id` =  13
WHERE `DTYPE`='Part';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 13,
    `magento_category_id` = 6
WHERE `DTYPE`='Cartridge';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 26,
    `magento_category_id` = 16
WHERE `DTYPE`='Turbo';

UPDATE `part_type` SET 
    `magento_attribute_set_id`= 22,
    `magento_category_id` = 11
WHERE `DTYPE`='Kit';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 24,
    `magento_category_id` = 14
WHERE `DTYPE`='PistonRing';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 20,
    `magento_category_id` = 10
WHERE `DTYPE`='JournalBearing';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 17,
    `magento_category_id` = 8
WHERE `DTYPE`='Gasket';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 15,
    `magento_category_id` = 5
WHERE `DTYPE`='BearingSpacer';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 16,
    `magento_category_id` = 7
WHERE `DTYPE`='CompressorWheel';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 25,
    `magento_category_id` = 15
WHERE `DTYPE`='TurbineWheel';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 14,
    `magento_category_id` = 4
WHERE `DTYPE`='BearingHousing';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 12,
    `magento_category_id` = 2
WHERE `DTYPE`='Backplate';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 19,
    `magento_category_id` = 9
WHERE `DTYPE`='Heatshield';

UPDATE `part_type` SET
    `magento_attribute_set_id`= 23,
    `magento_category_id` = 12
WHERE `DTYPE`='NozzleRing';


UPDATE `part` SET `part`.`dtype` = (SELECT COALESCE(`part_type`.`dtype`, 'Part') FROM `part_type` WHERE `id` = `part`.`part_type_id`);