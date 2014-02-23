DROP DATABASE IF EXISTS `ti`;
CREATE DATABASE IF NOT EXISTS `ti` CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON ti.* to ti@'localhost' IDENTIFIED BY 'ti';
USE `ti`;

--
-- Cars
--
CREATE TABLE `car_year` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `car_make` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `import_pk` BIGINT NULL,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `car_fuel_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `car_model` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `car_make_id` BIGINT NOT NULL,
  `import_pk` BIGINT NULL,
  UNIQUE INDEX (`name`, `car_make_id`),
  FOREIGN KEY (`car_make_id`) REFERENCES `car_make` (`id`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `car_engine` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `engine_size` VARCHAR(50) NOT NULL,
  `car_fuel_type_id` BIGINT NULL,
  UNIQUE INDEX (`engine_size`, `car_fuel_type_id`),
  FOREIGN KEY (`car_fuel_type_id`) REFERENCES `car_fuel_type` (`id`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `car_model_engine_year` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `car_model_id` BIGINT NOT NULL,
  `car_engine_id` BIGINT NOT NULL,
  `car_year_id` BIGINT NOT NULL,
  `import_pk` BIGINT NULL,
  UNIQUE INDEX (`car_model_id`, `car_engine_id`, `car_year_id`),
  FOREIGN KEY (`car_model_id`) REFERENCES `car_model` (`id`),
  FOREIGN KEY (`car_engine_id`) REFERENCES `car_engine` (`id`),
  FOREIGN KEY (`car_year_id`) REFERENCES `car_year` (`id`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

--
-- Manufacturer
--
CREATE TABLE `manfr_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `manfr` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `manfr_type_id` BIGINT NOT NULL,
  `parent_manfr_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  UNIQUE INDEX (`name`),
  FOREIGN KEY (`manfr_type_id`) REFERENCES `manfr_type` (`id`),
  FOREIGN KEY (`parent_manfr_id`) REFERENCES `manfr` (`id`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;


--
-- Part
--
CREATE TABLE `part_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `parent_part_type_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  FOREIGN KEY (`parent_part_type_id`) REFERENCES `part_type` (`id`),
  PRIMARY KEY(`id`)
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
  PRIMARY KEY(`id`),
  INDEX (`manfr_part_num`, `manfr_id`),
  FOREIGN KEY (`manfr_id`) REFERENCES `manfr` (`id`),
  FOREIGN KEY (`part_type_id`) REFERENCES `part_type` (`id`)
) ENGINE = INNODB;

CREATE TABLE `product_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `part_id` BIGINT NOT NULL,
  `filename` VARCHAR(255) NULL,
  PRIMARY KEY(`id`),
  UNIQUE KEY(`filename`),
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

--
-- Part Types
--
CREATE TABLE `cool_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NULL,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `gasket_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NULL,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `kit_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(1000) NOT NULL,
  `import_pk` BIGINT NULL,
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `seal_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `import_pk` BIGINT NOT NULL,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `turbo_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `manfr_id` BIGINT NOT NULL,
  `import_pk` BIGINT NULL,
  UNIQUE INDEX (`name`, `manfr_id`),
  FOREIGN KEY (`manfr_id`) REFERENCES `manfr` (`id`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `turbo_model` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `turbo_type_id` BIGINT NULL,
  `import_pk` BIGINT NULL,
  FOREIGN KEY (`turbo_type_id`) REFERENCES `turbo_type` (`id`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;


--
-- Interchanges
--
CREATE TABLE `interchange_header` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `interchange_item` (
  `part_id` BIGINT NOT NULL,
  `interchange_header_id` BIGINT NOT NULL,
  FOREIGN KEY (`interchange_header_id`) REFERENCES `interchange_header` (`id`),
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  PRIMARY KEY(`part_id`)
) ENGINE = INNODB;

CREATE TABLE `i_interchange_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(250) NULL,
  `part_num_id` BIGINT NULL,
  `interchange_header_id` BIGINT NULL,
  `interchange_cnt_uniq` BIGINT NULL,
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(`id`)
) ENGINE = INNODB;


--
-- Bill of Materials
--
CREATE TABLE `bom` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_part_id` BIGINT NOT NULL,
  `child_part_id` BIGINT NOT NULL,
  `quantity` INTEGER NOT NULL,
  UNIQUE INDEX (`parent_part_id`, `child_part_id`),
  FOREIGN KEY (`parent_part_id`) REFERENCES `part` (`id`),
  FOREIGN KEY (`child_part_id`) REFERENCES `part` (`id`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `bom_alt_header` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `bom_alt_item` (
  `bom_alt_header_id` BIGINT NOT NULL,
  `bom_id` BIGINT NOT NULL,
  `part_id` BIGINT NOT NULL,
  UNIQUE KEY (`bom_alt_header_id`, `bom_id`),
  FOREIGN KEY (`bom_alt_header_id`) REFERENCES `bom_alt_header` (`id`),
  FOREIGN KEY (`bom_id`) REFERENCES `bom` (`id`),
  FOREIGN KEY (`part_id`)  REFERENCES `part` (`id`)
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
  `name` VARCHAR(50) NULL,
  `kit_type_id` BIGINT NOT NULL,
  FOREIGN KEY (`kit_type_id`) REFERENCES `kit_type` (`id`),
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `nozzle_ring` (
  `part_id` BIGINT NOT NULL,
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
-- Turbo-Car Relationship
CREATE TABLE `turbo_car_model_engine_year` (
  `part_id` BIGINT NOT NULL,
  `car_model_engine_year_id` BIGINT NOT NULL,
  `import_pk` BIGINT NULL,
  FOREIGN KEY (`car_model_engine_year_id`) REFERENCES `car_model_engine_year` (`id`),
  FOREIGN KEY (`part_id`) REFERENCES `turbo` (`part_id`),
  PRIMARY KEY(`part_id`, `car_model_engine_year_id`)
) ENGINE = INNODB;

--
-- Part-Turbo Index
--
CREATE TABLE `part_turbo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_id` BIGINT NOT NULL,
  `turbo_id` BIGINT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  FOREIGN KEY (`turbo_id`) REFERENCES `turbo` (`part_id`)
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
  UNIQUE INDEX (`email`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `groups` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `version` INTEGER NOT NULL DEFAULT 1,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`)
) ENGINE = INNODB;

CREATE TABLE `user_group` (
  `user_id` BIGINT NOT NULL,
  `group_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `group_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE = INNODB;

CREATE TABLE `role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `display` VARCHAR(100) NOT NULL,
  UNIQUE INDEX (`name`),
  PRIMARY KEY(`id`)
);

CREATE TABLE `group_role` (
  `group_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`group_id`, `role_id`),
  FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE = INNODB;

INSERT INTO `role` (name, display) VALUES ('ROLE_READ', 'Search and view part information.');
INSERT INTO `role` (name, display) VALUES ('ROLE_CREATE_PART', 'Create parts.');
INSERT INTO `role` (name, display) VALUES ('ROLE_INTERCHANGE', 'Alter interchangeability.');
INSERT INTO `role` (name, display) VALUES ('ROLE_BOM', 'Modify part interchangeability.');
INSERT INTO `role` (name, display) VALUES ('ROLE_ADMIN', 'Superpowers.');

INSERT INTO `groups` (name) VALUES ('Reader');
INSERT INTO `groups` (name) VALUES ('Writer');
INSERT INTO `groups` (name) VALUES ('Admin');

INSERT INTO `group_role` (group_id, role_id) VALUES
  ((SELECT id FROM groups WHERE `name` = 'Admin'), (SELECT id FROM role WHERE `name` = 'ROLE_ADMIN')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'), (SELECT id FROM role WHERE `name` = 'ROLE_READ')),
  ((SELECT id FROM groups WHERE `name` = 'Reader'), (SELECT id FROM role WHERE `name` = 'ROLE_READ')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_BOM')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_CREATE_PART')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_INTERCHANGE')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_READ'));

CREATE TABLE `changelog` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `change_date` DATETIME NOT NULL,
  `user_id` BIGINT NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `data` LONGTEXT NULL,
  PRIMARY KEY(`id`),
  KEY(`change_date`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = INNODB;

CREATE TABLE `mas90_std_price` (
  `ItemNumber` VARCHAR(50) NOT NULL,
  `StdPrice` DECIMAL(10,2) NOT NULL DEFAULT 0
) ENGINE = INNODB;

-- Legacy Tables
CREATE TABLE `part_turbo_type` (
  `part_id` BIGINT NOT NULL,
  `turbo_type_id` BIGINT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  FOREIGN KEY (`turbo_type_id`) REFERENCES `turbo_type` (`id`),
  PRIMARY KEY (`part_id`, `turbo_type_id`)
)
ENGINE = INNODB;
