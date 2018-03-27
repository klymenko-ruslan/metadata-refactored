-- GROUP 1

insert into part(id, manfr_part_num, manfr_id, part_type_id, name, description, inactive, import_pk, version)
values
(42077, '7-G-4811', 11, 3, 'KIT, UNIVERSAL, CT12B`', NULL, 0, 59691, 1),
(41405, '7-A-0005', 11, 3, 'carbon seal', NULL, 0, 4328, 1);

insert into kit(part_id, kit_type_id)
values
(42077, 14),
(41405, 12);

insert into interchange_header(id, name, description) values(5, NULL, NULL);
insert into interchange_item(part_id, interchange_header_id)
values
(42077, 5),
(41405, 5);

-- GROUP 2

insert into part(id, manfr_part_num, manfr_id, part_type_id, name, description, inactive, import_pk, version)
values
(40392, '701813-0001', 1, 3, 'dynamic seal', NULL, 0, 16330, 1),
(40393, '701826-0001', 1, 3, 'dynamic seal', NULL, 0,  4337, 1),
(41587, '7-A-2476', 11, 3, 'dynamic seal', NULL, 0,  4337, 1);

insert into kit(part_id, kit_type_id)
values
(40392, 12),
(40393, 12),
(41587, 12);

insert into interchange_header(id, name, description) values(24, NULL, NULL);
insert into interchange_item(part_id, interchange_header_id)
values
(40392, 24),
(40393, 24),
(41587, 24);
