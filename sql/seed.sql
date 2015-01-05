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
