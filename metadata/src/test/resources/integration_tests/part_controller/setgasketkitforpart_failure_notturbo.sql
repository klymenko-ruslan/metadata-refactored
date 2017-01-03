insert into part(id, manfr_part_num, manfr_id, part_type_id, inactive, version) values
(25861, '17201-0L020', 4, 2 /* not Turbo (2 - Cartridge) */, 0, 1),
(69079, 'pending285', 4, 49, 0, 1);

insert into gasket_kit(part_id) values (69079);
insert into cartridge(part_id) values (25861);
