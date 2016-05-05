delete from part_type where id > 21;

insert into part_type(id, name, magento_attribute_set, value) values
(22, 'Actuator', 'Actuator', 'actuator'),
(23, 'Compressor Cover', 'Compressor Cover', 'compressor_cover'),
(24, 'Plug', 'Plug', 'plug'),
(25, 'Turbine Housing', 'Turbine Housing', 'turbine_housing'),
-- (26, 'Backplate', 'Backplate', 'backplate'),
(27, 'Bolt Screw', 'Bolt Screw', 'bolt_screw'),
(28, 'Fitting', 'Fitting', 'fitting'),
(29, 'Journal Bearing Spacer', 'Journal Bearing Spacer', 'journal_bearing_housing'),
(30, 'Nut', 'Nut', 'nut'),
(31, 'Pin', 'Pin', 'pin'),
(32, 'Retaining Ring', 'Retaining Ring', 'retaining_ring'),
(33, 'Seal Plate', 'Seal Plate', 'seal_plate'),
(34, 'Spring', 'Spring', 'spring'),
(35, 'Thrust Bearing', 'Thrust Bearing', 'thrust_bearing'),
(36, 'Thrust Collar', 'Thrust Collar', 'thrust_collar'),
(37, 'Thrust Spacer', 'Thrust Spacer', 'thrust_spacer'),
(38, 'Thrust Washer', 'Thrust Washer', 'thrust_washer'),
(39, 'Washer', 'Washer', 'washer'),
(40, 'Carbon Seal', 'Carbon Seal', 'carbon_seal');

drop table if exists actuator;
create table actuator (
    part_id bigint(20) not null,
    key part_id (part_id),
    constraint actuator_ibfk_1 foreign key (part_id) references part (id)
) engine=innodb default charset=utf8;

drop table if exists compressor_cover;
create table compressor_cover (
    part_id bigint(20) not null,
    key part_id (part_id),
    constraint ccover_ibfk_1 foreign key (part_id) references part (id)
) engine=innodb default charset=utf8;

drop table if exists plug;
create table plug (
    part_id bigint(20) not null,
    key part_id (part_id),
    constraint plug_ibfk_1 foreign key (part_id) references part (id)
) engine=innodb default charset=utf8;

drop table if exists turbine_housing;
create table turbine_housing (
    part_id bigint(20) not null,
    key part_id (part_id),
    constraint thousing_ibfk_1 foreign key (part_id) references part (id)
) engine=innodb default charset=utf8;



drop table if exists crit_dim;
drop table if exists crit_dim_enum_val;
drop table if exists crit_dim_enum;

create table crit_dim_enum (
    id int not null auto_increment,
    name varchar(64) not null,
    primary key (id)
) comment='Enumerations for critical dimensions.' engine=innodb;

insert into crit_dim_enum(id, name) values
( 1, 'yesNoEnum'),
( 2, 'waterCooledEnum'),
( 3, 'dynCsEnum'),
( 4, 'superbackFlatbackEnum'),
( 5, 'mountingHoleThreadCallout'),
( 6, 'matlEnum'),
( 7, 'oilInletThreadEnum'),
( 8, 'oilInletGlangeThreadEnum'),
( 9, 'oilDrainThreadEnum'),
(10, 'oilDrainFlangeThreadEnum'),
(11, 'coolantPortThread1Enum'),
(12, 'coolantPortThread2Enum'),
(13, 'quadrantEnum'),
(15, 'oilFeedEnum');


create table crit_dim_enum_val (
    id int not null auto_increment,
    crit_dim_enum_id int not null references crit_dim_enum(id) on delete cascade on update cascade,
    val varchar(64) not null,
    primary key (id),
    unique key (id, crit_dim_enum_id)
) comment='Enumeration values for critical dimensions enumerations.' engine=innodb;

insert into crit_dim_enum_val(id, crit_dim_enum_id, val) values
(  1, 1, 'YES'),
(  2, 1, 'NO'),
(  3, 2, 'OIL'),
(  4, 2, 'WATER'),
-- dynCsEnum
(  5, 3, 'DYNAMIC'),
(  6, 3, 'CARBON SEAL'),
-- superbackFlatbackEnum
(  7, 4, 'SUPERBACK'),
(  8, 4, 'FLATBACK'),
-- matlEnum
(  9, 6, 'ALUMINUM'),
( 10, 6, 'CAST IRON'),
-- quadrant
( 11, 13, 'I'),
( 12, 13, 'II'),
( 13, 13, 'III'),
( 14, 13, 'IV');

create table crit_dim (
    id              bigint not null,
    part_type_id    bigint not null,
    seq_num         int not null,
    data_type       enum ('DECIMAL', 'ENUMERATION', 'INTEGER', 'TEXT') not null,
    enum_id         int,
    unit            enum ('DEGREES', 'GRAMS', 'INCHES'),
    tolerance       tinyint(1) comment '0 - nominal, 1 - tolerance/limit, null - not a tolerance',
    name            varchar(255) not null,
    json_name       varchar(32) not null comment 'Name of a property in serialized to JSON part''s object.',
    null_allowed    tinyint(1) not null comment 'Validation: Is NULL allowed?',
    null_display    varchar(32) comment 'How to display NULL values.',
    min_val         decimal(15, 6) comment 'Validation: minal (inclusive) allowed value for numeric types (DECIMAL, INTEGER).',
    max_val         decimal(15, 6) comment 'Validation: maximal (inclusive) allowed value for numeric types (DECIMAL, INTEGER).',
    regex           varchar(255) comment 'Validation: JS regular expression',
    parent_id       bigint,
    length          tinyint comment 'Lenth on a web page',
    scale           tinyint comment 'Scale on a web pate.',
    primary key(id),
    unique key(part_type_id, seq_num),
    foreign key (part_type_id) references part_type(id),
    foreign key (enum_id) references crit_dim_enum(id) on delete set null on update cascade,
    foreign key (parent_id) references crit_dim(id) on delete set null on update cascade
) engine=innodb;

insert into crit_dim
( id, part_type_id, seq_num,     data_type,      unit, tolerance, name,                           json_name,                        enum_id,      null_allowed, null_display, min_val, max_val, regex, parent_id, length, scale)
values
-- Backplate
(  1,           14,       1, 'ENUMERATION',      null,      null, 'DYN/CS',                       'dynCs',                                3,                 1,         null,    null,    null,  null,      null,   null,  null),
(  2,           14,       2, 'ENUMERATION',      null,      null, 'SUPERBACK/FLATBACK',           'superbackFlatback',                    4,                 1,         null,    null,    null,  null,      null,   null,  null),
(  3,           14,       3, 'INTEGER',          null,      null, '# MOUNTING HOLES',             'numMountingHoles',                  null,                 1,         null,    null,    null,  null,      null,      2,  null),
(  4,           14,       4, 'ENUMERATION',      null,      null, 'MOUNTING HOLE THREAD CALLOUT', 'mountingHoleThreadCallout',            5,                 1,         null,    null,    null,  null,      null,   null,  null),
(  5,           14,       5,     'DECIMAL',  'INCHES',         0, 'DIA "A"',                      'diaA',                              null,                 1,         null,       0,    null,  null,      null,      6,     3),
(  6,           14,       6,     'DECIMAL',  'INCHES',         1, 'DIA "A" TOL',                  'diaATol',                           null,                 1,         null,       0,    null,  null,         5,      6,     3),
(  7,           14,       7,     'DECIMAL',  'INCHES',         0, 'DIA "B"',                      'diaB',                              null,                 1,         null,       0,    null,  null,      null,      6,     3),
(  8,           14,       8,     'DECIMAL',  'INCHES',         1, 'DIA "B" TOL',                  'diaBTol',                           null,                 1,         null,       0,    null,  null,         7,      6,     3),
(  9,           14,       9,     'DECIMAL',  'INCHES',         0, 'DIA "C"',                      'diaC',                              null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 10,           14,      10,     'DECIMAL',  'INCHES',         1, 'DIA "C" TOL',                  'diaCTol',                           null,                 1,         null,       0,    null,  null,         9,      6,     3),
( 11,           14,      11,     'DECIMAL',  'INCHES',         0, 'DIA "D"',                      'diaD',                              null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 12,           14,      12,     'DECIMAL',  'INCHES',         1, 'DIA "D" TOL',                  'diaDTol',                           null,                 1,         null,       0,    null,  null,        11,      6,     3),
( 13,           14,      13,     'DECIMAL',  'INCHES',         0, 'CWC DIA "E"',                  'cwcDiaE',                           null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 14,           14,      14,     'DECIMAL',  'INCHES',         1, 'CWC DIA "E" TOL',              'cwcDiaETol',                        null,                 1,         null,       0,    null,  null,        13,      6,     3),
( 15,           14,      15,     'DECIMAL',  'INCHES',         0, 'BORE DIA',                     'boreDia',                           null,                 1,         null,       0,    null,  null,      null,      6,     4),
( 16,           14,      16,     'DECIMAL',  'INCHES',         1, 'BORE DIA TOL',                 'boreDiaTol',                        null,                 1,         null,       0,    null,  null,        15,      6,     4),
( 17,           14,      17,     'DECIMAL',  'INCHES',         0, 'MOUNTING HOLE DIA',            'mountingHoleDia',                   null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 18,           14,      18,     'DECIMAL',  'INCHES',         0, 'OAL',                          'oal',                               null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 19,           14,      19,     'DECIMAL',  'INCHES',         1, 'OAL TOL',                      'oalTol',                            null,                 1,         null,       0,    null,  null,        18,      6,     3),
( 20,           14,      20,     'DECIMAL',  'INCHES',         0, 'HUB POS "F"',                  'hubPosF',                           null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 21,           14,      21,     'DECIMAL',  'INCHES',         1, 'HUB POS "F" TOL',              'hubPosFTol',                        null,                 1,         null,       0,    null,  null,        20,      6,     3),
( 22,           14,      22,     'DECIMAL',  'INCHES',         0, 'CC LOC POS "G"',               'ccLocPosG',                         null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 23,           14,      23,     'DECIMAL',  'INCHES',         1, 'CC LOC POS "G" TOL',           'ccLocPosGTol',                      null,                 1,         null,       0,    null,  null,        22,      6,     3),
( 24,           14,      24,     'DECIMAL', 'DEGREES',         0, 'LEAD IN CHMFR ½-ANGLE',        'leadInChmfrAngle',                  null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 25,           14,      25,     'DECIMAL',  'INCHES',         0, 'LEAD IN CHMFR LEN',            'leadInChmfrLen',                    null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 26,           14,      26, 'ENUMERATION',      null,      null, 'MAT''L',                       'matl',                                 6,                 1,         null,    null,    null,  null,      null,   null,  null),
( 27,           14,      27,     'DECIMAL',   'GRAMS',         0, 'WEIGHT',                       'weight',                            null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 28,           14,      28,     'INTEGER',      null,      null, 'DIAGRAM #',                    'diagramNum',                        null,                 1,         null,    null,    null,  null,      null,      3,  null),
-- Bearing housing
( 29,           13,       1, 'ENUMERATION',     null,      null, 'WATER COOLED',                 'waterCooled',                           2,                 1,         null,    null,    null,  null,      null,   null,  null),
( 30,           13,       2,     'DECIMAL',  'INCHES',         0, 'CWC DIA',                     'cwcDia',                             null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 31,           13,       3,     'DECIMAL',  'INCHES',         1, 'CWC DIA TOL',                 'cwcDiaTol',                          null,                 1,         null,       0,    null,  null,        30,      6,     3),
( 32,           13,       4,     'DECIMAL', 'INCHES',      null, 'BORE DIA MAX',                 'boreDiaMax',                         null,                 1,         null,       0,    null,  null,      null,      6,     4),
( 33,           13,       5,     'DECIMAL', 'INCHES',      null, 'BORE DIA MIN',                 'boreDiaMin',                         null,                 1,         null,       0,    null,  null,      null,      6,     4),
( 34,           13,       6,     'DECIMAL', 'INCHES',         0, 'C/E DIA "A"',                  'ceDiaA',                             null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 35,           13,       7,     'DECIMAL', 'INCHES',         1, 'C/E DIA "A" TOL',              'ceDiaATol',                          null,                 1,         null,       0,    null,  null,        34,      6,     3),
( 36,           13,       8,     'DECIMAL', 'INCHES',         0, 'T/E DIA "D"',                  'teDiaD',                             null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 37,           13,       9,     'DECIMAL', 'INCHES',         1, 'T/E DIA "D" TOL',              'teDiaDTol',                          null,                 1,         null,       0,    null,  null,        36,      6,     3),
( 38,           13,      10,     'DECIMAL', 'INCHES',         0, 'C/E DIA "B"',                  'ceDiaB',                             null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 39,           13,      11,     'DECIMAL', 'INCHES',         1, 'C/E DIA "B" TOL',              'ceDiaBTol',                          null,                 1,         null,       0,    null,  null,        38,      6,     3),
( 40,           13,      12,     'DECIMAL', 'INCHES',         0, 'C/E DIA "C"',                  'ceDiaC',                             null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 41,           13,      13,     'DECIMAL', 'INCHES',         1, 'C/E DIA "C" TOL',              'ceDiaCTol',                          null,                 1,         null,       0,    null,  null,        40,      6,     3),
( 42,           13,      14,     'DECIMAL', 'INCHES',         0, 'T/E DIA "E"',                  'teDiaE',                             null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 43,           13,      15,     'DECIMAL', 'INCHES',         1, 'T/E DIA "E" TOL',              'teDiaETol',                          null,                 1,         null,       0,    null,  null,        42,      6,     3),
( 44,           13,      16,     'DECIMAL', 'INCHES',         0, 'T/E DIA "F"',                  'teDiaF',                             null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 45,           13,      17,     'DECIMAL', 'INCHES',         1, 'T/E DIA "F" TOL',              'teDiaFTol',                          null,                 1,         null,       0,    null,  null,        44,      6,     3),
( 46,           13,      18,     'DECIMAL', 'INCHES',         0, 'OAL',                          'oal',                                null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 47,           13,      19,     'DECIMAL', 'INCHES',         1, 'OAL TOL',                      'oalTol',                             null,                 1,         null,       0,    null,  null,        46,      6,     3),
( 48,           13,      20, 'ENUMERATION',     null,      null, 'OIL INLET THREAD',             'oilInletThread',                        7,                 1,         null,    null,    null,  null,      null,   null,  null),
( 49,           13,      30, 'ENUMERATION',     null,      null, 'OIL INLET FLANGE THREAD',      'oilInletGlangeThread',                  8,                 1,         null,    null,    null,  null,      null,   null,  null),
( 50,           13,      31, 'ENUMERATION',     null,      null, 'OIL DRAIN THREAD',             'oilDrainThread',                        9,                 1,         null,    null,    null,  null,      null,   null,  null),
( 51,           13,      32, 'ENUMERATION',     null,      null, 'OIL DRAIN FLANGE THREAD',      'oilDrainFlangeThread',                 10,                 1,         null,    null,    null,  null,      null,   null,  null),
( 52,           13,      33, 'ENUMERATION',     null,      null, 'COOLANT PORT THREAD 1',        'coolantPortThread1',                   11,                 1,         null,    null,    null,  null,      null,   null,  null),
( 53,           13,      34, 'ENUMERATION',     null,      null, 'COOLANT PORT THREAD 2',        'coolantPortThread2',                   12,                 1,         null,    null,    null,  null,      null,   null,  null),
( 54,           13,      35,     'DECIMAL', 'INCHES',         0, 'PR BORE DIA',                  'prBoreDia',                          null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 55,           13,      36,     'DECIMAL', 'INCHES',         1, 'PR BORE DIA TOL',              'prBoreDiaTol',                       null,                 1,         null,       0,    null,  null,        54,      6,     3),
( 56,           13,      37,     'DECIMAL',     null,      null, 'LEAD IN CHMFR ½-ANGLE',        'leadInChmfr05Angle',                 null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 57,           13,      38,     'DECIMAL',     null,      null, 'LEAD IN CHMFR LEN',            'leadInChmfrLen',                     null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 58,           13,      39, 'ENUMERATION',     null,      null, 'QUADRANT',                     'quadrant',                             13,                 1,         null,    null,    null,  null,      null,   null,  null),
( 59,           13,      40,     'DECIMAL', 'DEGREES',        0, 'ARM ANGLE',                    'armAngle',                           null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 60,           13,      41, 'ENUMERATION',     null,      null, 'SINGLE/DUAL OIL FEED',         'oilFeed',                            null,                 1,         null,    null,    null,  null,      null,   null,  null), 
( 61,           13,      42, 'ENUMERATION',     null,      null, 'SPINNING BEARING',             'spinningBearing',                       1,                 1,         null,    null,    null,  null,      null,   null,  null),
( 62,           13,      43,     'DECIMAL',  'GRAMS',      null, 'WEIGHT',                       'weight',                             null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 63,           13,      44,     'DECIMAL',     null,      null, 'DIAGRAM #',                    'diagramNum',                         null,                 1,         null,       0,    null,  null,      null,      6,     1);

alter table backplate add column dyn_cs int, add foreign key (dyn_cs) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table backplate add column superback_flatback int, add foreign key (superback_flatback) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table backplate add column num_mounting_holes int(2);
alter table backplate add column mounting_hole_thread_callout int, add foreign key (mounting_hole_thread_callout) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table backplate add column dia_a decimal(6, 3);
alter table backplate add column dia_a_tol decimal(6, 3);
alter table backplate add column dia_b decimal(6, 3);
alter table backplate add column dia_b_tol decimal(6, 3);
alter table backplate add column dia_c decimal(6, 3);
alter table backplate add column dia_c_tol decimal(6, 3);
alter table backplate add column dia_d decimal(6, 3);
alter table backplate add column dia_d_tol decimal(6, 3);
alter table backplate add column cwc_dia_e decimal(6, 3);
alter table backplate add column cwc_dia_e_tol decimal(6, 3);
alter table backplate add column bore_dia decimal(6, 4);
alter table backplate add column bore_dia_tol decimal(6, 4);
alter table backplate add column mounting_hole_dia decimal(6, 3);
alter table backplate add column oal decimal(6, 3);
alter table backplate add column oal_tol decimal(6, 3);
alter table backplate add column hub_pos_f decimal(6, 3);
alter table backplate add column hub_pos_f_tol decimal(6, 3);
alter table backplate add column cc_loc_pos_g decimal(6, 3);
alter table backplate add column cc_loc_pos_g_tol decimal(6, 3);
alter table backplate add column lead_in_chmfr_angle decimal(6, 1);
alter table backplate add column lead_in_chmfr_len decimal(6, 3);
alter table backplate add column matl int, add foreign key (matl) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table backplate add column weight decimal(6, 1);
alter table backplate add column diagram_num int(3);


alter table bearing_housing add column water_cooled int, add foreign key (water_cooled) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column cwc_dia decimal(6,3);
alter table bearing_housing add column cwc_dia_tol decimal(6,3);
alter table bearing_housing add column bore_dia_max decimal(6,3);
alter table bearing_housing add column bore_dia_min decimal(6,3);
alter table bearing_housing add column ce_dia_a decimal(6,3);
alter table bearing_housing add column ce_dia_a_tol decimal(6,3);
alter table bearing_housing add column te_dia_d decimal(6,3);
alter table bearing_housing add column te_dia_d_tol decimal(6,3);
alter table bearing_housing add column ce_dia_b decimal(6,3);
alter table bearing_housing add column ce_dia_b_tol decimal(6,3);
alter table bearing_housing add column ce_dia_c decimal(6,3);
alter table bearing_housing add column ce_dia_c_tol decimal(6,3);
alter table bearing_housing add column te_dia_e decimal(6,3);
alter table bearing_housing add column te_dia_e_tol decimal(6,3);
alter table bearing_housing add column te_dia_f decimal(6,3);
alter table bearing_housing add column te_dia_f_tol decimal(6,3);
alter table bearing_housing add column oal decimal(6,3);
alter table bearing_housing add column oal_tol decimal(6,3);
alter table bearing_housing add column oil_inlet_thread int, add foreign key (oil_inlet_thread) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column oil_inlet_glande_thread int, add foreign key (oil_inlet_glande_thread) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column oil_drain_thread int, add foreign key (oil_drain_thread) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column oil_drain_flange_thread int, add foreign key (oil_drain_flange_thread) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column coolant_port_thread1 int, add foreign key (coolant_port_thread1) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column coolant_port_thread2 int, add foreign key (coolant_port_thread2) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column pr_bore_dia decimal(6,3);
alter table bearing_housing add column pr_bore_dia_tol decimal(6,3);
alter table bearing_housing add column lead_in_chmfr_angle decimal(6,1);
alter table bearing_housing add column lead_in_chmfr_len decimal(6,3);
alter table bearing_housing add column quadrant int, add foreign key (quadrant) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column arm_angle decimal(6,1);
alter table bearing_housing add column oil_feed int, add foreign key (oil_feed) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column spinning_bearing int, add foreign key (spinning_bearing) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column weight decimal(6,1);
alter table bearing_housing add column diagram_num int;

update bearing_housing set
    water_cooled = 4,
    ce_dia_a = 3.125,
    ce_dia_a_tol = 0.005,
    ce_dia_b = 3.650,
    ce_dia_b_tol = 0.005,
    ce_dia_c = 2.95,
    ce_dia_c_tol = 0.005,
    cwc_dia = 2.235,
    cwc_dia_tol = 0.005,
    bore_dia_max = 0.5495,
    bore_dia_min = 0.5445,
    pr_bore_dia = 0.617,
    pr_bore_dia_tol = 0.001,
    spinning_bearing = 1,
    te_dia_d = 4.382,
    te_dia_d_tol = 0.005,
    te_dia_e = 3.088,
    te_dia_e_tol = 0.005,
    te_dia_f = 2.030,
    te_dia_f_tol = 0.005,
    arm_angle = 22.5,
    oal = 2.708,
    oal_tol = 0.005,
    weight = 1500,
    diagram_num = 6,
    lead_in_chmfr_angle = 22,
    lead_in_chmfr_len = .015
where part_id=44024;



