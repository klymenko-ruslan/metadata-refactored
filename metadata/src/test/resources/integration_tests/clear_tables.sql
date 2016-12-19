-- Clear tables, exclude dictionaries, when test finished.

delete from rebuildbomdescendancy_tbl;
delete from bom;
delete from interchange_item;
delete from interchange_header;
delete from cartridge;
delete from kit;
delete from bearing_housing;
delete from part;
delete from changelog;
delete from mas90sync_failure;
delete from mas90sync_success;
delete from mas90sync;
