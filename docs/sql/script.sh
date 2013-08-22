#!/bin/bash
sed -ri 's/\r\n/\n/' "data.sql" &&
sed -ri 's/DROP.*?;//' "data.sql" &&
sed -ri 's/CREATE.*?;//' "data.sql" &&
sed -ri '/CREATE/,/.*?;/ D' "data.sql" &&
sed -ri 's/ti_website_datamodel_dbo/ti/' "data.sql" &&

cat metadata_schema.sql > "script.sql" &&
cat pre_import.sql >> "script.sql" &&
cat data.sql >> "script.sql" &&
cat post_import.sql >> "script.sql" &&

dos2unix "script.sql"

sed -ri 's/`([A-Z0-9_]+)`/`\L\1`/g' metadata_schema.sql

