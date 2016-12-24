#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

import argparse
import subprocess
import os
import glob

from datetime import datetime

parser = argparse.ArgumentParser()

parser.add_argument("--db-host", help="database host", default="localhost")
parser.add_argument("--db-port", help="database port", default=3306)
parser.add_argument("--db-user", help="database user", default="root")
parser.add_argument("--db-password", help="database password", default="root")
parser.add_argument("--db-name", help="name of a database", default="metadata")
parser.add_argument("--store-dir", help="directory to store a backup",
	default="/var/metadata/backup/db")
parser.add_argument("--history-size", help="how many backups to store before "
	"remove the most old", type=int, default=14)

args = parser.parse_args()

now = datetime.now()

arc_name=os.path.join(args.store_dir,
	"{db}-{:%Y%m%d%H%M}.sql.bz2".format(now, db=args.db_name))

cmd_arch = ("mysqldump -R --add-drop-database -h{host} -P{port} -u{user} "
	"-p{psw} {db}  | bzip2 -9 -c > {arc_name}").format(host=args.db_host,
	port=args.db_port, user=args.db_user, psw=args.db_password,
	db=args.db_name, arc_name=arc_name
)

subprocess.call(["bash", "-c", cmd_arch])

arc_mask = "{db}-*.sql.bz2".format(db=args.db_name)
arcs = glob.glob(os.path.join(args.store_dir, arc_mask))
arcs.sort()

n = len(arcs)
if n > args.history_size:
	to_del = arcs[0:n - args.history_size]
	for f in to_del:
		os.remove(f)
