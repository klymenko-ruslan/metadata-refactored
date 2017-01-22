insert into turbo_type(id, name, manfr_id) values (260, 'H1E', 2);
insert into turbo_model(id, name, turbo_type_id) values (339, 'H1E', 260);
insert into part(id, manfr_part_num, manfr_id, part_type_id, inactive, version) values (14510, '3534378', 2, 1, 0, 1);
insert into turbo(part_id, turbo_model_id) values (14510, 339);

insert into part(id, manfr_part_num, manfr_id, part_type_id, inactive, version) values (17754, '3768655', 2, 1, 0, 1);
insert into turbo_type(id, name, manfr_id) values (281, 'HE341VE', 2);
insert into turbo_model(id, name, turbo_type_id) values (3412, 'HE341VE', 281);
insert into turbo(part_id, turbo_model_id) values (17754, 3412);

insert into source(id, source_name_id, name, description, url, created, updated, create_user_id, update_user_id) values
(1, 2, 'name-0', 'name-0 email', null, '2017-01-22 13:42:24', '2017-01-22 13:42:24', 1, 1),
(2, 1, 'name-1', 'name-1 website', 'https://www.youtube.com/watch?v=SAN4CxbtS8Y', '2017-01-22 13:43:22', '2017-01-22 13:43:22', 1, 1);

