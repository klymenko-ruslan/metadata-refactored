#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# vim: set fileencoding=utf-8 :
# vim: tabstop=8 expandtab shiftwidth=4 softtabstop=4

"""The script runs e2e tests."""

import sys
import http.client

print 'Indexing of all documetns.'

try:
    httpconn = http.client.HTTPConnection('localhost', 8080, timeout=60)
    httpconn.request('POST', '/metadata/search/indexall')
    response = httpconn.getresponse()
except http.client.HTTPException e:
    print('Starting of the indexing job failed. Server response: {}'.format(e))
    sys.exit(1)

if response.status != http.HTTPStatus.OK:
    print('The indexing job failed: {}')
    sys.exit(1)
print('The indexing has been finished.')
