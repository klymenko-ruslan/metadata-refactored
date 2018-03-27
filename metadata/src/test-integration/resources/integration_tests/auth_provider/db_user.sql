insert into user(id, name, email, username, password, password_reset_token, enabled, auth_provider_id)
values(16, 'John Rambo', 'john.rambo@zorallabs.com', 'dbtest', '$2a$10$iyW5ZrYOkkjh6QNnSrfj/emEjUrqD.2DEUjCI80Immfl2J7NagIM6', null, 1, null);

-- Add John Rambo to the Admin group.
insert into user_group(user_id, group_id) values(16, 3);
