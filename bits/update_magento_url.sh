#!/bin/bash

if [ -n "$1" ]; then
  echo Setting URL to: $1
  mysql -u root magento -e "update core_config_data set value = '$1' where path like 'web/%/base_url';"
else
  echo Current URL:
  mysql -u root magento -e "select path,value from core_config_data where path like 'web/%/base_url';"
fi
