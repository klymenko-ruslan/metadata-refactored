insert into part(id, manfr_part_num, manfr_id, part_type_id, name, description, inactive, import_pk, version)
values
(1000, '1-A-3246', 11, 3, NULL, 'CHRA, GT1749V', 0, 59691, 1),
(1010, '9-A-4340', 11, 3, NULL, '*ND* NZL RG ASY,GT17-704013-1', 0, 4328, 1);

insert into kit(part_id, kit_type_id)
values
(1000, 14),
(1010, 12);
