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
  `car_model_id` BIGINT NULL,
  `car_engine_id` BIGINT NULL,
  `car_year_id` BIGINT NULL,
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
  `magento_attribute_set` VARCHAR(50),
  `value` VARCHAR(50) NOT NULL,
  UNIQUE INDEX `value_UNIQUE` (`value` ASC),
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
  `version` INTEGER NOT NULL DEFAULT 1
  PRIMARY KEY(`id`),
  INDEX (`manfr_part_num`, `manfr_id`),
  UNIQUE KEY (`id`, `version`),
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
-- Interchanges (has triggers)
--
CREATE TABLE `interchange_header` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `new_part_id` BIGINT(20) NULL,
  UNIQUE INDEX (`new_part_id`),
  FOREIGN KEY (`new_part_id`) REFERENCES `part` (`id`),
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
-- Bill of Materials (has triggers)
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
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `bom_alt_header_id` BIGINT NOT NULL,
  `bom_id` BIGINT NOT NULL,
  `part_id` BIGINT NOT NULL,
  UNIQUE KEY (`bom_alt_header_id`, `bom_id`),
  FOREIGN KEY (`bom_alt_header_id`) REFERENCES `bom_alt_header` (`id`),
  FOREIGN KEY (`bom_id`) REFERENCES `bom` (`id`),
  FOREIGN KEY (`part_id`)  REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE bom_descendant (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_bom_id` BIGINT NOT NULL,
  `descendant_bom_id` BIGINT NOT NULL,
  `distance` INTEGER NOT NULL,
  `type` VARCHAR(20),
  `qty` INTEGER NOT NULL,
  INDEX(distance),
  UNIQUE INDEX(`part_bom_id`, `descendant_bom_id`, `distance`, `type`, `qty`),
  INDEX(part_bom_id, `type`, distance, descendant_bom_id),
  FOREIGN KEY (part_bom_id) REFERENCES bom (id) ON DELETE CASCADE,
  FOREIGN KEY (descendant_bom_id) REFERENCES bom (id) ON DELETE CASCADE
) ENGINE= INNODB;


--
-- Parts
--
CREATE TABLE `backplate` (
  `part_id` BIGINT NOT NULL,
  `seal_type_id` BIGINT NULL,
  `style_compressor_wheel` VARCHAR(100) NULL,
  `seal_type` VARCHAR(100) NULL,
  `overall_diameter` DECIMAL(10,6) NULL,
  `compressor_wheel_diameter` DECIMAL(10,6) NULL,
  `piston_ring_diameter` DECIMAL(10,6) NULL,
  `compressor_housing_diameter` DECIMAL(10,6) NULL,
  `notes` VARCHAR(500) NULL,
  `secondary_diameter` DECIMAL(10,6) NULL,
  `overall_height` DECIMAL(10,6) NULL,
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
  `outside_dim_min` DECIMAL(10,6) NULL,
  `outside_dim_max` DECIMAL(10,6) NULL,
  `inside_dim_min` DECIMAL(10,6) NULL,
  `inside_dim_max` DECIMAL(10,6) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `cartridge` (
  `part_id` BIGINT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `compressor_wheel` (
  `part_id` BIGINT NOT NULL,
  `inducer_oa` DECIMAL(10,6) NULL,
  `tip_height_b` DECIMAL(10,6) NULL,
  `exducer_oc` DECIMAL(10,6) NULL,
  `hub_length_d` DECIMAL(10,6) NULL,
  `bore_oe` DECIMAL(10,6) NULL,
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
  `overall_diameter` DECIMAL(10,6) NULL,
  `inside_diameter` DECIMAL(10,6) NULL,
  `inducer_diameter` DECIMAL(10,6) NULL,
  `notes` VARCHAR(500) NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `journal_bearing` (
  `part_id` BIGINT NOT NULL,
  `outside_dim_min` DECIMAL(10,6) NOT NULL,
  `outside_dim_max` DECIMAL(10,6) NOT NULL,
  `inside_dim_min` DECIMAL(10,6) NOT NULL,
  `inside_dim_max` DECIMAL(10,6) NOT NULL,
  `width` DECIMAL(10,6) NOT NULL,
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
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `piston_ring` (
  `part_id` BIGINT NOT NULL,
  `outside_dim_min` DECIMAL(10,6) NULL,
  `outside_dim_max` DECIMAL(10,6) NULL,
  `width_min` DECIMAL(10,6) NULL,
  `width_max` DECIMAL(10,6) NULL,
  `i_gap_min` DECIMAL(10,6) NULL,
  `i_gap_max` DECIMAL(10,6) NULL,
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

CREATE TABLE `standard_oversize_part` (
  `standard_part_id` BIGINT NOT NULL,
  `oversize_part_id` BIGINT NOT NULL,
  PRIMARY KEY (`standard_part_id`,`oversize_part_id`),
  KEY `standard_oversize_part_oversized_part_id_idx` (`oversize_part_id`),
  CONSTRAINT `standard_oversize_part_standard_part_id` FOREIGN KEY (`standard_part_id`) REFERENCES `part` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `standard_oversize_part_oversized_part_id` FOREIGN KEY (`oversize_part_id`) REFERENCES `part` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB;

CREATE TABLE `turbine_wheel` (
  `part_id` BIGINT NOT NULL,
  `exduce_oa` DECIMAL(10,6) NULL,
  `tip_height_b` DECIMAL(10,6) NULL,
  `inducer_oc` DECIMAL(10,6) NULL,
  `journal_od` DECIMAL(10,6) NULL,
  `stem_oe` DECIMAL(10,6) NULL,
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
-- Kit 'services' Cartridge Relationship (allows non-cartridges)
--
CREATE TABLE `kit_part_common_component` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `kit_id` BIGINT NOT NULL,
  `part_id` BIGINT NOT NULL,
  `exclude` BIT NOT NULL,
  FOREIGN KEY (`kit_id`) REFERENCES `part` (`id`),
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  UNIQUE INDEX (`kit_id`, `part_id`)
) ENGINE = INNODB;

--
-- Turbo-Car Relationship
--
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
  `password_reset_token` CHAR(36) NULL,
  `enabled` BOOLEAN NOT NULL DEFAULT 1,
  UNIQUE INDEX (`email`),
  UNIQUE INDEX (`password_reset_token`),
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

INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_READ', 'Search and view part information.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_PART_IMAGES', 'Add and remove part images.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_CREATE_PART', 'Create parts.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_ALTER_PART', 'Alter existing parts.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_DELETE_PART', 'Delete existing parts.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_INTERCHANGE', 'Alter interchangeability.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_BOM', 'Alter BOM.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_BOM_ALT', 'Alter BOM alternates.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_ADMIN', 'Superpowers.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_TURBO_MODEL_CRUD', 'CRUD operations turbo types and models.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_SALES_NOTE_READ', 'Read sales notes.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_SALES_NOTE_SUBMIT', 'Submit draft sales notes for approval.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_SALES_NOTE_APPROVE', 'Approve submitted sales notes.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_SALES_NOTE_REJECT', 'Reject submitted and approved sales notes.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_SALES_NOTE_RETRACT', 'Retract published sales notes.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_SALES_NOTE_PUBLISH', 'Publish approved sales notes.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_APPLICATION_CRUD', 'CRUD operations on applications.');
INSERT IGNORE INTO `role` (name, display) VALUES ('ROLE_MAS90_SYNC', 'Start MAS90 synchronization process.');

INSERT IGNORE INTO `groups` (name) VALUES ('Reader');
INSERT IGNORE INTO `groups` (name) VALUES ('Writer');
INSERT IGNORE INTO `groups` (name) VALUES ('Admin');

INSERT IGNORE INTO `group_role` (group_id, role_id) VALUES
  ((SELECT id FROM groups WHERE `name` = 'Reader'), (SELECT id FROM role WHERE `name` = 'ROLE_READ')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_BOM_ALT')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_BOM')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_CREATE_PART')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_ALTER_PART')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_DELETE_PART')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_INTERCHANGE')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_PART_IMAGES')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_READ')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_TURBO_MODEL_CRUD')),
  ((SELECT id FROM groups WHERE `name` = 'Reader'), (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_READ')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_READ')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_SUBMIT')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_APPROVE')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_REJECT')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_RETRACT')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_PUBLISH')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_APPLICATION_CRUD')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_MAS90_SYNC')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_ADMIN')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_READ')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_BOM_ALT')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_BOM')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_CREATE_PART')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_ALTER_PART')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_DELETE_PART')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_INTERCHANGE')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_PART_IMAGES')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_TURBO_MODEL_CRUD')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_READ')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_SUBMIT')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_APPROVE')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_REJECT')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_RETRACT')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_SALES_NOTE_PUBLISH')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_APPLICATION_CRUD')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_MAS90_SYNC'));

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

CREATE TABLE `part_turbo_type` (
  `part_id` BIGINT NOT NULL,
  `turbo_type_id` BIGINT NOT NULL,
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  FOREIGN KEY (`turbo_type_id`) REFERENCES `turbo_type` (`id`),
  PRIMARY KEY (`part_id`, `turbo_type_id`)
)
ENGINE = INNODB;

CREATE TABLE `deleted_parts` (
  `id` BIGINT NOT NULL,
  `dt` TIMESTAMP,
  UNIQUE INDEX (id)
) ENGINE = INNODB;

--
-- Sales Notes
--
DROP TABLE IF EXISTS `sales_note`;
CREATE TABLE `sales_note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL,
  `write_date` datetime NOT NULL,
  `create_uid` bigint(20) NOT NULL,
  `write_uid` bigint(20) NOT NULL,
  `state` varchar(15) NOT NULL COMMENT 'draft;submitted;approved;rejected',
  `comment` longtext NOT NULL,
  `published` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_sales_note_user_create_uid_idx` (`create_uid`),
  KEY `fk_sales_note_user_write_uid_idx` (`write_uid`),
  CONSTRAINT `fk_sales_note_user_create_uid` FOREIGN KEY (`create_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_sales_note_user_write_uid` FOREIGN KEY (`write_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `sales_note_attachment`;
CREATE TABLE `sales_note_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL,
  `write_date` datetime NOT NULL,
  `create_uid` bigint(20) NOT NULL,
  `write_uid` bigint(20) NOT NULL,
  `sales_note_id` bigint(20) NOT NULL,
  `filename` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `filename_UNIQUE` (`filename`),
  KEY `sales_note_attachment_sales_note_sales_note_id_idx` (`sales_note_id`),
  KEY `fk_sales_note_attachment_user_create_uid_idx` (`create_uid`),
  KEY `fk_sales_note_attachment_user_write_uid_idx` (`write_uid`),
  CONSTRAINT `sales_note_attachment_user_create_uid` FOREIGN KEY (`create_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sales_note_attachment_user_write_uid` FOREIGN KEY (`write_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sales_note_attachment_sales_note_sales_note_id` FOREIGN KEY (`sales_note_id`) REFERENCES `sales_note` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `sales_note_part`;
CREATE TABLE `sales_note_part` (
  `sales_note_id` bigint(20) NOT NULL,
  `part_id` bigint(20) NOT NULL,
  `create_date` datetime NOT NULL,
  `write_date` datetime NOT NULL,
  `create_uid` bigint(20) NOT NULL,
  `write_uid` bigint(20) NOT NULL,
  `primary_part` tinyint(1) NOT NULL,
  PRIMARY KEY (`sales_note_id`,`part_id`),
  KEY `sales_note_part_part_part_id_idx` (`part_id`),
  KEY `sales_note_part_user_create_uid_idx` (`create_uid`),
  KEY `sales_note_part_user_write_uid_idx` (`write_uid`),
  CONSTRAINT `sales_note_part_user_create_uid` FOREIGN KEY (`create_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sales_note_part_user_write_uid` FOREIGN KEY (`write_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sales_note_part_sales_note_sales_note_id` FOREIGN KEY (`sales_note_id`) REFERENCES `sales_note` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `sales_note_part_part_part_id` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DELIMITER $$

DROP TRIGGER IF EXISTS sales_note_part_BEFORE_UPDATE$$
CREATE TRIGGER `sales_note_part_BEFORE_UPDATE` BEFORE UPDATE ON `sales_note_part` FOR EACH ROW
BEGIN
    DECLARE constraint_violation CONDITION FOR SQLSTATE '45000';
    DECLARE n INT;
    IF  new.primary_part <> 0 AND new.primary_part <>  1 THEN
        SIGNAL constraint_violation
        SET MESSAGE_TEXT = 'primary_part value must be 0 or 1';
    END IF;
    IF new.primary_part = 1 THEN
        SET n = (SELECT 1 FROM sales_note_part WHERE sales_note_id = new.sales_note_id AND primary_part = 1 LIMIT 1);
        IF n = 1 THEN
            SIGNAL constraint_violation
            SET MESSAGE_TEXT = 'sales note may have only one primary part';
        END IF;
    END IF;
END$$

DROP TRIGGER IF EXISTS sales_note_part_BEFORE_INSERT$$
CREATE TRIGGER `sales_note_part_BEFORE_INSERT` BEFORE INSERT ON `sales_note_part` FOR EACH ROW
BEGIN
    DECLARE constraint_violation CONDITION FOR SQLSTATE '45000';
    DECLARE n INT;
    IF  new.primary_part <> 0 and new.primary_part <> 1 THEN
        SIGNAL SQLSTATE '45000'   
        SET MESSAGE_TEXT = 'primary_part value must be 0 or 1';
    END IF;
    IF new.primary_part = 1 THEN
        SET n = (SELECT 1 FROM sales_note_part WHERE sales_note_id = new.sales_note_id AND primary_part = 1 LIMIT 1);
        IF n = 1 THEN
            SIGNAL constraint_violation
            SET MESSAGE_TEXT = 'sales note may have only one primary part';
        END IF;
    END IF;
END$$

DELIMITER ;

DROP TABLE IF EXISTS `mas90sync`;
CREATE TABLE `mas90sync` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `started` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'When the sync.process started.',
  `finished` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'When the sync.process finished.',
  `to_process` bigint(20) DEFAULT '0' COMMENT 'Total number of records to process.',
  `updated` bigint(20) DEFAULT '0' COMMENT 'Number of updates.',
  `inserted` bigint(20) DEFAULT '0' COMMENT 'Number of inserts.',
  `skipped` bigint(20) DEFAULT '0' COMMENT 'Number of skipped items.',
  `user_id` bigint(20) DEFAULT NULL COMMENT 'Ref. to an user who initiated the sync.process. NULL -- the process started by scheduler.',
  `status` enum('IN_PROGRESS','CANCELLED','FINISHED', 'FAILED') NOT NULL COMMENT 'Status of the sync.process.',
  PRIMARY KEY (`id`),
  KEY `usrid_fk` (`user_id`),
  CONSTRAINT `usrid_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1106 DEFAULT CHARSET=utf8 COMMENT='History of the syncronizations with MAS90.'

--
-- Views
--

-- Provides complete part descendant hierarchy.
-- Includes interchangeable parts, excludes bom alternates
-- #439
DROP VIEW IF EXISTS vbom_descendant;
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW `vbom_descendant` AS
    SELECT 
        `bd`.`id` AS `id`,
        `bd`.`part_bom_id` AS `part_bom_id`,
        `bd`.`descendant_bom_id` AS `descendant_bom_id`,
        `bd`.`distance` AS `distance`,
        IF(((`ii2`.`part_id` <> `b`.`parent_part_id`)
                OR (`ii`.`part_id` <> `bc`.`child_part_id`)),
            'Interchange',
            `bd`.`type`) AS `type`,
        `bd`.`qty` AS `qty`,
        `ii2`.`part_id` AS `part_id_ancestor`,
        `ii`.`part_id` AS `part_id_descendant`
    FROM
        ((((((`bom_descendant` `bd`
        JOIN `bom` `b` ON ((`bd`.`part_bom_id` = `b`.`id`)))
        INNER JOIN `interchange_item` `ii1` ON ((`b`.`parent_part_id` = `ii1`.`part_id`)))
        INNER JOIN `interchange_item` `ii2` ON ((`ii2`.`interchange_header_id` = `ii1`.`interchange_header_id`)))
        JOIN `bom` `bc` ON ((`bd`.`descendant_bom_id` = `bc`.`id`)))
        INNER JOIN `interchange_item` `ii3` ON ((`bc`.`child_part_id` = `ii3`.`part_id`)))
        INNER JOIN `interchange_item` `ii` ON ((`ii3`.`interchange_header_id` = `ii`.`interchange_header_id`)));

-- Provides part.id of direct descendants.
-- Interchange parts are excluded.
-- #440
DROP VIEW IF EXISTS vbom_descendant_direct;
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW `vbom_descendant_direct` AS
    SELECT 
        `bd`.`id` AS `id`,
        `bd`.`part_bom_id` AS `part_bom_id`,
        `bd`.`descendant_bom_id` AS `descendant_bom_id`,
        `bd`.`distance` AS `distance`,
        `bd`.`type` AS `type`,
        `bd`.`qty` AS `qty`,
        `b`.`parent_part_id` AS `part_id_ancestor`,
        `bc`.`child_part_id` AS `part_id_descendant`
    FROM
        ((`bom_descendant` `bd`
        JOIN `bom` `b` ON ((`bd`.`part_bom_id` = `b`.`id`)))
        JOIN `bom` `bc` ON ((`bd`.`descendant_bom_id` = `bc`.`id`)))
    WHERE
        (`bd`.`type` = 'direct');

-- bom_ancestor backward compatability
-- #441
DROP VIEW IF EXISTS vbom_ancestor;
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW `vbom_ancestor` AS
    SELECT
        bd.id,
        `ii`.`part_id` AS `part_id`,
        `ii2`.`part_id` AS `ancestor_part_id`,
        `bd`.`distance` AS `distance`,
        IF(((`ii2`.`part_id` <> `b`.`parent_part_id`)
                OR (`ii`.`part_id` <> `bc`.`child_part_id`)),
            'Interchange',
            `bd`.`type`) AS `type`
    FROM
        ((((((`bom_descendant` `bd`
        JOIN `bom` `b` ON ((`bd`.`part_bom_id` = `b`.`id`)))
        INNER JOIN `interchange_item` `ii1` ON ((`b`.`parent_part_id` = `ii1`.`part_id`)))
        INNER JOIN `interchange_item` `ii2` ON ((`ii2`.`interchange_header_id` = `ii1`.`interchange_header_id`)))
        JOIN `bom` `bc` ON ((`bd`.`descendant_bom_id` = `bc`.`id`)))
        INNER JOIN `interchange_item` `ii3` ON ((`bc`.`child_part_id` = `ii3`.`part_id`)))
        INNER JOIN `interchange_item` `ii` ON ((`ii3`.`interchange_header_id` = `ii`.`interchange_header_id`)));

-- Parts and their ancestor turbos
DROP VIEW IF EXISTS vpart_turbo;
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW vpart_turbo AS (
  SELECT DISTINCT
    ba.part_id,
    ba.ancestor_part_id AS turbo_id
  FROM
    vbom_ancestor ba
    JOIN turbo t ON t.part_id = ba.ancestor_part_id
) UNION DISTINCT (
  SELECT part_id, part_id FROM turbo
);

DROP VIEW IF EXISTS vapp;
CREATE
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW vapp AS
  SELECT DISTINCT
    p.id AS part_id,
    cmake.name AS car_make,
    cyear.name AS car_year,
    cmodel.name AS car_model
    FROM part p
    JOIN vpart_turbo pt ON pt.part_id = p.id
    JOIN turbo_car_model_engine_year c ON c.part_id = pt.turbo_id
    LEFT JOIN car_model_engine_year cmey ON cmey.id = c.car_model_engine_year_id
    LEFT JOIN car_model cmodel ON cmodel.id = cmey.car_model_id
    LEFT JOIN car_make cmake ON cmake.id = cmodel.car_make_id
    LEFT JOIN car_year cyear ON cyear.id = cmey.car_year_id;

DROP VIEW IF EXISTS vtapp;
CREATE
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW vtapp AS
  SELECT DISTINCT
    t.part_id,
    cmake.name AS car_make,
    cyear.name AS car_year,
    cmodel.name AS car_model
    FROM turbo t
    JOIN turbo_car_model_engine_year c ON c.part_id = t.part_id
    LEFT JOIN car_model_engine_year cmey ON cmey.id = c.car_model_engine_year_id
    LEFT JOIN car_model cmodel ON cmodel.id = cmey.car_model_id
    LEFT JOIN car_make cmake ON cmake.id = cmodel.car_make_id
    LEFT JOIN car_year cyear ON cyear.id = cmey.car_year_id;

DROP VIEW IF EXISTS vbom;
CREATE
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW vbom AS
  SELECT
    b.id AS bom_id,

    pp.id AS p_part_id,
    ppt.`name` AS p_part_type,
    pp.manfr_part_num AS p_part_number,
    ppm.name AS p_manufacturer,

    cp.id AS c_part_id,
    cpt.`name` AS c_part_type,
    cp.manfr_part_num AS c_part_number,
    cpm.name AS c_manufacturer
  FROM bom b
    JOIN part pp ON pp.id = b.parent_part_id
    JOIN part_type ppt ON ppt.id = pp.part_type_id
    JOIN manfr ppm ON ppm.id = pp.manfr_id

    JOIN part cp ON cp.id = b.child_part_id
    JOIN part_type cpt ON cpt.id = cp.part_type_id
    JOIN manfr cpm ON cpm.id = cp.manfr_id;

DROP VIEW IF EXISTS vbalt;
CREATE
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW vbalt AS
  SELECT
    bai.bom_id,
    b.child_part_id,
    bai.bom_alt_header_id,
    bah.`name` AS alt_header_name,
    bah.description AS alt_header_desc,
    bai.part_id AS alt_part_id,
    pt.`name` AS alt_part_type,
    p.manfr_part_num AS alt_part_number,
    m.`name` AS alt_manufacturer,
    m.`id` AS alt_manufacturer_id
  FROM
    bom_alt_item bai
    JOIN bom b ON b.id = bai.bom_id
    JOIN bom_alt_header bah ON bah.id = bai.bom_alt_header_id
    JOIN part p ON p.id = bai.part_id
    JOIN part_type pt ON pt.id = p.part_type_id
    JOIN manfr m ON m.id = p.manfr_id;

DROP VIEW IF EXISTS vparts;
CREATE
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW vparts AS
  SELECT
    p.id AS part_id,
    pt.`name` AS part_type,
    p.manfr_part_num AS part_number,
    m.name AS manufacturer
  FROM part p
    JOIN part_type pt ON pt.id = p.part_type_id
    JOIN manfr m ON m.id = p.manfr_id;

DROP VIEW IF EXISTS vint;
CREATE
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW vint AS
  SELECT DISTINCT
    ii1.interchange_header_id AS interchange_header_id,

    p.id AS part_id,
    pt.`name` AS part_type,
    p.manfr_part_num AS part_number,
    pm.name AS manufacturer,

    ip.id AS i_part_id,
    ipt.`name` AS i_part_type,
    ip.manfr_part_num AS i_part_number,
    ipm.name AS i_manufacturer
  FROM part p
    JOIN part_type pt ON pt.id = p.part_type_id
    JOIN manfr pm ON pm.id = p.manfr_id

    JOIN interchange_item ii1 ON ii1.part_id = p.id
    LEFT JOIN interchange_item ii2 ON ii2.interchange_header_id = ii1.interchange_header_id

    LEFT JOIN part ip ON ip.id = ii2.part_id
    JOIN part_type ipt ON ipt.id = ip.part_type_id
    LEFT JOIN manfr ipm ON ipm.id = ip.manfr_id
  WHERE p.id != ii2.part_id;

DROP VIEW IF EXISTS vint_ti;
CREATE
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW vint_ti AS
  SELECT DISTINCT
    ii1.interchange_header_id AS interchange_header_id,

    p.id AS part_id,
    ip.id AS ti_part_id
  FROM
    part p
    JOIN interchange_item ii1 ON ii1.part_id = p.id
    LEFT JOIN interchange_item ii2 ON
        ii2.interchange_header_id = ii1.interchange_header_id
        AND ii1.part_id != ii2.part_id

    LEFT JOIN part ip ON ip.id = ii2.part_id
    LEFT JOIN manfr ipm ON ipm.id = ip.manfr_id
  WHERE ip.manfr_id = 11;


-- See #270 magento: add "where used" table to part detail pages
DROP VIEW IF EXISTS `vwhere_used`;
CREATE
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW `vwhere_used` AS
    SELECT
        -- The principal
        p.id                   AS principal_id,
        pt.`name`              AS principal_type,

        -- The ancestor
        ap.id                  AS sku,
        apm.`name`             AS manufacturer,
        ap.manfr_part_num      AS part_number,
        aptii.ti_part_id       AS ti_sku,
        aptiip.manfr_part_num  AS ti_part_number,

        -- Ancestor part type, used by turbo principals
        aptype.`name`             AS part_type,

        -- Ancestor turbo's type, used by cartridge principals
        apt2t.`name`           AS turbo_type,

        -- Ancestor cartridge's turbo mfr p/n, used by all other principals
        apcatp.manfr_part_num  AS turbo_part_number
    FROM
        -- The principal
        vbom_ancestor ba
        JOIN part      p  ON p.id  = ba.part_id
        JOIN part_type pt ON pt.id = p.part_type_id

        -- The ancestor
        JOIN part      ap     ON ap.id  = ba.ancestor_part_id
        JOIN part_type aptype ON aptype.id = ap.part_type_id
        JOIN manfr     apm    ON apm.id = ap.manfr_id

        -- Ancestor turbo type, used by cartridge principals
        LEFT JOIN turbo       apt2  ON apt2.part_id = ap.id
        LEFT JOIN turbo_model apt2m ON apt2m.id     = apt2.turbo_model_id
        LEFT JOIN turbo_type  apt2t ON apt2t.id     = apt2m.turbo_type_id

        -- Ancestor cartridges and their interchanges, used by non-cartridge and non-turbo principals
        LEFT JOIN cartridge apc    ON apc.part_id    = ba.ancestor_part_id
        LEFT JOIN vint_ti   aptii  ON aptii.part_id  = ba.ancestor_part_id
        LEFT JOIN part      aptiip ON aptiip.id      = aptii.ti_part_id

        -- Ancestor cartridges' ancestor turbos, used by non-cartridge and non-turbo principals
        LEFT JOIN vbom_ancestor apcba  ON apcba.part_id = apc.part_id AND apcba.distance != 0
        LEFT JOIN turbo         apcat  ON apcat.part_id = apcba.ancestor_part_id
        LEFT JOIN part          apcatp ON apcatp.id     = apcat.part_id
    WHERE
        ba.distance != 0
        AND CASE pt.`magento_attribute_set`
             -- Turbos list all ancestors
            WHEN 'Turbo' THEN 1

            -- Cartridges list turbo ancestors
            WHEN 'Cartridge' THEN aptype.`name` = 'Turbo'

            -- Everything else lists cartridge ancestors
            ELSE aptype.`name` = 'Cartridge'
        END;

DROP VIEW IF EXISTS `vkp`;
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW `vkp` AS SELECT DISTINCT
    kii.part_id  AS kit_id,
    pii.part_id AS part_id,
    kc.exclude
FROM
    -- All the kit's interchanges
    kit_part_common_component kc
    JOIN interchange_item ki ON ki.part_id = kc.kit_id
    JOIN interchange_item kii ON kii.interchange_header_id = ki.interchange_header_id

    -- All the target part's interchanges
    JOIN interchange_item pi ON pi.part_id = kc.part_id
    JOIN interchange_item pii ON pii.interchange_header_id = pi.interchange_header_id;

DROP VIEW IF EXISTS `vpart_turbotype_kits`;
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW `vpart_turbotype_kits` AS (
    SELECT
        kc.part_id AS part_id,
        kc.kit_id
    FROM
        kit_part_common_component kc
    WHERE kc.exclude = 0
) UNION DISTINCT (
    SELECT
        p.id AS part_id,
        k.part_id AS kit_id
    FROM

        -- Part-Kit by common turbo type
        part p
        JOIN part_turbo_type ptt ON ptt.part_id = p.id
        JOIN part_turbo_type ptt2 ON ptt2.turbo_type_id = ptt.turbo_type_id
        JOIN kit k ON
            k.part_id = ptt2.part_id AND p.id != k.part_id

        -- Join exclusion table
        LEFT JOIN kit_part_common_component kc ON
            kc.kit_id = k.part_id
            AND kc.part_id = p.id
            AND kc.exclude = 1
    -- Exclusion
    WHERE kc.exclude IS NULL
) UNION DISTINCT ( -- Turbo Type from Turbos
    SELECT
        t.part_id AS part_id,
        k.part_id AS kit_id
    FROM

        -- Turbo-Kit by common turbo type
        turbo t
        JOIN turbo_model tm ON tm.id = t.turbo_model_id
        JOIN part_turbo_type ptt ON
            ptt.turbo_type_id = tm.turbo_type_id AND ptt.part_id != t.part_id
        JOIN kit k ON
            k.part_id = ptt.part_id AND t.part_id != k.part_id

        -- Join exclusion table
        LEFT JOIN kit_part_common_component kc ON
            kc.kit_id = k.part_id
            AND kc.part_id = t.part_id
            AND kc.exclude = 1
    -- Exclusion
    WHERE kc.exclude IS NULL
);

--
-- Magmi Views
--
-- #443, #468
DROP VIEW IF EXISTS `vmagmi_bom`;
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW `vmagmi_bom` AS
SELECT DISTINCT
  bd.part_id_ancestor         AS ancestor_sku,
  bd.part_id_descendant       AS descendant_sku,
  bd.qty                      AS quantity,
  bd.distance                 AS distance,
  bd.`type`                   AS `type`,
  dppt.`value`                AS part_type_parent,
  if(db.id is not null, 1, 0) AS has_bom,

  -- Alternates
  alt.child_part_id       AS alt_sku,
  alt.alt_manufacturer_id AS alt_mfr_id,

  -- TI Interchanges
  vit.ti_part_id              AS int_sku
FROM vbom_descendant bd

  -- for descendant Part Type Parent
  INNER JOIN part       dp ON dp.id = bd.part_id_descendant
  INNER JOIN part_type dpt ON dpt.id = dp.part_type_id
  LEFT JOIN part_type dppt ON dppt.id = dpt.parent_part_type_id

  -- For descendant has_bom
  LEFT JOIN vbalt   alt ON alt.bom_id = bd.part_bom_id
  LEFT JOIN vint_ti vit ON vit.part_id = bd.part_id_descendant
  LEFT JOIN bom      db ON db.parent_part_id = bd.part_id_descendant

  -- Find interchange parts that are already direct descendants
  LEFT JOIN vbom_descendant_direct bdd on bd.part_id_ancestor = bdd.part_id_ancestor and bd.part_id_descendant = bdd.part_id_descendant and bd.type = 'Interchange'

WHERE
  -- Remove interchange parts that are already direct descendants
  bdd.id is null
;


DROP VIEW IF EXISTS `vmagmi_service_kits`;
CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `metaserver`@`%` 
    SQL SECURITY DEFINER
VIEW `vmagmi_service_kits` AS
SELECT DISTINCT
  p.id               AS sku,
  k.id               AS kitSku,
  k.manfr_part_num   AS kitPartNumber,
  k.description      AS description,
  kti.id             AS tiKitSku,
  kti.manfr_part_num AS tiKitPartNumber
FROM
  part p
  JOIN vpart_turbotype_kits vpttk ON p.id        = vpttk.part_id
  JOIN part                 k     ON k.id        = vpttk.kit_id
  LEFT JOIN vint_ti         iti   ON iti.part_id = k.id
  LEFT JOIN part            kti   ON kti.id      = iti.ti_part_id;

DROP VIEW IF EXISTS `vmagmi_ti_chra`;
CREATE
  ALGORITHM=UNDEFINED
  DEFINER=`metaserver`@`%`
  SQL SECURITY DEFINER
VIEW `vmagmi_ti_chra` AS
SELECT
  `p`.`id` AS `id`,
  (CASE WHEN (`pt`.`manfr_part_num` IS NOT NULL) THEN 'yes' ELSE 'no' END) AS `has_ti_chra`
FROM (
  `part` `p`
  LEFT JOIN (
    `vbom_descendant` `bd`
    JOIN `part` `pt` ON (`bd`.`part_id_descendant` = `pt`.`id`
                         AND `pt`.`manfr_id` = 11
                         AND `pt`.`part_type_id` = 2))
      ON`p`.`id` = `bd`.`part_id_ancestor`);


-- Standard-Oversize Part
DROP VIEW IF EXISTS `vmagmi_sop`;
CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `metaserver`@`%`
    SQL SECURITY DEFINER
VIEW `vmagmi_sop` AS
SELECT
    ssop.oversize_part_id                                   AS part_id,
    ssop.standard_part_id                                   AS standard_part_sku,
    GROUP_CONCAT(DISTINCT ii2.part_id ORDER BY ii2.part_id ASC SEPARATOR ',') AS oversize_part_skus
FROM

    -- Get the oversize TI parts for each standard-size part
    standard_oversize_part AS ssop
    JOIN standard_oversize_part AS osop ON osop.standard_part_id = ssop.standard_part_id
    LEFT JOIN part AS op ON op.id = osop.oversize_part_id AND op.manfr_id = 11

    -- Get the interchanges for the oversize parts
    LEFT JOIN interchange_item AS ii ON ii.part_id = op.id
    LEFT JOIN interchange_item AS ii2 ON ii2.interchange_header_id = ii.interchange_header_id
GROUP BY
  ssop.oversize_part_id,
  ssop.standard_part_id

-- add standard part to its own listing
UNION
SELECT
    ssop.standard_part_id AS part_id,
    ssop.standard_part_id AS standard_part_sku,
    GROUP_CONCAT(DISTINCT ii2.part_id ORDER BY ii2.part_id ASC SEPARATOR ',') AS oversize_part_skus
FROM
    standard_oversize_part ssop
    LEFT JOIN part op ON op.id = ssop.oversize_part_id AND op.manfr_id = 11

    -- Get the interchanges for the oversize parts
    LEFT JOIN interchange_item ii ON ii.part_id = op.id
    LEFT JOIN interchange_item ii2 ON ii2.interchange_header_id = ii.interchange_header_id
GROUP BY ssop.standard_part_id;

--
-- Stored Procedures
--

-- Rebuild the bom_descendant table
DELIMITER $$
DROP PROCEDURE IF EXISTS RebuildBomAncestry$$
DROP PROCEDURE IF EXISTS RebuildBomDescendancy$$
CREATE DEFINER=`metaserver`@`%` PROCEDURE `RebuildBomDescendancy`()
    SQL SECURITY INVOKER
BEGIN

    START TRANSACTION;

    DELETE FROM bom_descendant;

    -- Add a self-referencing layer 1 of the hierarchy
    INSERT IGNORE INTO bom_descendant (
      part_bom_id
      , descendant_bom_id
      , distance
      , type
      , qty
    )
    SELECT 
        b.id
        , b.id
        , 1
        , 'direct'
        , b.quantity
    FROM
        bom b;

    -- Setup the starting @distance for the loop
    SET @distance = 1;


    -- Walk the hierarcy layer by layer
    REPEAT

        -- Increment distance for this layer
        SET @distance = @distance + 1;

        -- Add the next layer in the hierarchy
        INSERT IGNORE INTO bom_descendant (
            `part_bom_id`,
            `descendant_bom_id`,
            `distance`,
            `type`,
            `qty`
        )
        SELECT
            bd.part_bom_id,
            bc.id,
            @distance,
            IF (bd.`type` = 'Interchange' OR (ii2.part_id <> b.child_part_id), 'Interchange', 'direct'),
            bc.quantity * bd.qty -- Additive quantities
        FROM
            bom_descendant bd
            
            -- get part_id from bom table
            inner join bom as b on bd.descendant_bom_id = b.id

            -- find interchangeable parts of descendant part if exists
            left join interchange_item ii1 on b.child_part_id = ii1.part_id
            left join interchange_item ii2 on ii1.interchange_header_id = ii2.interchange_header_id

            -- find any bom components of descendant part (child_part_id or its interchangeable parts)
            inner join bom as bc on coalesce(ii2.part_id, b.child_part_id) = bc.parent_part_id

        WHERE bd.distance = @distance - 1;


    UNTIL ROW_COUNT() = 0
    END REPEAT;

    COMMIT;

  END$$
DELIMITER ;

-- Verify no interchangable parts have a contradictory setting
DELIMITER $$
DROP PROCEDURE IF EXISTS `errorOnContradictoryKitPartCommonComponent`$$
CREATE PROCEDURE `errorOnContradictoryKitPartCommonComponent` ()
  SQL SECURITY INVOKER
  BEGIN
    IF (SELECT COUNT(*) FROM (
        SELECT
          kit_id, part_id, COUNT(exclude) ct
        FROM vkp
        GROUP BY
            vkp.kit_id, vkp.part_id
        HAVING
            ct > 1
    ) AS contradictions) > 0 THEN
        CALL `Contradictory VKP mappings found.`;
    END IF;
  END$$


--
-- Triggers
--
DROP TRIGGER IF EXISTS `kit_part_common_component_AI`$$
CREATE TRIGGER `kit_part_common_component_AI` AFTER INSERT ON `kit_part_common_component`
FOR EACH ROW
BEGIN
    CALL errorOnContradictoryKitPartCommonComponent();
END$$

DROP TRIGGER IF EXISTS `kit_part_common_component_AU`;
CREATE TRIGGER `kit_part_common_component_AU` AFTER UPDATE ON `kit_part_common_component`
FOR EACH ROW
BEGIN
    CALL errorOnContradictoryKitPartCommonComponent();
END$$

DROP TRIGGER IF EXISTS `interchange_item_AI`;
CREATE TRIGGER `interchange_item_AI` AFTER INSERT ON `interchange_item`
FOR EACH ROW
BEGIN
    CALL errorOnContradictoryKitPartCommonComponent();
END$$

DROP TRIGGER IF EXISTS `interchange_item_AU`;
CREATE TRIGGER `interchange_item_AU` AFTER UPDATE ON `interchange_item`
FOR EACH ROW
BEGIN
    CALL errorOnContradictoryKitPartCommonComponent();
END$$

DROP TRIGGER IF EXISTS `part_AFTER_INSERT`;
CREATE TRIGGER `part_AFTER_INSERT` AFTER INSERT ON `part`
FOR EACH ROW
BEGIN

    -- create new interchange_header record
    insert into interchange_header (new_part_id) value (new.id);

    -- get id of new interchange_header record
    set @i_header_id_new = (select id from interchange_header where new_part_id = new.id);

    -- create new interchange_item record
    insert into interchange_item (part_id, interchange_header_id) values (new.id, @i_header_id_new);

END$$

DELIMITER ;
