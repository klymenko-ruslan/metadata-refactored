#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

"""TODO."""

import argparse
import collections
import csv
import os
import sys

argparser = argparse.ArgumentParser()
argparser.add_argument("--in-data", required=False, default=os.path.join(
    os.getcwd(), "in", "mycsvfile.csv"))
args = argparser.parse_args()


part_types = {
    "1": "turbo",
    "2": "cartridge",
    "3": "kit",
    "4": "piston_ring",
    "5": "journal_bearing",
    "6": "gasket",
    "8": "fast_wearing_component",
    "9": "major_component",
    "10": "minor_component",
    "11": "compressor_wheel",
    "12": "turbine_wheel",
    "13": "bearing_housing",
    "14": "backplate_sealplate",
    "15": "heatshield",
    "16": "nozzle_ring",
    "17": "o_ring",
    "18": "oil_deflector",
    "19": "clamp",
    "20": "thrust_part",
    "21": "misc_minor_component",
    "30": "actuator",
    "31": "compressor_cover",
    "32": "plug",
    "33": "turbine_housing",
    "34": "backplate",
    "35": "bolt_screw",
    "36": "fitting",
    "37": "journal_bearing_spacer",
    "38": "nut",
    "39": "pin",
    "40": "retaining_ring",
    "41": "seal_plate",
    "42": "spring",
    "43": "thrust_bearing",
    "44": "thrust_collar",
    "45": "thrust_spacer",
    "46": "thrust_washer",
    "47": "washer",
    "48": "carbon_seal",
    "49": "gasket_kit",
    "50": "misc",
    "51": "p",
    "52": "shroud"
}


Row = collections.namedtuple("Row", "status part_type_id_new productline "
                             "manfr_part_num part_type_id_old "
                             "part_id manfr_id")


def modify(status, pid, manfr_part_num, ptid_old, ptid_new):
    """TODO."""
    retval = ("-- {status}\n-- [{pid}] - {manfr_part_num}, "
              "{ptid_old} -> {ptid_new}\n".
              format(status=status, pid=pid, manfr_part_num=manfr_part_num,
                     ptid_old=ptid_old, ptid_new=ptid_new))
    old_table = part_types[ptid_old]
    new_table = part_types[ptid_new]
    if new_table == "kit":
        retval = ("-- Skipped: a foreign key constraint fails "
                  "(`metadata`.`kit`, CONSTRAINT `kit_ibfk_1` FOREIGN KEY "
                  "(`kit_type_id`) REFERENCES `kit_type` (`id`))\n")
    else:
        retval += ("update part set part_type_id={ptid_new} "
                   "where id={pid};\n".
                   format(ptid_new=ptid_new, pid=pid))
        retval += ("delete from {old_table} where part_id={pid};\n".
                   format(old_table=old_table, pid=pid))
        retval += ("insert into {new_table}(part_id) values({pid});\n".
                   format(pid=pid, new_table=new_table))
    return retval

with open(args.in_data, "rt") as f:
    parser = csv.reader(f)
    for rownum, record in enumerate(parser):
        if rownum:
            row = Row._make(record)
            if row.status in ["NOT TI and has TI interchange with "
                              "different part_type: update",
                              "is TI and part_type NOT equals: update"]:
                if row.part_type_id_new == row.part_type_id_old:
                    raise ValueError("Unexpected row: {}".format(row))
                s = modify(row.status, row.part_id, row.manfr_part_num,
                           row.part_type_id_old, row.part_type_id_new)
                print(s)
