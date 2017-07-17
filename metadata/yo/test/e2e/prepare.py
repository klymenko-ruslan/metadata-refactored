#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# vim: set fileencoding=utf-8 :
# vim: tabstop=8 expandtab shiftwidth=4 softtabstop=4

"""Prepare test environment for e2e testing.

This script does following:
    * registers an user 'metaserver_e2e' in a MySql instance
    * drops if exists a database 'metadata_e2e'
    * creates the database 'metadata_e2e'
    * grants permissions for the user to the database
    * import to the database a dump
    * copy product images to a filestorage which will be used by
      a testing instance of 'metadata' webapp.

"""

import argparse
import mysql.connector
import subprocess
import time
import sys
import shutil
import os

SELF_DIR = os.path.dirname(os.path.realpath(__file__))
DB_DUMP_FILENAME = os.path.join(SELF_DIR, 'prepare', 'metadata-e2e.sql')
PRODUCT_IMG_ORIGINALS_DIR = os.path.join(SELF_DIR, 'prepare', 'img',
                                         'product', 'originals')
PRODUCT_IMG_RESIZED_DIR = os.path.join(SELF_DIR, 'prepare', 'img', 'product',
                                       'resized')

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
parser.add_argument('--db-password', default='metaserver_e2e',
                    help='Password for the user \'metaserver_e2e\'')
parser.add_argument('--db-name', default='metadata_e2e',
                    help='Database name (\'metaserver_e2e\') for use during '
                    'running of e2e tests.')
parser.add_argument('--files-storage-path', default='/tmp/metadata',
                    help='A path to a directory where file storage for'
                    'testing \'metadata\' instance is located.')

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
        dropDbUser(dbaCnx, args.db_host, args.db_username)
    else:
        print('User \'{}\' is not registered in the database. '
              .format(args.db_username))
    print('Register user \'{}\'.'.format(args.db_username))
    registerDbUser(dbaCnx, args.db_host, args.db_username, args.db_password)
    print('A database \'{}\' is being creted.'.format(args.db_name))
    createDatabase(dbaCnx, args.db_name)
    print('Grant permission on the database to the user.')
    grantPermission(dbaCnx, args.db_host, args.db_name,
                    args.db_username, args.db_password)
    print('Prepare file storage [product images].')
    copyProductImages()
    print('Import a dump to the database.')
    importDb(DB_DUMP_FILENAME, args.db_host, args.db_port, args.db_name,
             args.dba_username, args.dba_password)


def importDb(filename, dbhost, dbport, dbname, dbausername, dbapassword):
    """Import a database dump."""
    cmd = ('mysql -B -q -h{host} -P{port} -u{username} -p{password} {dbname} '
           '--max-allowed-packet=256M < {dumpfile}').format(
               host=dbhost, port=dbport, username=dbausername,
               password=dbapassword, dbname=dbname, dumpfile=filename)
    retval = subprocess.call(cmd, shell=True)
    if retval != 0:
        print('Import of a database dump failed.', file=sys.stderr)
        sys.exit(1)


def grantPermission(dbaCnx, dbhost, dbname, username, password):
    """Grant permissions to a database to an user."""
    cursor = dbaCnx.cursor()
    try:
        query = ('grant all privileges on {dbname}.* '
                 'to \'{username}\'@\'{dbhost}\' '
                 'identified by \'{password}\'').format(
                     dbhost=dbhost, dbname=dbname, username=username,
                     password=password)
        cursor.execute(query)
        cursor.execute('flush privileges')
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


def dropDbUser(dbaCnx, dbhost, username):
    """Drop user in a database server."""
    cursor = dbaCnx.cursor()
    try:
        query = 'drop user %(username)s@%(dbhost)s'
        cursor.execute(query, {'dbhost': dbhost, 'username': username})
        cursor.execute('flush privileges')
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


def copyProductImages():
    """Copy products images to a filestorage."""
    if os.path.isdir(args.files_storage_path):
        shutil.rmtree(args.files_storage_path, ignore_errors=False)
    productImagesDir = os.path.join(args.files_storage_path, 'product_images')
    originalsDir = os.path.join(productImagesDir, 'originals')
    resizedDir = os.path.join(productImagesDir, 'resized')
    shutil.copytree(PRODUCT_IMG_ORIGINALS_DIR, originalsDir)
    shutil.copytree(PRODUCT_IMG_RESIZED_DIR, resizedDir)


dbaCnx = mysql.connector.connect(host=args.db_host, port=args.db_port,
                                 user=args.dba_username,
                                 password=args.dba_password)

try:
    t0 = time.time()
    main(dbaCnx)
    t1 = time.time()
    print('The script has been finished in {} second(s).'.format(int(t1 - t0)))
    print
    print('The database is ready for tests. Now you have to do following:\n')
    print('1. Start in a separate window a \'webdriver\':\n'
          '\n\t$ webdriver-manager start\n')
    print('2. Start in a separate window an \'elasticsearch\':\n'
          '\n\t$ elasticsearch -v -Ecluster.name=es-metadata-e2e\n')
    print('3. Start in a separate window the \'metadata\' webapp with '
          'profile \'e2e\':')
    print('\t$ ./metadata.py\n')
    print('4. Run in this window the e2e test suites:\n\n\t$ ./runtests.py\n')
finally:
    dbaCnx.close()
