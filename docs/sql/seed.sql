--
-- Admin User
--
INSERT INTO `user` (
  `name`, `email`, `password`
) VALUES (
  "Admin", "", ""
);
SET @ADMINUSERID = last_insert_id();

INSERT INTO `group` (
  `name`
) VALUES (
  "Administrators"
);
SET @ADMINGROUPID = last_insert_id();

INSERT INTO `user_group` (
  `user_id`, `group_id`
) VALUES (
  @ADMINUSERID, @ADMINGROUPID
);

INSERT INTO `role` (
  `name`, `display`
) VALUES (
  "ADMIN", "Administrator"
);
SET @ADMINROLEID = last_insert_id();


INSERT INTO `group_role` (
  `group_id`, `role_id`
) VALUES (
  @ADMINGROUPID, @ADMINROLEID
);


REPLACE INTO `part_type` (`id`,`name`,`parent_part_type_id`,`import_pk`) VALUES 
 (6,'gasket',NULL,NULL),
 (7,'bearing spacer',NULL,NULL),
 (8,'fast wearing component',NULL,NULL),
 (9,'major component',NULL,NULL),
 (10,'minor component',NULL,NULL),
 (11,'Compressor Wheel',9,11),
 (12,'Turbine Wheel',9,12),
 (13,'Bearing Housing',9,13),
 (14,'Backplate / Sealplate',9,14),
 (15,'Heatshield / Shroud',9,15),
 (16,'Nozzle Ring',9,16),
 (17,'O Ring',10,50),
 (18,'Oil Deflector',10,51),
 (19,'Clamp',10,52),
 (20,'Thrust Parts',10,53),
 (21,'Miscellaneous Minor Components',10,54);

