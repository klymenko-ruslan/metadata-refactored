insert into role(id, name, display) values(32, 'ROLE_MANUFACTURER_CRUD', 'CRUD operations on manufacturers.');
insert into group_role(group_id, role_id) values(3 , 32);

alter table manfr add column not_external tinyint(1) not null default 0;