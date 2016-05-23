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
import shutil
import sys


KEY_PT_ID = "_pt_id"
KEY_PT_META = "_pt_meta"
KEY_CDA = "_cda"
KEY_LIST = "_list"
KEY_CDDBID = "_dbid"

KEY_COLNAME = "_col_name"
KEY_TYPES = "_types"


IDX_NAME_MAXLEN = 32


YESNOENUM_ID = 1

REGEX_NONALPHANUM = re.compile(r'\W')

seq_part_type = 30
seq_critdim = 1
seq_critdim_enum = YESNOENUM_ID + 1
seq_critdim_enum_val = 100

Warn = collections.namedtuple("Warn", ["id", "message"])

WARN_BAD_SEQ = Warn(1, "A critical dimension [{cd_id}] - '{cd_name}' has "
                    "nullable field 'sequence'.")
WARN_ENUM_NOT_DEF = Warn(2, "Enumeration for the critical dimension "
                         "[{id_}] - '{name}' is not defined.")
WARN_INVALID_REF = Warn(3, "A critical dimension [{cd_id}] - '{cd_name}' "
                        "that belongs to a part type [{cd_ptid}] "
                        "references on a parent critical dimension "
                        "[{pcd_id}] - '{pcd_name}' that belongs "
                        "to other part type [{pcd_ptid}]. Skipped.")
WARN_OBSOLETE_PART_TYPES = Warn(4, "Found obsolete part types.")
WARN_NO_CRITDIMS = Warn(5, "Part type [{ptid:d}] - '{name:s}' has no defined "
                        "critical dimensions.")

Types = collections.namedtuple("Types", ["field_type", "sql_type",
                                         "data_type", "jpa_type"])

JpaType = collections.namedtuple("JpaType", ["java_type", "is_enum"])

def format_warn(w, **kwargs):
    """Return a formatted string with warning."""
    return "-- WARN[{warn_id}]: {warn_msg}".format(warn_id=w.id,
                                                   warn_msg=w.message.format(
                                                       **kwargs))


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
    if retval[0].isdigit():
        retval = "the" + retval
    return retval


def normalizeName(name):
    """
    Transform input string as:
        * convert to lowercast;
        * remove volwes.
    """
    retval = ""
    for c in name.lower():
        if c not in "aeiou_":
            retval += c
    return retval


def name2MagentoAttributeSet(name):
    """
    Convert a string (table name) to a value for
    a field 'magento_attribute_set' in a table path_type.
    This field contains a string where:
        * first character in a word is capital
    """
    retval = ""
    for w in name.split("_"):
        retval += (" " + w.capitalize())
    return retval


def name2idxName(table_name, json_name, idxNames):
    """
    Convert a string to a valid fied identifier in ElasticSearch index.
    The name is limited by 32 characters. If a generated name is longer
    then a new attempt is done in oder to satisfy to the constraint.
    If the second generation attemp failed then exception is thrown.

    In the end the subroutine checked that generated name is unique.

    Example: ('backplate', 'diaA') -> 'bckpltDiaA'
    """
    pref = normalizeName(table_name)
    suff = json_name.capitalize()
    nname = pref + suff
    if len(nname) > IDX_NAME_MAXLEN:
        suff = normalizeName(suff).capitalize()
        nname = pref + suff
        if len(nname) > IDX_NAME_MAXLEN:
            raise ValueException("Internal error. "
                                 "Index name generation failed: "
                                 "table_name='{}', json_name='{}' => '{}'."
                                 .format(table_name, json_name, nname))
    if nname in idxNames:
        raise ValueException("Internal error. "
                             "Index name generation failed. "
                             ": The generated name is not unique: "
                             "table_name='{}', json_name='{}' => '{}'."
                             .format(table_name, json_name, nname))
    return nname


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
        jpa_type = JpaType(java_type = "Double", is_enum=False)
    elif field_type == "INTEGER":
        length = cd["length"]
        sql_type = "int({})".format(length)
        data_type = "INTEGER"
        jpa_type = JpaType(java_type = "Integer", is_enum=False)
    elif field_type == "LIST":
        sql_type = "int"
        data_type = "ENUMERATION"
        jpa_type = JpaType(java_type = "CriticalDimensionEnumVal",
                           is_enum=True)
    elif field_type == "LOGICAL":
        sql_type = "int"
        data_type = "ENUMERATION"
        jpa_type = JpaType(java_type = "CriticalDimensionEnumVal",
                           is_enum=True)
    elif field_type == "MEMO":
        length = cd["length"] or 4096
        sql_type = "varchar({})".format(length)
        data_type = "TEXT"
        jpa_type = JpaType(java_type = "String", is_enum=False)
    else:
        raise ValueError("Unsupported field type: {}".format(data_type))
    return Types(field_type=field_type, sql_type=sql_type,
                 data_type=data_type, jpa_type=jpa_type)


def cd2sqlreference(cd):
    """Convert critical dimension to SQL foreign key reference."""
    data_type = cd["field_type"]
    retval = ""
    if data_type == "LIST" or data_type == "LOGICAL":
        retval = "references crit_dim_enum_val(id) on delete set null " \
            "on update cascade"
    return retval


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
    idx_name        varchar({idx_name_len}) not null unique comment 'Name of
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
    -- unique key(part_type_id, seq_num),
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
""".format(idx_name_len=IDX_NAME_MAXLEN,yesnoenum_id=YESNOENUM_ID)


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


def register_crit_dim_enum(cd, table_name, col_name):
    global seq_critdim_enum, seq_critdim_enum_val
    lst = cd.get(KEY_LIST)
    if not lst:
        lst_selection = cd.get("list_selection")
        if lst_selection:
            # TODO: should be added a warning?
            lst = list()
            for itm in lst_selection.split(";"):
                lst.append(dict(list_name=itm))
        else:
            return (None, None)
    sql = ""
    seq_critdim_enum += 1
    id_ = seq_critdim_enum
    name = normalizeName(table_name) + col_name.capitalize() + "Enum"
    sql = "insert into crit_dim_enum(id, name) values({id_}, " \
        "'{name}');\n".format(id_=id_, name=name)
    if lst:
        sql += "insert into crit_dim_enum_val(id, crit_dim_enum_id, val) " \
            "values\n"
        add_comma = False
        for itm in lst:
            if add_comma: sql += ",\n"
            sql += "({id2_}, {id_}, '{val}')".format(
                id2_=seq_critdim_enum_val, id_=id_, val=itm["list_name"])
            seq_critdim_enum_val += 1
            add_comma = True
        if add_comma: sql += ";\n"
    return (id_, sql)


def register_crit_dim(part_type_id, table_name, cda_, crit_dim_attributes,
                      crit_dim_attributes_idx_by_id):
    global alter_file
    sql = "insert into crit_dim(id, part_type_id, seq_num, data_type, " \
        "unit, "\
        "tolerance, name, json_name, idx_name, " \
        "enum_id, null_allowed, null_display, " \
        "min_val, "\
        "max_val, regex, parent_id, length, " \
        "scale) "\
        "values\n"
    add_comma = False
    registered_enums = ""
    idx_names = set()
    for cd in cda_:
        types = cd[KEY_TYPES]
        id_ = crit_dim_attributes_idx_by_id[cd["id"]][KEY_CDDBID]
        seq_num = cd["sequence"]
        if seq_num is None:
            seq_num = "null"
        print(format_warn(WARN_BAD_SEQ, cd_id=cd["id"], cd_name=cd["name"]),
              file=alter_file)
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
        idx_name = name2idxName(table_name, json_name, idx_names)
        idx_names.add(idx_name)
        if types.field_type == "BOOLEAN":
            enum_id = YESNOENUM_ID
        elif types.data_type == "ENUMERATION":
            (eid, snippet) = register_crit_dim_enum(cd, table_name, json_name)
            if eid is None:
                print(format_warn(WARN_ENUM_NOT_DEF, id_=id_, name=name),
                      file=alter_file)
                enum_id = "null"
            else:
                enum_id = eid
                registered_enums += snippet
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
            pcd = crit_dim_attributes_idx_by_id[parent_attribute]
            pcd_ptid = pcd["part_type_id"]
            cd_ptid = cd["part_type_id"]
            if pcd_ptid == cd_ptid:
                parent_id = pcd[KEY_CDDBID]
            else:
                print(format_warn(WARN_INVALID_REF, cd_id=cd["id"],
                                  cd_name=cd["name"], cd_ptid=cd_ptid,
                                  pcd_id=pcd["id"], pcd_name=pcd["name"],
                                  pcd_ptid=pcd_ptid), file=alter_file)
                continue
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
    if registered_enums: sql = registered_enums + sql
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
argparser.add_argument("--out-dir", required=False,
                       default=os.path.join(os.getcwd(), "out"),
                       help="Output directory.")
args = argparser.parse_args()

if os.path.exists(args.out_dir):
    shutil.rmtree(args.out_dir)
os.mkdir(args.out_dir)

with open(args.in_extra_data) as fp: extra_data = json.load(fp)

filename_alter = os.path.join(args.out_dir, "alter.sql")
with open(filename_alter, "w", encoding="utf-8") as alter_file:

    (obsolete_part_type, part_types, crit_dim_attributes,
        crit_dim_attributes_idx_by_id) = load_input_data(args)

    for cd in crit_dim_attributes:
        seq_critdim += 1
        cd[KEY_CDDBID] = seq_critdim

    print(createTableCritDimSql(), file=alter_file)

    if obsolete_part_type:
        print(format_warn(WARN_OBSOLETE_PART_TYPES), file=alter_file)
        for ptm in obsolete_part_type:
            print("-- delete from part_type where id={0:d}; -- {1:s}".format(
                ptm["id"], ptm["name"]), file=alter_file)
            ed = get_part_type_mapping(extra_data, ptm["value"])
            if ed["exists"] == True:
                print("-- drop table {};".format(ed["table"]), file=alter_file)
        print(file=alter_file)

    for pt in part_types:
        pt_id = pt.get(KEY_PT_ID)
        if pt_id is None:
            # Register this new part type.
            mas=name2MagentoAttributeSet(pt["name_value"])
            print("insert into part_type(id, name, magento_attribute_set, "
                  "value) values({id_}, '{name}', '{mas}', '{value}');"
                  .format(
                    id_=seq_part_type, name=pt["name"], mas=mas,
                    value=pt["name_value"]), file=alter_file)
            pt_id = seq_part_type
            pt[KEY_PT_ID] = pt_id
            seq_part_type += 1
        cda_ = pt.get(KEY_CDA)
        if not cda_:
            print(format_warn(WARN_NO_CRITDIMS, ptid=pt[KEY_PT_ID],
                            name=pt["name"]), file=alter_file)
            if cda_ is None:
                cda_ = list()
                pt[KEY_CDA] = cda_
        ed = get_part_type_mapping(extra_data, pt["name_value"])
        entity_table_exists = ed["exists"]
        if entity_table_exists:
            sql = generate_alters(ed, cda_)
        else:
            sql = generate_create_table(ed, cda_)

        print(sql, file=alter_file)

        sql = register_crit_dim(pt_id, ed["table"], cda_, crit_dim_attributes,
            crit_dim_attributes_idx_by_id)

        print(sql, file=alter_file)


# Generate java code snippets.
for pt in part_types:
    cda_ = pt.get(KEY_CDA)
    if not cda_: continue
    ed = get_part_type_mapping(extra_data, pt["name_value"])
    table_name = ed["table"]
    class_name = ed["class"]
    snippet_file_name = os.path.join(args.out_dir,
                                     "{}.java".format(class_name))
    members_snippet = ""
    getters_setters_snippet = ""
    with open(snippet_file_name, "w", encoding="utf-8") as snippet_file:
        for cd in cda_:
            mem_name = cd[KEY_COLNAME]
            types = cd[KEY_TYPES]
            mem_type = types.jpa_type.java_type
            annotations = "    @JsonView(View.Summary.class)\n"
            annotations += "    @JsonProperty(\"{}\")\n".format(mem_name)
            if types.jpa_type.is_enum:
                annotations += ("    @ManyToOne(fetch = EAGER)\n"
                                "    @JoinColumn(name = \"{}\")\n"
                                .format(mem_name))
            else:
                annotations += "    @Column(name = \"{}\")\n".format(mem_name)
            members_snippet += annotations
            members_snippet += "    private {mem_type:s} {mem_name:s};\n\n" \
                .format(mem_type=mem_type, mem_name=mem_name)
            method_suff = mem_name.capitalize()
            getters_setters_snippet += "    public {mem_type:s} " \
                "get{method_suff:s}() {{\n" \
                "        return {mem_name:s};\n" \
                "    }}\n\n" \
                "    public void set{method_suff:s}({mem_type:s} " \
                "{mem_name:s}) {{\n" \
                "        this.{mem_name:s} = {mem_name:s};\n" \
                "    }}\n\n" \
                .format(mem_type=mem_type, mem_name=mem_name,
                        method_suff=method_suff)
        print("package com.turbointernational.metadata.domain.part.types;\n",
              file=snippet_file)
        print("import com.fasterxml.jackson.annotation.JsonProperty;",
              file=snippet_file)
        print("import com.fasterxml.jackson.annotation.JsonView;",
              file=snippet_file)
        print("import com.turbointernational.metadata.domain."
              "criticaldimension.CriticalDimensionEnumVal;", file=snippet_file)
        print("import com.turbointernational.metadata.domain.part.Part;",
              file=snippet_file)
        print("import com.turbointernational.metadata.web.View;\n",
              file=snippet_file)
        print("import javax.persistence.*;\n", file=snippet_file)
        print("import static javax.persistence.FetchType.EAGER;\n\n",
              file=snippet_file)
        print("/**\n * Created by dmytro.trunykov@zorallabs.com.\n */",
              file=snippet_file)
        print("@Entity", file=snippet_file)
        print("@Table(name = \"{}\")".format(table_name), file=snippet_file)
        print("@PrimaryKeyJoinColumn(name = \"part_id\")", file=snippet_file)
        print("public class {} extends Part {{".format(class_name),
              file=snippet_file)
        print("    //<editor-fold defaultstate=\"collapsed\" " \
              "desc=\"Properties: critical dimensions\">", file=snippet_file)
        print(members_snippet, file=snippet_file)
        print("    //</editor-fold>", file=snippet_file)
        print("", file=snippet_file)
        print("    //<editor-fold defaultstate=\"collapsed\" " \
              "desc=\"Getters and setters: critical dimensions\">",
              file=snippet_file)
        print(getters_setters_snippet, file=snippet_file)
        print("    //</editor-fold>", file=snippet_file)
        print("}", file=snippet_file)

