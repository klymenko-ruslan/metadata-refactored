create table changelog_source_link_description_attachment(
    id bigint(20) not null auto_increment,
    created timestamp not null default now(),
    name varchar(255),
    original_name varchar(255),
    size bigint(20),
    mime varchar(255) not null,
    filename varchar(1024),
    changelog_source_link_id bigint(20),
    primary key(id),
    key changelog_source_link_id(changelog_source_link_id),
    constraint changelog_source_link_fk foreign key(changelog_source_link_id) references changelog_source_link(id)
);
