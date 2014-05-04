ALTER TABLE `part`
  ADD COLUMN `temp1_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  ADD COLUMN `temp2_int` BIGINT NULL,
  ADD COLUMN `temp3_int` BIGINT NULL,
  ADD COLUMN `temp1_dec` DECIMAL(18, 6) NULL,
  ADD COLUMN `temp2_dec` DECIMAL(18, 6) NULL,
  ADD COLUMN `temp3_dec` DECIMAL(18, 6) NULL,
  ADD COLUMN `temp4_dec` DECIMAL(18, 6) NULL,
  ADD COLUMN `temp5_dec` DECIMAL(18, 6) NULL,
  ADD COLUMN `temp6_dec` DECIMAL(18, 6) NULL,
  ADD COLUMN `temp2_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  ADD COLUMN `temp3_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  ADD COLUMN `temp4_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  ADD COLUMN `temp5_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  ADD COLUMN `temp6_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  ADD COLUMN `temp7_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  ADD COLUMN `temp8_char` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL;

CREATE TABLE IF NOT EXISTS `attribute_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_types_id` BIGINT NULL,
  `name` VARCHAR(255) NOT NULL,
  UNIQUE INDEX (`part_types_id`, `name`),
  FOREIGN KEY (`part_types_id`) REFERENCES `part_type` (`id`)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS `part_attribute` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `part_id` BIGINT NULL,
  `attribute_type_id` BIGINT NULL,
  `value` VARCHAR(255) NULL,
  UNIQUE INDEX (`part_id`, `attribute_type_id`),
  FOREIGN KEY (`attribute_type_id`) REFERENCES `attribute_type` (`id`),
  FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS `bom_hierarchy` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `child_part_type_id` BIGINT NOT NULL,
  `parent_part_type_id` BIGINT NOT NULL,
  UNIQUE INDEX (`parent_part_type_id`, `child_part_type_id`),
  INDEX (`child_part_type_id`),
  INDEX (`parent_part_type_id`),
  FOREIGN KEY (`child_part_type_id`) REFERENCES `part_type` (`id`),
  FOREIGN KEY (`parent_part_type_id`) REFERENCES `part_type` (`id`)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS `sql server destination` (
  `turbo_type_id` int(10) DEFAULT NULL,
  `turbo_type_name` varchar(50) DEFAULT NULL,
  `turbo_manfr_id` int(10) DEFAULT NULL,
  `turbo_manfr_name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;