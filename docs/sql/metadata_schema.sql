DROP DATABASE IF EXISTS `ti`;
CREATE DATABASE IF NOT EXISTS `ti` CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON ti.* to ti@'localhost' IDENTIFIED BY 'ti';
USE `ti`;

--
-- Manufacturer
--
CREATE TABLE `MANFR_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `MANFR` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `manfr_type_id` BIGINT NOT NULL,
  `parent_manfr_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  FOREIGN KEY (`manfr_type_id`) REFERENCES `MANFR_TYPE` (`ID`),
  FOREIGN KEY (`parent_manfr_id`) REFERENCES `MANFR` (`ID`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;


--
-- Part
--
CREATE TABLE `PART_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `parent_part_type_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  `dtype` VARCHAR(50),
  `version` INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (`parent_part_type_id`) REFERENCES `PART_TYPE` (`ID`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `PART` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `manfr_part_num` VARCHAR(255) NULL,
  `manfr_id` BIGINT NOT NULL,
  `part_type_id` BIGINT NOT NULL,
  `Name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `inactive` BOOLEAN NOT NULL DEFAULT 0,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  `DTYPE` VARCHAR(50) DEFAULT 'Part',
  PRIMARY KEY(`ID`, `version`),
  INDEX (`manfr_part_num`, `manfr_id`),
  FOREIGN KEY (`manfr_id`) REFERENCES `MANFR` (`ID`),
  FOREIGN KEY (`part_type_id`) REFERENCES `PART_TYPE` (`ID`)
) ENGINE = INNODB;


-- BEGIN TRIGGERS
DELIMITER $$
CREATE TRIGGER dtype_BI
  BEFORE INSERT ON `PART`
  FOR EACH ROW
    BEGIN
      SET NEW.`DTYPE` = (SELECT COALESCE(`PART_TYPE`.`DTYPE`, 'Part') FROM `PART_TYPE` WHERE `ID` = NEW.`part_type_id`);
    END$$
    
CREATE TRIGGER dtype_BU
  BEFORE UPDATE ON `PART`
  FOR EACH ROW
    BEGIN
      SET NEW.`DTYPE` = (SELECT COALESCE(`PART_TYPE`.`DTYPE`, 'Part') FROM `PART_TYPE` WHERE `ID` = NEW.`part_type_id`);
    END$$
    
DELIMITER ;
-- END TRIGGERS

--
-- Part Types
--
CREATE TABLE `COOL_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `GASKET_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `KIT_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(1000) NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `SEAL_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `TURBO_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `manfr_id` BIGINT NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`, `manfr_id`),
  FOREIGN KEY (`manfr_id`) REFERENCES `MANFR` (`ID`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `TURBO_MODEL` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `turbo_type_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (`turbo_type_id`) REFERENCES `TURBO_TYPE` (`ID`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;


--
-- Interchanges
--
CREATE TABLE `INTERCHANGE_HEADER` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `INTERCHANGE_ITEM` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `interchange_header_id` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (`interchange_header_id`) REFERENCES `INTERCHANGE_HEADER` (`ID`),
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`),
  PRIMARY KEY(`part_id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `i_interchange_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(250) NULL,
  `part_num_id` BIGINT NULL,
  `interchange_header_id` BIGINT NULL,
  `interchange_cnt_uniq` BIGINT NULL,
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;


--
-- Bill of Materials
--
CREATE TABLE `BOM` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_part_id` BIGINT NOT NULL,
  `child_part_id` BIGINT NOT NULL,
  `quantity` INTEGER NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`parent_part_id`, `child_part_id`),
  FOREIGN KEY (`parent_part_id`) REFERENCES `PART` (`ID`),
  FOREIGN KEY (`child_part_id`) REFERENCES `PART` (`ID`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `BOM_ALT_HEADER` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `BOM_ALT_ITEM` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `bom_alt_header_id` BIGINT NOT NULL,
  `bom_id` BIGINT NOT NULL,
  `part_id` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE KEY (`bom_alt_header_id`, `bom_id`),
  FOREIGN KEY (`bom_alt_header_id`) REFERENCES `BOM_ALT_HEADER` (`ID`),
  FOREIGN KEY (`bom_id`) REFERENCES `BOM` (`ID`),
  FOREIGN KEY (`part_id`)  REFERENCES `PART` (`ID`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;


--
-- Parts
--
CREATE TABLE `BACKPLATE` (
  `part_id` BIGINT NOT NULL,
  `seal_type_id` BIGINT NULL,
  `style_compressor_wheel` VARCHAR(100) NULL,
  `seal_type` VARCHAR(100) NULL,
  `overall_diameter` FLOAT NULL,
  `compressor_wheel_diameter` FLOAT NULL,
  `piston_ring_diameter` FLOAT NULL,
  `compressor_housing_diameter` FLOAT NULL,
  `notes` VARCHAR(500) NULL,
  `secondary_diameter` FLOAT NULL,
  `overall_height` FLOAT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `BEARING_HOUSING` (
  `part_id` BIGINT NOT NULL,
  `cool_type_id` BIGINT NULL,
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
  `part_id` BIGINT NOT NULL,
  `outside_dim_min` FLOAT NULL,
  `outside_dim_max` FLOAT NULL,
  `inside_dim_min` FLOAT NULL,
  `inside_dim_max` FLOAT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `CARTRIDGE` (
  `part_id` BIGINT NOT NULL,
  FOREIGN KEY (`PART_ID`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `COMPRESSOR_WHEEL` (
  `part_id` BIGINT NOT NULL,
  `inducer_oa` FLOAT NULL,
  `tip_height_b` FLOAT NULL,
  `exducer_oc` FLOAT NULL,
  `hub_length_d` FLOAT NULL,
  `bore_oe` FLOAT NULL,
  `trim_no_blades` VARCHAR(100) NULL,
  `application` VARCHAR(100) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `GASKET` (
  `part_id` BIGINT NOT NULL,
  `gasket_type_id` BIGINT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`),
  FOREIGN KEY (`gasket_type_id`) REFERENCES `GASKET_TYPE` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `HEATSHIELD` (
  `part_id` BIGINT NOT NULL,
  `overall_diameter` FLOAT NULL,
  `inside_diameter` FLOAT NULL,
  `inducer_diameter` FLOAT NULL,
  `notes` VARCHAR(500) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `JOURNAL_BEARING` (
  `part_id` BIGINT NOT NULL,
  `outside_dim_min` FLOAT NOT NULL,
  `outside_dim_max` FLOAT NOT NULL,
  `inside_dim_min` FLOAT NOT NULL,
  `inside_dim_max` FLOAT NOT NULL,
  `width` FLOAT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `KIT` (
  `PART_ID` BIGINT NOT NULL,
  `kit_type_id` BIGINT NOT NULL,
  FOREIGN KEY (`kit_type_id`) REFERENCES `KIT_TYPE` (`ID`),
  FOREIGN KEY (`PART_ID`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `NOZZLE_RING` (
  `part_id` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `PISTON_RING` (
  `part_id` BIGINT NOT NULL,
  `outside_dim_min` FLOAT NULL,
  `outside_dim_max` FLOAT NULL,
  `width_min` FLOAT NULL,
  `width_max` FLOAT NULL,
  `i_gap_min` FLOAT NULL,
  `i_gap_max` FLOAT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `STANDARD_BEARING_SPACER` (
  `standard_part_id` BIGINT NOT NULL,
  `oversized_part_id` BIGINT NOT NULL,
  PRIMARY KEY (`standard_part_id`, `oversized_part_id`),
  FOREIGN KEY (`standard_part_id`) REFERENCES `BEARING_SPACER` (`part_id`),
  FOREIGN KEY (`oversized_part_id`) REFERENCES `BEARING_SPACER` (`part_id`)
) ENGINE = INNODB;

CREATE TABLE `STANDARD_JOURNAL_BEARING` (
  `standard_part_id` BIGINT NOT NULL,
  `oversized_part_id` BIGINT NOT NULL,
  PRIMARY KEY (`standard_part_id`, `oversized_part_id`),
  FOREIGN KEY (`standard_part_id`) REFERENCES `JOURNAL_BEARING` (`part_id`),
  FOREIGN KEY (`oversized_part_id`) REFERENCES `JOURNAL_BEARING` (`part_id`)
) ENGINE = INNODB;

CREATE TABLE `TURBINE_WHEEL` (
  `part_id` BIGINT NOT NULL,
  `exduce_oa` FLOAT NULL,
  `tip_height_b` FLOAT NULL,
  `inducer_oc` FLOAT NULL,
  `journal_od` FLOAT NULL,
  `stem_oe` FLOAT NULL,
  `trim_no_blades` VARCHAR(100) NULL,
  `shaft_thread_f` VARCHAR(100) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `TURBO` (
  `PART_ID` BIGINT NOT NULL,
  `turbo_model_id` BIGINT NOT NULL,
  `cool_type_id` BIGINT NULL,
  FOREIGN KEY (`PART_ID`) REFERENCES `PART` (`ID`),
  FOREIGN KEY (`cool_type_id`) REFERENCES `COOL_TYPE` (`ID`),
  FOREIGN KEY (`turbo_model_id`) REFERENCES `TURBO_MODEL` (`ID`)
) ENGINE = INNODB;


--
-- Part-Turbo Type join table
--
CREATE TABLE `PART_TURBO_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_id` BIGINT NOT NULL,
  `turbo_type_id` BIGINT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`),
  FOREIGN KEY (`turbo_type_id`) REFERENCES `TURBO_TYPE` (`ID`)
) ENGINE = INNODB;


--
-- Metadata Security
--
CREATE TABLE `USER` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `enabled` BOOLEAN NOT NULL DEFAULT 1,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`email`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `GROUPS` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`ID`, `version`)
) ENGINE = INNODB;

CREATE TABLE `USER_GROUP` (
  `user_id` BIGINT NOT NULL,
  `group_id` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY (`user_id`, `group_id`, `version`),
  FOREIGN KEY (`user_id`) REFERENCES `USER` (`id`),
  FOREIGN KEY (`group_id`) REFERENCES `GROUPS` (`id`)
) ENGINE = INNODB;

CREATE TABLE `ROLE` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `display` VARCHAR(100) NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`ID`, `version`)
);

CREATE TABLE `GROUP_ROLE` (
  `group_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY (`group_id`, `role_id`, `version`),
  FOREIGN KEY (`group_id`) REFERENCES `GROUPS` (`id`),
  FOREIGN KEY (`role_id`) REFERENCES `ROLE` (`id`)
) ENGINE = INNODB;
