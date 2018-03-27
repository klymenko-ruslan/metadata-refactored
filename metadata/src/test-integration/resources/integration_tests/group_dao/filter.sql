delete from user;
delete from user_group;
delete from groups;
delete from role;

insert into role(id, name, display) values
(1, 'ROLE_READ', 'Search and view part information.'),
(2, 'ROLE_PART_IMAGES', 'Add and remove part images.'),
(3, 'ROLE_CREATE_PART', 'Create parts.'),
(4, 'ROLE_ALTER_PART', 'Alter existing parts.'),
(5, 'ROLE_DELETE_PART', 'Delete existing parts.'),
(6, 'ROLE_INTERCHANGE', 'Alter interchangeability.'),
(7, 'ROLE_BOM', 'Alter BOM.'),
(8, 'ROLE_BOM_ALT', 'Alter BOM alternates.'),
(9, 'ROLE_ADMIN', 'Superpowers.'),
(10, 'ROLE_TURBO_MODEL_CRUD', 'CRUD operations turbo types and models.'),
(11, 'ROLE_SALES_NOTE_READ', 'Read sales notes.'),
(12, 'ROLE_SALES_NOTE_SUBMIT', 'Submit draft sales notes for approval.'),
(13, 'ROLE_SALES_NOTE_APPROVE', 'Approve submitted sales notes.'),
(14, 'ROLE_SALES_NOTE_REJECT', 'Reject submitted and approved sales notes.'),
(15, 'ROLE_SALES_NOTE_RETRACT', 'Retract published sales notes.'),
(16, 'ROLE_SALES_NOTE_PUBLISH', 'Publish approved sales notes.'),
(17, 'ROLE_APPLICATION_CRUD', 'CRUD operations on applications.'),
(18, 'ROLE_MAS90_SYNC', 'Start MAS90 synchronization process.'),
(19, 'ROLE_ALTER_PART_MANUFACTURER', 'Edit Part Manufacturer'),
(20, 'ROLE_ALTER_PART_NUMBER', 'Edit Part Number'),
(21, 'ROLE_CHLOGSRC_CREATE', 'Create a changelog source.'),
(22, 'ROLE_CHLOGSRC_READ', 'Read a changelog source.'),
(23, 'ROLE_CHLOGSRC_UPDATE', 'Update a changelog source.'),
(24, 'ROLE_CHLOGSRC_DELETE', 'Delete a changelog source.'),
(25, 'ROLE_CHLOGSRC_SKIP', 'User may skip to enter a source for all eligible changelogs objects.'),
(26, 'ROLE_CHLOGSRCNAME_CREATE', 'Create a changelog source name.'),
(27, 'ROLE_CHLOGSRCNAME_READ', 'Read a changelog source name.'),
(28, 'ROLE_CHLOGSRCNAME_UPDATE', 'Update a changelog source name.'),
(29, 'ROLE_CHLOGSRCNAME_DELETE', 'Delete a changelog source name.'),
(30, 'ROLE_SERVICE', 'Manage services (e.g. map changelog source functionality on services).'),
(31, 'ROLE_PRICE_READ', 'Read prices for a part.'),
(32, 'ROLE_MANUFACTURER_CRUD', 'CRUD operations on manufacturers.');

insert into groups(id, name) values
(1, 'Reader'),
(2, 'Writer'),
(3, 'Admin'),
(4, 'Sales Notes Editor'),
(5, 'Image Manager'),
(6, 'MAS90 Sync'),
(7, 'Sales Notes User'),
(8, 'Sales Writer'),
(9, 'QC'),
(10, 'Test Group'),
(11, 'Source Admin');

insert into group_role(group_id, role_id) values
(1, 1), (2, 1), (3, 1), (8, 1), (9, 1), (3, 2), (5, 2), (2, 3), (3, 3), (8, 3), (2, 4), (3, 4), (8, 4), (9, 4), (2, 5),
(3, 5), (2, 6), (3, 6), (8, 6), (2, 7), (3, 7), (8, 7), (2, 8), (3, 8), (8, 8), (3, 9), (2, 10), (3, 10), (8, 10),
(1, 11), (2, 11), (3, 11), (4, 11), (7, 11), (2, 12), (3, 12), (4, 12), (7, 12), (3, 13), (4, 13), (3, 14), (4, 14),
(3, 15), (4, 15), (3, 16), (4, 16), (2, 17), (3, 17), (8, 17), (3, 18), (6, 18), (3, 19), (3, 20), (2, 21), (3, 21),
(11, 21), (1, 22), (2, 22), (3, 22), (8, 22), (11, 22), (2, 23), (3, 23), (11, 23), (3, 24), (11, 24), (11, 25),
(2, 26), (3, 26), (11, 26), (1, 27), (2, 27), (3, 27),  (8, 27), (11, 27), (2, 28), (3, 28), (11, 28), (3, 29),
(11, 29), (3, 30), (11, 30), (3, 31), (3, 32);

insert into user(id, name, email, password, enabled, username) values
(1, 'Foo', 'foo@test.org', '???', 1, 'foo'),
(2, 'Foo1', 'foo1@test.org', '???', 1, 'foo1');

insert into user_group(user_id, group_id) values
(1, 1), (1, 3),
(2, 1), (2, 7), (2, 8);

