create table service(
    id                  bigint(20) primary key,
    name                varchar(255) unique not null,
    description         varchar(255),
    required_source     tinyint(1) not null default 0
);

insert into service(id, name, description, required_source) values
(1, 'BOM', 'BOM service', 0),
(2, 'INTERCHANGE', 'Interchange service', 0),
(3, 'MAS90SYNC', 'Mas90Sync service', 0),
(4, 'SALESNOTES', 'Salesnotes service', 0),
(5, 'APPLICATIONS', 'Applications service', 0),
(6, 'KIT', 'KIT service', 0),
(7, 'PART', 'Part service', 0),
(8, 'TURBOMODEL', 'Turbo model service', 0),
(9, 'TURBOTYPE', 'Turbo type service', 0);

insert into role(id, name, display) values (30, 'ROLE_SERVICE', 'Manage services (e.g. map changelog source functionality on services).');
insert into group_role(group_id, role_id) values (3, 30);