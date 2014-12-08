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
  `magento_attribute_set` VARCHAR(50),
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
-- Interchanges (has triggers)
--
CREATE TABLE `interchange_header` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
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
  `bom_alt_header_id` BIGINT NOT NULL,
  `bom_id` BIGINT NOT NULL,
  `part_id` BIGINT NOT NULL,
  UNIQUE KEY (`bom_alt_header_id`, `bom_id`),
  FOREIGN KEY (`bom_alt_header_id`) REFERENCES `bom_alt_header` (`id`),
  FOREIGN KEY (`bom_id`) REFERENCES `bom` (`id`),
  FOREIGN KEY (`part_id`)  REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE `bom_ancestor` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_id` BIGINT NOT NULL,
  `ancestor_part_id` BIGINT NOT NULL,
  `distance` INTEGER NOT NULL,
  `type` VARCHAR(20) NOT NULL,
  INDEX(distance),
  UNIQUE INDEX(`part_id`, `ancestor_part_id`),
  INDEX(part_id, `type`, distance, ancestor_part_id)
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


DROP VIEW IF EXISTS vpart_turbo;
CREATE VIEW vpart_turbo AS (
  SELECT DISTINCT
    ba.part_id,
    ba.ancestor_part_id AS turbo_id
  FROM
    bom_ancestor ba
    JOIN turbo t ON t.part_id = ba.ancestor_part_id
) UNION DISTINCT (
  SELECT part_id, turbo_id FROM part_turbo
);

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
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_ADMIN')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_BOM')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_BOM_ALT')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_CREATE_PART')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_ALTER_PART')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_DELETE_PART')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_INTERCHANGE')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_PART_IMAGES')),
  ((SELECT id FROM groups WHERE `name` = 'Admin'),  (SELECT id FROM role WHERE `name` = 'ROLE_READ')),
  ((SELECT id FROM groups WHERE `name` = 'Writer'), (SELECT id FROM role WHERE `name` = 'ROLE_TURBO_MODEL_CRUD'));

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


DROP VIEW IF EXISTS vapp;
CREATE VIEW vapp AS
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
CREATE VIEW vtapp AS
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
CREATE VIEW vbom AS
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
CREATE VIEW vbalt AS
  SELECT
    bai.bom_id,
    bai.part_id,
    bai.bom_alt_header_id,
    bah.`name` AS alt_header_name,
    bah.description AS alt_header_desc,
    bai.part_id AS alt_part_id,
    pt.`name` AS alt_part_type,
    p.manfr_part_num AS alt_part_number,
    m.`name` AS alt_manufacturer
  FROM
    bom_alt_item bai
    JOIN bom b ON b.id = bai.bom_id
    JOIN bom_alt_header bah ON bah.id = bai.bom_alt_header_id
    JOIN part p ON p.id = bai.part_id
    JOIN part_type pt ON pt.id = p.part_type_id
    JOIN manfr m ON m.id = p.manfr_id;

DROP VIEW IF EXISTS vparts;
CREATE VIEW vparts AS
  SELECT
    p.id AS part_id,
    pt.`name` AS part_type,
    p.manfr_part_num AS part_number,
    m.name AS manufacturer
  FROM part p
    JOIN part_type pt ON pt.id = p.part_type_id
    JOIN manfr m ON m.id = p.manfr_id;

DROP VIEW IF EXISTS vint;
CREATE VIEW vint AS
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
CREATE VIEW vint_ti AS
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
CREATE VIEW `vwhere_used` AS
    SELECT DISTINCT
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
        bom_ancestor ba
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
        LEFT JOIN bom_ancestor apcba  ON apcba.part_id = apc.part_id AND apcba.distance != 0
        LEFT JOIN turbo        apcat  ON apcat.part_id = apcba.ancestor_part_id
        LEFT JOIN part         apcatp ON apcatp.id     = apcat.part_id
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
CREATE VIEW `vkp` AS SELECT DISTINCT
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

CREATE VIEW `vpart_turbotype_kits` AS (
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


-- Rebuild the BOM ancestry table
DELIMITER $$
DROP PROCEDURE IF EXISTS RebuildBomAncestry$$
CREATE PROCEDURE RebuildBomAncestry()
  BEGIN

    START TRANSACTION;

    -- Empty the table
    TRUNCATE bom_ancestor;

    -- Create self-references
    INSERT IGNORE INTO bom_ancestor (
      `part_id`,
      `ancestor_part_id`,
      `distance`,
      `type`
    )
    SELECT DISTINCT
      id     AS part_id,
      id     AS ancestor_part_id,
      0      AS distance,
      'Self' AS `type`
    FROM
      part;

    REPEAT
        SET @distance = (SELECT MAX(distance) FROM bom_ancestor);

        -- Add interchange parents (metaphor: interchanges are my half-siblings, add all my half-parents)
        INSERT IGNORE INTO bom_ancestor (
          `part_id`,
          `ancestor_part_id`,
          `distance`,
          `type`
        )
        SELECT DISTINCT
          ba.part_id          AS part_id,          -- Me
          bom.parent_part_id  AS ancestor_part_id, -- My half-parent
          @distance + 1       AS distance,
          IF (@distance = 0, 'InterchangeDirect', 'InterchangeIndirect') AS `type`
        FROM
          bom_ancestor ba
          JOIN interchange_item ii ON ii.part_id = ba.ancestor_part_id
          JOIN interchange_item ii2 ON
            ii2.interchange_header_id = ii.interchange_header_id
            AND ii2.part_id <> ii.part_id
          JOIN bom ON bom.child_part_id = ii2.part_id
        WHERE ba.distance = @distance;

        -- Add the next level of parents
        INSERT IGNORE INTO bom_ancestor (
          `part_id`,
          `ancestor_part_id`,
          `distance`,
          `type`
        )
        SELECT DISTINCT
          ba.part_id           AS part_id,
          bom.parent_part_id   AS ancestor_part_id,
          @distance + 1      AS distance,
          IF (@distance = 0, 'Direct', IF(ba.`type` LIKE 'Interchange%', 'InterchangeIndirect', 'Indirect')) AS `type`
        FROM
          bom_ancestor ba
          JOIN bom ON bom.child_part_id = ba.ancestor_part_id
        WHERE ba.distance = @distance;

    UNTIL ROW_COUNT() = 0
    END REPEAT;

    COMMIT;
    
  END$$
DELIMITER ;

-- Verify no interchangable parts have a contradictory setting
DELIMITER $$
DROP PROCEDURE IF EXISTS `errorOnContradictoryKitPartCommonComponent`$$
CREATE PROCEDURE `errorOnContradictoryKitPartCommonComponent` ()
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

DELIMITER ;
