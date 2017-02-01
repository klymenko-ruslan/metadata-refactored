alter table changelog_source_link add column part_id bigint(20) default null references part(id);
create index ix_chlgsrclnk_prtid on changelog_source_link(part_id);