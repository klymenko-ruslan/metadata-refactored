insert into role(id, name, display) values
(21, 'ROLE_CHLOGSRC_CREATE', 'Create a changelog source.'),
(22, 'ROLE_CHLOGSRC_READ', 'Read a changelog source.'),
(23, 'ROLE_CHLOGSRC_UPDATE', 'Update a changelog source.'),
(24, 'ROLE_CHLOGSRC_DELETE', 'Delete a changelog source.');

-- Add to the 'Admin' group.
insert into group_role(group_id, role_id) values
(3, 21),
(3, 22),
(3, 23),
(3, 24);

