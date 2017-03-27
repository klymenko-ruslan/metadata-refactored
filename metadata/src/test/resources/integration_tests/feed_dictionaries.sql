drop procedure if exists rebuildbomdescendancy;
drop procedure if exists rebuildbomdescendancyforpart;
drop table if exists rebuildbomdescendancy_tbl;

create table rebuildbomdescendancy_tbl(
    id  integer
);

-- Mocked stored procedure.
create procedure rebuildbomdescendancy()
    modifies sql data
    insert into rebuildbomdescendancy_tbl(id) values(1);

create procedure rebuildbomdescendancyforpart(part_id bigint, clean tinyint)
    modifies sql data
    insert into rebuildbomdescendancy_tbl(id) values(1);

insert into part_type(id, name, parent_part_type_id, magento_attribute_set, value)
values
(1, 'Turbo', NULL, 'Turbo', 'turbo'),
(2, 'Cartridge', NULL, 'Cartridge', 'cartridge'),
(3, 'Kit', NULL, 'Kit', 'kit'),
(4, 'Piston Ring', NULL, 'Piston Ring', 'piston_ring'),
(5, 'Journal Bearing', NULL, 'Journal Bearing', 'journal_bearing'),
(6, 'Gasket', NULL, 'Gasket', 'gasket'),
(8, 'Fast Wearing Component', NULL, 'Fast Wearing Component', 'fast_wearing_component'),
(9, 'Major Component', NULL, 'Major Component', 'major_component'),
(10, 'Minor Component', NULL, 'Minor Component', 'minor_component'),
(11, 'Compressor Wheel', 9, 'Compressor Wheel', 'compressor_wheel'),
(12, 'Turbine Wheel', 9, 'Turbine Wheel', 'turbine_wheel'),
(13, 'Bearing Housing', 9, 'Bearing Housing', 'bearing_housing'),
(14, 'Backplate / Sealplate', 9, 'Backplate / Sealplate', 'backplate_sealplate'),
(15, 'Heatshield / Shroud', 9, 'Heatshield', 'heatshield'),
(16, 'Nozzle Ring', 9, 'Nozzle Ring', 'nozzle_ring'),
(17, 'O Ring', 10, 'O Ring', 'o_ring'),
(18, 'Oil Deflector', 10, 'Oil Deflector', 'oil_deflector'),
(19, 'Clamp', 10, 'Clamp', 'clamp'),
(20, 'Thrust Parts', 10, 'Thrust Parts', 'thrust_parts'),
(21, 'Miscellaneous Minor Components', 10, 'Miscellaneous Minor Components', 'misc_minor_components'),
(30, 'Actuator', NULL, 'Actuator', 'actuator'),
(31, 'Compressor Cover', NULL, 'Compressor Cover', 'compressor_cover'),
(32, 'Plug', NULL, 'Plug', 'plug'),
(33, 'Turbine Housing', NULL, 'Turbine Housing', 'turbine_housing'),
(34, 'Backplate', NULL, 'Backplate', 'backplate'),
(35, 'Bolt - Screw', NULL, 'Bolt Screw', 'bolt_screw'),
(36, 'Fitting', NULL, 'Fitting', 'fitting'),
(37, 'Journal Bearing Spacer', NULL, 'Journal Bearing Spacer', 'journal_bearing_spacer'),
(38, 'Nut', NULL, 'Nut', 'nut'),
(39, 'Pin', NULL, 'Pin', 'pin'),
(40, 'Retaining Ring', NULL, 'Retaining Ring', 'retaining_ring'),
(41, 'Seal Plate', NULL, 'Seal Plate', 'seal_plate'),
(42, 'Spring', NULL, 'Spring', 'spring'),
(43, 'Thrust Bearing', NULL, 'Thrust Bearing', 'thrust_bearing'),
(44, 'Thrust Collar', NULL, 'Thrust Collar', 'thrust_collar'),
(45, 'Thrust Spacer', NULL, 'Thrust Spacer', 'thrust_spacer'),
(46, 'Thrust Washer', NULL, 'Thrust Washer', 'thrust_washer'),
(47, 'Washer', NULL, 'Washer', 'washer'),
(48, 'Carbon Seal', NULL, 'Carbon Seal', 'carbon_seal'),
(49, 'Gasket Kit', NULL, 'Gasket Kit', 'gasket_kit'),
(50, 'Misc', NULL, NULL, 'misc'),
(51, 'Part', NULL, NULL, 'p'),
(52, 'Shroud', NULL, NULL, 'shroud');

insert into manfr_type(id, name) values(1, 'turbo');

insert into manfr(id, name, manfr_type_id, parent_manfr_id, not_external)
values
(1, 'Garrett', 1, NULL, 0),
(2, 'Holset', 1, NULL, 0),
(3, 'I.H.I.', 1, NULL, 0),
(4, 'Toyota', 1, NULL, 0),
(5, 'Hitachi', 1, NULL, 0),
(6, 'KKK', 1, NULL, 0),
(7, 'Komatsu', 1, NULL, 0),
(8, 'Mitsubishi', 1, NULL, 0),
(9, 'Schwitzer', 1, NULL, 0),
(10, 'Rotomaster', 1, NULL, 0),
(11, 'Turbo International', 1, NULL, 0);

insert into kit_type(id, name)
values
(1, 'Floating Carbon Seal'),
(2, 'Full'),
(3, 'Gasket'),
(4, 'Hardware'),
(5, 'Installation'),
(6, 'Journal Bearing'),
(7, 'Major'),
(8, 'Minor/Basic'),
(9, 'Misc.'),
(10, 'Nozzle Ring Assemblies'),
(11, 'Seal Plate Assembley'),
(12, 'Service'),
(13, 'Service (minor)'),
(14, 'Thrust'),
(15, 'Universal');

insert into user(id, name, username, email, password) values(1, 'Admin', 'Admin', 'admin@gmail.com', '');
-- insert into groups(id, name) values(1, 'Administrators');
-- insert into user_group(user_id, group_id) values(1, 1);
-- insert into role(id, name, display) values(1, 'ADMIN', 'Administrator');
-- insert into group_role(group_id, role_id) values(1, 1);

insert into user(id, name, username, email, password, password_reset_token, enabled)
values (10, 'mock', 'mock@gmail.com', 'mock@gmail.com', '123', null, 1);


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
(29, 'ROLE_CHLOGSRCNAME_DELETE', 'Delete a changelog source name.');

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
(3, 18),
(3, 19),
(3, 20),
(3, 21),
(3, 22),
(3, 23),
(3, 24),
(3, 25),
(3, 26),
(3, 27),
(3, 28),
(3, 29);

insert into user_group(user_id, group_id) values(1, 3);

insert into source_name(id, name) values
(1, 'website'),
(2, 'email'),
(3, 'spreadsheet'),
(4, 'PDF');

insert into service(id, name, description, required_source) values
(1, 'BOM', 'BOM service', 0),
(2, 'INTERCHANGE', 'Interchange service', 0),
(3, 'MAS90SYNC', 'Mas90Sync service', 0),
(4, 'SALESNOTES', 'Salesnotes service', 0),
(5, 'APPLICATIONS', 'Applications service', 0),
(6, 'KIT', 'KIT service', 0),
(7, 'PART', 'Part service', 0),
(8, 'TURBOMODEL', 'Turbo model service', 0),
(9, 'TURBOTYPE', 'Turbo type service', 0);
