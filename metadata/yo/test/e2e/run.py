#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# vim: set fileencoding=utf-8 :
# vim: tabstop=8 expandtab shiftwidth=4 softtabstop=4

""" Prepare test environment for e2e testing.

This script does following:
    * registers an user 'metaserver_e2e' in a MySql instance
    * drops if exists a database 'metadata_e2e'
    * creates the database 'metadata_e2e'
    * grants permissions for the user to the database
    * import to the database a dump

"""

import argparse
import mysql.connector

parser = argparse.ArgumentParser(description='Preparing of'
                                 ' a test environment.')
parser.add_argument('--db-host', default='localhost',
                    help='Host with a running instance of MySql server.')
parser.add_argument('--db-port', default=3306, type=int,
                    help='A port for the instntance of the MySql server.')
parser.add_argument('--dba-username', default='root', help='DBA name.')
parser.add_argument('--dba-password', default='root', help='DBA password.')
parser.add_argument('--db-username', default='metaserver_e2e',
                    help='Username for the database \'metadata_e2e\'.')
parser.add_argument('--db-password', default='root',
                    help='Password for the user \'metaserver_e2e\'')
parser.add_argument('--db-name', default='metadata_e2e',
                    help='Database name (\'metaserver_e2e\') for use during '
                    'running of e2e test.')

args = parser.parse_args()


def main(dbaCnx):
    """Main function."""
    print('Drop database (if exists) \'{}\'.'.format(args.db_name))
    dropDatabaseIfExists(dbaCnx, args.db_name)
    dbUserExists = ifDbUserExists(dbaCnx, args.db_username)
    if dbUserExists:
        print('Found registered user \'{}\' in the database. '
              'It will be deleted and registered again.'
              .format(args.db_username))
        print('Delete user \'{}\'.'.format(args.db_username))
        dropDbUser(dbaCnx, args.db_username)
    else:
        print('User \'{}\' is not registered in the database. '
              .format(args.db_username))
    print('Register user \'{}\'.'.format(args.db_username))
    registerDbUser(dbaCnx, args.db_host, args.db_username, args.db_password)
    print('A database \'{}\' is being creted.'.format(args.db_name))
    createDatabase(dbaCnx, args.db_name)
    print('Grant permission on the database to the user.')
    grantPermission(dbaCnx, args.db_host, args.db_username, args.db_name)


def grantPermission(dbaCnx, dbhost, dbusername, dbname):
    """Grant permissions to a database to an user."""
    cursor = dbaCnx.cursor()
    try:
        query = ('grant all privileges on {dbname}.* '
                 'to \'{dbusername}\'@\'{dbhost}\'').format(
                     dbname=dbname, dbusername=dbusername, dbhost=dbhost)
        cursor.execute(query)
    finally:
        cursor.close()


def createDatabase(dbaCnx, dbname):
    """Create a database."""
    cursor = dbaCnx.cursor(prepared=True)
    try:
        cursor.execute('create database ' + dbname)
    finally:
        cursor.close()


def dropDatabaseIfExists(dbaCnx, dbname):
    """Drop database if it exists."""
    cursor = dbaCnx.cursor()
    try:
        cursor.execute('drop database if exists ' + dbname)
    finally:
        cursor.close()


def registerDbUser(dbaCnx, dbhost, username, password):
    """Register user in a database server."""
    cursor = dbaCnx.cursor()
    try:
        query = ('create user %(username)s@%(dbhost)s '
                 'identified by %(password)s')
        cursor.execute(query, {
            'username': username,
            'password': password,
            'dbhost': dbhost}
        )
    finally:
        cursor.close()


def dropDbUser(dbaCnx, username):
    """Drop user in a database server."""
    cursor = dbaCnx.cursor()
    try:
        query = 'drop user %(username)s'
        cursor.execute(query, {'username': username})
    finally:
        cursor.close()


def ifDbUserExists(dbaCnx, username):
    """Check if user registered in MySql instance or not."""
    cursor = dbaCnx.cursor()
    try:
        query = (
            'select exists('
            '  select 1 from mysql.user '
            '  where user = %(username)s'
            ')'
        )
        cursor.execute(query, {'username': username})
        row = cursor.fetchone()
        if row is None:
            return False
        else:
            return bool(row[0])
    finally:
        cursor.close()


dbaCnx = mysql.connector.connect(host=args.db_host, port=args.db_port,
                                 user=args.dba_username,
                                 password=args.dba_password)

try:
    main(dbaCnx)
finally:
    dbaCnx.close()
