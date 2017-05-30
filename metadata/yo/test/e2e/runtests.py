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
args = parser.parse_args()


print('Checking of prerequisites.')

if shutil.which('protractor') is None:
    print('Executable \'protractor\' not found. '
          'Is it (http://www.protractortest.org) installed?', file=sys.stderr)
    sys.exit(1)

jpsout = subprocess.getoutput('jps')

if re.search(r'selenium-server-standalone-[\d\.]+.jar', jpsout) is None:
    print('Running instance of a webdriver-manager not found. '
          'Please run: \'webdriver-manager start\' in a separate window.',
          file=sys.stderr)
    sys.exit(1)

if re.search(r'Elasticsearch', jpsout) is None:
    print('Running instance of an Elasticsearch not found.',
          file=sys.stderr)
    sys.exit(1)

if re.search(r'metadata-e2e.jar', jpsout) is None:
    print('Running instance of \'metadata\' webapp not found. '
          'Please run \'metadata.py\'.', file=sys.stderr)
    sys.exit(1)

if not args.skip_indexing:
  print('Indexing of all documents.')

  try:
      httpconn = http.client.HTTPConnection('localhost', 8080, timeout=60)
      httpconn.request('POST', '/metadata/search/indexall')
      response = httpconn.getresponse()
  except http.client.HTTPException as e:
      print('Starting of the indexing job failed. Server response: {}'.format(e))
      sys.exit(1)

  if response.status != http.client.OK:
      print('The indexing job failed: {}')
      sys.exit(1)
  print('The indexing has been finished.')

subprocess.call('protractor ./protractor.conf.js', shell=True)
