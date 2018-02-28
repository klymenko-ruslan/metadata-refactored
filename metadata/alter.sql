-- drop table if exists interchange_item;
-- drop table if exists interchange_header;
-- drop table if exists bom_descendant;
-- drop table if exists bom_alt_item;
-- drop table if exists bom_alt_header;
-- drop table if exists bom;

insert into role(id, name, display) values(33, 'ROLE_AUDIT_READ', 'Read audit log.');