insert into turbo_type(id, name, manfr_id) values(260, 'H1E', 2);
insert into turbo_model(id, name, turbo_type_id) values(339, 'H1E', 260);
insert into part(id, manfr_part_num, manfr_id, part_type_id, inactive, version)
values(14510, 3534378, 2, 1, 0, 0);
insert into turbo(part_id, turbo_model_id) values(14510, 339);