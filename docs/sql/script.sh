#!/bin/bash

# Replace with unix newlines
dos2unix data.sql

# Remove drops
sed -ri 's/DROP.*?;//' "data.sql" &&

# Remove creates
sed -ri 's/CREATE.*?;//' "data.sql" &&
sed -ri '/CREATE/,/.*?;/ D' "data.sql" &&

# Fix namespace
sed -ri 's/ti_website_datamodel_dbo/ti/' "data.sql" &&

# Build script
cat metadata_schema.sql > "script.sql" &&
cat pre_import.sql >> "script.sql" &&
cat data.sql >> "script.sql" &&
cat post_import.sql >> "script.sql" &&
cat images.sql >> "script.sql" &&

# Fix newlines
dos2unix "script.sql"

# Lowercase identifiers
sed -ri 's/`([A-Z0-9_]+)`/`\L\1`/g' metadata_schema.sql

