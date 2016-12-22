insert into auth_provider(id, typ) values(10, 'LDAP');

insert into auth_provider_ldap(id, name, host, port, protocol, domain)
values(10, 'TurboInternational AD', 'ldap.turbointernational.com', 636, 'LDAPS_SOFT', 'turbointernational.local');

insert into user(id, name, email, username, password, password_reset_token, enabled, auth_provider_id)
values(16, 'John Rambo', 'john.rambo@zorallabs.com', 'ldaptest', null, null, 1, 10);

insert into user(id, name, email, username, password, password_reset_token, enabled, auth_provider_id)
values(17, 'Coby Reddick', 'creddick@turbointernational.com', 'creddick', null, null, 1, 10);

