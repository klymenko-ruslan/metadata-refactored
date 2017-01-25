delete from changelog_source;
delete from changelog_source_link;
alter table changelog_source_link add column created timestamp not null;
alter table changelog_source_link add column create_user_id bigint(20) not null;
alter table changelog_source_link add constraint chlgsrclnkcusrid foreign key (create_user_id) references user(id);
create index ix_chlgsrclnk_cusrcrtd on changelog_source_link(create_user_id, created);
