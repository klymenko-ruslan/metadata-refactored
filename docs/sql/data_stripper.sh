#!/bin/bash
sed -ri 's/\r\n/\n/' data.sql
sed -ri 's/DROP.*?;//' data.sql
sed -ri 's/CREATE.*?;//' data.sql
sed -ri '/CREATE/,/.*?;/ D' data.sql
sed -ri 's/ti_website_datamodel_dbo/ti/' data.sql

