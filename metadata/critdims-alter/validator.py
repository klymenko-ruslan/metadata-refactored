#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

"""TODO:."""

import argparse
import csv
import glob
import os


cwd = os.getcwd()
argparser = argparse.ArgumentParser(description="Check import data on "
                                    "validity.")
argparser.add_argument("--in-tsv-dir", required=False,
                       default=os.path.join(cwd, "in", "data"))
args = argparser.parse_args()

collector = dict()

for fn in glob.glob(os.path.join(args.in_tsv_dir, "*.tsv")):
    basename = os.path.basename(fn)
    if basename in ["enumeration.tsv", "enum_item.tsv"]:
        continue
    with open(fn) as f:
        for row in csv.reader(f, delimiter="\t"):
            part_num = row[1]
            files = collector.get(part_num)
            if files is None:
                collector[part_num] = list([basename])
            else:
                files.append(basename)

duplicates = {
    part_num: files for part_num, files in collector.items() if len(files) > 1
}

for part_num in sorted(duplicates.keys()):
    files = duplicates[part_num]
    print("{} - {}".format(part_num, ", ".join(sorted(files))))
