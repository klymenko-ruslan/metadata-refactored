alter table source modify column updated timestamp not null default current_timestamp;
alter table source modify column name varchar(255) not null unique;