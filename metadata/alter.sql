insert into role(id, name, display) values
(21, 'ROLE_CHLOGSRC_CREATE', 'Create a changelog source.'),
(22, 'ROLE_CHLOGSRC_READ', 'Read a changelog source.'),
(23, 'ROLE_CHLOGSRC_UPDATE', 'Update a changelog source.'),
(24, 'ROLE_CHLOGSRC_DELETE', 'Delete a changelog source.');

-- Add to the 'Admin' group.
insert into group_role(group_id, role_id) values
(3, 21),
(3, 22),
(3, 23),
(3, 24);

create table source_name(
    id      bigint(20) not null auto_increment,
    name    varchar(255) not null,
    primary key (id)
);

insert into source_name(id, name) values
(1, 'website'),
(2, 'email'),
(3, 'spreadsheet'),
(4, 'PDF');

create table source(
    id              bigint(20) not null auto_increment,
    source_name_id  bigint(20) not null,
    name            varchar(255) not null,
    description     text,
    url             text,
    created         timestamp not null,
    updated         timestamp not null,
    create_user_id  bigint(20) not null,
    update_user_id  bigint(20) not null,

    primary key (id),
    constraint srcsrcnmid foreign key (source_name_id) references source_name(id),
    constraint srccrtusrid foreign key (create_user_id) references user(id),
    constraint srcupdusrid foreign key (update_user_id) references user(id)

);

create table source_attachment(
    id          bigint(20) not null auto_increment,
    source_id   bigint(20) not null,
    name        varchar(255) not null,
    description text,
    primary key (id),
    constraint scrattsrcid foreign key (source_id) references source(id)
);

create table changelog_source(
    changelog_id bigint(20) not null,
    source_id bigint(20) not null,
    description varchar(255),
    raiting int,
    primary key(changelog_id, source_id),
    constraint chlgsrcchid foreign key (changelog_id) references changelog(id),
    constraint chlgscrsrcid foreign key (source_id) references source(id)
);



