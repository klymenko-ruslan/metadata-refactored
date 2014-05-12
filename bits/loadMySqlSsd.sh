#!/bin/bash

MYSQL_DUMP_FILE="`find /var/magento_sql_backups/ -type f | tail -1`"
MYSQL_USER=root
MYSQL_PASS='-pOI1unqzzO{=.s#?'
MYSQL_DB=magento


# Stop services, just in case they're running
service apache2 stop
service mysql stop

# Copy in the base mysql data and fix it's permissions
echo "Copying mysql data to /var/lib/mysql/"
cp -r /root/mysql.restore/* /var/lib/mysql/
chown -R mysql:mysql /var/lib/mysql/
chmod 700 /var/lib/mysql/

# Start mysql
service mysql start

# Load last export
echo "Loading mysql dump: $MYSQL_DUMP_FILE"
time zcat $MYSQL_DUMP_FILE | mysql -u $MYSQL_USER $MYSQL_PASS $MYSQL_DB

# Start Apache
service apache2 start

echo "Done."
