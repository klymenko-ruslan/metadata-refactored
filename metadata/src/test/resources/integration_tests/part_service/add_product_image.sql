insert into part(id, manfr_part_num, manfr_id, part_type_id, inactive, version) values
(25861, '17201-0L020', 4, 1, 0, 1);
insert into turbo_type(id, name, manfr_id) values (138, 'CT', 4);
insert into turbo_model(id, name, turbo_type_id) values (4013, 'CT', 138);
insert into turbo(part_id, turbo_model_id, cool_type_id, gasket_kit_id) values (25861, 4013, null, null);