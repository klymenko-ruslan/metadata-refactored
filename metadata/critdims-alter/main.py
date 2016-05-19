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
import collections
import json
import os
import re
import sys


KEY_PT_ID = "_pt_id"
KEY_PT_META = "_pt_meta"
KEY_CDA = "_cda"
KEY_LIST = "_list"
KEY_CDDBID = "_dbid"

KEY_COLNAME = "_col_name"
KEY_TYPES = "_types"

YESNOENUM_ID = 1

REGEX_NONALPHANUM = re.compile(r'\W')

seq_part_type = 30
seq_critdim = 1

Types = collections.namedtuple("Types", ["field_type", "sql_type",
                                         "data_type"])


isAscii = lambda s: len(s) == len(s.encode())


def def_sql_null(d, k):
    if k not in d: return "null"
    retval = d[k]
    if retval is None: return "null"
    return retval


def sql_str_praram(s):
    if s is None: return "null"
    s = s.replace("'", "''")
    return "'" + s + "'"


def name2jsonName(s):
    """
    Convert a string to a valid JSON identifier.

    Example: 'T/E RAMP DRAIN DIA "F" TOL' -> 'teRampDrainDiaFTol'
    """
    retval = ""
    cap = False
    prevSingleChar = False
    for w in REGEX_NONALPHANUM.split(s):
        if w != "":
            if w == "½":
                w = "05"
            if not isAscii(w):
                raise ValueError("Found unsupported unicode character: " + w)
            n = len(w)
            if not cap or w != "TOL" and n == 1 and prevSingleChar:
                retval += w.lower()
                cap = True
            else:
                retval += w.capitalize()
                cap = True
            prevSingleChar = n == 1
    return retval


def name2idxName(table_name, jsonName):
    """
    Convert a string to a valid fied identifier in ElasticSearch index.

    Example: ('backplate', 'diaA') -> 'bckpltDiaA'
    """
    pref = ""
    for c in table_name.lower():
        if c not in "aeiou_":
            pref += c
    return pref + jsonName.capitalize()


def cd2types(cd):
    """
    Convert critical dimension to SQL type and type for critical
    dimension descriptor to enum item of the data_type field in the table
    that contains critical dimensions -- 'crit_dim'.
    """
    retval = None
    field_type = cd["field_type"]
    if field_type == "DECIMAL":
        length = cd["length"]
        scale = cd["scale"]
        sql_type = "decimal({},{})".format(length, scale)
        data_type = "DECIMAL"
    elif field_type == "INTEGER":
        length = cd["length"]
        sql_type = "int({})".format(length)
        data_type = "INTEGER"
    elif field_type == "LIST":
        sql_type = "int"
        data_type = "ENUMERATION"
    elif field_type == "LOGICAL":
        sql_type = "int"
        data_type = "ENUMERATION"
    elif field_type == "MEMO":
        length = cd["length"] or 4096
        sql_type = "varchar({})".format(length)
        data_type = "TEXT"
    else:
        raise ValueError("Unsupported field type: {}".format(data_type))
    return Types(field_type=field_type, sql_type=sql_type,
                 data_type=data_type)


def cd2sqlreference(cd):
    """Convert critical dimension to SQL foreign key reference."""
    data_type = cd["field_type"]
    retval = ""
    if data_type == "LIST" or data_type == "LOGICAL":
        retval = "references crit_dim_enum_val(id) on delete set null " \
            "on update cascade"
    return retval


def format_warn(s):
    """Return a formatted string with warning."""
    return "-- WARN: " + s


def load_input_data(args):
    """Load data."""
    with open(args.in_part_type) as fp: part_types = json.load(fp)["part_type"]

    part_types_idx_by_id = {pt["id"]: pt for pt in part_types}
    part_types_idx_by_value = {pt["name_value"]: pt for pt in part_types}

    with open(args.in_crit_dim_attribute) as fp:
        crit_dim_attributes = json.load(fp)["crit_dim_attribute"]

    for cd in crit_dim_attributes:
        pt_id = cd["part_type_id"]
        part_type = part_types_idx_by_id[pt_id]
        _cda = part_type.get(KEY_CDA)
        if _cda is None:
            _cda = list()
            part_type[KEY_CDA] = _cda
        _cda.append(cd)
        _cda.sort(key=lambda x: x["sequence"]
                  if x["sequence"] is not None else 0)

    crit_dim_attributes_idx_by_id = {cd["id"]: cd for cd
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

    return (obsolete_part_type, part_types,
            crit_dim_attributes, crit_dim_attributes_idx_by_id)


def createTableCritDimSql():
    """SQL statements to create tables for critical dimensions."""
    return r"""
drop table if exists crit_dim cascade;
drop table if exists crit_dim_enum cascade;
drop table if exists crit_dim_enum_val cascade;
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
    length          tinyint comment 'Lenth on a web page',
    scale           tinyint comment 'Scale on a web pate.',
    primary key(id),
    unique key(part_type_id, seq_num),
    foreign key (part_type_id) references part_type(id),
    foreign key (enum_id) references crit_dim_enum(id)
                        on delete set null on update cascade,
    foreign key (parent_id) references crit_dim(id)
                        on delete set null on update cascade
) engine=innodb;

insert into crit_dim_enum(id, name) values({yesnoenum_id}, 'yesNoEnum');

insert into crit_dim_enum_val(id, crit_dim_enum_id, val) values
(  1, {yesnoenum_id}, 'YES'),
(  2, {yesnoenum_id}, 'NO');
""".format(yesnoenum_id=YESNOENUM_ID)


def get_part_type_mapping(extra_data, key):
    ed = extra_data["part_type_mapping"].get(key)
    if ed is None:
        print("Extra info for 'part_type_mapping' for key '{}' "
                "not found.".format(ptm["value"]))
        sys.exit(2)
    return ed


def generate_alters(ed, cda_):
    table_name = ed["table"]
    columns = set()
    retval = ""
    for cd in cda_:
        col_name = name2jsonName(cd["name"])
        types = cd2types(cd)
        cd[KEY_COLNAME] = col_name
        cd[KEY_TYPES] = types
        col_type = types.sql_type
        col_ref = cd2sqlreference(cd)
        if col_name in columns:
            raise ValueError("In table '{}' found duplicated columns '{}'.".format(table_name, col_name))
        else:
            columns.add(col_name)
        retval += "alter table {} add column {} {}".format(
            table_name, col_name, col_type)
        if col_ref:
            retval += ", add foreign key ({}) {}".format(col_name, col_ref)
        retval += ";\n"
    return retval


def generate_create_table(ed, cda_):
    table_name = ed["table"]
    retval = "create table {} (\n" \
        "\tpart_id bigint(20) not null,\n" \
        "\tkey part_id (part_id)".format(table_name)
    columns = set()
    if cda_:
        retval += ",\n"
        for idx, cd in enumerate(cda_):
            if idx:
                retval += ",\n"
            col_name = name2jsonName(cd["name"])
            if col_name in columns:
                raise ValueError("In table '{}' found duplicated columns '{}'."
                                 .format(table_name, col_name))
            else:
                columns.add(col_name)
            types = cd2types(cd)
            cd[KEY_COLNAME] = col_name
            cd[KEY_TYPES] = types
            col_type = types.sql_type
            col_ref = cd2sqlreference(cd)
            retval += "\t{} {} {}".format(col_name, col_type, col_ref)
    retval += "\n) engine=innodb default charset=utf8;"
    return retval


def register_crit_dim(part_type_id, table_name, cda_, crit_dim_attributes,
                      crit_dim_attributes_idx_by_id):
    sql = "insert into crit_dim(id, part_type_id, seq_num, data_type, " \
        "unit, "\
        "tolerance, name, json_name, idx_name, " \
        "enum_id, null_allowed, null_display, " \
        "min_val, "\
        "max_val, regex, parent_id, length, " \
        "scale) "\
        "values\n"
    add_comma = False
    for cd in cda_:
        types = cd[KEY_TYPES]
        id_ = crit_dim_attributes_idx_by_id[cd["id"]][KEY_CDDBID]
        seq_num = cd["sequence"]
        data_type = types.data_type
        if cd["unit"] is not None:
            unit = cd["unit"]
        else:
            unit = None
        if cd["tolerance"] == "PLUS;MINUS":
            tolerance = 1
        else:
            tolerance = 0
        name = cd["name"]
        json_name = cd[KEY_COLNAME]
        idx_name = name2idxName(table_name, json_name)
        if types.field_type == "BOOLEAN":
            enum_id = YESNOENUM_ID
        elif types.data_type == "ENUMERATION":
            enum_id = "null" # TODO
        else:
            enum_id = "null"
        null_allowed = "true"
        null_display = None
        if types.data_type in ["DECIMAL", "INTEGER"]:
            min_val = 0
        else:
            min_val = "null"
        max_val = "null"
        regex = None
        parent_attribute = cd["parent_attribute"]
        if parent_attribute is None:
            parent_id= "null"
        else:
            parent_id = crit_dim_attributes_idx_by_id[parent_attribute][KEY_CDDBID]
        length = def_sql_null(cd, "length")
        scale = def_sql_null(cd, "scale")
        if add_comma: sql += ",\n"
        values = "({id_}, {part_type_id}, {seq_num}, {data_type}, {unit}, " \
            "{tolerance}, {name}, {json_name}, {idx_name}, " \
            "{enum_id}, {null_allowed}, {null_display}, {min_val}, " \
            "{max_val}, {regex}, {parent_id}, {length}, " \
            "{scale})".format(
            id_=id_, part_type_id=part_type_id, seq_num=seq_num,
            data_type=sql_str_praram(data_type), unit=sql_str_praram(unit),
            tolerance=tolerance, name=sql_str_praram(name),
            json_name=sql_str_praram(json_name),
            idx_name=sql_str_praram(idx_name), enum_id=enum_id,
            null_allowed=null_allowed,
            null_display=sql_str_praram(null_display), min_val=min_val,
            max_val=max_val, regex=sql_str_praram(regex),
            parent_id=parent_id, length=length, scale=scale)
        sql += values
        add_comma = True
    if add_comma:
        sql += ";\n"
    else:
        sql = ""
    return sql


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
argparser.add_argument("--in-extra-data", required=False,
                       default=os.path.join(os.getcwd(), "in",
                                            "extra_data.json"),
                       help="File in JSON format with extra info.")
args = argparser.parse_args()

with open(args.in_extra_data) as fp: extra_data = json.load(fp)

(obsolete_part_type, part_types, crit_dim_attributes,
    crit_dim_attributes_idx_by_id) = load_input_data(args)

for cd in crit_dim_attributes:
    seq_critdim += 1
    cd[KEY_CDDBID] = seq_critdim

# print(json.dumps(part_types, indent=2))

print(createTableCritDimSql())

# print("delete from part_types where id >= {};".format(seq_part_type))
#

if obsolete_part_type:
    print(format_warn("Found obsolete part types."))
    for ptm in obsolete_part_type:
        print("-- delete from part_type where id={0:d}; -- {1:s}".format(
            ptm["id"], ptm["name"]))
        ed = get_part_type_mapping(extra_data, ptm["value"])
        if ed["exists"] == True:
            print("-- drop table {};".format(ed["table"]))
    print()

for pt in part_types:
    pt_id = pt.get(KEY_PT_ID)
    if pt_id is None:
        # Register this new part type.
        print("insert into part_type(id, name, magento_attribute_set, value) "
              "values({id_}, '{name}', '{mas}', '{value}');".format(
                  id_=seq_part_type, name=pt["name"], mas=pt["name_value"],
                  value=pt["name_value"]))
        pt_id = seq_part_type
        pt[KEY_PT_ID] = pt_id
        seq_part_type += 1
    cda_ = pt.get(KEY_CDA)
    if not cda_:
        print(format_warn("Part type [{0:d}] - {1:s} has no defined "
                          "critical dimensions.".format(pt[KEY_PT_ID],
                                                        pt["name"])))
        if cda_ is None:
            cda_ = list()
    ed = get_part_type_mapping(extra_data, pt["name_value"])
    entity_table_exists = ed["exists"]
    if entity_table_exists:
        sql = generate_alters(ed, cda_)
    else:
        sql = generate_create_table(ed, cda_)

    print(sql)

    sql = register_crit_dim(pt_id, ed["table"], cda_, crit_dim_attributes,
        crit_dim_attributes_idx_by_id)

    print(sql)

