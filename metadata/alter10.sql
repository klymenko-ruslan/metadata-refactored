delete from changelog_source;
delete from changelog_source_link;
drop index ix_chlgsrclnk_prtid on changelog_source_link;
alter table changelog_source_link drop column part_id;

create table changelog_part(
    id bigint(20) not null auto_increment primary key,
    changelog_id bigint(20) not null references changelog(id),
    part_id bigint(20) references part(id),
    typ enum('BOM_PARENT', 'BOM_CHILD')
);
