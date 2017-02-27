insert into role(id, name, display)
values(31, 'ROLE_PRICE_READ', 'Read prices for a part.');
insert into group_role(group_id, role_id) values(3, 31);