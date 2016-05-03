drop table crit_dim;
drop table crit_dim_enum_val;
drop table crit_dim_enum;

create table crit_dim_enum (
    id int not null auto_increment,
    name varchar(64) not null,
    primary key (id)
) comment='Enumerations for critical dimensions.' engine=innodb;

insert into crit_dim_enum(id, name)
values
(1, 'yesNoEnum'),
(2, 'waterCooledEnum');

create table crit_dim_enum_val (
    id int not null auto_increment,
    crit_dim_enum_id int not null references crit_dim_enum(id) on delete cascade on update cascade,
    val varchar(64) not null,
    primary key (id),
    unique key (id, crit_dim_enum_id)
) comment='Enumeration values for critical dimensions enumerations.' engine=innodb;

insert into crit_dim_enum_val
(id, crit_dim_enum_id, val)
values
(1, 1, 'Yes'),
(2, 1, 'No'),
(3, 2, 'Oil'),
(4, 2, 'Water');

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
(id, part_type_id, seq_num,     data_type,     unit, tolerance, name,                      json_name,              enum_id,                null_allowed, null_display, min_val, max_val, regex, parent_id, length, scale)
values
-- Bearing housing
(  1,          13,       1, 'ENUMERATION',     null,      null, 'WATER COOLED',            'waterCooled',                          2,                 1,         'No',    null,    null,  null,      null,   null,  null),
(  2,          13,       2,     'DECIMAL', 'INCHES',         0, 'C/E DIA "A"',             'ceDiaA',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
(  3,          13,       3,     'DECIMAL', 'INCHES',         1, 'C/E DIA "A" TOL',         'ceDiaATol',                         null,                 1,         null,       0,    null,  null,         2,      6,     3),
(  4,          13,       4,     'DECIMAL', 'INCHES',         0, 'C/E DIA "B"',             'ceDiaB',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
(  5,          13,       5,     'DECIMAL', 'INCHES',         1, 'C/E DIA "B" TOL',         'ceDiaBTol',                         null,                 1,         null,       0,    null,  null,         4,      6,     3),
(  6,          13,       6,     'DECIMAL', 'INCHES',         0, 'C/E DIA "C"',             'ceDiaC',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
(  7,          13,       7,     'DECIMAL', 'INCHES',         1, 'C/E DIA "C" TOL',         'ceDiaCTol',                         null,                 1,         null,       0,    null,  null,         6,      6,     3),
( 10,          13,      10,     'DECIMAL', 'INCHES',      null, 'BORE DIA MAX',            'boreDiaMax',                        null,                 1,         null,       0,    null,  null,      null,      6,     4),
( 11,          13,      11,     'DECIMAL', 'INCHES',      null, 'BORE DIA MIN',            'boreDiaMin',                        null,                 1,         null,       0,    null,  null,      null,      6,     4),
( 12,          13,      12,     'DECIMAL', 'INCHES',         0, 'PR BORE DIA',             'prBoreDia',                         null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 13,          13,      13,     'DECIMAL', 'INCHES',         1, 'PR BORE DIA TOL',         'prBoreDiaTol',                      null,                 1,         null,       0,    null,  null,        12,      6,     3),
( 14,          13,      14, 'ENUMERATION',     null,      null, 'OIL FEED',                'oilFeed',                           null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 15,          13,      15, 'ENUMERATION',     null,      null, 'SPINNING BEARING',        'spinningBearing',                      1,                 1,         null,    null,    null,  null,      null,   null,  null),
( 16,          13,      16, 'ENUMERATION',     null,      null, 'OIL INLET THREAD',        'oilInletThread',                    null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 17,          13,      17, 'ENUMERATION',     null,      null, 'OIL DRAIN THREAD',        'oilDrainThread',                    null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 18,          13,      18, 'ENUMERATION',     null,      null, 'OIL DRAIN FLANGE THREAD', 'oilDrainFlangeThread',              null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 19,          13,      19, 'ENUMERATION',     null,      null, 'COOLANT PORT THREAD 1',   'coolantPortThread1',                null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 20,          13,      20, 'ENUMERATION',     null,      null, 'COOLANT PORT THREAD 2',   'coolantPortThread2',                null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 21,          13,      21,     'DECIMAL', 'INCHES',         0, 'T/E DIA "D"',             'teDiaD',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 22,          13,      22,     'DECIMAL', 'INCHES',         1, 'T/E DIA "D" TOL',         'teDiaDTol',                         null,                 1,         null,       0,    null,  null,        21,      6,     3),
( 23,          13,      23,     'DECIMAL', 'INCHES',         0, 'T/E DIA "E"',             'teDiaE',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 24,          13,      24,     'DECIMAL', 'INCHES',         1, 'T/E DIA "E" TOL',         'teDiaETol',                         null,                 1,         null,       0,    null,  null,        23,      6,     3),
( 25,          13,      25,     'DECIMAL', 'INCHES',         0, 'T/E DIA "F"',             'teDiaF',                            null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 26,          13,      26,     'DECIMAL', 'INCHES',         1, 'T/E DIA "F" TOL',         'teDiaFTol',                         null,                 1,         null,       0,    null,  null,        25,      6,     3),
( 27,          13,      27,     'DECIMAL', 'DEGREES',        0, 'ARM ANGLE',               'armAngle',                          null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 28,          13,      28, 'ENUMERATION',     null,      null, 'QUADRANT',                'quadrant',                          null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 29,          13,      29,     'DECIMAL', 'INCHES',         0, 'OAL',                     'oal',                               null,                 1,         null,       0,    null,  null,      null,      6,     3),
( 30,          13,      30,     'DECIMAL', 'INCHES',         1, 'OAL TOL',                 'oalTol',                            null,                 1,         null,       0,    null,  null,        29,      6,     3),
( 31,          13,      31,     'DECIMAL',  'GRAMS',      null, 'WEIGHT',                  'weight',                            null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 32,          13,      32,     'DECIMAL',     null,      null, 'DIAGRAM #',               'diagramNum',                        null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 33,          13,      33, 'ENUMERATION',     null,      null, 'OIL INLET FLANGE THREAD', 'oil_inlet_glange_thread',           null,                 1,         null,    null,    null,  null,      null,   null,  null),
( 34,          13,      34,     'DECIMAL',     null,      null, 'LEAD IN CHMFR ½-ANGLE',   'leadInChmfr05Angle',                null,                 1,         null,       0,    null,  null,      null,      6,     1),
( 35,          13,      35,     'DECIMAL',     null,      null, 'LEAD IN CHMFR LEN',       'leadInChmfrLen',                    null,                 1,         null,       0,    null,  null,      null,      6,     1);

alter table bearing_housing add column ce_dia_a decimal(6,3);
alter table bearing_housing add column ce_dia_a_tol decimal(6,3);
alter table bearing_housing add column ce_dia_b decimal(6,3);
alter table bearing_housing add column ce_dia_b_tol decimal(6,3);
alter table bearing_housing add column ce_dia_c decimal(6,3);
alter table bearing_housing add column ce_dia_c_tol decimal(6,3);
alter table bearing_housing add column cwc_dia decimal(6,3);
alter table bearing_housing add column cwc_dia_tol decimal(6,3);
alter table bearing_housing add column bore_dia_max decimal(6,3);
alter table bearing_housing add column bore_dia_min decimal(6,3);
alter table bearing_housing add column pr_bore_dia decimal(6,3);
alter table bearing_housing add column pr_bore_dia_tol decimal(6,3);
alter table bearing_housing add column te_dia_d decimal(6,3);
alter table bearing_housing add column te_dia_d_tol decimal(6,3);
alter table bearing_housing add column te_dia_e decimal(6,3);
alter table bearing_housing add column te_dia_e_tol decimal(6,3);
alter table bearing_housing add column te_dia_f decimal(6,3);
alter table bearing_housing add column te_dia_f_tol decimal(6,3);
alter table bearing_housing add column arm_angle decimal(6,1);
alter table bearing_housing add column oal decimal(6,3);
alter table bearing_housing add column oal_tol decimal(6,3);
alter table bearing_housing add column weight decimal(6,1);
alter table bearing_housing add column diagram_num int;
alter table bearing_housing add column led_in_chmfr_angle decimal(6,1);
alter table bearing_housing add column led_in_chmfr_len decimal(6,3);
alter table bearing_housing add column water_cooled int, add foreign key (water_cooled) references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing add column spinning_bearing int, add foreign key (spinning_bearing) references crit_dim_enum_val(id) on delete set null on update cascade;

/*
alter table bearing_housing modify column water_cooled int references crit_dim_enum_val(id) on delete set null on update cascade;
alter table bearing_housing modify column spinning_bearing int references crit_dim_enum_val(id) on delete set null on update cascade;
*/

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
    led_in_chmfr_angle = 22,
    led_in_chmfr_len = .015
where part_id=44024;



