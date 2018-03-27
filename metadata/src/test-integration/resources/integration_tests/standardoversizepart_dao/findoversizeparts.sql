insert into part(id, manfr_part_num, manfr_id, part_type_id, inactive, version) values
(46718, '8-A-0832', 11, 4, 0, 1),
(46719, '8-A-1127', 11, 4, 0, 1),
(46726, '8-A-1555', 11, 4, 0, 1);

insert into piston_ring(part_id)
values (46718), (46719), (46726);

insert into standard_oversize_part(standard_part_id, oversize_part_id) values
(46718, 46719),
(46718, 46726);
