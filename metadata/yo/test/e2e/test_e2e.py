#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# vim: set fileencoding=utf-8 :
# vim: tabstop=8 expandtab shiftwidth=4 softtabstop=4

"""The script runs e2e tests."""

import http.client

url = 'http://localhost:8080/metadata/search/indexall'

response = http.client.HTTPConnection.request('POST', url)
print('Response: {}'.format(response))
