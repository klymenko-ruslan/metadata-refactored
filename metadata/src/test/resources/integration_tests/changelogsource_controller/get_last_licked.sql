insert into source(id, source_name_id, name, description, url, created, updated, create_user_id, update_user_id) values
(1, 2, 'name-0', 'name-0 email', null, '2017-01-22 13:42:24', '2017-01-22 13:42:24', 1, 1),
(2, 2, 'name-1', 'name-1 email', null, '2017-01-22 13:42:24', '2017-01-22 13:42:24', 1, 1);
insert into source_attachment(id, source_id, name) values (1, 1, 'test');
insert into changelog_source_link(id, created, create_user_id) values
(1, now(), 1),
(2, now(), 1);
insert into changelog(id, change_date, user_id, description) values
(1, now(), 1, 'test'),
(2, now(), 1, 'test'),
(3, now(), 1, 'test'),
(4, now(), 1, 'test');
insert into changelog_source(changelog_id, source_id, lnk_id) values
(1, 1, 1),
(2, 2, 2),
(3, 2, 2),
(4, 2, 2);
