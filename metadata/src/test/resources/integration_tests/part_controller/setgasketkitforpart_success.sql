insert into part(id, manfr_part_num, manfr_id, part_type_id, inactive, version) values
(25861, '17201-0L020', 4, 1, 0, 1),
(69079, 'pending285', 4, 49, 0, 1);

insert into turbo_type(id, name, manfr_id) values (138, 'CT', 4);
insert into turbo_model(id, name, turbo_type_id) values (4013, 'CT', 138);

insert into gasket_kit(part_id) values (69079);
insert into turbo(part_id, turbo_model_id, cool_type_id, gasket_kit_id) values (25861, 4013, null, null);


-- Prepare BOMs for the parts 25861 and 69079.

insert into part(id, manfr_part_num, manfr_id, part_type_id, inactive, version) values
(69080, 210612, 4, 6, 0, 1),
(69081, 210623, 4, 6, 0, 1),
(69082, 210660, 4, 6, 0, 1),
(69083, 210659, 4, 6, 0, 1);

insert into gasket(part_id) values (69080), (69081), (69082), (69083);

insert into bom(id, parent_part_id, child_part_id, quantity) values
(62817, 25861, 69080, 1),
(62818, 25861, 69081, 1),
(62819, 25861, 69082, 1),
(62820, 25861, 69083, 1),
(62592, 69079, 69080, 1),
(62593, 69079, 69081, 1),
(62594, 69079, 69082, 1),
(62595, 69079, 69083, 1);
