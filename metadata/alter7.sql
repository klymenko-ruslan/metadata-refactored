delete from changelog_source;
alter table changelog_source drop primary key;
alter table changelog_source drop foreign key chlgsrcchid;
alter table changelog_source drop key chlgsrcchid;
alter table changelog_source drop column changelog_id;
alter table changelog_source add primary key(lnk_id, source_id);
alter table changelog_source_link add column changelog_id bigint(20) not null;
delete from changelog_source_link;
alter table changelog_source_link add constraint chlgsrclnkchlgid foreign key (changelog_id) references changelog(id);
alter table changelog_source_link modify column changelog_id bigint(20) not null unique;
alter table changelog_source change column raiting rating int(11) default null;