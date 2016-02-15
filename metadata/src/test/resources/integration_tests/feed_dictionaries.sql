insert into part_type(id, name, parent_part_type_id, import_pk, magento_attribute_set, value)
values
(1, 'Turbo', NULL, NULL, 'Turbo', 'turbo'),
(2, 'Cartridge', NULL, NULL, 'Cartridge', 'cartridge'),
(3, 'Kit', NULL, NULL, 'Kit', 'kit'),
(4, 'Piston Ring', NULL, NULL, 'Piston Ring', 'piston_ring'),
(5, 'Journal Bearing', NULL, NULL, 'Journal Bearing', 'journal_bearing'),
(6, 'Gasket', NULL, NULL, 'Gasket', 'gasket'),
(7, 'Bearing Spacer', NULL, NULL, 'Bearing Spacer', 'bearing_spacer'),
(8, 'Fast Wearing Component', NULL, NULL, 'Part', 'fast_wearing_component'),
(9, 'Major Component', NULL, NULL, 'Part', 'major_component'),
(10, 'Minor Component', NULL, NULL, 'Part', 'minor_component'),
(11, 'Compressor Wheel', 9, 11, 'Compressor Wheel', 'compressor_wheel'),
(12, 'Turbine Wheel', 9, 12, 'Turbine Wheel', 'turbine_wheel'),
(13, 'Bearing Housing', 9, 13, 'Bearing Housing', 'bearing_housing'),
(14, 'Backplate / Sealplate', 9, 14, 'Backplate', 'backplate_sealplate'),
(15, 'Heatshield / Shroud', 9, 15, 'Heatshield', 'heatshield_shroud'),
(16, 'Nozzle Ring', 9, 16, 'Nozzle Ring', 'nozzle_ring'),
(17, 'O Ring', 10, 50, 'Part', 'o_ring'),
(18, 'Oil Deflector', 10, 51, 'Part', 'oil_deflector'),
(19, 'Clamp', 10, 52, 'Part', 'clamp'),
(20, 'Thrust Parts', 10, 53, 'Part', 'thrust_parts'),
(21, 'Miscellaneous Minor Components', 10, 54, 'Part', 'misc_minor_components');

insert into manfr_type(id, name) values(1, 'turbo');

insert into manfr(id, name, manfr_type_id, parent_manfr_id, import_pk)
values
(1, 'Garrett', 1, NULL, 1),
(2, 'Holset', 1, NULL, 3),
(3, 'I.H.I.', 1, NULL, 5),
(4, 'Toyota', 1, NULL, 6),
(5, 'Hitachi', 1, NULL, 7),
(6, 'KKK', 1, NULL, 8),
(7, 'Komatsu', 1, NULL, 9),
(8, 'Mitsubishi', 1, NULL, 10),
(9, 'Schwitzer', 1, NULL, 12),
(10, 'Rotomaster', 1, NULL, 13),
(11, 'Turbo International', 1, NULL, 999);

insert into kit_type(id, name, import_pk)
values
(1, 'Floating Carbon Seal', 9),
(2, 'Full', 10),
(3, 'Gasket', 3),
(4, 'Hardware', 11),
(5, 'Installation', 4),
(6, 'Journal Bearing', 5),
(7, 'Major', 12),
(8, 'Minor/Basic', 13),
(9, 'Misc.', 6),
(10, 'Nozzle Ring Assemblies',  7),
(11, 'Seal Plate Assembley', 14),
(12, 'Service', 1),
(13, 'Service (minor)', 8),
(14, 'Thrust', 15),
(15, 'Universal', 16);

insert into user(id, name, email, password, password_reset_token, enabled)
values (10, 'mock', 'mock@gmail.com', '123', null, 1);

-- Mocked stored procedure.
--create procedure RebuildBomDescendancy()
--    modifies sql data
--begin atomic
--    declare temp_id integer;
--end
