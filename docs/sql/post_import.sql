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
DROP TABLE IF EXISTS `ATTRIBUTE_TYPE`;
DROP TABLE IF EXISTS `PART_ATTRIBUTE`;
DROP TABLE IF EXISTS `BOM_HIERARCHY`;
DROP TABLE IF EXISTS `sql server destination`;