#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

"""An utility to backup GraphDb database."""

import argparse
import subprocess
import os
import tempfile
import glob
import shutil

from datetime import datetime

parser = argparse.ArgumentParser()

parser.add_argument("--arangodb-protocol", help="A protocol to use "
                    "to connect to the service (default: http+tcp).",
                    default="http+tcp")
parser.add_argument("--arangodb-host", help="Host where ArangoDb service "
                    "is running (default: localhost).", default="localhost")
parser.add_argument("--arangodb-port", help="A port of the service "
                    "(default: 8529).", type=int, default=8529)
parser.add_argument("--arangodb-user", help="A username to connect "
                    "to the service.", default="root")
parser.add_argument("--arangodb-password", help="A password to connect to "
                    "the service.", default="root")
parser.add_argument("--arangodb-database", help="A name of a database "
                    "to backup.", default="GraphDb")
parser.add_argument("--store-dir", help="directory to store a backup",
                    default="/var/metadata/backup/arangodb")
parser.add_argument("--history-size", help="how many backups to store before "
                    "remove the most old", type=int, default=14)

args = parser.parse_args()


def _mkArchive(dumpdir):
    dumpsubdir = os.path.join(dumpdir, 'dump')
    os.chdir(dumpdir)
    now = datetime.now()
    arc_name = os.path.join(args.store_dir, "graphdb-{:%Y%m%d%H%M}.sql.bz2"
                            .format(now))
    cmd_arch = ("arangodump --server.endpoint ""{protocol}://"
                "{host}:{port} "
                "--server.database ""{database}"" "
                "--server.username ""{username}"" "
                "--server.password ""{password}"" --progress false "
                "--dump-data true --output-directory ""{destdir}"""
                .format(protocol=args.arangodb_protocol,
                        host=args.arangodb_host, port=args.arangodb_port,
                        database=args.arangodb_database,
                        username=args.arangodb_user,
                        password=args.arangodb_password, destdir=dumpsubdir))
    subprocess.call(["bash", "-c", cmd_arch])
    cmd_tar = ("tar cvjf {destfile} -C ""{workdir}"" ""{subdir}"""
               .format(destfile=arc_name, workdir=dumpdir, subdir="dump"))
    print("cmd_tar: {}".format(cmd_tar))
    subprocess.call(["bash", "-c", cmd_tar])


def _delObsoleteBackups():
    arc_mask = "graphdb-*.sql.bz2"
    arcs = glob.glob(os.path.join(args.store_dir, arc_mask))
    arcs.sort()
    n = len(arcs)
    if n > args.history_size:
        to_del = arcs[0:n - args.history_size]
        for f in to_del:
            s.remove(f)


dumpdir = tempfile.mkdtemp(prefix="tmp", suffix="graphdb")
print("dumpdir: {}".format(dumpdir))
try:
    _mkArchive(dumpdir)
finally:
    shutil.rmtree(dumpdir)

_delObsoleteBackups()
