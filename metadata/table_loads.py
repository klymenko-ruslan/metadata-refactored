#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""TODO:."""

import datetime
import mysql.connector

config = {
    'user': 'metaserver',
    'password': 'metaserver',
    'host': '127.0.0.1',
    'database': 'metadata',
    'raise_on_warnings': True
}

conn = mysql.connector.connect(**config)
cur = conn.cursor()
cur.execute('show tables')
table_names = cur.fetchall()
for row in table_names:
    tname = row[0]
    if tname.startswith('test'):
        continue
    t0 = datetime.datetime.now()
    cur.execute('select count(*) from {}'.format(tname))
    t1 = datetime.datetime.now()
    t = t1 - t0
    count = cur.fetchone()[0]
    print '{},{},{}'.format(tname, count, t.microseconds)
conn.close()
