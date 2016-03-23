insert into auth_provider(id, typ) values(10, 'LDAP');

insert into auth_provider_ldap(id, name, host, port, protocol, domain)
values(10, 'TurboInternational AD', 'ldap.turbointernational.com', 389, 'LDAP', 'turbointernational.local');

insert into user(id, name, email, logon, password, password_reset_token, enabled, auth_provider_id)
values(16, 'John Rambo', 'john.rambo@zorallabs.com', 'ldaptest', '$2a$10$VTOODpmq0M1I5olJ.ih5IeZ7AKiNSOOSSMIcJIgmujZ5q5nXt0dSa', null, 1, 10);

insert into groups(id, name) values(3, 'Admin');

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
(18, 'ROLE_MAS90_SYNC', 'Start MAS90 synchronization process.');

insert into group_role(group_id, role_id) values
(3, 1),
(3, 2),
(3, 3),
(3, 4),
(3, 5),
(3, 6),
(3, 7),
(3, 8),
(3, 9),
(3, 10),
(3, 11),
(3, 12),
(3, 13),
(3, 14),
(3, 15),
(3, 16),
(3, 17),
(3, 18 );

insert into user_group(user_id, group_id) values(16, 3);