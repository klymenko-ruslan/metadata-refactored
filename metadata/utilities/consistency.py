#!/usr/bin/env python3
# vim: set fileencoding=utf-8 :
# vim: set tabstop=8 expandtab shiftwidth=4 softtabstop=4 :

"""A script to check data consistency between MySql database and GraphDb."""

import argparse
from collections import namedtuple
import json
import urllib.request
import sys

import mysql.connector
from elasticsearch import Elasticsearch
from tqdm import tqdm


parser = argparse.ArgumentParser(description='Utility to check data '
                                 'consistency between MySql database '
                                 'and GraphDb.')
parser.add_argument('--db-host', default='localhost',
                    help='Host with a running instance of MySql server.')
parser.add_argument('--db-port', default=3306, type=int,
                    help='A port for the instntance of the MySql server.')
parser.add_argument('--db-name', default='metadata',
                    help='Database name (\'metaserver\') to export.')
parser.add_argument('--db-username', default='metaserver',
                    help='Username for the database \'metadata\'.')
parser.add_argument('--db-password', default='metaserver',
                    help='Password for the user \'metaserver\'')
parser.add_argument('--graphdb-host', default='localhost',
                    help='Host with a running instance of GraphDb service.')
parser.add_argument('--graphdb-port', default=9009, type=int,
                    help='A port for the instntance of the GraphDb service.')
parser.add_argument('--es-host', default='localhost',
                    help='Host with a running instance of ElasticSearch '
                    'service.')
parser.add_argument('--es-port', default=9200, type=int,
                    help='A port for the instance of the ElasticSearch '
                    'service.')
parser.add_argument('--es-index-part', default='metadata_dev_part',
                    help='An index name for Parts in the ElasticSearch.')
args = parser.parse_args()

PartRecord = namedtuple('PartRecord', 'id manfr_part_num manfr_id '
                        'part_type_id')


def _error(p, msg):
    print('Found error for {}. {}'.format(_formatPart(p), msg),
          file=sys.stderr)


def _fatal(msg):
    print('Fatal error. {}'.format(msg), file=sys.stderr)
    sys.exit(1)


def _formatPart(p):
    return '[{}] - {}'.format(p.id, p.manfr_part_num)


def _checkPart(partInGraphDb, partInDb):
    #  TODO: the attribute must be returned as int
    if int(partInGraphDb['partId']) != (partInDb.id):
        _error(partInDb, 'Different part ID numbers in '
               'Db [{}] and GraphDb [{}]'
               .format(partInGraphDb['partId'], partInDb.id))
    if partInGraphDb['partTypeId'] != partInDb.part_type_id:
        _error(partInDb, 'Different part types numbers in '
               'Db [{}] and GraphDb [{}]'
               .format(partInGraphDb['partTypeId'], partInDb.part_type_id))
    if partInGraphDb['manufacturerId'] != partInDb.manfr_id:
        _error(partInDb, 'Different manufacturer part numbers in '
               'Db [{}] and GraphDb [{}]'
               .format(partInGraphDb['manufacturerId'], partInDb.manfr_id))


def _checkBoms(p, parts):
    #  TODO: the attribute must be returned as int
    pid = int(p.id)
    url = 'http://{}:{}/parts/{}/boms'.format(args.graphdb_host,
                                              args.graphdb_port,
                                              pid)
    req = urllib.request.Request(url, method='GET')
    res = urllib.request.urlopen(req)
    retcode = res.getcode()
    if retcode != 200:
        _fatal('Unexpected return code during processing a part {}: {}'
               .format(_formatPart(p), retcode))
    body = res.read()
    # print('body: {}'.format(body))
    boms = json.loads(body.decode('utf-8'))
    for b in boms:
        child_id = b['partId']
        #  TODO: the attribute must be returned as int
        child_id = int(child_id)
        if child_id not in parts:
            _error(p, 'Invalid ID of a child in BOM: {}. Part with this ID '
                   'is not exist in the DB.'.format(child_id))
        qty = int(b['qty'])
        if qty < 0:
            _error(p, 'Found a negative value {} of attribute "qty" '
                   'in a BOM for child part ID {}.'
                   .format(qty, child_id))
        # Check interchanges in the BOM entry.
        for interchange_id in b['interchanges']:
            #  TODO: the attribute must be returned as int
            interchange_id = int(interchange_id)
            if interchange_id not in parts:
                _error(p, 'Invalid ID of an interchange [{}] in BOM: {}. '
                       'Part with this ID is not exist in the DB.'
                       .format(interchange_id, child_id))
        # Check alternatives.
        url_alternatives = ('http://{}:{}/boms/{}/children/{}/alternatives'
                            .format(args.graphdb_host, args.graphdb_port,
                                    pid, child_id))
        req_alt = urllib.request.Request(url_alternatives, method='GET')
        res_alt = urllib.request.urlopen(req_alt)
        retcode_alt = res_alt.getcode()
        if retcode_alt != 200:
            _fatal('Unexpected return code during processing a BOM''s '
                   'child part [{}] for the part {}: {}'
                   .format(child_id, _formatPart(p), retcode_alt))
        body_alt = res_alt.read()
        alt_boms = json.loads(body_alt.decode('utf-8'))
        for alt in alt_boms:
            for alt_part_id in alt['parts']:
                if alt_part_id not in parts:
                    _error(p, 'Invalid ID of an Alternative BOM for the '
                           'BOM''s child part [{}]: {}.'
                           .format(child_id, alt_part_id))


def _checkInterchanges(p, es_src, parts):
    url = 'http://{}:{}/parts/{}/interchanges'.format(args.graphdb_host,
                                                      args.graphdb_port,
                                                      p.id)
    req = urllib.request.Request(url, method='GET')
    res = urllib.request.urlopen(req)
    retcode = res.getcode()
    if retcode != 200:
        _fatal('Unexpected return code during processing a part {}: {}'
               .format(_formatPart(p), retcode))
    body = res.read()
    # print('body: {}'.format(body))
    response_obj = json.loads(body.decode('utf-8'))
    # Check that interchange stored in the ElasticSearch is
    # the same as returned by GraphDb.
    es_interchange = es_src.get('interchange')
    es_interchange_id = es_interchange.get('id')
    es_parts = [ip['partId'] for ip in es_interchange.get('parts')]
    # print('es_parts: {}'.format(es_parts))
    if es_interchange_id != response_obj['headerId']:
        _error(p, 'Interchange ID in the GrapgDb ({}) is different than in '
               'the ElasticSearch ({}).'.format(response_obj['headerId'],
                                                es_interchange_id))
    gdb_parts = response_obj['parts']
    for pid in gdb_parts:
        if pid not in parts:
            _error(p, 'Invalid interchange. Part with ID [{}] '
                   'found in the GraphDb but not found in the MySql database.'
                   .format(pid))
    for pid in es_parts:
        if pid not in parts:
            _error(p, 'Invalid interchange. Part with ID [{}] '
                   'found in the ElasticSearch but not found in the MySql '
                   'database.'.format(pid))
    if set(es_parts) != set(gdb_parts):
        _error(p, 'Interchanges registered in the ElasticSearch ({}) '
               'are not the same interchanges registered in the GraphDb ({}).'
               .format(es_parts, gdb_parts))


def _checkAncestors(p, parts):
    #  TODO: the attribute must be returned as int
    pid = int(p.id)
    url = 'http://{}:{}/parts/{}/ancestors'.format(args.graphdb_host,
                                                   args.graphdb_port,
                                                   pid)
    req = urllib.request.Request(url, method='GET')
    res = urllib.request.urlopen(req)
    retcode = res.getcode()
    if retcode != 200:
        _fatal('Unexpected return code during processing ancestors '
               'for the part {}: {}'
               .format(_formatPart(p), retcode))
    body = res.read()
    # print('body: {}'.format(body))
    ancestors = json.loads(body.decode('utf-8'))
    for a in ancestors:
        ancestor_id = a['partId']
        if ancestor_id not in parts:
            _error(p, 'Invalid ID of an ancestor: {}. Part with this ID '
                   'is not exist in the DB.'.format(ancestor_id))


def checkElasticSearch(es, p):
    """Check presense of a part in an ElasticSearch index."""
    try:
        response = es.get(index=args.es_index_part, doc_type='part', id=p.id)
        src = response.get('_source')
        src_id = src.get('id')
        if src_id != p.id:
            _error(p, 'Part ID in the ElasticSearch is different '
                   '(is it ever possible?): {}'.format(src_id))
        src_pn = src.get('manufacturerPartNumber')
        if src_pn != p.manfr_part_num:
            _error(p, 'Part number in the ElasticSearch is different: {}'
                   .format(src_pn))
        src_mnfr_id = src.get('manufacturer').get('id')
        if src_mnfr_id != p.manfr_id:
            _error(p, 'Manufacturer ID in the ElasticSearch is different: {}'
                   .format(src_mnfr_id))
        src_pt_id = src.get('partType').get('id')
        if src_pt_id != p.part_type_id:
            _error(p, 'Part type ID int the ElasticSearch is different: {}'
                   .format(src_pt_id))
        return src
    except TransportError as e:
        if e.status_code == 404:
            _error(p, 'Part not found in the ElasticSearch index.')
            return src
        else:
            raise e


cnx = mysql.connector.connect(host=args.db_host, port=args.db_port,
                              database=args.db_name,
                              user=args.db_username,
                              password=args.db_password,
                              connection_timeout=3600000)

es = Elasticsearch([args.es_host], port=args.es_port)

try:
    cur = cnx.cursor()
    try:
        print('Loading of parts ...')
        cur.execute('select id, manfr_part_num, manfr_id, part_type_id '
                    'from part')
        parts = {rec[0]: PartRecord(*rec) for rec in cur.fetchall()}
        print('Loaded {} parts.'.format(len(parts)))
        try:
            print('Porcessing...')
            for id in tqdm(parts.keys()):
                p = parts[id]
                es_src = checkElasticSearch(es, p)
                url = 'http://{}:{}/parts/{}'.format(args.graphdb_host,
                                                     args.graphdb_port,
                                                     id)
                req = urllib.request.Request(url, method='GET')
                res = urllib.request.urlopen(req)
                retcode = res.getcode()
                body = res.read()
                # print('retcode: {}'.format(retcode))
                # print('body: {}'.format(body))
                if retcode == 404:
                    _error(p, 'Part not found in the GraphDb.')
                elif retcode != 200:
                    _fatal('Unexpected HTTP response code: {}. Part: {}.'
                           .format(retcode, _formatPart(p)))
                    sys.exit(1)
                obj = json.loads(body.decode('utf-8'))
                _checkPart(obj, p)
                _checkBoms(p, parts)
                _checkInterchanges(p, es_src, parts)
                _checkAncestors(p, parts)
        except urllib.error.HTTPError as e:
            _fatal(repr(e))
    finally:
        cur.close()
finally:
    cnx.close()
