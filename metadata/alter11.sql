insert into role(id, name, display) values
(26, 'ROLE_CHLOGSRCNAME_CREATE', 'Create a changelog source name.'),
(27, 'ROLE_CHLOGSRCNAME_READ', 'Read a changelog source name.'),
(28, 'ROLE_CHLOGSRCNAME_UPDATE', 'Update a changelog source name.'),
(29, 'ROLE_CHLOGSRCNAME_DELETE', 'Delete a changelog source name.');

-- Add to the 'Admin' group.
insert into group_role(group_id, role_id) values
(3, 26),
(3, 27),
(3, 28),
(3, 29);
