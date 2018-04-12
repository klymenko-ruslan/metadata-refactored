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
import pprint
import csv
from enum import Enum
from functools import lru_cache


GRAPHDB_COLLECTION_PARTS = 'parts'
GRAPHDB_COLLECTION_BOMEDGES = 'bom_edges'
GRAPHDB_COLLECTION_INTERCHANGEHEADERS = 'interchange_headers'
GRAPHDB_COLLECTION_INTERCHANGEEDGES = 'interchange_edges'
GRAPHDB_COLLECTION_ALTINTERCHANGEHEADERS = 'alt_interchange_headers'
GRAPHDB_COLLECTION_ALTINTERCHANGEEDGES = 'alt_interchange_edges'
GRAPHDB_GRAPH_BOM = 'BomGraph'


class GraphDbCollectionType(Enum):

    """Type of a collention in the GraphDb."""

    document = 1
    edge = 2


SELF_DIR = os.path.dirname(os.path.realpath(__file__))
DB_DUMP_FILENAME = os.path.join(SELF_DIR, 'prepare', 'metadata-e2e.sql')
BOMEDGES_DUMP_FILENAME = os.path.join(SELF_DIR, 'prepare', 'bom_edges.csv')
INTERCHANGEHEADERS_DUMP_FILENAME = os.path.join(SELF_DIR, 'prepare',
                                                'interchange_headers.csv')
INTERCHANGEEDGES_DUMP_FILENAME = os.path.join(SELF_DIR, 'prepare',
                                              'interchange_edges.csv')
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
    prepareDb(dbaCnx)
    prepareFileStorage()
    prepareGraphDb(dbaCnx)


def prepareDb(dbconn):
    """Import test data into a MySQL."""
    print('Drop database (if exists) \'{}\'.'.format(args.db_name))
    _dropDatabaseIfExists(dbconn, args.db_name)
    dbUserExists = _ifDbUserExists(dbconn, args.db_username)
    if dbUserExists:
        print('Found registered user \'{}\' in the database. '
              'It will be deleted and registered again.'
              .format(args.db_username))
        print('Delete user \'{}\'.'.format(args.db_username))
        _dropDbUser(dbconn, args.db_host, args.db_username)
    else:
        print('User \'{}\' is not registered in the database. '
              .format(args.db_username))
    print('Register user \'{}\'.'.format(args.db_username))
    _registerDbUser(dbconn, args.db_host, args.db_username, args.db_password)
    print('A database \'{}\' is being creted.'.format(args.db_name))
    _createDatabase(dbconn, args.db_name)
    print('Grant permission on the database to the user.')
    _grantPermission(dbconn, args.db_host, args.db_name,
                     args.db_username, args.db_password)
    print('Import a dump to the database.')
    importDb(DB_DUMP_FILENAME, args.db_host, args.db_port, args.db_name,
             args.dba_username, args.dba_password)


def prepareFileStorage():
    """Prepare images and attachments."""
    print('Prepare file storage.')
    filesStorageDir = _prepareFileStorage()
    print('Copy images to the storage.')
    _copyProductImages(filesStorageDir)
    print('Copy attachments to the storage.')
    _copyAttachments(filesStorageDir)


def prepareGraphDb(dbconn):
    """Import test data into an ArangoDB."""
    httpconn = http.client.HTTPConnection(args.graphdb_host,
                                          args.graphdb_port, timeout=5)
    # httpconn.set_debuglevel(3)
    try:
        jwtDba = _loginGraphDb(httpconn, args.graphdba_username,
                               args.graphdba_password)
        _deleteGraphDbUser(httpconn, jwtDba)
        _createGraphDbInstance(httpconn, jwtDba, args.graphdb_name,
                               args.graphdb_username, args.graphdb_password)
        jwtUsr = _loginGraphDb(httpconn, args.graphdb_username,
                               args.graphdb_password)
        graphdbname = args.graphdb_name
        dbname = args.db_name
        _createCollections(httpconn, jwtUsr, graphdbname)
        _loadCollections(httpconn, jwtUsr, dbconn, graphdbname, dbname)
    except http.client.HTTPException as e:
        print('HTTP request to the ArangoDB service failed ({}): {}'
              .format(e.__class__, e))
        sys.exit(1)
    finally:
        httpconn.close()


def _createGraphDbInstance(httpconn, jwt, dbname, username, password):
    print('GraphDb: a database "{}" is beign created.'.format(dbname))
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
    createDbObj = {
        'name': dbname,
        'users': [
            {
                'username': username,
                'password': password
            }
        ]
    }
    createDbJson = json.dumps(createDbObj)
    headers = _prepareGraphDbHeaders(jwt, createDbJson)
    httpconn.request('POST', '/_api/database', createDbJson, headers)
    response = httpconn.getresponse()
    _readGraphDbResponse(response)


def _deleteGraphDbUser(httpconn, jwt):
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
    # createUserObj = dict(user=args.graphdb_username,
    #                      passwd=args.graphdb_password, active=True)
    # createUserJson = json.dumps(createUserObj)
    # headers = _prepareGraphDbHeaders(jwt, createUserJson)
    # httpconn.request('POST', '/_api/user', createUserJson, headers)
    # response = httpconn.getresponse()
    # _readGraphDbResponse(response)


def _createCollections(httpconn, jwt, graphdbname):
    _createCollection(httpconn, jwt, graphdbname,
                      GRAPHDB_COLLECTION_ALTINTERCHANGEEDGES,
                      GraphDbCollectionType.edge)
    _createCollection(httpconn, jwt, graphdbname,
                      GRAPHDB_COLLECTION_ALTINTERCHANGEHEADERS,
                      GraphDbCollectionType.document)
    _createCollection(httpconn, jwt, graphdbname, GRAPHDB_COLLECTION_BOMEDGES,
                      GraphDbCollectionType.edge)
    _createCollection(httpconn, jwt, graphdbname,
                      GRAPHDB_COLLECTION_INTERCHANGEEDGES,
                      GraphDbCollectionType.edge)
    _createCollection(httpconn, jwt, graphdbname,
                      GRAPHDB_COLLECTION_INTERCHANGEHEADERS,
                      GraphDbCollectionType.document)
    _createCollection(httpconn, jwt, graphdbname, GRAPHDB_COLLECTION_PARTS,
                      GraphDbCollectionType.document)
    _createGraph(httpconn, jwt, graphdbname, GRAPHDB_GRAPH_BOM,
                 GRAPHDB_COLLECTION_PARTS, GRAPHDB_COLLECTION_BOMEDGES,
                 GRAPHDB_COLLECTION_INTERCHANGEHEADERS,
                 GRAPHDB_COLLECTION_INTERCHANGEEDGES,
                 GRAPHDB_COLLECTION_ALTINTERCHANGEHEADERS,
                 GRAPHDB_COLLECTION_ALTINTERCHANGEEDGES)


def _createCollection(httpconn, jwt, graphdbname, collectionName,
                      collectionType):
    print('GraphDb: a collection ({}) "{}" is beign created.'
          .format(collectionType.name, collectionName))
    url = _prepareGraphDbUrl(graphdbname, '/_api/collection')
    createCollectionObj = {
        'name': collectionName,
        'type': collectionType.name,
        'isSystem': False
    }
    createCollectionJson = json.dumps(createCollectionObj)
    headers = _prepareGraphDbHeaders(jwt, createCollectionJson)
    httpconn.request('POST', url, createCollectionJson, headers)
    response = httpconn.getresponse()
    _readGraphDbResponse(response)


def _createGraph(httpconn, jwt, graphdbname, graphName, parts, bomEdges,
                 interchangeHeaders, interchangeEdges,
                 altInterchangeHeaders, altInterchangeEdges):
    print('GraphDb: a graph "{}" is beign created.'.format(graphName))
    createGraphObj = {
        'name': graphName,
        'edgeDefinitions': [
            {
                'collection': bomEdges,
                'from': [parts],
                'to': [parts]
            },
            {
                'collection': interchangeEdges,
                'from': [interchangeHeaders],
                'to': [parts]
            },
            {
                'collection': altInterchangeEdges,
                'from': [altInterchangeHeaders],
                'to': [parts]
            }

        ]
    }
    url = _prepareGraphDbUrl(graphdbname, '/_api/gharial')
    createGraphJson = json.dumps(createGraphObj)
    headers = _prepareGraphDbHeaders(jwt, createGraphJson)
    httpconn.request('POST', url, createGraphJson, headers)
    response = httpconn.getresponse()
    _readGraphDbResponse(response)


def _loadCollections(httpconn, jwt, dbconn, graphdbname, dbname):
    dbconn.database = dbname
    _loadGraphDbCollectionParts(httpconn, jwt, dbconn, graphdbname)
    _loadGraphDbCollectionBomEdges(httpconn, jwt, graphdbname)
    _loadGraphDbCollectionInterchangeHeaders(httpconn, jwt, graphdbname)
    _loadGraphDbCollectionInterchangeEdges(httpconn, jwt, graphdbname)


def _loadGraphDbCollectionParts(httpconn, jwt, dbconn, graphdbname):
    print('GraphDb: loading a collection "{}".'
          .format(GRAPHDB_COLLECTION_PARTS))
    cursor = dbconn.cursor()
    try:
        cursor.execute('select id, part_type_id, manfr_id from part')
        for (part_id, part_type_id, manfr_id) in cursor:
            partObj = {
                '_key': str(part_id),
                'partId': part_id,
                'partTypeId': part_type_id,
                'manufacturerId': manfr_id
            }
            _loadGraphDbDoc(httpconn, jwt, graphdbname,
                            GRAPHDB_COLLECTION_PARTS, partObj)
    finally:
        cursor.close()


def _loadGraphDbCollectionBomEdges(httpconn, jwt, graphdbname):
    print('GraphDb: loading a collection "{}".'
          .format(GRAPHDB_COLLECTION_BOMEDGES))
    with open(BOMEDGES_DUMP_FILENAME) as csvfile:
        csvreader = csv.reader(csvfile)
        for row in csvreader:
            _from = row[0]
            _to = row[1]
            qty = int(row[2])
            typ = row[3]
            docObj = {
                '_from': _getGraphDbPartKey(_from),
                '_to': _getGraphDbPartKey(_to),
                'quantity': qty,
                'type': typ
            }
            _loadGraphDbDoc(httpconn, jwt, graphdbname,
                            GRAPHDB_COLLECTION_BOMEDGES, docObj)


def _loadGraphDbCollectionInterchangeHeaders(httpconn, jwt, graphdbname):
    print('GraphDb: loading a collection "{}".'
          .format(GRAPHDB_COLLECTION_INTERCHANGEHEADERS))
    with open(INTERCHANGEHEADERS_DUMP_FILENAME) as csvfile:
        csvreader = csv.reader(csvfile)
        for row in csvreader:
            header = int(row[0])
            typ = row[1]
            docObj = {
                'header': header,
                'type': typ
            }
            _loadGraphDbDoc(httpconn, jwt, graphdbname,
                            GRAPHDB_COLLECTION_INTERCHANGEHEADERS, docObj)


def _loadGraphDbCollectionInterchangeEdges(httpconn, jwt, graphdbname):
    print('GraphDb: loading a collection "{}".'
          .format(GRAPHDB_COLLECTION_INTERCHANGEEDGES))
    with open(INTERCHANGEEDGES_DUMP_FILENAME) as csvfile:
        csvreader = csv.reader(csvfile)
        for row in csvreader:
            _from = row[0]
            _to = row[1]
            typ = row[2]
            docObj = {
                '_from': _getGraphDbInterchangeHeaderKey(_from),
                '_to': _getGraphDbPartKey(_to),
                'type': typ
            }
            _loadGraphDbDoc(httpconn, jwt, graphdbname,
                            GRAPHDB_COLLECTION_INTERCHANGEEDGES, docObj)


def _loadGraphDbDoc(httpconn, jwt, graphdbname, collectionName, docObj):
    url = _prepareGraphDbUrl(graphdbname, '/_api/document/{}'
                             .format(collectionName))
    docJson = json.dumps(docObj)
    headers = _prepareGraphDbHeaders(jwt, docJson)
    httpconn.request('POST', url, docJson, headers)
    response = httpconn.getresponse()
    _readGraphDbResponse(response)


def _loginGraphDb(httpconn, username, password):
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


def _readGraphDbResponse(response):
    if response.status not in [http.HTTPStatus.OK, http.HTTPStatus.CREATED,
                               http.HTTPStatus.ACCEPTED]:
        raise IOError('HTTP error during request to the GraphDb storage: '
                      '{} {}'.format(response.status, response.reason))
    bts = response.read()
    body = str(bts, 'utf-8')
    if len(body) == 0:
        retval = dict()
    else:
        retval = json.loads(body)
        if 'error' in retval and retval['error'] == True:
            raise IOError('The GraphDb storage returns API error. '
                          'Response body: {}'.format(body))
    return retval


def _getGraphDbPartKey(id_):
    return _getGraphDbDocKey(GRAPHDB_COLLECTION_PARTS, id_)


def _getGraphDbInterchangeHeaderKey(id_):
    return _getGraphDbDocKey(GRAPHDB_COLLECTION_INTERCHANGEHEADERS, id_)


@lru_cache(64)
def _getGraphDbDocKey(collectionName, id_):
    return '{}/{}'.format(collectionName, id_)


@lru_cache(32)
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


@lru_cache(8)
def _prepareGraphDbUrl(graphdbname, serviceUrl):
    if graphdbname is None:
        return serviceUrl
    else:
        url = '/_db/{graphdbname}{url}'.format(graphdbname=graphdbname,
                                               url=serviceUrl)
        return url


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


def _grantPermission(dbconn, dbhost, dbname, username, password):
    """Grant permissions to a database to an user."""
    cursor = dbconn.cursor()
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


def _createDatabase(dbconn, dbname):
    """Create a database."""
    cursor = dbconn.cursor(prepared=True)
    try:
        cursor.execute('create database ' + dbname)
    finally:
        cursor.close()


def _dropDatabaseIfExists(dbconn, dbname):
    """Drop database if it exists."""
    cursor = dbconn.cursor()
    try:
        cursor.execute('drop database if exists ' + dbname)
    finally:
        cursor.close()


def _registerDbUser(dbconn, dbhost, username, password):
    """Register user in a database server."""
    cursor = dbconn.cursor()
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


def _dropDbUser(dbconn, dbhost, username):
    """Drop user in a database server."""
    cursor = dbconn.cursor()
    try:
        query = 'drop user %(username)s@%(dbhost)s'
        cursor.execute(query, {'dbhost': dbhost, 'username': username})
        cursor.execute('flush privileges')
    finally:
        cursor.close()


def _ifDbUserExists(dbconn, username):
    """Check if user registered in MySql instance or not."""
    cursor = dbconn.cursor()
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


def _prepareFileStorage():
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


def _copyProductImages(filesStorageDir):
    """Copy products images to a filestorage."""
    productImagesDir = os.path.join(filesStorageDir, 'product_images')
    originalImagesDir = os.path.join(productImagesDir, 'originals')
    resizedImagesDir = os.path.join(productImagesDir, 'resized')
    shutil.copytree(PRODUCT_IMG_ORIGINALS_DIR, originalImagesDir)
    shutil.copytree(PRODUCT_IMG_RESIZED_DIR, resizedImagesDir)


def _copyAttachments(filesStorageDir):
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


dbconn = mysql.connector.connect(host=args.db_host, port=args.db_port,
                                 user=args.dba_username,
                                 password=args.dba_password)

try:
    t0 = time.time()
    main(dbconn)
    t1 = time.time()
    print('prepare_headers cache info: {}'
          .format(_prepareGraphDbHeaders.cache_info()))
    print('prepare_url cache info: {}'
          .format(_prepareGraphDbUrl.cache_info()))
    print('The script has been finished in {:.2f} second(s).'.format(t1 - t0))
    print
    print('The database is ready for tests. Now you have to do following:\n')
    print('1. Start in a separate window a \'webdriver\':\n'
          '\n\t$ webdriver-manager start\n')
    print('2. Start in a separate window an \'elasticsearch\':\n'
          '\n\t$ elasticsearch -v -Ecluster.name=es-metadata-e2e\n')
    print('3. Start in a separate window a GraphDb REST service (TurboBom):\n'
          '\n\t$ cd TurboBom'
          '\n\t$ export NODE_ENV=test_e2e'
          '\n\t$ grunt workon')
    print('4. Start in a separate window the \'metadata\' webapp with '
          'profile \'e2e\':\n'
          '\n\t$ ./metadata.py\n')
    print('5. Run in this window the e2e test suites:\n\n\t$ ./runtests.py\n')
finally:
    dbconn.close()
