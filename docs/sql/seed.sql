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

