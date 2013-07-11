CREATE DATABASE IF NOT EXISTS `ti` CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `ti`;

--
-- Manufacturer
--
CREATE TABLE `MANFR_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  UNIQUE INDEX (`name`)
) ENGINE = INNODB;

CREATE TABLE `MANFR` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `manfr_type_id` INT(10) NOT NULL,
  `parent_manfr_id` INT(10) NULL,
  `import_pk` INT(10) NULL,
  UNIQUE INDEX (`name`),
  FOREIGN KEY (`manfr_type_id`) REFERENCES `MANFR_TYPE` (`ID`),
  FOREIGN KEY (`parent_manfr_id`) REFERENCES `MANFR` (`ID`)
) ENGINE = INNODB;


--
-- Part
--
CREATE TABLE `PART_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `parent_part_type_id` INT(10) NULL,
  `import_pk` INT(10) NULL,
  FOREIGN KEY (`parent_part_type_id`) REFERENCES `PART_TYPE` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `PART` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `manfr_part_num` VARCHAR(255) NULL,
  `manfr_id` INT(10) NOT NULL,
  `part_type_id` INT(10) NOT NULL,
  `Name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `inactive` TINYINT NOT NULL DEFAULT 0,
  `import_pk` INT(10) NULL,
  `temp1_char` VARCHAR(255) NULL,
  `temp2_int` INT(10) NULL,
  `temp3_int` INT(10) NULL,
  `temp1_dec` DECIMAL(18, 6) NULL,
  `temp2_dec` DECIMAL(18, 6) NULL,
  `temp3_dec` DECIMAL(18, 6) NULL,
  `temp4_dec` DECIMAL(18, 6) NULL,
  `temp5_dec` DECIMAL(18, 6) NULL,
  `temp6_dec` DECIMAL(18, 6) NULL,
  `temp2_char` VARCHAR(255) NULL,
  `temp3_char` VARCHAR(255) NULL,
  `temp4_char` VARCHAR(255) NULL,
  `temp5_char` VARCHAR(255) NULL,
  `temp6_char` VARCHAR(255) NULL,
  `temp7_char` VARCHAR(255) NULL,
  `temp8_char` VARCHAR(255) NULL,
  INDEX (`manfr_part_num`, `manfr_id`),
  FOREIGN KEY (`manfr_id`) REFERENCES `MANFR` (`ID`),
  FOREIGN KEY (`part_type_id`) REFERENCES `PART_TYPE` (`ID`)
) ENGINE = INNODB;


--
-- Part Attributes
--
CREATE TABLE `ATTRIBUTE_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_types_id` INT(10) NULL,
  `name` VARCHAR(255) NOT NULL,
  UNIQUE INDEX (`part_types_id`, `name`),
  FOREIGN KEY (`part_types_id`) REFERENCES `PART_TYPE` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `PART_ATTRIBUTE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_id` INT(10) NULL,
  `attribute_type_id` INT(10) NULL,
  `value` VARCHAR(255) NULL,
  UNIQUE INDEX (`part_id`, `attribute_type_id`),
  FOREIGN KEY (`attribute_type_id`) REFERENCES `ATTRIBUTE_TYPE` (`ID`),
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;


--
-- Part Types
--
CREATE TABLE `COOL_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` INT(10) NULL,
  UNIQUE INDEX (`name`)
) ENGINE = INNODB;

CREATE TABLE `GASKET_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` INT(10) NULL,
  UNIQUE INDEX (`name`)
) ENGINE = INNODB;

CREATE TABLE `KIT_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(1000) NOT NULL,
  `import_pk` INT(10) NULL
) ENGINE = INNODB;

CREATE TABLE `SEAL_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` INT(10) NOT NULL,
  UNIQUE INDEX (`name`)
) ENGINE = INNODB;

CREATE TABLE `TURBO_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `manfr_id` INT(10) NOT NULL,
  `import_pk` INT(10) NULL,
  FOREIGN KEY (`manfr_id`) REFERENCES `MANFR` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `PART_TURBO_TYPE` (
  `part_id` INT(10) NOT NULL,
  `turbo_type_id` INT(10) NOT NULL,
  PRIMARY KEY (`part_id`, `turbo_type_id`),
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`),
  FOREIGN KEY (`turbo_type_id`) REFERENCES `TURBO_TYPE` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `TURBO_MODEL` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NULL,
  `turbo_type_id` INT(10) NULL,
  `import_pk` INT(10) NULL,
  FOREIGN KEY (`turbo_type_id`) REFERENCES `TURBO_TYPE` (`ID`)
) ENGINE = INNODB;


--
-- Interchanges
--
CREATE TABLE `INTERCHANGE_HEADER` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL
) ENGINE = INNODB;

CREATE TABLE `INTERCHANGE_ITEM` (
  `part_id` INT(10) NOT NULL PRIMARY KEY,
  `interchange_header_id` INT(10) NOT NULL,
  FOREIGN KEY (`interchange_header_id`) REFERENCES `INTERCHANGE_HEADER` (`ID`),
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `i_interchange_log` (
  `id` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `description` VARCHAR(250) NULL,
  `part_num_id` INT(10) NULL,
  `interchange_header_id` INT(10) NULL,
  `interchange_cnt_uniq` INT(10) NULL,
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = INNODB;


--
-- Bill of Materials
--
CREATE TABLE `BOM` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `parent_part_id` INT(10) NOT NULL,
  `child_part_id` INT(10) NOT NULL,
  `quantity` INT(10) NOT NULL,
  UNIQUE INDEX (`parent_part_id`, `child_part_id`),
  FOREIGN KEY (`parent_part_id`) REFERENCES `PART` (`ID`),
  FOREIGN KEY (`child_part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `BOM_ALT_HEADER` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL
) ENGINE = INNODB;

CREATE TABLE `BOM_ALT_ITEM` (
  `bom_alt_header_id` INT(10) NOT NULL,
  `bom_id` INT(10) NOT NULL,
  `part_id` INT(10) NOT NULL,
  PRIMARY KEY (`bom_alt_header_id`, `bom_id`),
  FOREIGN KEY (`bom_alt_header_id`) REFERENCES `BOM_ALT_HEADER` (`ID`),
  FOREIGN KEY (`bom_id`) REFERENCES `BOM` (`ID`),
  FOREIGN KEY (`part_id`)  REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `BOM_HIERARCHY` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `child_part_type_id` INT(10) NOT NULL,
  `parent_part_type_id` INT(10) NOT NULL,
  UNIQUE INDEX (`parent_part_type_id`, `child_part_type_id`),
  INDEX (`child_part_type_id`),
  INDEX (`parent_part_type_id`),
  FOREIGN KEY (`child_part_type_id`) REFERENCES `PART_TYPE` (`ID`),
  FOREIGN KEY (`parent_part_type_id`) REFERENCES `PART_TYPE` (`ID`)
) ENGINE = INNODB;


--
-- Parts
--
CREATE TABLE `BACKPLATE` (
  `part_id` INT(10) NOT NULL PRIMARY KEY,
  `seal_type_id` INT(10) NULL,
  `style_compressor_wheel` VARCHAR(100) NULL,
  `seal_type` VARCHAR(100) NULL,
  `overall_diameter` DECIMAL(18, 6) NULL,
  `compressor_wheel_diameter` DECIMAL(18, 6) NULL,
  `piston_ring_diameter` DECIMAL(18, 6) NULL,
  `compressor_housing_diameter` DECIMAL(18, 6) NULL,
  `notes` VARCHAR(500) NULL,
  `secondary_diameter` DECIMAL(18, 6) NULL,
  `overall_height` DECIMAL(18, 6) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `BEARING_HOUSING` (
  `part_id` INT(10) NOT NULL PRIMARY KEY,
  `cool_type_id` INT(10) NULL,
  `oil_inlet` VARCHAR(100) NULL,
  `oil_outlet` VARCHAR(100) NULL,
  `oil` VARCHAR(100) NULL,
  `outlet_flange_holes` VARCHAR(100) NULL,
  `water_ports` VARCHAR(100) NULL,
  `design_features` VARCHAR(100) NULL,
  `bearing_type` VARCHAR(100) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `BEARING_SPACER` (
  `part_id` INT(10) NOT NULL PRIMARY KEY,
  `outside_dim_min` DECIMAL(18, 6) NULL,
  `outside_dim_max` DECIMAL(18, 6) NULL,
  `inside_dim_min` DECIMAL(18, 6) NULL,
  `inside_dim_max` DECIMAL(18, 6) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `CARTRIDGE` (
  `PART_ID` INT(10) NOT NULL PRIMARY KEY,
  FOREIGN KEY (`PART_ID`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `COMPRESSOR_WHEEL` (
  `part_id` INT(10) NOT NULL PRIMARY KEY,
  `inducer_oa` DECIMAL(18, 6) NULL,
  `tip_height_b` DECIMAL(18, 6) NULL,
  `exducer_oc` DECIMAL(18, 6) NULL,
  `hub_length_d` DECIMAL(18, 6) NULL,
  `bore_oe` DECIMAL(18, 6) NULL,
  `trim_no_blades` VARCHAR(100) NULL,
  `application` VARCHAR(100) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `GASKET` (
  `part_id` INT(10) NOT NULL,
  `gasket_type_id` INT(10) NOT NULL,
  PRIMARY KEY (`part_id`),
  FOREIGN KEY (`gasket_type_id`) REFERENCES `GASKET_TYPE` (`ID`)
    ON UPDATE NO ACTION,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `HEATSHIELD` (
  `part_id` INT(10) NOT NULL PRIMARY KEY,
  `overall_diameter` DECIMAL(18, 6) NULL,
  `inside_diameter` DECIMAL(18, 6) NULL,
  `inducer_diameter` DECIMAL(18, 6) NULL,
  `notes` VARCHAR(500) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `JOURNAL_BEARING` (
  `part_id` INT(10) NOT NULL PRIMARY KEY,
  `outside_dim_min` DECIMAL(18, 6) NOT NULL,
  `outside_dim_max` DECIMAL(18, 6) NOT NULL,
  `inside_dim_min` DECIMAL(18, 6) NOT NULL,
  `inside_dim_max` DECIMAL(18, 6) NOT NULL,
  `width` DECIMAL(18, 6) NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `KIT` (
  `PART_ID` INT(10) NOT NULL PRIMARY KEY,
  `name` VARCHAR(255) NULL,
  `kit_type_id` INT(10) NOT NULL,
  FOREIGN KEY (`kit_type_id`) REFERENCES `KIT_TYPE` (`ID`),
  FOREIGN KEY (`PART_ID`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `NOZZLE_RING` (
  `part_id` INT(10) NOT NULL PRIMARY KEY,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `PISTON_RING` (
  `part_id` INT(10) NOT NULL PRIMARY KEY,
  `outside_dim_min` DECIMAL(18, 6) NULL,
  `outside_dim_max` DECIMAL(18, 6) NULL,
  `width_min` DECIMAL(18, 6) NULL,
  `width_max` DECIMAL(18, 6) NULL,
  `i_gap_min` DECIMAL(18, 6) NULL,
  `i_gap_max` DECIMAL(18, 6) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `STANDARD_BEARING_SPACER` (
  `standard_part_id` INT(10) NOT NULL,
  `oversized_part_id` INT(10) NOT NULL,
  PRIMARY KEY (`standard_part_id`, `oversized_part_id`),
  FOREIGN KEY (`standard_part_id`) REFERENCES `BEARING_SPACER` (`part_id`),
  FOREIGN KEY (`oversized_part_id`) REFERENCES `BEARING_SPACER` (`part_id`)
) ENGINE = INNODB;

CREATE TABLE `STANDARD_JOURNAL_BEARING` (
  `standard_part_id` INT(10) NOT NULL,
  `oversized_part_id` INT(10) NOT NULL,
  PRIMARY KEY (`standard_part_id`, `oversized_part_id`),
  FOREIGN KEY (`standard_part_id`) REFERENCES `JOURNAL_BEARING` (`part_id`),
  FOREIGN KEY (`oversized_part_id`) REFERENCES `JOURNAL_BEARING` (`part_id`)
) ENGINE = INNODB;

CREATE TABLE `TURBINE_WHEEL` (
  `part_id` INT(10) NOT NULL PRIMARY KEY,
  `exduce_oa` DECIMAL(18, 6) NULL,
  `tip_height_b` DECIMAL(18, 6) NULL,
  `inducer_oc` DECIMAL(18, 6) NULL,
  `journal_od` DECIMAL(18, 6) NULL,
  `stem_oe` DECIMAL(18, 6) NULL,
  `trim_no_blades` VARCHAR(100) NULL,
  `shaft_thread_f` VARCHAR(100) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `TURBO` (
  `PART_ID` INT(10) NOT NULL PRIMARY KEY,
  `turbo_model_id` INT(10) NOT NULL,
  `cool_type_id` INT(10) NULL,
  FOREIGN KEY (`PART_ID`) REFERENCES `PART` (`ID`),
  FOREIGN KEY (`cool_type_id`) REFERENCES `COOL_TYPE` (`ID`),
  FOREIGN KEY (`turbo_model_id`) REFERENCES `TURBO_MODEL` (`ID`)
) ENGINE = INNODB;
