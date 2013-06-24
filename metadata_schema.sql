SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `turbointernational`
  CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `turbointernational`;
-- -------------------------------------
-- Tables

DROP TABLE IF EXISTS `PART_ATTRIBUTE`;
CREATE TABLE `PART_ATTRIBUTE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_id` INT(10) NULL,
  `attribute_type_id` INT(10) NULL,
  `value` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  UNIQUE INDEX `parts_id_attribute_types_id` (`part_id`, `attribute_type_id`),
  FOREIGN KEY (`attribute_type_id`) REFERENCES `ATTRIBUTE_TYPE` (`ID`),
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `KIT_TYPE`;
CREATE TABLE `KIT_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `import_pk` INT(10) NULL,
  PRIMARY KEY (`ID`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `MANFR_TYPE`;
CREATE TABLE `MANFR_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `ID` (`ID`),
  UNIQUE INDEX `name` (`name`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `MANFR`;
CREATE TABLE `MANFR` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `manfr_type_id` INT(10) NOT NULL,
  `parent_manfr_id` INT(10) NULL,
  `import_pk` INT(10) NULL,
  PRIMARY KEY (`ID`),
  INDEX `ID` (`ID`),
  INDEX `manfr_type_id` (`manfr_type_id`),
  INDEX `MANFR_TYPESMANFR` (`manfr_type_id`),
  UNIQUE INDEX `name` (`name`),
  INDEX `parent_manfr_id` (`parent_manfr_id`),
  CONSTRAINT `MANFR_FK00` FOREIGN KEY `MANFR_FK00` (`manfr_type_id`)
    REFERENCES `MANFR_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `MANFR_FK01` FOREIGN KEY `MANFR_FK01` (`parent_manfr_id`)
    REFERENCES `MANFR` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `INTERCHANGE_ITEM`;
CREATE TABLE `INTERCHANGE_ITEM` (
  `part_id` INT(10) NOT NULL,
  `interchange_header_id` INT(10) NOT NULL,
  PRIMARY KEY (`part_id`),
  INDEX `interchange_header_id` (`interchange_header_id`),
  INDEX `INTERCHANGE_HEADERINTERCHANGE_I` (`interchange_header_id`),
  INDEX `parts_id` (`part_id`),
  UNIQUE INDEX `PARTSINTERCHANGE_ITEMS` (`part_id`),
  CONSTRAINT `INTERCHANGE_ITEM_FK00` FOREIGN KEY `INTERCHANGE_ITEM_FK00` (`interchange_header_id`)
    REFERENCES `INTERCHANGE_HEADER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `INTERCHANGE_ITEM_FK01` FOREIGN KEY `INTERCHANGE_ITEM_FK01` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `BOM_ALT_HEADER`;
CREATE TABLE `BOM_ALT_HEADER` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `description` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID`),
  INDEX `ID` (`ID`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `BOM`;
CREATE TABLE `BOM` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `parent_part_id` INT(10) NOT NULL,
  `child_part_id` INT(10) NOT NULL,
  `quantity` INT(10) NOT NULL,
  UNIQUE INDEX `parts_id_partent_child` (`parent_part_id`, `child_part_id`),
  FOREIGN KEY (`parent_part_id`) REFERENCES `PART` (`ID`)
  FOREIGN KEY (`child_part_id`) REFERENCES `PART` (`ID`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `ATTRIBUTE_TYPE`;
CREATE TABLE `ATTRIBUTE_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_types_id` INT(10) NULL,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  INDEX `part_types_id` (`part_types_id`),
  UNIQUE INDEX `part_types_id_name` (`part_types_id`, `name`),
  FOREIGN KEY `PART_TYPESATTRIBUTE_TYPES` (`part_types_id`) REFERENCES `PART_TYPE` (`ID`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `BOM_ALT_ITEM`;
CREATE TABLE `BOM_ALT_ITEM` (
  `bom_alt_header_id` INT(10) NOT NULL,
  `bom_id` INT(10) NOT NULL,
  `part_id` INT(10) NOT NULL,
  PRIMARY KEY (`bom_alt_header_id`, `bom_id`),
  INDEX `bom_alt_header_id` (`bom_alt_header_id`),
  UNIQUE INDEX `bom_alt_header_id_bom_id` (`bom_alt_header_id`, `bom_id`),
  INDEX `BOM_ALT_HEADERBOM_ALT_ITEMS` (`bom_alt_header_id`),
  INDEX `bom_id` (`bom_id`),
  INDEX `BOMBOM_ALT_ITEMS` (`bom_id`),
  INDEX `parts_id` (`part_id`),
  INDEX `PARTSBOM_ALT_ITEMS` (`part_id`),
  CONSTRAINT `BOM_ALT_ITEM_FK00` FOREIGN KEY `BOM_ALT_ITEM_FK00` (`bom_alt_header_id`)
    REFERENCES `BOM_ALT_HEADER` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `BOM_ALT_ITEM_FK01` FOREIGN KEY `BOM_ALT_ITEM_FK01` (`bom_id`)
    REFERENCES `BOM` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `BOM_ALT_ITEM_FK02` FOREIGN KEY `BOM_ALT_ITEM_FK02` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `TURBO_TYPE`;
CREATE TABLE `TURBO_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `manfr_id` INT(10) NOT NULL,
  `import_pk` INT(10) NULL,
  PRIMARY KEY (`ID`),
  INDEX `ID` (`ID`),
  INDEX `manfr_id` (`manfr_id`),
  INDEX `MANFRTURBO_TYPE` (`manfr_id`),
  CONSTRAINT `TURBO_TYPE_FK00` FOREIGN KEY `TURBO_TYPE_FK00` (`manfr_id`)
    REFERENCES `MANFR` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `SEAL_TYPE`;
CREATE TABLE `SEAL_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `import_pk` INT(10) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `uniq_seal_type_name` (`name`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `COOL_TYPE`;
CREATE TABLE `COOL_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `import_pk` INT(10) NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `IX_COOL_TYPE` (`name`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `GASKET_TYPE`;
CREATE TABLE `GASKET_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `import_pk` INT(10) NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `uniq_gasket_type_name` (`name`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `KIT`;
CREATE TABLE `KIT` (
  `PART_ID` INT(10) NOT NULL,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `kit_type_id` INT(10) NOT NULL,
  PRIMARY KEY (`PART_ID`),
  CONSTRAINT `FK_KIT_KIT_TYPE` FOREIGN KEY `FK_KIT_KIT_TYPE` (`kit_type_id`)
    REFERENCES `KIT_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_KIT_PART` FOREIGN KEY `FK_KIT_PART` (`PART_ID`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `TURBINE_WHEEL`;
CREATE TABLE `TURBINE_WHEEL` (
  `part_id` INT(10) NOT NULL,
  `exduce_oa` DECIMAL(18, 6) NULL,
  `tip_height_b` DECIMAL(18, 6) NULL,
  `inducer_oc` DECIMAL(18, 6) NULL,
  `journal_od` DECIMAL(18, 6) NULL,
  `stem_oe` DECIMAL(18, 6) NULL,
  `trim_no_blades` VARCHAR(100) NULL,
  `shaft_thread_f` VARCHAR(100) NULL,
  PRIMARY KEY (`part_id`),
  CONSTRAINT `FK_TURBINE_WHEEL_PART` FOREIGN KEY `FK_TURBINE_WHEEL_PART` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `COMPRESSOR_WHEEL`;
CREATE TABLE `COMPRESSOR_WHEEL` (
  `part_id` INT(10) NOT NULL,
  `inducer_oa` DECIMAL(18, 6) NULL,
  `tip_height_b` DECIMAL(18, 6) NULL,
  `exducer_oc` DECIMAL(18, 6) NULL,
  `hub_length_d` DECIMAL(18, 6) NULL,
  `bore_oe` DECIMAL(18, 6) NULL,
  `trim_no_blades` VARCHAR(100) NULL,
  `application` VARCHAR(100) NULL,
  PRIMARY KEY (`part_id`),
  CONSTRAINT `FK_COMPRESSOR_WHEEL_PART` FOREIGN KEY `FK_COMPRESSOR_WHEEL_PART` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `HEATSHIELD`;
CREATE TABLE `HEATSHIELD` (
  `part_id` INT(10) NOT NULL,
  `overall_diameter` DECIMAL(18, 6) NULL,
  `inside_diameter` DECIMAL(18, 6) NULL,
  `inducer_diameter` DECIMAL(18, 6) NULL,
  `notes` VARCHAR(500) NULL,
  PRIMARY KEY (`part_id`),
  CONSTRAINT `FK_HEATSHIELD_PART` FOREIGN KEY `FK_HEATSHIELD_PART` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `NOZZLE_RING`;
CREATE TABLE `NOZZLE_RING` (
  `part_id` INT(10) NOT NULL,
  PRIMARY KEY (`part_id`),
  CONSTRAINT `FK_NOZZLE_RING_PART` FOREIGN KEY `FK_NOZZLE_RING_PART` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `BACKPLATE`;
CREATE TABLE `BACKPLATE` (
  `part_id` INT(10) NOT NULL,
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
  PRIMARY KEY (`part_id`),
  CONSTRAINT `FK_BACKPLATE_PART` FOREIGN KEY `FK_BACKPLATE_PART` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `BEARING_HOUSING`;
CREATE TABLE `BEARING_HOUSING` (
  `part_id` INT(10) NOT NULL,
  `cool_type_id` INT(10) NULL,
  `oil_inlet` VARCHAR(100) NULL,
  `oil_outlet` VARCHAR(100) NULL,
  `oil` VARCHAR(100) NULL,
  `outlet_flange_holes` VARCHAR(100) NULL,
  `water_ports` VARCHAR(100) NULL,
  `design_features` VARCHAR(100) NULL,
  `bearing_type` VARCHAR(100) NULL,
  PRIMARY KEY (`part_id`),
  CONSTRAINT `FK_BEARING_HOUSING_PART` FOREIGN KEY `FK_BEARING_HOUSING_PART` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `GASKET`;
CREATE TABLE `GASKET` (
  `part_id` INT(10) NOT NULL,
  `gasket_type_id` INT(10) NOT NULL,
  PRIMARY KEY (`part_id`),
  CONSTRAINT `FK_GASKET_GASKET_TYPE` FOREIGN KEY `FK_GASKET_GASKET_TYPE` (`gasket_type_id`)
    REFERENCES `GASKET_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_GASKET_PART` FOREIGN KEY `FK_GASKET_PART` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `BOM_HIERARCHY`;
CREATE TABLE `BOM_HIERARCHY` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `child_part_type_id` INT(10) NOT NULL,
  `parent_part_type_id` INT(10) NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `ID` (`ID`),
  INDEX `parent_part_type_id` (`parent_part_type_id`),
  UNIQUE INDEX `part_type_id_parent_part_type_id` (`child_part_type_id`, `parent_part_type_id`),
  INDEX `PART_TYPESBOM_HIERARCHY` (`child_part_type_id`),
  INDEX `PART_TYPESBOM_HIERARCHY1` (`parent_part_type_id`),
  CONSTRAINT `BOM_HIERARCHY_FK00` FOREIGN KEY `BOM_HIERARCHY_FK00` (`child_part_type_id`)
    REFERENCES `PART_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `BOM_HIERARCHY_FK01` FOREIGN KEY `BOM_HIERARCHY_FK01` (`parent_part_type_id`)
    REFERENCES `PART_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `STANDARD_BEARING_SPACER`;
CREATE TABLE `STANDARD_BEARING_SPACER` (
  `standard_part_id` INT(10) NOT NULL,
  `oversized_part_id` INT(10) NOT NULL,
  PRIMARY KEY (`standard_part_id`, `oversized_part_id`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `CARTRIDGE`;
CREATE TABLE `CARTRIDGE` (
  `PART_ID` INT(10) NOT NULL,
  PRIMARY KEY (`PART_ID`),
  CONSTRAINT `FK_CARTRIDGE_PART` FOREIGN KEY `FK_CARTRIDGE_PART` (`PART_ID`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `BEARING_SPACER`;
CREATE TABLE `BEARING_SPACER` (
  `part_id` INT(10) NOT NULL,
  `outside_dim_min` DECIMAL(18, 6) NULL,
  `outside_dim_max` DECIMAL(18, 6) NULL,
  `inside_dim_min` DECIMAL(18, 6) NULL,
  `inside_dim_max` DECIMAL(18, 6) NULL,
  PRIMARY KEY (`part_id`),
  CONSTRAINT `FK_BEARING_SPACER_PART` FOREIGN KEY `FK_BEARING_SPACER_PART` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `PISTON_RING`;
CREATE TABLE `PISTON_RING` (
  `part_id` INT(10) NOT NULL,
  `outside_dim_min` DECIMAL(18, 6) NULL,
  `outside_dim_max` DECIMAL(18, 6) NULL,
  `width_min` DECIMAL(18, 6) NULL,
  `width_max` DECIMAL(18, 6) NULL,
  `i_gap_min` DECIMAL(18, 6) NULL,
  `i_gap_max` DECIMAL(18, 6) NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `STANDARD_JOURNAL_BEARING`;
CREATE TABLE `STANDARD_JOURNAL_BEARING` (
  `standard_part_id` INT(10) NOT NULL,
  `oversized_part_id` INT(10) NOT NULL,
  PRIMARY KEY (`standard_part_id`, `oversized_part_id`),
  CONSTRAINT `FK_STANDARD_JOURNAL_BEARING_JOURNAL_BEARING` FOREIGN KEY `FK_STANDARD_JOURNAL_BEARING_JOURNAL_BEARING` (`standard_part_id`)
    REFERENCES `JOURNAL_BEARING` (`part_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_STANDARD_JOURNAL_BEARING_JOURNAL_BEARING1` FOREIGN KEY `FK_STANDARD_JOURNAL_BEARING_JOURNAL_BEARING1` (`oversized_part_id`)
    REFERENCES `JOURNAL_BEARING` (`part_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `JOURNAL_BEARING`;
CREATE TABLE `JOURNAL_BEARING` (
  `part_id` INT(10) NOT NULL,
  `outside_dim_min` DECIMAL(18, 6) NOT NULL,
  `outside_dim_max` DECIMAL(18, 6) NOT NULL,
  `inside_dim_min` DECIMAL(18, 6) NOT NULL,
  `inside_dim_max` DECIMAL(18, 6) NOT NULL,
  `width` DECIMAL(18, 6) NOT NULL,
  PRIMARY KEY (`part_id`),
  CONSTRAINT `FK_JOURNAL_BEARING_PART` FOREIGN KEY `FK_JOURNAL_BEARING_PART` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `PART_TURBO_TYPE`;
CREATE TABLE `PART_TURBO_TYPE` (
  `part_id` INT(10) NOT NULL,
  `turbo_type_id` INT(10) NOT NULL,
  PRIMARY KEY (`part_id`, `turbo_type_id`),
  CONSTRAINT `FK_PART_TURBO_TYPE_PART` FOREIGN KEY `FK_PART_TURBO_TYPE_PART` (`part_id`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_PART_TURBO_TYPE_TURBO_TYPE` FOREIGN KEY `FK_PART_TURBO_TYPE_TURBO_TYPE` (`turbo_type_id`)
    REFERENCES `TURBO_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `PART`;
CREATE TABLE `PART` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `manfr_part_num` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `manfr_id` INT(10) NOT NULL,
  `part_type_id` INT(10) NOT NULL,
  `Name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `description` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `inactive` TINYINT NOT NULL DEFAULT (0),
  `import_pk` INT(10) NULL,
  `temp1_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `temp2_int` INT(10) NULL,
  `temp3_int` INT(10) NULL,
  `temp1_dec` DECIMAL(18, 6) NULL,
  `temp2_dec` DECIMAL(18, 6) NULL,
  `temp3_dec` DECIMAL(18, 6) NULL,
  `temp4_dec` DECIMAL(18, 6) NULL,
  `temp5_dec` DECIMAL(18, 6) NULL,
  `temp6_dec` DECIMAL(18, 6) NULL,
  `temp2_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `temp3_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `temp4_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `temp5_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `temp6_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `temp7_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `temp8_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID`),
  INDEX `manfr_id` (`manfr_id`),
  INDEX `manfr_id_manfr_part_id` (`manfr_id`, `manfr_part_num`),
  INDEX `manfr_part_id` (`manfr_part_num`),
  INDEX `MANFRPARTS` (`manfr_id`),
  INDEX `part_type_id` (`part_type_id`),
  INDEX `PART_TYPESPARTS` (`part_type_id`),
  CONSTRAINT `PART_FK00` FOREIGN KEY `PART_FK00` (`manfr_id`)
    REFERENCES `MANFR` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `PART_FK01` FOREIGN KEY `PART_FK01` (`part_type_id`)
    REFERENCES `PART_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `INTERCHANGE_HEADER`;
CREATE TABLE `INTERCHANGE_HEADER` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `description` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID`),
  INDEX `ID` (`ID`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `i_interchange_log`;
CREATE TABLE `i_interchange_log` (
  `id` INT(10) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `part_num_id` INT(10) NULL,
  `interchange_header_id` INT(10) NULL,
  `interchange_cnt_uniq` INT(10) NULL,
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `TURBO`;
CREATE TABLE `TURBO` (
  `PART_ID` INT(10) NOT NULL,
  `turbo_model_id` INT(10) NOT NULL,
  `cool_type_id` INT(10) NULL,
  PRIMARY KEY (`PART_ID`),
  CONSTRAINT `FK_TURBO_PART` FOREIGN KEY `FK_TURBO_PART` (`PART_ID`)
    REFERENCES `PART` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_TURBO_COOL_TYPE` FOREIGN KEY `FK_TURBO_COOL_TYPE` (`cool_type_id`)
    REFERENCES `COOL_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_TURBO_TURBO_MODEL` FOREIGN KEY `FK_TURBO_TURBO_MODEL` (`turbo_model_id`)
    REFERENCES `TURBO_MODEL` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `TURBO_MODEL`;
CREATE TABLE `TURBO_MODEL` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `turbo_type_id` INT(10) NULL,
  `import_pk` INT(10) NULL,
  PRIMARY KEY (`ID`),
  INDEX `ID` (`ID`),
  INDEX `turbo_type_id` (`turbo_type_id`),
  INDEX `TURBO_TYPETURBO_MODEL` (`turbo_type_id`),
  CONSTRAINT `TURBO_MODEL_FK00` FOREIGN KEY `TURBO_MODEL_FK00` (`turbo_type_id`)
    REFERENCES `TURBO_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `PART_TYPE`;
CREATE TABLE `PART_TYPE` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `parent_part_type_id` INT(10) NULL,
  `import_pk` INT(10) NULL,
  PRIMARY KEY (`ID`),
  INDEX `ID` (`ID`),
  INDEX `parent_part_type_id` (`parent_part_type_id`),
  CONSTRAINT `FK_PART_TYPE_PART_TYPE` FOREIGN KEY `FK_PART_TYPE_PART_TYPE` (`parent_part_type_id`)
    REFERENCES `PART_TYPE` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;



SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------
-- EOF

