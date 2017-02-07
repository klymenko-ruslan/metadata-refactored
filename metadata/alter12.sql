alter table changelog_part modify column typ enum('BOM_PARENT','BOM_CHILD', 'PART0', 'PART1') default null;
alter table changelog_part change column typ role enum('BOM_PARENT','BOM_CHILD', 'PART0', 'PART1') default null;
create index ix_prtid on changelog_part(part_id);