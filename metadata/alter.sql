-- Fix for found inconsistneces in the current critical dimensions descriptors.
update crit_dim set tolerance='UPPER' where idx_name='rtnngrngGroovewidthuppertol';
update crit_dim set tolerance='LOWER' where idx_name='rtnngrngGroovewidthlowertol';

-- Add a new critical dimension.

delete from crit_dim_enum_val where id in(21100, 21101, 21102);
delete from crit_dim_enum where id=800;

insert into crit_dim_enum(id, name) values(800, 'BH_ArmAngleSource');
insert into crit_dim_enum_val(id, crit_dim_enum_id, val) values
(21100, 800, 'XXX'), (21101, 800, 'YYY'), (21102, 800, 'ZZZ');

delete from crit_dim where id=510;

update crit_dim set seq_num=seq_num+1 where part_type_id=13 and seq_num > 31;

insert into crit_dim(id, part_type_id, seq_num, data_type, enum_id, unit, tolerance, name, json_name, idx_name, null_allowed, null_display, min_val, max_val, regex, parent_id, length, scale, length_web, scale_web, is_visible_in_list, is_critical_dimension)
values(510, 13, 32, 'ENUMERATION', 800, null, null, 'ARM ANGLE SOURCE', 'armAngleSource', 'brnghsngArmangleSource', 1, null, null, null, null, 29, null, null, null, null, 0, 1);

alter table bearing_housing add column armAngleSource int(11);
