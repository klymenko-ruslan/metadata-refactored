alter table changelog_source drop column description;
alter table changelog_source add constraint chlgsrcrtng check(raiting >= 0 and raiting <=5);
alter table changelog_source drop foreign key `chlgsrcchid`;
alter table changelog_source drop primary key;

create table changelog_source_link(
    id bigint(20) not null,
    description text,
    primary key (id)
);

alter table changelog_source add column lnk_id bigint(20) not null;
delete from changelog_source;
alter table changelog_source add constraint chlgsrclnkid foreign key (lnk_id) references changelog_source_link(id);
alter table changelog_source add constraint chlgsrcchid foreign key (changelog_id) references changelog(id);
alter table changelog_source add primary key(lnk_id, changelog_id, source_id);
alter table changelog_source add key chlgsrclnkid(lnk_id);
