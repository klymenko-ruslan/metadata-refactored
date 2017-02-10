insert into part(id, manfr_part_num, manfr_id, part_type_id, inactive, version) values
(25861, '17201-0L020', 4, 1, 0, 1),
(69079, 'pending285', 4, 49, 0, 1);

insert into turbo_type(id, name, manfr_id) values (138, 'CT', 4);
insert into turbo_model(id, name, turbo_type_id) values (4013, 'CT', 138);

insert into gasket_kit(part_id) values (69079);
insert into turbo(part_id, turbo_model_id, cool_type_id, gasket_kit_id) values (25861, 4013, null, null);

insert into changelog(id, change_date, user_id, description, service) values
(1, '2017-01-22 14:24:43', 1, 'test', 'BOM');

insert into changelog_part(id, changelog_id, part_id, role) values
(1, 1, 69079, 'BOM_PARENT'),
(2, 1, 25861, 'BOM_CHILD');
