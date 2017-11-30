#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

"""
A script to start synchronization job.

The synchronization job do synchronization between MAS90
and a 'metadata' server.
It is expected that this script should be periodically started by cron.
"""

import argparse
import http.client
import json
import sys

argparser = argparse.ArgumentParser(description="A script to start job "
                                    "on the 'metadata' server a job "
                                    "of synchronization between MAS90 "
                                    "and 'metadata' server.")
argparser.add_argument("--metadata-host", required=False, default="localhost")
argparser.add_argument("--metadata-port", required=False, default=8080)
args = argparser.parse_args()

conn = http.client.HTTPConnection(args.metadata_host, args.metadata_port)
conn.request("GET", "/metadata/mas90sync/status")
response = conn.getresponse()
if response.status != 200:
    print("Getting of a status of a synchronization job failed. "
          "HTTP response: {} {}".format(response.status, response.reason))
    sys.exit(2)
else:
    response_body = response.read()
    json_str = response_body.decode("utf-8")
    obj = json.loads(json_str)
    if not obj["finished"]:
        print("A synchronization job is already in a progress.")
        print(json.dumps(obj, indent=2))
        sys.exit(3)

conn.request("POST", "/metadata/mas90sync/startsyncjob")
response = conn.getresponse()
if response.status != 200:
    print("Starting of a synchronization job failed. HTTP response: {} {}"
          .format(response.status, response.reason))
else:
    response_body = response.read()
    # Sample of a response:
    # b'{"startedOn":1467035579315,"userId":10000,"userName":"Sync Agent",
    # "partsUpdateTotalSteps":0,"partsUpdateCurrentStep":0,
    # "partsUpdateInserts":0,"partsUpdateUpdates":0,"partsUpdateSkipped":0,
    # "finished":false,"errors":[],"modifications":[]}'
    json_str = response_body.decode("utf-8")
    obj = json.loads(json_str)
    print(json.dumps(obj, indent=2))
