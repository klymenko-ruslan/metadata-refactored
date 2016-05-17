#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

r"""
Utility to convert critical dimension desctiptors to *.sql patch file.
The input data is a set of export to JSON tables from the provided MS-ACCESS
database.

Export of a table to JSON is broken in 'dbeaver' program.
A string values are not escaped, so if a value contain char '"' an output JSON
is not valid. Below is a regexp for Vim to fix this issue (the text to fix
must be selected before run this regex):

'<,'>s/^\(\t\t"\w\+" : "[^"]*\)\("\)\([^"]*\)\("\)\(.*",\)$/\1\\\2\3\\\4\5/gc

"""

import argparse
import json
import os


KEY_PT_ID = "_pt_id"
KEY_PT_META = "_pt_meta"
KEY_CDA = "_cda"
KEY_LIST = "_list"


def format_warn(s):
    """Return a formatted string with warning."""
    return "-- WARN: " + s


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
        _cda = part_type.get(KEY_CDA)
        if _cda is None:
            _cda = list()
            part_type[KEY_CDA] = _cda
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
        _list = cda.get(KEY_LIST)
        if _list is None:
            _list = list()
            cda[KEY_LIST] = _list
        _list.append(ls)

    with open(args.in_part_type_metadata) as fp:
        part_types_metadata = json.load(fp)["part_type"]

    obsolete_part_type = list()

    for ptm in part_types_metadata:
        value = ptm["value"]
        pt = part_types_idx_by_value.get(value)
        if pt is None:
            obsolete_part_type.append(ptm)
        else:
            pt[KEY_PT_META] = ptm
            pt[KEY_PT_ID] = ptm["id"]

    return (obsolete_part_type, part_types)


def createTableCritDimSql():
    """SQL statements to create tables for critical dimensions."""
    return r"""
drop table if exists crit_dim_enum_val, crit_dim_enum, crit_dim;
create table crit_dim_enum (
    id int not null auto_increment,
    name varchar(64) not null,
    primary key (id)
) comment='Enumerations for critical dimensions.' engine=innodb;
create table crit_dim_enum_val (
    id int not null auto_increment,
    crit_dim_enum_id int not null references crit_dim_enum(id)
                            on delete cascade on update cascade,
    val varchar(64) not null,
    primary key (id),
    unique key (id, crit_dim_enum_id)
) comment='Enumeration values for critical dimensions enumerations.'
    engine=innodb;
create table crit_dim (
    id              bigint not null,
    part_type_id    bigint not null,
    seq_num         int not null,
    data_type       enum ('DECIMAL', 'ENUMERATION',
                          'INTEGER', 'TEXT') not null,
    enum_id         int,
    unit            enum ('DEGREES', 'GRAMS', 'INCHES'),
    tolerance       tinyint(1) comment '0 - nominal,
                    1 - tolerance/limit, null - not a tolerance',
    name            varchar(255) not null,
    json_name       varchar(48) not null comment 'Name of a property
                        in serialized to JSON part''s object.
                        It must be the exact name of the property
                        in JPA entity.',
    idx_name        varchar(48) not null unique comment 'Name of
                        a property in the ElasticSearch mapping.',
    null_allowed    tinyint(1) not null comment 'Validation:
                        Is NULL allowed?',
    null_display    varchar(32) comment 'How to display NULL values.',
    min_val         decimal(15, 6) comment 'Validation:
                        minal (inclusive) allowed value for numeric
                        types (DECIMAL, INTEGER).',
    max_val         decimal(15, 6) comment 'Validation:
                        maximal (inclusive) allowed value for numeric
                        types (DECIMAL, INTEGER).',
    regex           varchar(255) comment 'Validation: JS regular
                        expression',
    parent_id       bigint,
    length          tinyint com: "",ment 'Lenth on a web page',
    scale           tinyint comment 'Scale on a web pate.',
    primary key(id),
    unique key(part_type_id, seq_num),
    foreign key (part_type_id) references part_type(id),
    foreign key (enum_id) references crit_dim_enum(id)
                        on delete set null on update cascade,
    foreign key (parent_id) references crit_dim(id)
                        on delete set null on update cascade
) engine=innodb;
"""


def generate_create_table(table_name, cda_):
    return "-- CREATE TABLE {}".format(table_name) # TODO


def generate_alters(table_name, cda_):
    return "-- ALTER TABLE {}".format(table_name) # TODO


# *****************************************************************************
#                                M A I N
# *****************************************************************************

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

(obsolete_part_type, part_types) = load_input_data(args)

# print(json.dumps(part_types, indent=2))

print(createTableCritDimSql())

seq_part_type = 30
#
# print("delete from part_types where id >= {};".format(seq_part_types))
#

if obsolete_part_type:
    print(format_warn("Found obsolete part types."))
    for ptm in obsolete_part_type:
        print("-- delete from part_type where id={0:d}; -- {1:s}".format(
            ptm["id"], ptm["name"]))
    print()

for pt in part_types:
    pt_id = pt.get(KEY_PT_ID)
    entity_table_exists = True
    if pt_id is None:
        # Register this new part type.
        print("insert into part_type(id, name, magento_attribute_set, value) "
              "values({id_}, '{name}', '{mas}', '{value}');".format(
                  id_=seq_part_type, name=pt["name"], mas=pt["name_value"],
                  value=pt["name_value"]))
        pt[KEY_PT_ID] = seq_part_type
        seq_part_type += 1
        entity_table_exists = False
    cda_ = pt.get(KEY_CDA)
    if not cda_:
        print(format_warn("Part type [{0:d}] - {1:s} has no defined "
                          "critical dimensions.".format(pt_id, pt["name"])))
        if cda_ is None:
            cda_ = list()
    table_name = pt["name_value"] 
    if entity_table_exists:
        sql = generate_alters(table_name, cda_)
    else:
        sql = generate_create_table(table_name, cda_)

    print(sql)
#     for cd in cda_:
#         print("".format())
