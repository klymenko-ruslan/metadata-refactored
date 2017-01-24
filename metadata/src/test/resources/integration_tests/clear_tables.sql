-- Clear tables, exclude dictionaries, when test finished.

delete from rebuildbomdescendancy_tbl;
delete from bom;
delete from interchange_item;
delete from interchange_header;
delete from cartridge;
delete from kit;
delete from bearing_housing;
delete from changelog_source;
delete from changelog_source_link;
delete from source_attachment;
delete from source;
delete from source_name;
delete from changelog;
delete from mas90sync_failure;
delete from mas90sync_success;
delete from mas90sync;

delete from actuator;
delete from backplate;
delete from backplate_sealplate;
delete from bearing_housing;
delete from bolt_screw;
delete from carbon_seal;
delete from cartridge;
delete from clamp;
delete from compressor_cover;
delete from compressor_wheel;
delete from fast_wearing_component;
delete from fitting;
delete from gasket;
delete from heatshield;
delete from journal_bearing;
delete from journal_bearing_spacer;
delete from kit;
delete from major_component;
delete from minor_component;
delete from misc;
delete from misc_minor_component;
delete from nozzle_ring;
delete from nut;
delete from oil_deflector;
delete from o_ring;
delete from p;
delete from pin;
delete from piston_ring;
delete from plug;
delete from retaining_ring;
delete from seal_plate;
delete from shroud;
delete from spring;
delete from thrust_bearing;
delete from thrust_collar;
delete from thrust_part;
delete from thrust_spacer;
delete from thrust_washer;
delete from turbine_housing;
delete from turbine_wheel;

delete from turbo;
delete from gasket_kit;
delete from turbo_model;
delete from turbo_type;

delete from washer;
delete from part;
alter table part alter column id restart with 1;
alter table source alter column id restart with 1;
alter table source_attachment alter column id restart with 1;

-- turbo_car_model_engine_year

