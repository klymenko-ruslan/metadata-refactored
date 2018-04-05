#!/usr/bin/env python3.5
# -*- coding: utf-8 -*-
# vim: set fileencoding=utf-8 :
# vim: tabstop=8 expandtab shiftwidth=4 softtabstop=4

"""Prepare test environment for e2e testing.

This script does following:
    * TODO: write about GraphDb
    * registers an user 'metaserver_e2e' in a MySql instance
    * drops if exists a database 'metadata_e2e'
    * creates the database 'metadata_e2e'
    * grants permissions for the user to the database
    * import to the database a dump
    * copy product images to a filestorage which will be used by
      a testing instance of 'metadata' webapp.
"""

import argparse
import http.client
import mysql.connector
import subprocess
import json
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
parser.add_argument('--graphdb-host', default='localhost',
                    help='Host with a running instance of ArangoDb 3.x '
                    'service.')
parser.add_argument('--graphdb-port', default=8529, type=int,
                    help='A port for the instntance of the ArangoDb service.')
parser.add_argument('--graphdba-username', default='root', help='DBA name for '
                    'the ArangoDb service.')
parser.add_argument('--graphdba-password', default='root', help='DBA password '
                    'for the ArangoDb service.')
parser.add_argument('--graphdb-username', default='GraphDbE2E',
                    help='Username for the database \'GraphDbE2E\'.')
parser.add_argument('--graphdb-password', default='GraphDbE2E',
                    help='Password for the user \'GraphDbE2E\'')
parser.add_argument('--graphdb-name', default='GraphDbE2E',
                    help='Database name (\'GraphDbE2E\') for use during '
                    'running of e2e tests.')
parser.add_argument('--files-storage-dir', default='/tmp/metadata',
                    help='A path to a directory where file storage for'
                    'testing \'metadata\' instance is located.')

args = parser.parse_args()


def main(dbaCnx):
    """Main function."""
    #  prepareDb()
    #  prepareFileStorage()
    prepareGraphDb()
    sys.exit(0)  #  TODO: remove when new functionality will be created


def prepareDb():
    """Import test data into a MySQL."""
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
    print('Import a dump to the database.')
    importDb(DB_DUMP_FILENAME, args.db_host, args.db_port, args.db_name,
             args.dba_username, args.dba_password)


def prepareFileStorage():
    """Prepare images and attachments."""
    print('Prepare file storage.')
    filesStorageDir = prepareFileStorage()
    print('Copy images to the storage.')
    copyProductImages(filesStorageDir)
    print('Copy attachments to the storage.')
    copyAttachments(filesStorageDir)


def prepareGraphDb():
    """Import test data into an ArangoDB."""
    try:
        httpconn = http.client.HTTPConnection(args.graphdb_host,
                                              args.graphdb_port, timeout=5)
        jwt = _loginGraphDb(httpconn)
        #  print('jwt: {}'.format(jwt))
        _createGraphDbInstance(httpconn, jwt)
        _createGraphDbUser(httpconn, jwt)
    except http.client.HTTPException as e:
        print('HTTP request to the ArangoDB service failed: {}'
              .format(e))
        print(e.__class__)
        sys.exit(1)
    finally:
        httpconn.close()


def _createGraphDbInstance(httpconn, jwt):
    headers = _prepareGraphDbHeaders(jwt, None)
    httpconn.request('GET', '/_api/database', None, headers)
    response = httpconn.getresponse()
    obj = _readGraphDbResponse(response)
    databases = obj['result']
    if args.graphdb_name in databases:
        httpconn.request('DELETE',
                         '/_api/database/{}'.format(args.graphdb_name),
                         None, headers)
        obj = _readGraphDbResponse(httpconn.getresponse())
    createDbObj = dict(name=args.graphdb_name)
    createDbJson = json.dumps(createDbObj)
    headers = _prepareGraphDbHeaders(jwt, createDbJson)
    httpconn.request('POST', '/_api/database', createDbJson, headers)
    response = httpconn.getresponse()
    _readGraphDbResponse(response)


def _createGraphDbUser(httpconn, jwt):
    headers = _prepareGraphDbHeaders(jwt, None)
    httpconn.request('GET', '/_api/user', None, headers)
    response = httpconn.getresponse()
    obj = _readGraphDbResponse(response)
    users = obj['result']
    usr = list(filter(lambda u: u['user'] == args.graphdb_username, users))
    if len(usr) > 0:
        httpconn.request('DELETE',
                         '/_api/user/{}'.format(args.graphdb_username),
                         None, headers)
        obj = _readGraphDbResponse(httpconn.getresponse())
    createUserObj = dict(user=args.graphdb_username,
                          passwd=args.graphdb_password, active=True)
    createUserJson = json.dumps(createUserObj)
    headers = _prepareGraphDbHeaders(jwt, createUserJson)
    httpconn.request('POST', '/_api/user', createUserJson, headers)
    response = httpconn.getresponse()
    _readGraphDbResponse(response)


def _loginGraphDb(httpconn):
    http_auth = dict(
        username=args.graphdba_username,
        password=args.graphdba_password
    )
    auth_body = json.dumps(http_auth)
    headers = _prepareGraphDbHeaders(None, auth_body)
    httpconn.request('POST', '/_open/auth', auth_body, headers)
    response = httpconn.getresponse()
    if response.status != http.HTTPStatus.OK:
        print('Authentication in the ArangoDb service failed: {}'
              .format(response.status))
        sys.exit(1)
    bts = response.read()
    body = str(bts, 'utf-8')
    obj = json.loads(body)
    jwt = obj['jwt']
    return jwt


def _prepareGraphDbHeaders(jwt, body):
    headers = {
        'Connection': 'Keep-Alive',
        'Content-Type': 'application/json',
        'x-arango-async': 'false'
    }
    if jwt is None:
        headers['X-Omit-WWW-Authenticate'] = True
    else:
        headers['Authorization'] = 'bearer {}'.format(jwt)
    if body is not None:
        headers['Content-Length'] = len(body)
    return headers


def _readGraphDbResponse(response):
    if response.status not in [http.HTTPStatus.OK, http.HTTPStatus.CREATED,
                               http.HTTPStatus.ACCEPTED]:
        raise IOError('HTTP error during request of the GraphDb storage: '
                      '{} {}'.format(response.status, response.reason))
    bts = response.read()
    body = str(bts, 'utf-8')
    if len(body) == 0:
        retval = dict()
    else:
        retval = json.loads(body)
        if retval['error'] == True:
            raise IOError('The GraphDb storage returns API error. '
                          'Response body: {}'.format(body))
    return retval


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


def prepareFileStorage():
    """
    Create a directory for a filestorage and required subdirs.

    The destination directory is removed in the begin if it exists.
    The function returns full filename of a root directory of the storage.
    """
    rootDir = args.files_storage_dir
    if os.path.isdir(rootDir):
        shutil.rmtree(rootDir, ignore_errors=False)
    os.makedirs(rootDir)
    return rootDir


def copyProductImages(filesStorageDir):
    """Copy products images to a filestorage."""
    productImagesDir = os.path.join(filesStorageDir, 'product_images')
    originalImagesDir = os.path.join(productImagesDir, 'originals')
    resizedImagesDir = os.path.join(productImagesDir, 'resized')
    shutil.copytree(PRODUCT_IMG_ORIGINALS_DIR, originalImagesDir)
    shutil.copytree(PRODUCT_IMG_RESIZED_DIR, resizedImagesDir)


def copyAttachments(filesStorageDir):
    """Copy attachments to a filestorage."""
    otherDir = os.path.join(filesStorageDir, 'other')
    attachmentsSalesnotesDir = os.path.join(otherDir, 'salesNote',
                                            'attachments')
    changelogSourcesDir = os.path.join(otherDir, 'changelog', 'sources',
                                       'attachments')
    changelogSourceLnkDscrAttchDir = os.path.join(otherDir, 'changelog',
                                                  'sources', 'link',
                                                  'description', 'attachments')
    os.makedirs(attachmentsSalesnotesDir)
    os.makedirs(changelogSourcesDir)
    os.makedirs(changelogSourceLnkDscrAttchDir)


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
