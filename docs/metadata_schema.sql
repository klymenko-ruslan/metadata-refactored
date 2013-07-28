CREATE DATABASE IF NOT EXISTS `ti` CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON ti.* to ti@'localhost' IDENTIFIED BY 'ti';
USE `ti`;

--
-- Manufacturer
--
CREATE TABLE `MANFR_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `version` INTEGER,
  UNIQUE INDEX (`name`)
) ENGINE = INNODB;

CREATE TABLE `MANFR` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `manfr_type_id` BIGINT NOT NULL,
  `parent_manfr_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER,
  UNIQUE INDEX (`name`),
  FOREIGN KEY (`manfr_type_id`) REFERENCES `MANFR_TYPE` (`ID`),
  FOREIGN KEY (`parent_manfr_id`) REFERENCES `MANFR` (`ID`)
) ENGINE = INNODB;


--
-- Part
--
CREATE TABLE `PART_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `parent_part_type_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER,
  FOREIGN KEY (`parent_part_type_id`) REFERENCES `PART_TYPE` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `PART` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `manfr_part_num` VARCHAR(255) NULL,
  `manfr_id` BIGINT NOT NULL,
  `part_type_id` BIGINT NOT NULL,
  `Name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `inactive` BOOLEAN NOT NULL DEFAULT 0,
  `import_pk` BIGINT NULL,
  `version` INTEGER,
  INDEX (`manfr_part_num`, `manfr_id`),
  FOREIGN KEY (`manfr_id`) REFERENCES `MANFR` (`ID`),
  FOREIGN KEY (`part_type_id`) REFERENCES `PART_TYPE` (`ID`)
) ENGINE = INNODB;

CREATE VIEW `PART_ORM_VIEW` AS
  SELECT
     p.*,
    `pt`.`name` DTYPE
  FROM
    `PART` `p`
    JOIN `PART_TYPE` `pt` ON `pt`.`id` = `p`.`part_type_id`;

--
-- Part Types
--
CREATE TABLE `COOL_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER,
  UNIQUE INDEX (`name`)
) ENGINE = INNODB;

CREATE TABLE `GASKET_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER,
  UNIQUE INDEX (`name`)
) ENGINE = INNODB;

CREATE TABLE `KIT_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(1000) NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER
) ENGINE = INNODB;

CREATE TABLE `SEAL_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NOT NULL,
  `version` INTEGER,
  UNIQUE INDEX (`name`)
) ENGINE = INNODB;

CREATE TABLE `TURBO_TYPE` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `manfr_id` BIGINT NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER,
  UNIQUE INDEX (`name`),
  FOREIGN KEY (`manfr_id`) REFERENCES `MANFR` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `TURBO_MODEL` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NULL,
  `turbo_type_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER,
  FOREIGN KEY (`turbo_type_id`) REFERENCES `TURBO_TYPE` (`ID`)
) ENGINE = INNODB;


--
-- Interchanges
--
CREATE TABLE `INTERCHANGE_HEADER` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL,
  `version` INTEGER
) ENGINE = INNODB;

CREATE TABLE `INTERCHANGE_ITEM` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `interchange_header_id` BIGINT NOT NULL,
  `version` INTEGER,
  FOREIGN KEY (`interchange_header_id`) REFERENCES `INTERCHANGE_HEADER` (`ID`),
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `i_interchange_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `description` VARCHAR(250) NULL,
  `part_num_id` BIGINT NULL,
  `interchange_header_id` BIGINT NULL,
  `interchange_cnt_uniq` BIGINT NULL,
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `version` INTEGER
) ENGINE = INNODB;


--
-- Bill of Materials
--
CREATE TABLE `BOM` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `parent_part_id` BIGINT NOT NULL,
  `child_part_id` BIGINT NOT NULL,
  `quantity` INTEGER NOT NULL,
  `version` INTEGER,
  UNIQUE INDEX (`parent_part_id`, `child_part_id`),
  FOREIGN KEY (`parent_part_id`) REFERENCES `PART` (`ID`),
  FOREIGN KEY (`child_part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `BOM_ALT_HEADER` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `version` INTEGER
) ENGINE = INNODB;

CREATE TABLE `BOM_ALT_ITEM` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `bom_alt_header_id` BIGINT NOT NULL,
  `bom_id` BIGINT NOT NULL,
  `part_id` BIGINT NOT NULL,
  `version` INTEGER,
  UNIQUE KEY (`bom_alt_header_id`, `bom_id`),
  FOREIGN KEY (`bom_alt_header_id`) REFERENCES `BOM_ALT_HEADER` (`ID`),
  FOREIGN KEY (`bom_id`) REFERENCES `BOM` (`ID`),
  FOREIGN KEY (`part_id`)  REFERENCES `PART` (`ID`)
) ENGINE = INNODB;


--
-- Parts
--
CREATE TABLE `BACKPLATE` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
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
  `version` INTEGER,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `BEARING_HOUSING` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `cool_type_id` BIGINT NULL,
  `oil_inlet` VARCHAR(100) NULL,
  `oil_outlet` VARCHAR(100) NULL,
  `oil` VARCHAR(100) NULL,
  `outlet_flange_holes` VARCHAR(100) NULL,
  `water_ports` VARCHAR(100) NULL,
  `design_features` VARCHAR(100) NULL,
  `bearing_type` VARCHAR(100) NULL,
  `version` INTEGER,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `BEARING_SPACER` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `outside_dim_min` FLOAT NULL,
  `outside_dim_max` FLOAT NULL,
  `inside_dim_min` FLOAT NULL,
  `inside_dim_max` FLOAT NULL,
  `version` INTEGER,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `CARTRIDGE` (
  `PART_ID` BIGINT NOT NULL PRIMARY KEY,
  `version` INTEGER,
  FOREIGN KEY (`PART_ID`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `COMPRESSOR_WHEEL` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `inducer_oa` FLOAT NULL,
  `tip_height_b` FLOAT NULL,
  `exducer_oc` FLOAT NULL,
  `hub_length_d` FLOAT NULL,
  `bore_oe` FLOAT NULL,
  `trim_no_blades` VARCHAR(100) NULL,
  `application` VARCHAR(100) NULL,
  `version` INTEGER,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `GASKET` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `gasket_type_id` BIGINT NOT NULL,
  `version` INTEGER,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`),
  FOREIGN KEY (`gasket_type_id`) REFERENCES `GASKET_TYPE` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `HEATSHIELD` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `overall_diameter` FLOAT NULL,
  `inside_diameter` FLOAT NULL,
  `inducer_diameter` FLOAT NULL,
  `notes` VARCHAR(500) NULL,
  `version` INTEGER,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `JOURNAL_BEARING` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `outside_dim_min` FLOAT NOT NULL,
  `outside_dim_max` FLOAT NOT NULL,
  `inside_dim_min` FLOAT NOT NULL,
  `inside_dim_max` FLOAT NOT NULL,
  `width` FLOAT NOT NULL,
  `version` INTEGER,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `KIT` (
  `PART_ID` BIGINT NOT NULL PRIMARY KEY,
  `kit_type_id` BIGINT NOT NULL,
  `version` INTEGER,
  FOREIGN KEY (`kit_type_id`) REFERENCES `KIT_TYPE` (`ID`),
  FOREIGN KEY (`PART_ID`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `NOZZLE_RING` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `version` INTEGER,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `PISTON_RING` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `outside_dim_min` FLOAT NULL,
  `outside_dim_max` FLOAT NULL,
  `width_min` FLOAT NULL,
  `width_max` FLOAT NULL,
  `i_gap_min` FLOAT NULL,
  `i_gap_max` FLOAT NULL,
  `version` INTEGER,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `STANDARD_BEARING_SPACER` (
  `standard_part_id` BIGINT NOT NULL,
  `oversized_part_id` BIGINT NOT NULL,
  `version` INTEGER,
  PRIMARY KEY (`standard_part_id`, `oversized_part_id`),
  FOREIGN KEY (`standard_part_id`) REFERENCES `BEARING_SPACER` (`part_id`),
  FOREIGN KEY (`oversized_part_id`) REFERENCES `BEARING_SPACER` (`part_id`)
) ENGINE = INNODB;

CREATE TABLE `STANDARD_JOURNAL_BEARING` (
  `standard_part_id` BIGINT NOT NULL,
  `oversized_part_id` BIGINT NOT NULL,
  `version` INTEGER,
  PRIMARY KEY (`standard_part_id`, `oversized_part_id`),
  FOREIGN KEY (`standard_part_id`) REFERENCES `JOURNAL_BEARING` (`part_id`),
  FOREIGN KEY (`oversized_part_id`) REFERENCES `JOURNAL_BEARING` (`part_id`)
) ENGINE = INNODB;

CREATE TABLE `TURBINE_WHEEL` (
  `part_id` BIGINT NOT NULL PRIMARY KEY,
  `exduce_oa` FLOAT NULL,
  `tip_height_b` FLOAT NULL,
  `inducer_oc` FLOAT NULL,
  `journal_od` FLOAT NULL,
  `stem_oe` FLOAT NULL,
  `trim_no_blades` VARCHAR(100) NULL,
  `shaft_thread_f` VARCHAR(100) NULL,
  `version` INTEGER,
  FOREIGN KEY (`part_id`) REFERENCES `PART` (`ID`)
) ENGINE = INNODB;

CREATE TABLE `TURBO` (
  `PART_ID` BIGINT NOT NULL PRIMARY KEY,
  `turbo_model_id` BIGINT NOT NULL,
  `cool_type_id` BIGINT NULL,
  `version` INTEGER,
  FOREIGN KEY (`PART_ID`) REFERENCES `PART` (`ID`),
  FOREIGN KEY (`cool_type_id`) REFERENCES `COOL_TYPE` (`ID`),
  FOREIGN KEY (`turbo_model_id`) REFERENCES `TURBO_MODEL` (`ID`)
) ENGINE = INNODB;


--
-- Metadata Security
--
CREATE TABLE `USER` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `enabled` BOOLEAN NOT NULL DEFAULT 1,
  `version` INTEGER,
  UNIQUE INDEX (`email`)
) ENGINE = INNODB;

CREATE TABLE `GROUP` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `version` INTEGER,
  UNIQUE INDEX (`name`)
) ENGINE = INNODB;

CREATE TABLE `USER_GROUP` (
  `user_id` BIGINT NOT NULL,
  `group_id` BIGINT NOT NULL,
  `version` INTEGER,
  PRIMARY KEY (`user_id`, `group_id`),
  FOREIGN KEY (`user_id`) REFERENCES `USER` (`id`),
  FOREIGN KEY (`group_id`) REFERENCES `GROUP` (`id`)
) ENGINE = INNODB;

CREATE TABLE `ROLE` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `display` VARCHAR(100) NOT NULL,
  `version` INTEGER,
  UNIQUE INDEX (`name`)
);

CREATE TABLE `GROUP_ROLE` (
  `group_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  `version` INTEGER,
  PRIMARY KEY (`group_id`, `role_id`),
  FOREIGN KEY (`group_id`) REFERENCES `GROUP` (`id`),
  FOREIGN KEY (`role_id`) REFERENCES `ROLE` (`id`)
) ENGINE = INNODB;

--
-- Admin User
--
INSERT INTO `USER` (
  `name`, `email`, `password`
) VALUES (
  "Admin", "", ""
);
SET @ADMINUSERID = last_insert_id();

INSERT INTO `GROUP` (
  `name`
) VALUES (
  "Administrators"
);
SET @ADMINGROUPID = last_insert_id();

INSERT INTO `USER_GROUP` (
  `user_id`, `group_id`
) VALUES (
  @ADMINUSERID, @ADMINGROUPID
);

INSERT INTO `ROLE` (
  `name`, `display`
) VALUES (
  "ADMIN", "Administrator"
);
SET @ADMINROLEID = last_insert_id();


INSERT INTO `GROUP_ROLE` (
  `group_id`, `role_id`
) VALUES (
  @ADMINGROUPID, @ADMINROLEID
);
