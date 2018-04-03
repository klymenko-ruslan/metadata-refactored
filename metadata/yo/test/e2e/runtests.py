#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# vim: set fileencoding=utf-8 :
# vim: tabstop=8 expandtab shiftwidth=4 softtabstop=4

"""The script runs e2e tests."""

import argparse
import http.client
import shutil
import subprocess
import sys
import re


parser = argparse.ArgumentParser(description='Build and runs the \'metadata\' '
                                 'webapp for e2e testing.')
parser.add_argument('--skip-indexing', action='store_true',
                    help='Skip step to index documents. It is useful during '
                    'test development.')
parser.add_argument('--protractor-opts', default='',
                    help='Options to pass to Protractor')
args = parser.parse_args()


print('Checking of prerequisites.')

if shutil.which('protractor') is None:
    print('Executable \'protractor\' not found. '
          'Is it (http://www.protractortest.org) installed?', file=sys.stderr)
    sys.exit(1)

jpsout = subprocess.getoutput('jps')

# Check that webdriver is running.
if re.search(r'selenium-server-standalone-[\d\.]+.jar', jpsout) is None:
    print('Running instance of a webdriver-manager not found. '
          'Please run: \'webdriver-manager start\' in a separate window.',
          file=sys.stderr)
    sys.exit(1)

# Check that Elasticsearch is running.
if re.search(r'Elasticsearch', jpsout) is None:
    print('Running instance of an Elasticsearch not found.',
          file=sys.stderr)
    sys.exit(1)

# Check that ArangoDb is running.
try:
    httpconn = http.client.HTTPConnection('localhost', 8529, timeout=3)
    httpconn.request('GET', '')
    response = httpconn.getresponse()
except http.client.HTTPException as e:
    print('Starting of the indexing job failed. Server response: {}'
          .format(e))
    sys.exit(1)
if (response.status != http.HTTPStatus.OK and
        response.status != http.HTTPStatus.MOVED_PERMANENTLY and
        response.status != http.HTTPStatus.TEMPORARY_REDIRECT):
    print('The ArangoDb service is not running: {}'.format(response.status))
    sys.exit(1)

# Check that metadata webapp is running.
if re.search(r'metadata-e2e.jar', jpsout) is None:
    print('Running instance of \'metadata\' webapp not found. '
          'Please run \'./metadata.py\'.', file=sys.stderr)
    sys.exit(1)

if not args.skip_indexing:
    print('Indexing of all documents.')
    try:
        httpconn = http.client.HTTPConnection('localhost', 8080, timeout=60)
        httpconn.request('POST', '/metadata/search/indexall')
        response = httpconn.getresponse()
    except http.client.HTTPException as e:
        print('Starting of the indexing job failed. Server response: {}'
              .format(e))
        sys.exit(1)
    if response.status != http.HTTPStatus.OK:
        print('The indexing job failed: {}'.format(resopnse.status))
        sys.exit(1)
    print('The indexing has been finished.')

cmd = 'protractor {opts} ./protractor.conf.js'.format(
    opts=args.protractor_opts)
print('Command line to run Protractor: {}'.format(cmd))
subprocess.call(cmd, shell=True)
