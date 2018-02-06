alter table changelog modify column service enum('BOM','INTERCHANGE','MAS90SYNC','SALESNOTES','APPLICATIONS','KIT','PART','TURBOMODEL','TURBOTYPE', 'CRITICALDIM', 'IMAGE') default null;
insert into service(id, name, description, required_source) values
(10, 'CRITICALDIM', 'Critical dimensions changes', false),
(11, 'IMAGE', 'Part images changes', false);