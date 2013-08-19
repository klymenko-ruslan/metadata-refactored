ALTER TABLE `KIT` DROP COLUMN `name`;


ALTER TABLE `PART`
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


DROP TABLE IF EXISTS `PART_TURBO_TYPE`;
DROP TABLE IF EXISTS `PART_ATTRIBUTE`;
DROP TABLE IF EXISTS `ATTRIBUTE_TYPE`;
DROP TABLE IF EXISTS `BOM_HIERARCHY`;
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


UPDATE `PART` SET `PART`.`DTYPE` = (SELECT COALESCE(`PART_TYPE`.`DTYPE`, 'Part') FROM `PART_TYPE` WHERE `ID` = `PART`.`part_type_id`);