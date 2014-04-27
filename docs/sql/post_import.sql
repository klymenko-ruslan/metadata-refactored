-- There are zero-valued car_model_engine_year entries
ALTER TABLE car_model_engine_year CHANGE COLUMN car_model_id `car_model_id` BIGINT NULL;
UPDATE car_model_engine_year
SET car_model_id = null
WHERE car_model_id = 0;

ALTER TABLE car_model_engine_year CHANGE COLUMN car_engine_id `car_engine_id` BIGINT NULL;
UPDATE car_model_engine_year
SET car_engine_id = null
WHERE car_engine_id= 0;

ALTER TABLE car_model_engine_year CHANGE COLUMN car_year_id `car_year_id` BIGINT NULL;
UPDATE car_model_engine_year
SET car_year_id = null
WHERE car_year_id = 0;

-- Add version columns and update PK
ALTER TABLE `part` ADD COLUMN `version` INTEGER NOT NULL DEFAULT 1;
ALTER TABLE `part` ADD UNIQUE KEY (`id`, `version`);

ALTER TABLE `bom_alt_item` ADD COLUMN `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY;


-- Remove duplicate of part.name
ALTER TABLE `kit` DROP COLUMN `name`;

-- Remove temporary fields
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

-- Unused tables
DROP TABLE IF EXISTS `bom_hierarchy`;
DROP TABLE IF EXISTS `part_attribute`;
DROP TABLE IF EXISTS `attribute_type`;
DROP TABLE IF EXISTS `sql server destination`;

--
-- Part types
--
ALTER TABLE `part_type` ADD COLUMN `magento_attribute_set` VARCHAR(50);

-- Default
SET @partType = 'Part';
UPDATE `part_type` SET `magento_attribute_set`= @partType;

UPDATE `part_type` SET
    `name`= 'Turbo',
    `magento_attribute_set`= 'Turbo'
WHERE ID = 1;

UPDATE `part_type` SET
    `name`= 'Cartridge',
    `magento_attribute_set`= 'Cartridge'
WHERE ID = 2;

UPDATE `part_type` SET
    `name`= 'Kit',
    `magento_attribute_set`= 'Kit'
WHERE ID = 3;

UPDATE `part_type` SET
    `name`= 'Piston Ring',
    `magento_attribute_set`= 'PistonRing'
WHERE ID = 4;

UPDATE `part_type` SET
    `name`= 'Journal Bearing',
    `magento_attribute_set`= 'JournalBearing'
WHERE ID = 5;

UPDATE `part_type` SET
    `name`= 'Gasket',
    `magento_attribute_set`= 'Gasket'
WHERE ID = 6;

UPDATE `part_type` SET
    `name`= 'Bearing Spacer',
    `magento_attribute_set`= 'BearingSpacer'
WHERE ID = 7;

UPDATE `part_type` SET
    `name`= 'Fast Wearing Component',
    `magento_attribute_set`= 'Part'
WHERE ID = 8;

UPDATE `part_type` SET
    `name`= 'Major Component',
    `magento_attribute_set`= 'Part'
WHERE ID = 9;

UPDATE `part_type` SET
    `name`= 'Minor Component',
    `magento_attribute_set`= 'Part'
WHERE ID = 10;

UPDATE `part_type` SET
    `name`= 'Compressor Wheel',
    `magento_attribute_set`= 'CompressorWheel'
WHERE ID = 11;

UPDATE `part_type` SET
    `name`= 'Turbine Wheel',
    `magento_attribute_set`= 'TurbineWheel'
WHERE ID = 12;

UPDATE `part_type` SET
    `name`= 'Bearing Housing',
    `magento_attribute_set`= 'BearingHousing'
WHERE ID = 13;

UPDATE `part_type` SET
    `name`= 'Backplate / Sealplate',
    `magento_attribute_set`= 'Backplate'
WHERE ID = 14;

UPDATE `part_type` SET
    `name`= 'Heatshield / Shroud',
    `magento_attribute_set`= 'Heatshield'
WHERE ID = 15;

UPDATE `part_type` SET
    `name`= 'Nozzle Ring',
    `magento_attribute_set`= 'NozzleRing'
WHERE ID = 16;

UPDATE `part_type` SET
    `name`= 'O Ring',
    `magento_attribute_set`= 'Part'
WHERE ID = 17;

UPDATE `part_type` SET
    `name`= 'Oil Deflector',
    `magento_attribute_set`= 'Part'
WHERE ID = 18;

UPDATE `part_type` SET
    `name`= 'Clamp',
    `magento_attribute_set`= 'Part'
WHERE ID = 19;

UPDATE `part_type` SET
    `name`= 'Thrust Parts',
    `magento_attribute_set`= 'Part'
WHERE ID = 20;

UPDATE `part_type` SET
    `name`= 'Miscellaneous Minor Components',
    `magento_attribute_set`= 'Part'
WHERE ID = 21;

-- Delete bad part-type data
DELETE FROM `turbo` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 1);
DELETE FROM `cartridge` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 2);
DELETE FROM `kit` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 3);
DELETE FROM `piston_ring` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 4);  -- 4837 of these in the import data
DELETE FROM `journal_bearing` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 5);
DELETE FROM `gasket` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 6);
DELETE FROM `bearing_spacer` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 7);
DELETE FROM `compressor_wheel` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 11);
DELETE FROM `turbine_wheel` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 12);
DELETE FROM `bearing_housing` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 13);
DELETE FROM `backplate` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 14);
DELETE FROM `heatshield` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 15);
DELETE FROM `nozzle_ring` WHERE part_id NOT IN (SELECT id FROM part WHERE part_type_id = 16);

-- Reset BOM items with quantity 999 to 1
UPDATE `bom` SET quantity = 1 WHERE quantity = 999;

-- Rebuild the BOM ancestry
CALL rebuildBomAncestry();
