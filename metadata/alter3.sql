alter table changelog_source drop foreign key chlgsrclnkid;
alter table changelog_source_link modify column id bigint(20) not null auto_increment;
alter table changelog_source add constraint chlgsrclnkid foreign key (lnk_id) references changelog_source_link(id);
