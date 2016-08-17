#!/bin/sh

mysql -umetaserver -pmetaserver --batch --execute 'select id, manfr_part_num, manfr_id, part_type_id from part where manfr_part_num is not null and manfr_id=11' metadata > in/current_state.tsv
