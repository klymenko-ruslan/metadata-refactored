DROP DATABASE IF EXISTS `ti`;
CREATE DATABASE IF NOT EXISTS `ti` CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON ti.* to ti@'localhost' IDENTIFIED BY 'ti';
USE `ti`;

--
-- Manufacturer
--
CREATE TABLE `manfr_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `manfr` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `manfr_type_id` BIGINT NOT NULL,
  `parent_manfr_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  FOREIGN KEY (`manfr_type_id`) REFERENCES `manfr_type` (`id`),
  FOREIGN KEY (`parent_manfr_id`) REFERENCES `manfr` (`id`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;


--
-- Part
--
CREATE TABLE `part_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `parent_part_type_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  `dtype` VARCHAR(50),
  `magento_attribute_set` VARCHAR(50),
  `magento_category` VARCHAR(50),
  `version` INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (`parent_part_type_id`) REFERENCES `part_type` (`id`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `part` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `manfr_part_num` VARCHAR(255) NULL,
  `manfr_id` BIGINT NOT NULL,
  `part_type_id` BIGINT NOT NULL,
  `name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `inactive` BOOLEAN NOT NULL DEFAULT 0,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  `dtype` VARCHAR(50) DEFAULT 'Part',
  PRIMARY KEY(`id`, `version`),
  INDEX (`manfr_part_num`, `manfr_id`),
  FOREIGN KEY (`manfr_id`) REFERENCES `manfr` (`id`),
  FOREIGN KEY (`part_type_id`) REFERENCES `part_type` (`id`)
) ENGINE = INNODB;


-- BEGIN TRIGGERS
DELIMITER $$
CREATE TRIGGER dtype_BI
  BEFORE INSERT ON `part`
  FOR EACH ROW
    BEGIN
      SET NEW.`dtype` = (SELECT COALESCE(`part_type`.`dtype`, 'Part') FROM `part_type` WHERE `id` = NEW.`part_type_id`);
    END$$
    
CREATE TRIGGER dtype_BU
  BEFORE UPDATE ON `part`
  FOR EACH ROW
    BEGIN
      SET NEW.`dtype` = (SELECT COALESCE(`part_type`.`dtype`, 'Part') FROM `part_type` WHERE `id` = NEW.`part_type_id`);
    END$$
    
DELIMITER ;
-- END TRIGGERS

--
-- Part Types
--
CREATE TABLE `cool_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `gasket_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `kit_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(1000) NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `seal_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `turbo_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `manfr_id` BIGINT NOT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`, `manfr_id`),
  FOREIGN KEY (`manfr_id`) REFERENCES `manfr` (`id`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `turbo_model` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `turbo_type_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (`turbo_type_id`) REFERENCES `turbo_type` (`id`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;


--
-- Interchanges
--
CREATE TABLE `interchange_header` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `interchange_item` (
  `part_id` BIGINT NOT NULL,
  `interchange_header_id` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (`interchange_header_id`) REFERENCES `interchange_header` (`id`),
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
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
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;


--
-- Bill of Materials
--
CREATE TABLE `bom` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_part_id` BIGINT NOT NULL,
  `child_part_id` BIGINT NOT NULL,
  `quantity` INTEGER NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`parent_part_id`, `child_part_id`),
  FOREIGN KEY (`parent_part_id`) REFERENCES `part` (`id`),
  FOREIGN KEY (`child_part_id`) REFERENCES `part` (`id`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `bom_alt_header` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `bom_alt_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `bom_alt_header_id` BIGINT NOT NULL,
  `bom_id` BIGINT NOT NULL,
  `part_id` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE KEY (`bom_alt_header_id`, `bom_id`),
  FOREIGN KEY (`bom_alt_header_id`) REFERENCES `bom_alt_header` (`id`),
  FOREIGN KEY (`bom_id`) REFERENCES `bom` (`id`),
  FOREIGN KEY (`part_id`)  REFERENCES `part` (`id`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;


--
-- Parts
--
CREATE TABLE `backplate` (
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
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `bearing_housing` (
  `part_id` BIGINT NOT NULL,
  `cool_type_id` BIGINT NULL,
  `oil_inlet` VARCHAR(100) NULL,
  `oil_outlet` VARCHAR(100) NULL,
  `oil` VARCHAR(100) NULL,
  `outlet_flange_holes` VARCHAR(100) NULL,
  `water_ports` VARCHAR(100) NULL,
  `design_features` VARCHAR(100) NULL,
  `bearing_type` VARCHAR(100) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `bearing_spacer` (
  `part_id` BIGINT NOT NULL,
  `outside_dim_min` FLOAT NULL,
  `outside_dim_max` FLOAT NULL,
  `inside_dim_min` FLOAT NULL,
  `inside_dim_max` FLOAT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `cartridge` (
  `part_id` BIGINT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `compressor_wheel` (
  `part_id` BIGINT NOT NULL,
  `inducer_oa` FLOAT NULL,
  `tip_height_b` FLOAT NULL,
  `exducer_oc` FLOAT NULL,
  `hub_length_d` FLOAT NULL,
  `bore_oe` FLOAT NULL,
  `trim_no_blades` VARCHAR(100) NULL,
  `application` VARCHAR(100) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `gasket` (
  `part_id` BIGINT NOT NULL,
  `gasket_type_id` BIGINT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  FOREIGN KEY (`gasket_type_id`) REFERENCES `gasket_type` (`id`)
) ENGINE = INNODB;

CREATE TABLE `heatshield` (
  `part_id` BIGINT NOT NULL,
  `overall_diameter` FLOAT NULL,
  `inside_diameter` FLOAT NULL,
  `inducer_diameter` FLOAT NULL,
  `notes` VARCHAR(500) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `journal_bearing` (
  `part_id` BIGINT NOT NULL,
  `outside_dim_min` FLOAT NOT NULL,
  `outside_dim_max` FLOAT NOT NULL,
  `inside_dim_min` FLOAT NOT NULL,
  `inside_dim_max` FLOAT NOT NULL,
  `width` FLOAT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `kit` (
  `part_id` BIGINT NOT NULL,
  `kit_type_id` BIGINT NOT NULL,
  FOREIGN KEY (`kit_type_id`) REFERENCES `kit_type` (`id`),
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `nozzle_ring` (
  `part_id` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `piston_ring` (
  `part_id` BIGINT NOT NULL,
  `outside_dim_min` FLOAT NULL,
  `outside_dim_max` FLOAT NULL,
  `width_min` FLOAT NULL,
  `width_max` FLOAT NULL,
  `i_gap_min` FLOAT NULL,
  `i_gap_max` FLOAT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `standard_bearing_spacer` (
  `standard_part_id` BIGINT NOT NULL,
  `oversized_part_id` BIGINT NOT NULL,
  PRIMARY KEY (`standard_part_id`, `oversized_part_id`),
  FOREIGN KEY (`standard_part_id`) REFERENCES `bearing_spacer` (`part_id`),
  FOREIGN KEY (`oversized_part_id`) REFERENCES `bearing_spacer` (`part_id`)
) ENGINE = INNODB;

CREATE TABLE `standard_journal_bearing` (
  `standard_part_id` BIGINT NOT NULL,
  `oversized_part_id` BIGINT NOT NULL,
  PRIMARY KEY (`standard_part_id`, `oversized_part_id`),
  FOREIGN KEY (`standard_part_id`) REFERENCES `journal_bearing` (`part_id`),
  FOREIGN KEY (`oversized_part_id`) REFERENCES `journal_bearing` (`part_id`)
) ENGINE = INNODB;

CREATE TABLE `turbine_wheel` (
  `part_id` BIGINT NOT NULL,
  `exduce_oa` FLOAT NULL,
  `tip_height_b` FLOAT NULL,
  `inducer_oc` FLOAT NULL,
  `journal_od` FLOAT NULL,
  `stem_oe` FLOAT NULL,
  `trim_no_blades` VARCHAR(100) NULL,
  `shaft_thread_f` VARCHAR(100) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `turbo` (
  `part_id` BIGINT NOT NULL,
  `turbo_model_id` BIGINT NOT NULL,
  `cool_type_id` BIGINT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  FOREIGN KEY (`cool_type_id`) REFERENCES `cool_type` (`id`),
  FOREIGN KEY (`turbo_model_id`) REFERENCES `turbo_model` (`id`)
) ENGINE = INNODB;


--
-- Part-Turbo Type join table
--
CREATE TABLE `part_turbo_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_id` BIGINT NOT NULL,
  `turbo_type_id` BIGINT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  FOREIGN KEY (`turbo_type_id`) REFERENCES `turbo_type` (`id`)
) ENGINE = INNODB;


--
-- Metadata Security
--
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `enabled` BOOLEAN NOT NULL DEFAULT 1,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`email`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `groups` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`, `version`)
) ENGINE = INNODB;

CREATE TABLE `user_group` (
  `user_id` BIGINT NOT NULL,
  `group_id` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY (`user_id`, `group_id`, `version`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE = INNODB;

CREATE TABLE `role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `display` VARCHAR(100) NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`, `version`)
);

CREATE TABLE `group_role` (
  `group_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY (`group_id`, `role_id`, `version`),
  FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE = INNODB;

CREATE TABLE `changelog` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `change_date` DATETIME NOT NULL,
  `user_id` BIGINT NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `data` LONGTEXT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY(`id`, `version`),
  KEY(`change_date`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = INNODB;

CREATE TABLE `mas90_std_price` (
  `ItemNumber` VARCHAR(50) NOT NULL,
  `StdPrice` DECIMAL(10,2) NOT NULL DEFAULT 0
) ENGINE = INNODB;
