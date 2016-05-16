#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

r"""
Utility.

Vim:
'<,'>s/^\(\t\t"\w\+" : "[^"]*\)\("\)\([^"]*\)\("\)\(.*",\)$/\1\\\2\3\\\4\5/gc
"""

import argparse
import json
import os


def load_input_data(args):
    """Load data."""
    with open(args.in_part_type) as fp:
        part_types = json.load(fp)["part_type"]

    part_types_idx_by_id = {pt["id"]: pt for pt in part_types}
    part_types_idx_by_value = {pt["name_value"]: pt for pt in part_types}

    with open(args.in_crit_dim_attribute) as fp:
        crit_dim_attributes = json.load(fp)["crit_dim_attribute"]

    for cda in crit_dim_attributes:
        pt_id = cda["part_type_id"]
        part_type = part_types_idx_by_id[pt_id]
        _cda = part_type.get("_cda")
        if _cda is None:
            _cda = list()
            part_type["_cda"] = _cda
        _cda.append(cda)
        _cda.sort(key=lambda x: x["sequence"]
                  if x["sequence"] is not None else 0)

    crit_dim_attributes_idx_by_id = {cda["id"]: cda for cda
                                     in crit_dim_attributes}

    # d = dict()
    # for cda in crit_dim_attributes:
    #     name = cda["name"]
    #     lst = d.get(name)
    #     if lst is None:
    #         lst = list()
    #         d[name] = lst
    #     lst.append(cda)
    # for name, lst in d.items():
    #     if len(lst) > 1:
    #         print(name, len(lst))

    with open(args.in_list_selection) as fp:
        list_selections = json.load(fp)["list_selection"]

    for ls in list_selections:
        cda_id = ls["crit_dim_attribute_id"]
        cda = crit_dim_attributes_idx_by_id[cda_id]
        _list = cda.get("_list")
        if _list is None:
            _list = list()
            cda["_list"] = _list
        _list.append(ls)

    with open(args.in_part_type_metadata) as fp:
        part_types_metadata = json.load(fp)["part_type"]

    for ptm in part_types_metadata:
        value = ptm["value"]
        pt = part_types_idx_by_value.get(value)
        if pt is not None:
            pt["_pt_meta"] = pt

    return part_types


argparser = argparse.ArgumentParser(description="Utility to (re)create patch "
                                    "files to add support of the critical "
                                    "dimensions functionality to 'metadata' "
                                    "webapp.")
argparser.add_argument("--in-part-type-metadata", required=False,
                       default=os.path.join(os.getcwd(), "in",
                                            "part_type_metadata.json"),
                       help="File in JSON format with exported data from the "
                       "table 'part_type' in the 'metadata' database.")
argparser.add_argument("--in-part-type", required=False,
                       default=os.path.join(os.getcwd(), "in",
                                            "part_type.json"),
                       help="File in JSON format with exported data from the "
                       "table 'part_type'.")
argparser.add_argument("--in-crit-dim-attribute", required=False,
                       default=os.path.join(os.getcwd(), "in",
                                            "crit_dim_attribute.json"),
                       help="File in JSON format with exported data from the "
                       "table 'crit_dim_attribute'.")
argparser.add_argument("--in-list-selection", required=False,
                       default=os.path.join(os.getcwd(), "in",
                                            "list_selection.json"),
                       help="File in JSON format with exported data from the "
                       "table 'list_selection'.")
args = argparser.parse_args()

part_types = load_input_data(args)

print(json.dumps(part_types, indent=2))
