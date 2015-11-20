-- MySQL dump 10.13  Distrib 5.6.27, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: metadata
-- ------------------------------------------------------
-- Server version	5.6.27-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `backplate`
--

DROP TABLE IF EXISTS `backplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `backplate` (
  `part_id` bigint(20) NOT NULL,
  `seal_type_id` bigint(20) DEFAULT NULL,
  `style_compressor_wheel` varchar(100) DEFAULT NULL,
  `seal_type` varchar(100) DEFAULT NULL,
  `overall_diameter` decimal(10,6) DEFAULT NULL,
  `compressor_wheel_diameter` decimal(10,6) DEFAULT NULL,
  `piston_ring_diameter` decimal(10,6) DEFAULT NULL,
  `compressor_housing_diameter` decimal(10,6) DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `secondary_diameter` decimal(10,6) DEFAULT NULL,
  `overall_height` decimal(10,6) DEFAULT NULL,
  KEY `part_id` (`part_id`),
  CONSTRAINT `backplate_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bearing_housing`
--

DROP TABLE IF EXISTS `bearing_housing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bearing_housing` (
  `part_id` bigint(20) NOT NULL,
  `cool_type_id` bigint(20) DEFAULT NULL,
  `oil_inlet` varchar(100) DEFAULT NULL,
  `oil_outlet` varchar(100) DEFAULT NULL,
  `oil` varchar(100) DEFAULT NULL,
  `outlet_flange_holes` varchar(100) DEFAULT NULL,
  `water_ports` varchar(100) DEFAULT NULL,
  `design_features` varchar(100) DEFAULT NULL,
  `bearing_type` varchar(100) DEFAULT NULL,
  KEY `part_id` (`part_id`),
  CONSTRAINT `bearing_housing_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bearing_spacer`
--

DROP TABLE IF EXISTS `bearing_spacer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bearing_spacer` (
  `part_id` bigint(20) NOT NULL,
  `outside_dim_min` decimal(10,6) DEFAULT NULL,
  `outside_dim_max` decimal(10,6) DEFAULT NULL,
  `inside_dim_min` decimal(10,6) DEFAULT NULL,
  `inside_dim_max` decimal(10,6) DEFAULT NULL,
  KEY `part_id` (`part_id`),
  CONSTRAINT `bearing_spacer_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bom`
--

DROP TABLE IF EXISTS `bom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bom` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_part_id` bigint(20) NOT NULL,
  `child_part_id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parent_part_id` (`parent_part_id`,`child_part_id`),
  KEY `child_part_id` (`child_part_id`),
  CONSTRAINT `bom_ibfk_1` FOREIGN KEY (`parent_part_id`) REFERENCES `part` (`id`),
  CONSTRAINT `bom_ibfk_2` FOREIGN KEY (`child_part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57412 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bom_alt_header`
--

DROP TABLE IF EXISTS `bom_alt_header`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bom_alt_header` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1544 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bom_alt_item`
--

DROP TABLE IF EXISTS `bom_alt_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bom_alt_item` (
  `bom_alt_header_id` bigint(20) NOT NULL,
  `bom_id` bigint(20) NOT NULL,
  `part_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `bom_alt_header_id` (`bom_alt_header_id`,`bom_id`),
  KEY `bom_id` (`bom_id`),
  KEY `part_id` (`part_id`),
  CONSTRAINT `bom_alt_item_ibfk_1` FOREIGN KEY (`bom_alt_header_id`) REFERENCES `bom_alt_header` (`id`),
  CONSTRAINT `bom_alt_item_ibfk_2` FOREIGN KEY (`bom_id`) REFERENCES `bom` (`id`),
  CONSTRAINT `bom_alt_item_ibfk_3` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1544 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bom_descendant`
--

DROP TABLE IF EXISTS `bom_descendant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bom_descendant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `part_bom_id` bigint(20) NOT NULL,
  `descendant_bom_id` bigint(20) NOT NULL,
  `distance` int(11) NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  `qty` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `part_bom_id` (`part_bom_id`,`descendant_bom_id`,`distance`,`type`,`qty`),
  KEY `distance` (`distance`),
  KEY `part_bom_id_2` (`part_bom_id`,`type`,`distance`,`descendant_bom_id`),
  KEY `descendant_bom_id` (`descendant_bom_id`),
  CONSTRAINT `bom_descendant_ibfk_3` FOREIGN KEY (`part_bom_id`) REFERENCES `bom` (`id`) ON DELETE CASCADE,
  CONSTRAINT `bom_descendant_ibfk_4` FOREIGN KEY (`descendant_bom_id`) REFERENCES `bom` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=117990537 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `car_engine`
--

DROP TABLE IF EXISTS `car_engine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_engine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `engine_size` varchar(50) NOT NULL,
  `car_fuel_type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `engine_size` (`engine_size`,`car_fuel_type_id`),
  KEY `car_fuel_type_id` (`car_fuel_type_id`),
  CONSTRAINT `car_engine_ibfk_1` FOREIGN KEY (`car_fuel_type_id`) REFERENCES `car_fuel_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=555 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `car_fuel_type`
--

DROP TABLE IF EXISTS `car_fuel_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_fuel_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `car_make`
--

DROP TABLE IF EXISTS `car_make`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_make` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=268 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `car_model`
--

DROP TABLE IF EXISTS `car_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `car_make_id` bigint(20) NOT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`,`car_make_id`),
  KEY `car_make_id` (`car_make_id`),
  CONSTRAINT `car_model_ibfk_1` FOREIGN KEY (`car_make_id`) REFERENCES `car_make` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2135 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `car_model_engine_year`
--

DROP TABLE IF EXISTS `car_model_engine_year`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_model_engine_year` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `car_model_id` bigint(20) DEFAULT NULL,
  `car_engine_id` bigint(20) DEFAULT NULL,
  `car_year_id` bigint(20) DEFAULT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `car_model_id` (`car_model_id`,`car_engine_id`,`car_year_id`),
  KEY `car_engine_id` (`car_engine_id`),
  KEY `car_year_id` (`car_year_id`),
  CONSTRAINT `car_model_engine_year_ibfk_1` FOREIGN KEY (`car_model_id`) REFERENCES `car_model` (`id`),
  CONSTRAINT `car_model_engine_year_ibfk_2` FOREIGN KEY (`car_engine_id`) REFERENCES `car_engine` (`id`),
  CONSTRAINT `car_model_engine_year_ibfk_3` FOREIGN KEY (`car_year_id`) REFERENCES `car_year` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6424 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `car_year`
--

DROP TABLE IF EXISTS `car_year`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_year` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cartridge`
--

DROP TABLE IF EXISTS `cartridge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cartridge` (
  `part_id` bigint(20) NOT NULL,
  KEY `part_id` (`part_id`),
  CONSTRAINT `cartridge_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `changelog`
--

DROP TABLE IF EXISTS `changelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `changelog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `change_date` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `data` longtext,
  PRIMARY KEY (`id`),
  KEY `change_date` (`change_date`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `changelog_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=906 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `compressor_wheel`
--

DROP TABLE IF EXISTS `compressor_wheel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `compressor_wheel` (
  `part_id` bigint(20) NOT NULL,
  `inducer_oa` decimal(10,6) DEFAULT NULL,
  `tip_height_b` decimal(10,6) DEFAULT NULL,
  `exducer_oc` decimal(10,6) DEFAULT NULL,
  `hub_length_d` decimal(10,6) DEFAULT NULL,
  `bore_oe` decimal(10,6) DEFAULT NULL,
  `trim_no_blades` varchar(100) DEFAULT NULL,
  `application` varchar(100) DEFAULT NULL,
  KEY `part_id` (`part_id`),
  CONSTRAINT `compressor_wheel_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cool_type`
--

DROP TABLE IF EXISTS `cool_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cool_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deleted_parts`
--

DROP TABLE IF EXISTS `deleted_parts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deleted_parts` (
  `id` bigint(20) NOT NULL,
  `dt` timestamp NULL DEFAULT NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gasket`
--

DROP TABLE IF EXISTS `gasket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gasket` (
  `part_id` bigint(20) NOT NULL,
  `gasket_type_id` bigint(20) NOT NULL,
  KEY `part_id` (`part_id`),
  KEY `gasket_type_id` (`gasket_type_id`),
  CONSTRAINT `gasket_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  CONSTRAINT `gasket_ibfk_2` FOREIGN KEY (`gasket_type_id`) REFERENCES `gasket_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gasket_type`
--

DROP TABLE IF EXISTS `gasket_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gasket_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_role`
--

DROP TABLE IF EXISTS `group_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_role` (
  `group_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`group_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `group_role_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `group_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `version` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `heatshield`
--

DROP TABLE IF EXISTS `heatshield`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `heatshield` (
  `part_id` bigint(20) NOT NULL,
  `overall_diameter` decimal(10,6) DEFAULT NULL,
  `inside_diameter` decimal(10,6) DEFAULT NULL,
  `inducer_diameter` decimal(10,6) DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  KEY `part_id` (`part_id`),
  CONSTRAINT `heatshield_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `i_interchange_log`
--

DROP TABLE IF EXISTS `i_interchange_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `i_interchange_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(250) DEFAULT NULL,
  `part_num_id` bigint(20) DEFAULT NULL,
  `interchange_header_id` bigint(20) DEFAULT NULL,
  `interchange_cnt_uniq` bigint(20) DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16111 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interchange_header`
--

DROP TABLE IF EXISTS `interchange_header`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interchange_header` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `new_part_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `new_part_id_UNIQUE` (`new_part_id`),
  CONSTRAINT `new_part_id` FOREIGN KEY (`new_part_id`) REFERENCES `part` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=62634 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `interchange_item`
--

DROP TABLE IF EXISTS `interchange_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interchange_item` (
  `part_id` bigint(20) NOT NULL,
  `interchange_header_id` bigint(20) NOT NULL,
  PRIMARY KEY (`part_id`),
  KEY `interchange_header_id` (`interchange_header_id`),
  CONSTRAINT `interchange_item_ibfk_1` FOREIGN KEY (`interchange_header_id`) REFERENCES `interchange_header` (`id`),
  CONSTRAINT `interchange_item_ibfk_2` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`metaserver`@`%`*/ /*!50003 TRIGGER `interchange_item_AI` AFTER INSERT ON `interchange_item`
FOR EACH ROW
BEGIN
    CALL errorOnContradictoryKitPartCommonComponent();
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`metaserver`@`%`*/ /*!50003 TRIGGER `interchange_item_AU` AFTER UPDATE ON `interchange_item`
FOR EACH ROW
BEGIN
    CALL errorOnContradictoryKitPartCommonComponent();
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `journal_bearing`
--

DROP TABLE IF EXISTS `journal_bearing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `journal_bearing` (
  `part_id` bigint(20) NOT NULL,
  `outside_dim_min` decimal(10,6) NOT NULL,
  `outside_dim_max` decimal(10,6) NOT NULL,
  `inside_dim_min` decimal(10,6) NOT NULL,
  `inside_dim_max` decimal(10,6) NOT NULL,
  `width` decimal(10,6) NOT NULL,
  KEY `part_id` (`part_id`),
  CONSTRAINT `journal_bearing_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kit`
--

DROP TABLE IF EXISTS `kit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kit` (
  `part_id` bigint(20) NOT NULL,
  `kit_type_id` bigint(20) NOT NULL,
  KEY `kit_type_id` (`kit_type_id`),
  KEY `part_id` (`part_id`),
  CONSTRAINT `kit_ibfk_1` FOREIGN KEY (`kit_type_id`) REFERENCES `kit_type` (`id`),
  CONSTRAINT `kit_ibfk_2` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kit_part_common_component`
--

DROP TABLE IF EXISTS `kit_part_common_component`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kit_part_common_component` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `kit_id` bigint(20) NOT NULL,
  `part_id` bigint(20) NOT NULL,
  `exclude` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `kit_id` (`kit_id`,`part_id`),
  KEY `part_id` (`part_id`),
  CONSTRAINT `kit_part_common_component_ibfk_1` FOREIGN KEY (`kit_id`) REFERENCES `part` (`id`),
  CONSTRAINT `kit_part_common_component_ibfk_2` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`metaserver`@`%`*/ /*!50003 TRIGGER `kit_part_common_component_AI` AFTER INSERT ON `kit_part_common_component`
FOR EACH ROW
BEGIN
    CALL errorOnContradictoryKitPartCommonComponent();
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`metaserver`@`%`*/ /*!50003 TRIGGER `kit_part_common_component_AU` AFTER UPDATE ON `kit_part_common_component`
FOR EACH ROW
BEGIN
    CALL errorOnContradictoryKitPartCommonComponent();
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `kit_type`
--

DROP TABLE IF EXISTS `kit_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kit_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(1000) NOT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `manfr`
--

DROP TABLE IF EXISTS `manfr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manfr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `manfr_type_id` bigint(20) NOT NULL,
  `parent_manfr_id` bigint(20) DEFAULT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `manfr_type_id` (`manfr_type_id`),
  KEY `parent_manfr_id` (`parent_manfr_id`),
  CONSTRAINT `manfr_ibfk_1` FOREIGN KEY (`manfr_type_id`) REFERENCES `manfr_type` (`id`),
  CONSTRAINT `manfr_ibfk_2` FOREIGN KEY (`parent_manfr_id`) REFERENCES `manfr` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `manfr_type`
--

DROP TABLE IF EXISTS `manfr_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manfr_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mas90_std_price`
--

DROP TABLE IF EXISTS `mas90_std_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mas90_std_price` (
  `ItemNumber` varchar(50) NOT NULL,
  `StdPrice` decimal(10,2) NOT NULL DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nozzle_ring`
--

DROP TABLE IF EXISTS `nozzle_ring`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nozzle_ring` (
  `part_id` bigint(20) NOT NULL,
  KEY `part_id` (`part_id`),
  CONSTRAINT `nozzle_ring_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `part`
--

DROP TABLE IF EXISTS `part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `manfr_part_num` varchar(255) NOT NULL,
  `manfr_id` bigint(20) NOT NULL,
  `part_type_id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `inactive` tinyint(1) NOT NULL DEFAULT '0',
  `import_pk` bigint(20) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`,`version`),
  UNIQUE KEY `manfr_part_num` (`manfr_part_num`,`manfr_id`),
  KEY `manfr_id` (`manfr_id`),
  KEY `part_type_id` (`part_type_id`),
  CONSTRAINT `part_ibfk_1` FOREIGN KEY (`manfr_id`) REFERENCES `manfr` (`id`),
  CONSTRAINT `part_ibfk_2` FOREIGN KEY (`part_type_id`) REFERENCES `part_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63537 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`metaserver`@`%`*/ /*!50003 TRIGGER `metadata`.`part_AFTER_INSERT` AFTER INSERT ON `part` FOR EACH ROW
BEGIN

    
    insert into interchange_header (new_part_id) value (new.id);

    
    set @i_header_id_new = (select id from interchange_header where new_part_id = new.id);

    
    insert into interchange_item (part_id, interchange_header_id) values (new.id, @i_header_id_new);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `part_turbo`
--

DROP TABLE IF EXISTS `part_turbo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `part_turbo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `part_id` bigint(20) NOT NULL,
  `turbo_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `part_id` (`part_id`),
  KEY `turbo_id` (`turbo_id`),
  CONSTRAINT `part_turbo_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  CONSTRAINT `part_turbo_ibfk_2` FOREIGN KEY (`turbo_id`) REFERENCES `turbo` (`part_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `part_turbo_type`
--

DROP TABLE IF EXISTS `part_turbo_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `part_turbo_type` (
  `part_id` bigint(20) NOT NULL,
  `turbo_type_id` bigint(20) NOT NULL,
  PRIMARY KEY (`part_id`,`turbo_type_id`),
  KEY `turbo_type_id` (`turbo_type_id`),
  CONSTRAINT `part_turbo_type_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  CONSTRAINT `part_turbo_type_ibfk_2` FOREIGN KEY (`turbo_type_id`) REFERENCES `turbo_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `part_type`
--

DROP TABLE IF EXISTS `part_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `part_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `parent_part_type_id` bigint(20) DEFAULT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  `magento_attribute_set` varchar(50) DEFAULT NULL,
  `value` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `value_UNIQUE` (`value`),
  KEY `parent_part_type_id` (`parent_part_type_id`),
  CONSTRAINT `part_type_ibfk_1` FOREIGN KEY (`parent_part_type_id`) REFERENCES `part_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `piston_ring`
--

DROP TABLE IF EXISTS `piston_ring`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `piston_ring` (
  `part_id` bigint(20) NOT NULL,
  `outside_dim_min` decimal(10,6) DEFAULT NULL,
  `outside_dim_max` decimal(10,6) DEFAULT NULL,
  `width_min` decimal(10,6) DEFAULT NULL,
  `width_max` decimal(10,6) DEFAULT NULL,
  `i_gap_min` decimal(10,6) DEFAULT NULL,
  `i_gap_max` decimal(10,6) DEFAULT NULL,
  KEY `part_id` (`part_id`),
  CONSTRAINT `piston_ring_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `part_id` bigint(20) NOT NULL,
  `filename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `filename` (`filename`),
  KEY `part_id` (`part_id`),
  CONSTRAINT `product_image_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11129 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `display` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sales_note`
--

DROP TABLE IF EXISTS `sales_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sales_note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL,
  `write_date` datetime NOT NULL,
  `create_uid` bigint(20) NOT NULL,
  `write_uid` bigint(20) NOT NULL,
  `state` varchar(15) NOT NULL COMMENT 'draft;submitted;approved;rejected',
  `comment` longtext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_sales_note_user_create_uid_idx` (`create_uid`),
  KEY `fk_sales_note_user_write_uid_idx` (`write_uid`),
  CONSTRAINT `fk_sales_note_user_create_uid` FOREIGN KEY (`create_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_sales_note_user_write_uid` FOREIGN KEY (`write_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=327 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sales_note_attachment`
--

DROP TABLE IF EXISTS `sales_note_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sales_note_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL,
  `write_date` datetime NOT NULL,
  `create_uid` bigint(20) NOT NULL,
  `write_uid` bigint(20) NOT NULL,
  `sales_note_id` bigint(20) NOT NULL,
  `filename` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sales_note_id_filename` (`sales_note_id`,`filename`),
  KEY `sales_note_attachment_sales_note_sales_note_id_idx` (`sales_note_id`),
  KEY `fk_sales_note_attachment_user_create_uid_idx` (`create_uid`),
  KEY `fk_sales_note_attachment_user_write_uid_idx` (`write_uid`),
  CONSTRAINT `sales_note_attachment_sales_note_sales_note_id` FOREIGN KEY (`sales_note_id`) REFERENCES `sales_note` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sales_note_attachment_user_create_uid` FOREIGN KEY (`create_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sales_note_attachment_user_write_uid` FOREIGN KEY (`write_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sales_note_part`
--

DROP TABLE IF EXISTS `sales_note_part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sales_note_part` (
  `sales_note_id` bigint(20) NOT NULL,
  `part_id` bigint(20) NOT NULL,
  `create_date` datetime NOT NULL,
  `write_date` datetime NOT NULL,
  `create_uid` bigint(20) NOT NULL,
  `write_uid` bigint(20) NOT NULL,
  `primary_part` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`sales_note_id`,`part_id`),
  UNIQUE KEY `sales_note_part_primary_part_unq_idx` (`sales_note_id`,`primary_part`),
  KEY `sales_note_part_part_part_id_idx` (`part_id`),
  KEY `sales_note_part_user_create_uid_idx` (`create_uid`),
  KEY `sales_note_part_user_write_uid_idx` (`write_uid`),
  CONSTRAINT `sales_note_part_part_part_id` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `sales_note_part_sales_note_sales_note_id` FOREIGN KEY (`sales_note_id`) REFERENCES `sales_note` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `sales_note_part_user_create_uid` FOREIGN KEY (`create_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `sales_note_part_user_write_uid` FOREIGN KEY (`write_uid`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seal_type`
--

DROP TABLE IF EXISTS `seal_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seal_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `import_pk` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `standard_bearing_spacer`
--

DROP TABLE IF EXISTS `standard_bearing_spacer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `standard_bearing_spacer` (
  `standard_part_id` bigint(20) NOT NULL,
  `oversized_part_id` bigint(20) NOT NULL,
  PRIMARY KEY (`standard_part_id`,`oversized_part_id`),
  KEY `oversized_part_id` (`oversized_part_id`),
  CONSTRAINT `standard_bearing_spacer_ibfk_1` FOREIGN KEY (`standard_part_id`) REFERENCES `bearing_spacer` (`part_id`),
  CONSTRAINT `standard_bearing_spacer_ibfk_2` FOREIGN KEY (`oversized_part_id`) REFERENCES `bearing_spacer` (`part_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `standard_journal_bearing`
--

DROP TABLE IF EXISTS `standard_journal_bearing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `standard_journal_bearing` (
  `standard_part_id` bigint(20) NOT NULL,
  `oversized_part_id` bigint(20) NOT NULL,
  PRIMARY KEY (`standard_part_id`,`oversized_part_id`),
  KEY `oversized_part_id` (`oversized_part_id`),
  CONSTRAINT `standard_journal_bearing_ibfk_1` FOREIGN KEY (`standard_part_id`) REFERENCES `journal_bearing` (`part_id`),
  CONSTRAINT `standard_journal_bearing_ibfk_2` FOREIGN KEY (`oversized_part_id`) REFERENCES `journal_bearing` (`part_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `standard_oversize_part`
--

DROP TABLE IF EXISTS `standard_oversize_part`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `standard_oversize_part` (
  `standard_part_id` bigint(20) NOT NULL,
  `oversize_part_id` bigint(20) NOT NULL,
  PRIMARY KEY (`standard_part_id`,`oversize_part_id`),
  KEY `standard_oversize_part_oversized_part_id_idx` (`oversize_part_id`),
  CONSTRAINT `standard_oversize_part_oversized_part_id` FOREIGN KEY (`oversize_part_id`) REFERENCES `part` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `standard_oversize_part_standard_part_id` FOREIGN KEY (`standard_part_id`) REFERENCES `part` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`metaserver`@`%`*/ /*!50003 TRIGGER `standard_oversize_part_BEFORE_INSERT` BEFORE INSERT ON `standard_oversize_part` 
FOR EACH ROW
begin  
    if  new.standard_part_id = new.oversize_part_id then
        SIGNAL SQLSTATE '45000'   
        SET MESSAGE_TEXT = 'standard_part_id cannot be equal to the oversize_part_id';
    elseif (select part_type_id from part where id = new.standard_part_id) <> (select part_type_id from part where id = new.oversize_part_id ) then
            SIGNAL SQLSTATE '45000'   
            SET MESSAGE_TEXT = 'part_type_id must be the same for both parts';
    end if; 
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`metaserver`@`%`*/ /*!50003 TRIGGER `standard_oversize_part_BEFORE_UPDATE` BEFORE UPDATE ON `standard_oversize_part` 
FOR EACH ROW
begin  
    if  new.standard_part_id = new.oversize_part_id then
        SIGNAL SQLSTATE '45000'   
        SET MESSAGE_TEXT = 'standard_part_id cannot be equal to the oversize_part_id';
    elseif (select part_type_id from part where id = new.standard_part_id) <> (select part_type_id from part where id = new.oversize_part_id ) then
            SIGNAL SQLSTATE '45000'   
            SET MESSAGE_TEXT = 'part_type_id must be the same for both parts';
    end if; 
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary view structure for view `test_ii`
--

DROP TABLE IF EXISTS `test_ii`;
/*!50001 DROP VIEW IF EXISTS `test_ii`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `test_ii` AS SELECT 
 1 AS `p_id`,
 1 AS `interchange_header_id`,
 1 AS `pi_id`,
 1 AS `part_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `test_pi`
--

DROP TABLE IF EXISTS `test_pi`;
/*!50001 DROP VIEW IF EXISTS `test_pi`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `test_pi` AS SELECT 
 1 AS `id`,
 1 AS `interchange_header_id`,
 1 AS `part_id`,
 1 AS `part_id_comb`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `test_vmagmi_service_kits`
--

DROP TABLE IF EXISTS `test_vmagmi_service_kits`;
/*!50001 DROP VIEW IF EXISTS `test_vmagmi_service_kits`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `test_vmagmi_service_kits` AS SELECT 
 1 AS `sku`,
 1 AS `kitSku`,
 1 AS `kitPartNumber`,
 1 AS `description`,
 1 AS `tiKitSku`,
 1 AS `tiKitPartNumber`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `turbine_wheel`
--

DROP TABLE IF EXISTS `turbine_wheel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `turbine_wheel` (
  `part_id` bigint(20) NOT NULL,
  `exduce_oa` decimal(10,6) DEFAULT NULL,
  `tip_height_b` decimal(10,6) DEFAULT NULL,
  `inducer_oc` decimal(10,6) DEFAULT NULL,
  `journal_od` decimal(10,6) DEFAULT NULL,
  `stem_oe` decimal(10,6) DEFAULT NULL,
  `trim_no_blades` varchar(100) DEFAULT NULL,
  `shaft_thread_f` varchar(100) DEFAULT NULL,
  KEY `part_id` (`part_id`),
  CONSTRAINT `turbine_wheel_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `turbo`
--

DROP TABLE IF EXISTS `turbo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `turbo` (
  `part_id` bigint(20) NOT NULL,
  `turbo_model_id` bigint(20) NOT NULL,
  `cool_type_id` bigint(20) DEFAULT NULL,
  KEY `part_id` (`part_id`),
  KEY `cool_type_id` (`cool_type_id`),
  KEY `turbo_model_id` (`turbo_model_id`),
  CONSTRAINT `turbo_ibfk_1` FOREIGN KEY (`part_id`) REFERENCES `part` (`id`),
  CONSTRAINT `turbo_ibfk_2` FOREIGN KEY (`cool_type_id`) REFERENCES `cool_type` (`id`),
  CONSTRAINT `turbo_ibfk_3` FOREIGN KEY (`turbo_model_id`) REFERENCES `turbo_model` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `turbo_car_model_engine_year`
--

DROP TABLE IF EXISTS `turbo_car_model_engine_year`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `turbo_car_model_engine_year` (
  `part_id` bigint(20) NOT NULL,
  `car_model_engine_year_id` bigint(20) NOT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`part_id`,`car_model_engine_year_id`),
  KEY `car_model_engine_year_id` (`car_model_engine_year_id`),
  CONSTRAINT `turbo_car_model_engine_year_ibfk_1` FOREIGN KEY (`car_model_engine_year_id`) REFERENCES `car_model_engine_year` (`id`),
  CONSTRAINT `turbo_car_model_engine_year_ibfk_2` FOREIGN KEY (`part_id`) REFERENCES `turbo` (`part_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `turbo_model`
--

DROP TABLE IF EXISTS `turbo_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `turbo_model` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `turbo_type_id` bigint(20) DEFAULT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `turbo_type_id` (`turbo_type_id`),
  CONSTRAINT `turbo_model_ibfk_1` FOREIGN KEY (`turbo_type_id`) REFERENCES `turbo_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7945 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `turbo_type`
--

DROP TABLE IF EXISTS `turbo_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `turbo_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `manfr_id` bigint(20) NOT NULL,
  `import_pk` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`,`manfr_id`),
  KEY `manfr_id` (`manfr_id`),
  CONSTRAINT `turbo_type_ibfk_1` FOREIGN KEY (`manfr_id`) REFERENCES `manfr` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=951 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(100) NOT NULL,
  `password_reset_token` char(36) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `password_reset_token` (`password_reset_token`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `user_group_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_group_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `vapp`
--

DROP TABLE IF EXISTS `vapp`;
/*!50001 DROP VIEW IF EXISTS `vapp`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vapp` AS SELECT 
 1 AS `part_id`,
 1 AS `car_make`,
 1 AS `car_year`,
 1 AS `car_model`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vbalt`
--

DROP TABLE IF EXISTS `vbalt`;
/*!50001 DROP VIEW IF EXISTS `vbalt`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vbalt` AS SELECT 
 1 AS `bom_id`,
 1 AS `child_part_id`,
 1 AS `bom_alt_header_id`,
 1 AS `alt_header_name`,
 1 AS `alt_header_desc`,
 1 AS `alt_part_id`,
 1 AS `alt_part_type`,
 1 AS `alt_part_number`,
 1 AS `alt_manufacturer`,
 1 AS `alt_manufacturer_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vbom`
--

DROP TABLE IF EXISTS `vbom`;
/*!50001 DROP VIEW IF EXISTS `vbom`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vbom` AS SELECT 
 1 AS `bom_id`,
 1 AS `p_part_id`,
 1 AS `p_part_type`,
 1 AS `p_part_number`,
 1 AS `p_manufacturer`,
 1 AS `c_part_id`,
 1 AS `c_part_type`,
 1 AS `c_part_number`,
 1 AS `c_manufacturer`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vbom_ancestor`
--

DROP TABLE IF EXISTS `vbom_ancestor`;
/*!50001 DROP VIEW IF EXISTS `vbom_ancestor`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vbom_ancestor` AS SELECT 
 1 AS `id`,
 1 AS `part_id`,
 1 AS `ancestor_part_id`,
 1 AS `distance`,
 1 AS `type`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vbom_descendant`
--

DROP TABLE IF EXISTS `vbom_descendant`;
/*!50001 DROP VIEW IF EXISTS `vbom_descendant`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vbom_descendant` AS SELECT 
 1 AS `id`,
 1 AS `part_bom_id`,
 1 AS `descendant_bom_id`,
 1 AS `distance`,
 1 AS `type`,
 1 AS `qty`,
 1 AS `part_id_ancestor`,
 1 AS `part_id_descendant`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vbom_descendant_direct`
--

DROP TABLE IF EXISTS `vbom_descendant_direct`;
/*!50001 DROP VIEW IF EXISTS `vbom_descendant_direct`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vbom_descendant_direct` AS SELECT 
 1 AS `id`,
 1 AS `part_bom_id`,
 1 AS `descendant_bom_id`,
 1 AS `distance`,
 1 AS `type`,
 1 AS `qty`,
 1 AS `part_id_ancestor`,
 1 AS `part_id_descendant`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vint`
--

DROP TABLE IF EXISTS `vint`;
/*!50001 DROP VIEW IF EXISTS `vint`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vint` AS SELECT 
 1 AS `interchange_header_id`,
 1 AS `part_id`,
 1 AS `part_type`,
 1 AS `part_number`,
 1 AS `manufacturer`,
 1 AS `i_part_id`,
 1 AS `i_part_type`,
 1 AS `i_part_number`,
 1 AS `i_manufacturer`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vint_ti`
--

DROP TABLE IF EXISTS `vint_ti`;
/*!50001 DROP VIEW IF EXISTS `vint_ti`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vint_ti` AS SELECT 
 1 AS `interchange_header_id`,
 1 AS `part_id`,
 1 AS `ti_part_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vkp`
--

DROP TABLE IF EXISTS `vkp`;
/*!50001 DROP VIEW IF EXISTS `vkp`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vkp` AS SELECT 
 1 AS `kit_id`,
 1 AS `part_id`,
 1 AS `exclude`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vmagmi_bom`
--

DROP TABLE IF EXISTS `vmagmi_bom`;
/*!50001 DROP VIEW IF EXISTS `vmagmi_bom`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vmagmi_bom` AS SELECT 
 1 AS `ancestor_sku`,
 1 AS `descendant_sku`,
 1 AS `quantity`,
 1 AS `distance`,
 1 AS `type`,
 1 AS `part_type_parent`,
 1 AS `has_bom`,
 1 AS `alt_sku`,
 1 AS `alt_mfr_id`,
 1 AS `int_sku`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vmagmi_service_kits`
--

DROP TABLE IF EXISTS `vmagmi_service_kits`;
/*!50001 DROP VIEW IF EXISTS `vmagmi_service_kits`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vmagmi_service_kits` AS SELECT 
 1 AS `sku`,
 1 AS `kitSku`,
 1 AS `kitPartNumber`,
 1 AS `description`,
 1 AS `tiKitSku`,
 1 AS `tiKitPartNumber`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vmagmi_sop`
--

DROP TABLE IF EXISTS `vmagmi_sop`;
/*!50001 DROP VIEW IF EXISTS `vmagmi_sop`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vmagmi_sop` AS SELECT 
 1 AS `part_id`,
 1 AS `standard_part_sku`,
 1 AS `oversize_part_skus`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vmagmi_ti_chra`
--

DROP TABLE IF EXISTS `vmagmi_ti_chra`;
/*!50001 DROP VIEW IF EXISTS `vmagmi_ti_chra`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vmagmi_ti_chra` AS SELECT 
 1 AS `id`,
 1 AS `has_ti_chra`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vpart_turbo`
--

DROP TABLE IF EXISTS `vpart_turbo`;
/*!50001 DROP VIEW IF EXISTS `vpart_turbo`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vpart_turbo` AS SELECT 
 1 AS `part_id`,
 1 AS `turbo_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vpart_turbotype_kits`
--

DROP TABLE IF EXISTS `vpart_turbotype_kits`;
/*!50001 DROP VIEW IF EXISTS `vpart_turbotype_kits`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vpart_turbotype_kits` AS SELECT 
 1 AS `part_id`,
 1 AS `kit_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vparts`
--

DROP TABLE IF EXISTS `vparts`;
/*!50001 DROP VIEW IF EXISTS `vparts`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vparts` AS SELECT 
 1 AS `part_id`,
 1 AS `part_type`,
 1 AS `part_number`,
 1 AS `manufacturer`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vtapp`
--

DROP TABLE IF EXISTS `vtapp`;
/*!50001 DROP VIEW IF EXISTS `vtapp`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vtapp` AS SELECT 
 1 AS `part_id`,
 1 AS `car_make`,
 1 AS `car_year`,
 1 AS `car_model`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vwhere_used`
--

DROP TABLE IF EXISTS `vwhere_used`;
/*!50001 DROP VIEW IF EXISTS `vwhere_used`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `vwhere_used` AS SELECT 
 1 AS `principal_id`,
 1 AS `principal_type`,
 1 AS `sku`,
 1 AS `manufacturer`,
 1 AS `part_number`,
 1 AS `ti_sku`,
 1 AS `ti_part_number`,
 1 AS `part_type`,
 1 AS `turbo_type`,
 1 AS `turbo_part_number`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `test_ii`
--

/*!50001 DROP VIEW IF EXISTS `test_ii`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `test_ii` AS select `p`.`id` AS `p_id`,`ii1`.`interchange_header_id` AS `interchange_header_id`,`pi`.`id` AS `pi_id`,coalesce(`pi`.`id`,`p`.`id`) AS `part_id` from (((`part` `p` left join `interchange_item` `ii1` on((`p`.`id` = `ii1`.`part_id`))) left join `interchange_item` `ii2` on((`ii2`.`interchange_header_id` = `ii1`.`interchange_header_id`))) left join `part` `pi` on((`ii2`.`part_id` = `pi`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `test_pi`
--

/*!50001 DROP VIEW IF EXISTS `test_pi`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `test_pi` AS select `p`.`id` AS `id`,`ii`.`interchange_header_id` AS `interchange_header_id`,`ii`.`part_id` AS `part_id`,coalesce(`ii`.`part_id`,`p`.`id`) AS `part_id_comb` from (`part` `p` left join `interchange_item` `ii` on((`p`.`id` = `ii`.`part_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `test_vmagmi_service_kits`
--

/*!50001 DROP VIEW IF EXISTS `test_vmagmi_service_kits`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `test_vmagmi_service_kits` AS select `p`.`id` AS `sku`,`pk`.`id` AS `kitSku`,`pk`.`manfr_part_num` AS `kitPartNumber`,`pk`.`description` AS `description`,`kti`.`id` AS `tiKitSku`,`kti`.`manfr_part_num` AS `tiKitPartNumber` from ((((((((((((((`part` `p` left join ((((((`bom_descendant` `bd` join `bom` `b` on((`bd`.`part_bom_id` = `b`.`id`))) join `interchange_item` `ii1` on((`b`.`parent_part_id` = `ii1`.`part_id`))) join `interchange_item` `ii2` on((`ii2`.`interchange_header_id` = `ii1`.`interchange_header_id`))) join `bom` `bc` on((`bd`.`descendant_bom_id` = `bc`.`id`))) join `interchange_item` `ii3` on((`bc`.`child_part_id` = `ii3`.`part_id`))) join `interchange_item` `ii4` on((`ii3`.`interchange_header_id` = `ii4`.`interchange_header_id`))) on((`p`.`id` = `ii4`.`part_id`))) left join `kit_part_common_component` `kc1` on(((`kc1`.`part_id` = `p`.`id`) and (`kc1`.`exclude` = 0)))) left join `kit_part_common_component` `kc1a` on(((`kc1a`.`part_id` = `ii2`.`part_id`) and (`kc1a`.`exclude` = 0)))) left join ((`part_turbo_type` `ptt` join `part_turbo_type` `ptt2` on((`ptt2`.`turbo_type_id` = `ptt`.`turbo_type_id`))) join `kit` `k2` on(((`k2`.`part_id` = `ptt2`.`part_id`) and (`ptt`.`part_id` <> `k2`.`part_id`)))) on((`ptt`.`part_id` = `p`.`id`))) left join `kit_part_common_component` `kc2` on(((`kc2`.`kit_id` = `k2`.`part_id`) and (`kc2`.`part_id` = `ptt`.`part_id`) and (`kc2`.`exclude` = 1)))) left join ((`part_turbo_type` `ptta` join `part_turbo_type` `ptt2a` on((`ptt2a`.`turbo_type_id` = `ptta`.`turbo_type_id`))) join `kit` `k2a` on(((`k2a`.`part_id` = `ptt2a`.`part_id`) and (`ptta`.`part_id` <> `k2a`.`part_id`)))) on((`ptta`.`part_id` = `ii2`.`part_id`))) left join `kit_part_common_component` `kc2a` on(((`kc2a`.`kit_id` = `k2a`.`part_id`) and (`kc2a`.`part_id` = `ptta`.`part_id`) and (`kc2a`.`exclude` = 1)))) left join (((`turbo` `t1` join `turbo_model` `tm1` on((`tm1`.`id` = `t1`.`turbo_model_id`))) join `part_turbo_type` `ptt3` on(((`ptt3`.`turbo_type_id` = `tm1`.`turbo_type_id`) and (`ptt3`.`part_id` <> `t1`.`part_id`)))) join `kit` `k3` on(((`k3`.`part_id` = `ptt3`.`part_id`) and (`t1`.`part_id` <> `k3`.`part_id`)))) on((`t1`.`part_id` = `p`.`id`))) left join `kit_part_common_component` `kc3` on(((`kc3`.`kit_id` = `k3`.`part_id`) and (`kc3`.`part_id` = `t1`.`part_id`) and (`kc3`.`exclude` = 1)))) left join (((`turbo` `t1a` join `turbo_model` `tm1a` on((`tm1a`.`id` = `t1a`.`turbo_model_id`))) join `part_turbo_type` `ptt3a` on(((`ptt3a`.`turbo_type_id` = `tm1a`.`turbo_type_id`) and (`ptt3a`.`part_id` <> `t1a`.`part_id`)))) join `kit` `k3a` on(((`k3a`.`part_id` = `ptt3a`.`part_id`) and (`t1a`.`part_id` <> `k3a`.`part_id`)))) on((`t1a`.`part_id` = `ii2`.`part_id`))) left join `kit_part_common_component` `kc3a` on(((`kc3a`.`kit_id` = `k3a`.`part_id`) and (`kc3a`.`part_id` = `t1a`.`part_id`) and (`kc3a`.`exclude` = 1)))) join `part` `pk` on((`pk`.`id` in (`kc1`.`kit_id`,`kc1a`.`kit_id`,`k2`.`part_id`,`k2a`.`part_id`,`k3`.`part_id`,`k3a`.`part_id`)))) left join `vint_ti` `iti` on((`iti`.`part_id` = `pk`.`id`))) left join `part` `kti` on((`kti`.`id` = `iti`.`ti_part_id`))) where (isnull(`kc2`.`exclude`) and isnull(`kc2a`.`exclude`) and isnull(`kc3`.`exclude`) and isnull(`kc3a`.`exclude`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vapp`
--

/*!50001 DROP VIEW IF EXISTS `vapp`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vapp` AS select distinct `p`.`id` AS `part_id`,`cmake`.`name` AS `car_make`,`cyear`.`name` AS `car_year`,`cmodel`.`name` AS `car_model` from ((((((`part` `p` join `vpart_turbo` `pt` on((`pt`.`part_id` = `p`.`id`))) join `turbo_car_model_engine_year` `c` on((`c`.`part_id` = `pt`.`turbo_id`))) left join `car_model_engine_year` `cmey` on((`cmey`.`id` = `c`.`car_model_engine_year_id`))) left join `car_model` `cmodel` on((`cmodel`.`id` = `cmey`.`car_model_id`))) left join `car_make` `cmake` on((`cmake`.`id` = `cmodel`.`car_make_id`))) left join `car_year` `cyear` on((`cyear`.`id` = `cmey`.`car_year_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vbalt`
--

/*!50001 DROP VIEW IF EXISTS `vbalt`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vbalt` AS select `bai`.`bom_id` AS `bom_id`,`b`.`child_part_id` AS `child_part_id`,`bai`.`bom_alt_header_id` AS `bom_alt_header_id`,`bah`.`name` AS `alt_header_name`,`bah`.`description` AS `alt_header_desc`,`bai`.`part_id` AS `alt_part_id`,`pt`.`name` AS `alt_part_type`,`p`.`manfr_part_num` AS `alt_part_number`,`m`.`name` AS `alt_manufacturer`,`m`.`id` AS `alt_manufacturer_id` from (((((`bom_alt_item` `bai` join `bom` `b` on((`b`.`id` = `bai`.`bom_id`))) join `bom_alt_header` `bah` on((`bah`.`id` = `bai`.`bom_alt_header_id`))) join `part` `p` on((`p`.`id` = `bai`.`part_id`))) join `part_type` `pt` on((`pt`.`id` = `p`.`part_type_id`))) join `manfr` `m` on((`m`.`id` = `p`.`manfr_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vbom`
--

/*!50001 DROP VIEW IF EXISTS `vbom`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vbom` AS select `b`.`id` AS `bom_id`,`pp`.`id` AS `p_part_id`,`ppt`.`name` AS `p_part_type`,`pp`.`manfr_part_num` AS `p_part_number`,`ppm`.`name` AS `p_manufacturer`,`cp`.`id` AS `c_part_id`,`cpt`.`name` AS `c_part_type`,`cp`.`manfr_part_num` AS `c_part_number`,`cpm`.`name` AS `c_manufacturer` from ((((((`bom` `b` join `part` `pp` on((`pp`.`id` = `b`.`parent_part_id`))) join `part_type` `ppt` on((`ppt`.`id` = `pp`.`part_type_id`))) join `manfr` `ppm` on((`ppm`.`id` = `pp`.`manfr_id`))) join `part` `cp` on((`cp`.`id` = `b`.`child_part_id`))) join `part_type` `cpt` on((`cpt`.`id` = `cp`.`part_type_id`))) join `manfr` `cpm` on((`cpm`.`id` = `cp`.`manfr_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vbom_ancestor`
--

/*!50001 DROP VIEW IF EXISTS `vbom_ancestor`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vbom_ancestor` AS select `bd`.`id` AS `id`,`ii`.`part_id` AS `part_id`,`ii2`.`part_id` AS `ancestor_part_id`,`bd`.`distance` AS `distance`,if(((`ii2`.`part_id` <> `b`.`parent_part_id`) or (`ii`.`part_id` <> `bc`.`child_part_id`)),'Interchange',`bd`.`type`) AS `type` from ((((((`bom_descendant` `bd` join `bom` `b` on((`bd`.`part_bom_id` = `b`.`id`))) join `interchange_item` `ii1` on((`b`.`parent_part_id` = `ii1`.`part_id`))) join `interchange_item` `ii2` on((`ii2`.`interchange_header_id` = `ii1`.`interchange_header_id`))) join `bom` `bc` on((`bd`.`descendant_bom_id` = `bc`.`id`))) join `interchange_item` `ii3` on((`bc`.`child_part_id` = `ii3`.`part_id`))) join `interchange_item` `ii` on((`ii3`.`interchange_header_id` = `ii`.`interchange_header_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vbom_descendant`
--

/*!50001 DROP VIEW IF EXISTS `vbom_descendant`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vbom_descendant` AS select `bd`.`id` AS `id`,`bd`.`part_bom_id` AS `part_bom_id`,`bd`.`descendant_bom_id` AS `descendant_bom_id`,`bd`.`distance` AS `distance`,if(((`ii2`.`part_id` <> `b`.`parent_part_id`) or (`ii`.`part_id` <> `bc`.`child_part_id`)),'Interchange',`bd`.`type`) AS `type`,`bd`.`qty` AS `qty`,`ii2`.`part_id` AS `part_id_ancestor`,`ii`.`part_id` AS `part_id_descendant` from ((((((`bom_descendant` `bd` join `bom` `b` on((`bd`.`part_bom_id` = `b`.`id`))) join `interchange_item` `ii1` on((`b`.`parent_part_id` = `ii1`.`part_id`))) join `interchange_item` `ii2` on((`ii2`.`interchange_header_id` = `ii1`.`interchange_header_id`))) join `bom` `bc` on((`bd`.`descendant_bom_id` = `bc`.`id`))) join `interchange_item` `ii3` on((`bc`.`child_part_id` = `ii3`.`part_id`))) join `interchange_item` `ii` on((`ii3`.`interchange_header_id` = `ii`.`interchange_header_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vbom_descendant_direct`
--

/*!50001 DROP VIEW IF EXISTS `vbom_descendant_direct`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vbom_descendant_direct` AS select `bd`.`id` AS `id`,`bd`.`part_bom_id` AS `part_bom_id`,`bd`.`descendant_bom_id` AS `descendant_bom_id`,`bd`.`distance` AS `distance`,`bd`.`type` AS `type`,`bd`.`qty` AS `qty`,`b`.`parent_part_id` AS `part_id_ancestor`,`bc`.`child_part_id` AS `part_id_descendant` from ((`bom_descendant` `bd` join `bom` `b` on((`bd`.`part_bom_id` = `b`.`id`))) join `bom` `bc` on((`bd`.`descendant_bom_id` = `bc`.`id`))) where (`bd`.`type` = 'direct') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vint`
--

/*!50001 DROP VIEW IF EXISTS `vint`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vint` AS select distinct `ii1`.`interchange_header_id` AS `interchange_header_id`,`p`.`id` AS `part_id`,`pt`.`name` AS `part_type`,`p`.`manfr_part_num` AS `part_number`,`pm`.`name` AS `manufacturer`,`ip`.`id` AS `i_part_id`,`ipt`.`name` AS `i_part_type`,`ip`.`manfr_part_num` AS `i_part_number`,`ipm`.`name` AS `i_manufacturer` from (((((((`part` `p` join `part_type` `pt` on((`pt`.`id` = `p`.`part_type_id`))) join `manfr` `pm` on((`pm`.`id` = `p`.`manfr_id`))) join `interchange_item` `ii1` on((`ii1`.`part_id` = `p`.`id`))) left join `interchange_item` `ii2` on((`ii2`.`interchange_header_id` = `ii1`.`interchange_header_id`))) left join `part` `ip` on((`ip`.`id` = `ii2`.`part_id`))) join `part_type` `ipt` on((`ipt`.`id` = `ip`.`part_type_id`))) left join `manfr` `ipm` on((`ipm`.`id` = `ip`.`manfr_id`))) where (`p`.`id` <> `ii2`.`part_id`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vint_ti`
--

/*!50001 DROP VIEW IF EXISTS `vint_ti`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vint_ti` AS select distinct `ii1`.`interchange_header_id` AS `interchange_header_id`,`p`.`id` AS `part_id`,`ip`.`id` AS `ti_part_id` from ((((`part` `p` join `interchange_item` `ii1` on((`ii1`.`part_id` = `p`.`id`))) left join `interchange_item` `ii2` on(((`ii2`.`interchange_header_id` = `ii1`.`interchange_header_id`) and (`ii1`.`part_id` <> `ii2`.`part_id`)))) left join `part` `ip` on((`ip`.`id` = `ii2`.`part_id`))) left join `manfr` `ipm` on((`ipm`.`id` = `ip`.`manfr_id`))) where (`ip`.`manfr_id` = 11) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vkp`
--

/*!50001 DROP VIEW IF EXISTS `vkp`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vkp` AS select distinct `kii`.`part_id` AS `kit_id`,`pii`.`part_id` AS `part_id`,`kc`.`exclude` AS `exclude` from ((((`kit_part_common_component` `kc` join `interchange_item` `ki` on((`ki`.`part_id` = `kc`.`kit_id`))) join `interchange_item` `kii` on((`kii`.`interchange_header_id` = `ki`.`interchange_header_id`))) join `interchange_item` `pi` on((`pi`.`part_id` = `kc`.`part_id`))) join `interchange_item` `pii` on((`pii`.`interchange_header_id` = `pi`.`interchange_header_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vmagmi_bom`
--

/*!50001 DROP VIEW IF EXISTS `vmagmi_bom`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vmagmi_bom` AS select distinct `bd`.`part_id_ancestor` AS `ancestor_sku`,`bd`.`part_id_descendant` AS `descendant_sku`,`bd`.`qty` AS `quantity`,`bd`.`distance` AS `distance`,`bd`.`type` AS `type`,`dppt`.`value` AS `part_type_parent`,if((`db`.`id` is not null),1,0) AS `has_bom`,`alt`.`child_part_id` AS `alt_sku`,`alt`.`alt_manufacturer_id` AS `alt_mfr_id`,`vit`.`ti_part_id` AS `int_sku` from (((((((`vbom_descendant` `bd` join `part` `dp` on((`dp`.`id` = `bd`.`part_id_descendant`))) join `part_type` `dpt` on((`dpt`.`id` = `dp`.`part_type_id`))) left join `part_type` `dppt` on((`dppt`.`id` = `dpt`.`parent_part_type_id`))) left join `vbalt` `alt` on((`alt`.`bom_id` = `bd`.`part_bom_id`))) left join `vint_ti` `vit` on((`vit`.`part_id` = `bd`.`part_id_descendant`))) left join `bom` `db` on((`db`.`parent_part_id` = `bd`.`part_id_descendant`))) left join `vbom_descendant_direct` `bdd` on(((`bd`.`part_id_ancestor` = `bdd`.`part_id_ancestor`) and (`bd`.`part_id_descendant` = `bdd`.`part_id_descendant`) and (`bd`.`type` = 'Interchange')))) where isnull(`bdd`.`id`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vmagmi_service_kits`
--

/*!50001 DROP VIEW IF EXISTS `vmagmi_service_kits`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vmagmi_service_kits` AS select `p`.`id` AS `sku`,`pk`.`id` AS `kitSku`,`pk`.`manfr_part_num` AS `kitPartNumber`,`pk`.`description` AS `description`,`kti`.`id` AS `tiKitSku`,`kti`.`manfr_part_num` AS `tiKitPartNumber` from ((((((((((((((`part` `p` left join ((((((`bom_descendant` `bd` join `bom` `b` on((`bd`.`part_bom_id` = `b`.`id`))) join `interchange_item` `ii1` on((`b`.`parent_part_id` = `ii1`.`part_id`))) join `interchange_item` `ii2` on((`ii2`.`interchange_header_id` = `ii1`.`interchange_header_id`))) join `bom` `bc` on((`bd`.`descendant_bom_id` = `bc`.`id`))) join `interchange_item` `ii3` on((`bc`.`child_part_id` = `ii3`.`part_id`))) join `interchange_item` `ii4` on((`ii3`.`interchange_header_id` = `ii4`.`interchange_header_id`))) on((`p`.`id` = `ii4`.`part_id`))) left join `kit_part_common_component` `kc1` on(((`kc1`.`part_id` = `p`.`id`) and (`kc1`.`exclude` = 0)))) left join `kit_part_common_component` `kc1a` on(((`kc1a`.`part_id` = `ii2`.`part_id`) and (`kc1a`.`exclude` = 0)))) left join ((`part_turbo_type` `ptt` join `part_turbo_type` `ptt2` on((`ptt2`.`turbo_type_id` = `ptt`.`turbo_type_id`))) join `kit` `k2` on(((`k2`.`part_id` = `ptt2`.`part_id`) and (`ptt`.`part_id` <> `k2`.`part_id`)))) on((`ptt`.`part_id` = `p`.`id`))) left join `kit_part_common_component` `kc2` on(((`kc2`.`kit_id` = `k2`.`part_id`) and (`kc2`.`part_id` = `ptt`.`part_id`) and (`kc2`.`exclude` = 1)))) left join ((`part_turbo_type` `ptta` join `part_turbo_type` `ptt2a` on((`ptt2a`.`turbo_type_id` = `ptta`.`turbo_type_id`))) join `kit` `k2a` on(((`k2a`.`part_id` = `ptt2a`.`part_id`) and (`ptta`.`part_id` <> `k2a`.`part_id`)))) on((`ptta`.`part_id` = `ii2`.`part_id`))) left join `kit_part_common_component` `kc2a` on(((`kc2a`.`kit_id` = `k2a`.`part_id`) and (`kc2a`.`part_id` = `ptta`.`part_id`) and (`kc2a`.`exclude` = 1)))) left join (((`turbo` `t1` join `turbo_model` `tm1` on((`tm1`.`id` = `t1`.`turbo_model_id`))) join `part_turbo_type` `ptt3` on(((`ptt3`.`turbo_type_id` = `tm1`.`turbo_type_id`) and (`ptt3`.`part_id` <> `t1`.`part_id`)))) join `kit` `k3` on(((`k3`.`part_id` = `ptt3`.`part_id`) and (`t1`.`part_id` <> `k3`.`part_id`)))) on((`t1`.`part_id` = `p`.`id`))) left join `kit_part_common_component` `kc3` on(((`kc3`.`kit_id` = `k3`.`part_id`) and (`kc3`.`part_id` = `t1`.`part_id`) and (`kc3`.`exclude` = 1)))) left join (((`turbo` `t1a` join `turbo_model` `tm1a` on((`tm1a`.`id` = `t1a`.`turbo_model_id`))) join `part_turbo_type` `ptt3a` on(((`ptt3a`.`turbo_type_id` = `tm1a`.`turbo_type_id`) and (`ptt3a`.`part_id` <> `t1a`.`part_id`)))) join `kit` `k3a` on(((`k3a`.`part_id` = `ptt3a`.`part_id`) and (`t1a`.`part_id` <> `k3a`.`part_id`)))) on((`t1a`.`part_id` = `ii2`.`part_id`))) left join `kit_part_common_component` `kc3a` on(((`kc3a`.`kit_id` = `k3a`.`part_id`) and (`kc3a`.`part_id` = `t1a`.`part_id`) and (`kc3a`.`exclude` = 1)))) join `part` `pk` on((`pk`.`id` in (`kc1`.`kit_id`,`kc1a`.`kit_id`,`k2`.`part_id`,`k2a`.`part_id`,`k3`.`part_id`,`k3a`.`part_id`)))) left join `vint_ti` `iti` on((`iti`.`part_id` = `pk`.`id`))) left join `part` `kti` on((`kti`.`id` = `iti`.`ti_part_id`))) where (isnull(`kc2`.`exclude`) and isnull(`kc2a`.`exclude`) and isnull(`kc3`.`exclude`) and isnull(`kc3a`.`exclude`) and (`p`.`part_type_id` in (1,2,3))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vmagmi_sop`
--

/*!50001 DROP VIEW IF EXISTS `vmagmi_sop`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vmagmi_sop` AS select `ssop`.`oversize_part_id` AS `part_id`,`ssop`.`standard_part_id` AS `standard_part_sku`,group_concat(distinct `ii2`.`part_id` order by `ii2`.`part_id` ASC separator ',') AS `oversize_part_skus` from ((((`standard_oversize_part` `ssop` join `standard_oversize_part` `osop` on((`osop`.`standard_part_id` = `ssop`.`standard_part_id`))) left join `part` `op` on(((`op`.`id` = `osop`.`oversize_part_id`) and (`op`.`manfr_id` = 11)))) left join `interchange_item` `ii` on((`ii`.`part_id` = `op`.`id`))) left join `interchange_item` `ii2` on((`ii2`.`interchange_header_id` = `ii`.`interchange_header_id`))) group by `ssop`.`oversize_part_id`,`ssop`.`standard_part_id` union select `ssop`.`standard_part_id` AS `part_id`,`ssop`.`standard_part_id` AS `standard_part_sku`,group_concat(distinct `ii2`.`part_id` order by `ii2`.`part_id` ASC separator ',') AS `oversize_part_skus` from (((`standard_oversize_part` `ssop` left join `part` `op` on(((`op`.`id` = `ssop`.`oversize_part_id`) and (`op`.`manfr_id` = 11)))) left join `interchange_item` `ii` on((`ii`.`part_id` = `op`.`id`))) left join `interchange_item` `ii2` on((`ii2`.`interchange_header_id` = `ii`.`interchange_header_id`))) group by `ssop`.`standard_part_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vmagmi_ti_chra`
--

/*!50001 DROP VIEW IF EXISTS `vmagmi_ti_chra`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vmagmi_ti_chra` AS select `p`.`id` AS `id`,(case when (`pt`.`manfr_part_num` is not null) then 'yes' else 'no' end) AS `has_ti_chra` from (`part` `p` left join (`vbom_descendant` `bd` join `part` `pt` on(((`bd`.`part_id_descendant` = `pt`.`id`) and (`pt`.`manfr_id` = 11) and (`pt`.`part_type_id` = 2)))) on((`p`.`id` = `bd`.`part_id_ancestor`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vpart_turbo`
--

/*!50001 DROP VIEW IF EXISTS `vpart_turbo`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vpart_turbo` AS (select distinct `ba`.`part_id` AS `part_id`,`ba`.`ancestor_part_id` AS `turbo_id` from (`vbom_ancestor` `ba` join `turbo` `t` on((`t`.`part_id` = `ba`.`ancestor_part_id`)))) union (select `turbo`.`part_id` AS `part_id`,`turbo`.`part_id` AS `turbo_id` from `turbo`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vpart_turbotype_kits`
--

/*!50001 DROP VIEW IF EXISTS `vpart_turbotype_kits`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vpart_turbotype_kits` AS (select `kc`.`part_id` AS `part_id`,`kc`.`kit_id` AS `kit_id` from `kit_part_common_component` `kc` where (`kc`.`exclude` = 0)) union (select `p`.`id` AS `part_id`,`k`.`part_id` AS `kit_id` from ((((`part` `p` join `part_turbo_type` `ptt` on((`ptt`.`part_id` = `p`.`id`))) join `part_turbo_type` `ptt2` on((`ptt2`.`turbo_type_id` = `ptt`.`turbo_type_id`))) join `kit` `k` on(((`k`.`part_id` = `ptt2`.`part_id`) and (`p`.`id` <> `k`.`part_id`)))) left join `kit_part_common_component` `kc` on(((`kc`.`kit_id` = `k`.`part_id`) and (`kc`.`part_id` = `p`.`id`) and (`kc`.`exclude` = 1)))) where isnull(`kc`.`exclude`)) union (select `t`.`part_id` AS `part_id`,`k`.`part_id` AS `kit_id` from ((((`turbo` `t` join `turbo_model` `tm` on((`tm`.`id` = `t`.`turbo_model_id`))) join `part_turbo_type` `ptt` on(((`ptt`.`turbo_type_id` = `tm`.`turbo_type_id`) and (`ptt`.`part_id` <> `t`.`part_id`)))) join `kit` `k` on(((`k`.`part_id` = `ptt`.`part_id`) and (`t`.`part_id` <> `k`.`part_id`)))) left join `kit_part_common_component` `kc` on(((`kc`.`kit_id` = `k`.`part_id`) and (`kc`.`part_id` = `t`.`part_id`) and (`kc`.`exclude` = 1)))) where isnull(`kc`.`exclude`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vparts`
--

/*!50001 DROP VIEW IF EXISTS `vparts`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vparts` AS select `p`.`id` AS `part_id`,`pt`.`name` AS `part_type`,`p`.`manfr_part_num` AS `part_number`,`m`.`name` AS `manufacturer` from ((`part` `p` join `part_type` `pt` on((`pt`.`id` = `p`.`part_type_id`))) join `manfr` `m` on((`m`.`id` = `p`.`manfr_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vtapp`
--

/*!50001 DROP VIEW IF EXISTS `vtapp`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vtapp` AS select distinct `t`.`part_id` AS `part_id`,`cmake`.`name` AS `car_make`,`cyear`.`name` AS `car_year`,`cmodel`.`name` AS `car_model` from (((((`turbo` `t` join `turbo_car_model_engine_year` `c` on((`c`.`part_id` = `t`.`part_id`))) left join `car_model_engine_year` `cmey` on((`cmey`.`id` = `c`.`car_model_engine_year_id`))) left join `car_model` `cmodel` on((`cmodel`.`id` = `cmey`.`car_model_id`))) left join `car_make` `cmake` on((`cmake`.`id` = `cmodel`.`car_make_id`))) left join `car_year` `cyear` on((`cyear`.`id` = `cmey`.`car_year_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vwhere_used`
--

/*!50001 DROP VIEW IF EXISTS `vwhere_used`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`metaserver`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vwhere_used` AS select `p`.`id` AS `principal_id`,`pt`.`name` AS `principal_type`,`ap`.`id` AS `sku`,`apm`.`name` AS `manufacturer`,`ap`.`manfr_part_num` AS `part_number`,`aptii`.`ti_part_id` AS `ti_sku`,`aptiip`.`manfr_part_num` AS `ti_part_number`,`aptype`.`name` AS `part_type`,`apt2t`.`name` AS `turbo_type`,`apcatp`.`manfr_part_num` AS `turbo_part_number` from ((((((((((((((`vbom_ancestor` `ba` join `part` `p` on((`p`.`id` = `ba`.`part_id`))) join `part_type` `pt` on((`pt`.`id` = `p`.`part_type_id`))) join `part` `ap` on((`ap`.`id` = `ba`.`ancestor_part_id`))) join `part_type` `aptype` on((`aptype`.`id` = `ap`.`part_type_id`))) join `manfr` `apm` on((`apm`.`id` = `ap`.`manfr_id`))) left join `turbo` `apt2` on((`apt2`.`part_id` = `ap`.`id`))) left join `turbo_model` `apt2m` on((`apt2m`.`id` = `apt2`.`turbo_model_id`))) left join `turbo_type` `apt2t` on((`apt2t`.`id` = `apt2m`.`turbo_type_id`))) left join `cartridge` `apc` on((`apc`.`part_id` = `ba`.`ancestor_part_id`))) left join `vint_ti` `aptii` on((`aptii`.`part_id` = `ba`.`ancestor_part_id`))) left join `part` `aptiip` on((`aptiip`.`id` = `aptii`.`ti_part_id`))) left join `vbom_ancestor` `apcba` on(((`apcba`.`part_id` = `apc`.`part_id`) and (`apcba`.`distance` <> 0)))) left join `turbo` `apcat` on((`apcat`.`part_id` = `apcba`.`ancestor_part_id`))) left join `part` `apcatp` on((`apcatp`.`id` = `apcat`.`part_id`))) where ((`ba`.`distance` <> 0) and (case `pt`.`magento_attribute_set` when 'Turbo' then 1 when 'Cartridge' then (`aptype`.`name` = 'Turbo') else (`aptype`.`name` = 'Cartridge') end)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-19 10:53:18
