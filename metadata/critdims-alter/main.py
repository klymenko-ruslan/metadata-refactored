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

from enum import Enum

import argparse
import collections
import csv
import datetime
import json
import os
import re
import shutil
import sys


IDX_NAME_MAXLEN = 30

YESNOENUM_ID = 1

REGEX_NONALPHANUM = re.compile(r'\W')

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
WARN_CONFLICTED_PART_TYPE = Warn(6, "Part {p_id} [{part_num}] has "
                                 "different part type in the "
                                 "database ({db_pt_id}) and in "
                                 "the imported data ({imp_pt_id}).")


class PartTypeStatusEnum(Enum):

    """TODO."""

    obsolete = 1
    exist = 2
    new = 3

PartType = collections.namedtuple("PartType", "id, name, name_short, "
                                  "magento_attribute_set, value, status")

Types = collections.namedtuple("Types", "field_type, sql_type, "
                               "data_type, jpa_type")

JpaType = collections.namedtuple("JpaType", "java_type, is_enum")

ColumnMetaInfo = collections.namedtuple("ColumnMetaInfo", "col_name, types")

ImportValue = collections.namedtuple("ImportValue", "col_meta, value")

CurrentStateKey = collections.namedtuple("CurrentStateKey",
                                         "manfr_part_num, manfr_id")


class InputData:

    """
    Mapping between part type definitions and critical dimensions.

    Part types in the 'metadata' database and imported data have different IDs.
    They could be matched by a value in fields 'value'.
    """

    def __init__(self, in_part_types, in_part_types_metadata,
                 in_crit_dim_attributes, in_enumerations, in_enum_items,
                 in_extra_data, in_current_state):
        """
        Load input files and process them.

        The main issue in a processing of loaded fiels are dirrerent IDs
        which are used in input files exported from provided
        'MS Access' file and IDs for exist entities (part_type)
        in the 'metadata' database. Main logic below merges part types
        descriptors from these two sources and build a list of merged
        part types. Each item in the list contains main attributes of
        a part type (i.e. name, value, ID in the database) and also
        its status: is it new part type, exist one or obsolete.

        Also the constructor load descriptors for critical dimensions
        and groups them by part type ID to which them are blonged.

        Args:
            in_part_types (str): filename for 'in/part_type.json'.
            in_part_types_metadata (str): filename for
                                          'in/part_type_metadata.json'.
            crit_dim_attributes (str): filename for
                                       'in/crit_dim_attribute.json'.
            in_enumerations (str): filename for 'in/enumerations.json'.
            in_enum_items (str): filename for 'in/enum_items.json'.
            in_extra_data (str): filename for 'in/extra_data.json'.
        """
        with open(in_part_types) as fp:
            part_types_msaccess = json.load(fp)["part_type"]

        with open(in_part_types_metadata) as fp:
            part_types_metadata = json.load(fp)["part_type"]

        with open(in_crit_dim_attributes) as fp:
            crit_dim_attributes = json.load(fp)["crit_dim_attribute"]

        with open(in_enumerations) as fp:
            self.enumerations = json.load(fp)["enumeration"]

        with open(in_enum_items) as fp:
            self.enum_items = json.load(fp)["enum_item"]

        with open(in_extra_data) as fp:
            self.extra_data = json.load(fp)

        self.current_state_idx = dict()
        with open(in_current_state) as fp:
            current_state = csv.reader(fp, delimiter="\t")
            self.current_state_idx = {
                CurrentStateKey(cs[1], cs[2]): (cs[0], cs[3])
                for cs in current_state
            }
        # print("Debug: {}".format(self.current_state_idx))
        # CurrentStateKey(manfr_part_num='8-K-1140', part_type_id=48)
        # CurrentStateKey(manfr_part_num='7-E-2837', part_type_id='3'))
        # xxx = self.current_state_idx.get(
        #     CurrentStateKey(manfr_part_num='5-A-0293', part_type_id='11'))
        # print("xxx={}".format(xxx))

        # to search parents
        self.cda_by_id = {cd["id"]: cd for cd in crit_dim_attributes}

        self.part_types = list()

        ptmsa_idx_by_val = {
            ptmsa["name_value"]: ptmsa for ptmsa in part_types_msaccess
        }

        ptmd_idx_by_val = {
            ptmd["value"]: ptmd for ptmd in part_types_metadata
        }

        ptmsaid2ptid = dict()

        merged_ptmds = set()
        seq_pt_id = 30
        for ptmsa in part_types_msaccess:
            value = ptmsa["name_value"]
            ptmd = ptmd_idx_by_val.get(value)
            if ptmd is None:
                pt_id = seq_pt_id
                seq_pt_id += 1
                name = name2PartTypeName(ptmsa["name"])
                status = PartTypeStatusEnum.new
                mas = name2MagentoAttributeSet(ptmsa["name_value"])
            else:
                pt_id = ptmd["id"]
                name = ptmd["name"]
                status = PartTypeStatusEnum.exist
                mas = ptmd["magento_attribute_set"]
                merged_ptmds.add(pt_id)
            pt = PartType(id=pt_id, name=name,
                          name_short=ptmsa["name_short"],
                          magento_attribute_set=mas, value=value,
                          status=status)
            ptmsaid2ptid[ptmsa["id"]] = pt_id
            self.part_types.append(pt)

        for ptmd in part_types_metadata:
            pt_id = ptmd["id"]
            if pt_id not in merged_ptmds:
                pt = PartType(id=pt_id, name=ptmd["name"], name_short=None,
                              magento_attribute_set=ptmd[
                                  "magento_attribute_set"],
                              value=ptmd["value"],
                              status=PartTypeStatusEnum.obsolete)
                self.part_types.append(pt)

        self.pt_idx_by_id = {pt.id: pt for pt in self.part_types}

        # To have access for critical dimensions for a part type.
        self.cda_by_ptid = {pt.id: list() for pt in self.part_types}

        for cd in crit_dim_attributes:
            ptmsaid = cd["part_type_id"]
            # Translate internal ID to ID of merged part types.
            pt_id = ptmsaid2ptid[ptmsaid]
            cda = self.cda_by_ptid.get(pt_id)
            cda.append(cd)
            cda.sort(key=lambda x: x["sequence"]
                     if x["sequence"] is not None else 0)

    def getPartTypes(self):
        """Get merged part types."""
        return self.part_types

    def getPtById(self, id):
        """
        Get part type by ID as defined in import data.

        Args:
            id(int): 'id' as defined in self.part_types.

        Returns:
            None if part type not found.
        """
        return self.pt_idx_by_id.get(id, None)

#     def getPtByVal(self, val):
#         """
#         Get part type by 'value'.
#
#         Args:
#             val(str): 'name_value' from 'in/part_type.json' or 'value'
#                       from 'in/part_type_metadata.json'.
#
#         Returns:
#             None if part type not found.
#         """
#         return self.pt_idx_by_val.get(val, None)

    def getCdaByPt(self, pt):
        """
        Get a list of critical dimensions for part type.

        Args:
            pt (dict): part type record as defined 'in/part_type.json'.

        Returns:
            List of critical dimensions for the part type.
        """
        return self.cda_by_ptid[pt.id]

    def getCdaById(self, cd_id):
        """
        Get a critical dimension for ID.

        Args:
            cd_id (int): critica dimension ID.
        """
        return self.cda_by_id[cd_id]

    def getParentCda(self, cd):
        """
        Get a parent critical dimension for the dimension.

        Args:
            cd (dict): a critical dimension

        Returns:
            A parent critical dimension for the specified dimension or None.
        """
        pcd_id = cd["parent_attribute"]
        if pcd_id is not None:
            return self.getCdaById(pcd_id)
        else:
            return None

    def getEnumerations(self):
        """Get loaded 'in/enumerations.json'."""
        return self.enumerations

    def getEnumItems(self):
        """Get loaded 'in/enum_items.json'."""
        return self.enum_items

    def getObsoletePartTypes(self):
        """Get part types which are not used anymore."""
        for pt in self.part_types:
            if pt.status == PartTypeStatusEnum.obsolete:
                yield pt

    def getExtraDataForPt(self, pt):
        """
        Get 'part type mapping' in the 'extra_data' for the 'part type'.

        Args:
            pt (dict): a 'part type' dictionary

        Returns:
            A 'part type mapping' dictionary for the specified 'part type'.
        """
        return self.extra_data["part_type_mapping"][pt.value]


def format_warn(w, **kwargs):
    """Return a formatted string with warning."""
    return "-- WARN[{warn_id}]: {warn_msg}".format(warn_id=w.id,
                                                   warn_msg=w.message.format(
                                                       **kwargs))


def isAscii(s):
    """Test if a string contains ASCII only characters."""
    return len(s) == len(s.encode())


def def_sql_null(d, k):
    """
    Get a value for key 'k' in the dictionary 'd'.

    If the 'k' does not exist in the 'd' or the value is None
    then the function returns string "null".
    """
    if k not in d:
        return "null"
    retval = d[k]
    if retval is None:
        return "null"
    return retval


def sql_str_param(s):
    """Escape a string as SQL parameter."""
    if s is None:
        return "null"
    s = s.replace("'", "''")
    return "'" + s + "'"


def name2PartTypeName(name):
    """
    Convert a string to a part type for UI.

    Example: "bearing housing" => "Bearing Housing"
    """
    retval = ""
    for idx, w in enumerate(name.split()):
        if idx > 0:
            retval += " "
        retval += w.capitalize()
    return retval


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
    Transform input string.

    The transformation steps:
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
    Convert a string.

    Convert a string (table name) to a value for
    a field 'magento_attribute_set' in a table path_type.

    This field contains a string where:
        * first character in a word is capital
    """
    retval = ""
    for idx, w in enumerate(name.split("_")):
        if idx > 0:
            retval += " "
        retval += w.capitalize()
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
    Convert a sting.

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
        jpa_type = JpaType(java_type="Double", is_enum=False)
    elif field_type == "INTEGER":
        length = cd["length"]
        sql_type = "int({})".format(length)
        data_type = "INTEGER"
        jpa_type = JpaType(java_type="Integer", is_enum=False)
    elif field_type == "LIST":
        sql_type = "int"
        data_type = "ENUMERATION"
        jpa_type = JpaType(java_type="CriticalDimensionEnumVal",
                           is_enum=True)
    elif field_type == "LOGICAL":
        sql_type = "int"
        data_type = "ENUMERATION"
        jpa_type = JpaType(java_type="CriticalDimensionEnumVal",
                           is_enum=True)
    elif field_type == "MEMO":
        length = cd["length"] or 4096
        sql_type = "varchar({})".format(length)
        data_type = "TEXT"
        jpa_type = JpaType(java_type="String", is_enum=False)
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


def create_crit_dim_tables(alter_file, input_data):
    """SQL statements to create tables for critical dimensions."""
    print("""
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
    """.format(idx_name_len=IDX_NAME_MAXLEN,
               yesnoenum_id=YESNOENUM_ID), file=alter_file)

    for e in input_data.getEnumerations():
        print("insert into crit_dim_enum(id, name) "
              "values({enum_id}, '{enum_name}');"
              .format(enum_id=e["id"], enum_name=e["enum_text"]),
              file=alter_file)

    for ei in input_data.getEnumItems():
        print("insert into crit_dim_enum_val(id, crit_dim_enum_id, val) "
              "values({enum_itm_id}, {enum_id}, '{val}');"
              .format(enum_itm_id=ei["id"], enum_id=ei["enum_id"],
                      val=ei["enum_item_text"]),
              file=alter_file)


def delete_obsolete_part_types(alter_file, input_data):
    """TODO."""
    obsolete_part_types = input_data.getObsoletePartTypes()
    if obsolete_part_types:
        print(format_warn(WARN_OBSOLETE_PART_TYPES), file=alter_file)
        for pt in obsolete_part_types:
            print("-- delete from part_type where id={0:d}; -- {1:s}".format(
                pt.id, pt.name), file=alter_file)
            ed = input_data.getExtraDataForPt(pt)
            if ed["exists"] is True:
                print("-- drop table {};".format(ed["table"]), file=alter_file)
        print(file=alter_file)


def register_new_part_types(alter_file, input_data):
    """Insert new part types and critical dimensions descriptors."""
    ptid2columns_meta = dict()
    for pt in input_data.getPartTypes():
        if pt.status == PartTypeStatusEnum.new:
            print("insert into part_type(id, name, magento_attribute_set, "
                  "value) values({id_}, '{name}', '{mas}', '{value}');"
                  .format(id_=pt.id, name=pt.name,
                          mas=pt.magento_attribute_set, value=pt.value),
                  file=alter_file)
        pt_cda = input_data.getCdaByPt(pt)
        ed = input_data.getExtraDataForPt(pt)
        table_name = ed["table"]
        entity_table_exists = ed["exists"]
        if entity_table_exists:
            columns_meta, sql = generate_alters(table_name, pt_cda)
        else:
            columns_meta, sql = generate_create_table(table_name, pt_cda)
        ptid2columns_meta[pt.id] = columns_meta
        print(sql, file=alter_file)

        # Insert ciritical dimension descriptors to a table.
        register_crit_dim(alter_file, input_data, table_name, pt.id,
                          pt_cda, columns_meta)
    return ptid2columns_meta


def import_data(alter_file, input_data, ptid2columns_meta):
    """Populate tables."""
    for pt in input_data.getPartTypes():
        inserted = updated = conflicted = skipped = 0
        manfr_id = 11  # Turbo International
        pt_cda = input_data.getCdaByPt(pt)
        if pt.status != PartTypeStatusEnum.obsolete:
            datafile_name = os.path.join(args.in_data_dir,
                                         pt.name_short + ".tsv")
            columns_meta = ptid2columns_meta[pt.id]
            with open(datafile_name, "rt") as df:
                tsvin = csv.reader(df, delimiter='\t')
                for idx, row in enumerate(tsvin):
                    if idx == 0:
                        headers = row
                    else:
                        ed = input_data.getExtraDataForPt(pt)
                        table_name = ed["table"]
                        part_num = row[1]

                        # TODO: workaround
                        if part_num in ["9-Z-9999", "part"]:
                            skipped += 1
                            continue

                        # Range [2:] below skips 'id' and
                        # 'manufacturer part number'.
                        import_values = tsvrec2importval(pt_cda, columns_meta,
                                                         headers[2:], row[2:])
                        cs_key = CurrentStateKey(part_num, str(manfr_id))
                        obj = input_data.current_state_idx.get(cs_key)
                        if obj is None:
                            part_id, part_type_id = None, None
                        else:
                            part_id, part_type_id = obj
                        if part_id is None:
                            inserted += 1
                            sql = import_insert(None, part_num, manfr_id,
                                                pt.id, False, 1, table_name,
                                                import_values)
                        else:
                            if part_type_id != pt.id:
                                conflicted += 1
                                warn = format_warn(WARN_CONFLICTED_PART_TYPE,
                                                   p_id=part_id,
                                                   part_num=part_num,
                                                   db_pt_id=part_type_id,
                                                   imp_pt_id=pt.id)
                                print(warn, file=alter_file)
                                pt_db = input_data.getPtById(int(part_type_id))
                                ed2 = input_data.getExtraDataForPt(pt_db)
                                old_table_name = ed2["table"]
                                if old_table_name is not None:

                                    # if part_num == '5-A-0293':
                                    #     print("part_id: {}\npt_db: {}\n"
                                    #           "ed2: {}"
                                    #           .format(part_id, pt_db, ed2))
                                    #     print("table_name: {}\nold: {}"
                                    #           .format(table_name,
                                    #                   old_table_name))

                                    # Remove the part from obsolete place.
                                    sql = change_part_type(part_id, pt.id,
                                                           old_table_name)
                                    # And save it as a part with of the new
                                    # type.
                                    sql += import_insert(part_id, part_num,
                                                         manfr_id, pt.id,
                                                         False, 1, table_name,
                                                         import_values)

                            else:
                                updated += 1
                                sql = import_update(part_id, table_name,
                                                    import_values)
                        print(sql, file=alter_file)

        print("{}: inserted: {}, updated: {}, conflicts: {}, sipped: {}"
              .format(pt.name, inserted, updated, conflicted, skipped))


def generate_jpa_classes(alter_file, input_data, ptid2columns_meta):
    """Generate JPA java classes."""
    for pt in input_data.getPartTypes():
        # Generate java code snippets.
        ed = input_data.getExtraDataForPt(pt)
        table_name = ed["table"]
        class_name = ed["class"]
        columns_meta = ptid2columns_meta[pt.id]
        snippet_file_name = os.path.join(args.out_dir, "{}.java"
                                         .format(class_name))
        members_snippet = ""
        getters_setters_snippet = ""
        datetimestamp = datetime.datetime.now()
        with open(snippet_file_name, "w", encoding="utf-8") as snippet_file:
            pt_cda = input_data.getCdaByPt(pt)
            for cd in pt_cda:
                cd_id = cd["id"]
                col_meta = columns_meta[cd_id]
                mem_name = col_meta.col_name
                types = col_meta.types
                mem_type = types.jpa_type.java_type
                annotations = "    @JsonView(View.Summary.class)\n"
                annotations += "    @JsonProperty(\"{}\")\n".format(mem_name)
                if types.jpa_type.is_enum:
                    annotations += ("    @ManyToOne(fetch = LAZY)\n"
                                    "    @JoinColumn(name = \"{}\")\n"
                                    .format(mem_name))
                else:
                    annotations += ("    @Column(name = \"{}\")\n"
                                    .format(mem_name))
                members_snippet += annotations
                members_snippet += "    private {mem_type:s} {mem_name:s};\n\n" \
                    .format(mem_type=mem_type, mem_name=mem_name)
                method_suff = mem_name[0].upper() + mem_name[1:]
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
            print("package com.turbointernational.metadata.domain."
                  "part.types;\n", file=snippet_file)
            print("import com.fasterxml.jackson.annotation.JsonProperty;",
                  file=snippet_file)
            print("import com.fasterxml.jackson.annotation.JsonView;",
                  file=snippet_file)
            print("import com.turbointernational.metadata.domain."
                  "criticaldimension.CriticalDimensionEnumVal;",
                  file=snippet_file)
            print("import com.turbointernational.metadata.domain.part.Part;",
                  file=snippet_file)
            print("import com.turbointernational.metadata.web.View;\n",
                  file=snippet_file)
            print("import javax.persistence.*;\n", file=snippet_file)
            print("import static javax.persistence.FetchType.LAZY;\n\n",
                  file=snippet_file)
            print("/**\n * Created by dmytro.trunykov@zorallabs.com "
                  "on {}.\n */".format(datetimestamp),
                  file=snippet_file)
            print("@Entity", file=snippet_file)
            print("@Table(name = \"{}\")".format(table_name),
                  file=snippet_file)
            print("@PrimaryKeyJoinColumn(name = \"part_id\")",
                  file=snippet_file)
            print("public class {} extends Part {{\n".format(class_name),
                  file=snippet_file)
            print("    //<editor-fold defaultstate=\"collapsed\" "
                  "desc=\"Properties: critical dimensions\">\n",
                  file=snippet_file)
            print(members_snippet, file=snippet_file, end="")
            print("    //</editor-fold>", file=snippet_file)
            print("", file=snippet_file)
            print("    //<editor-fold defaultstate=\"collapsed\" "
                  "desc=\"Getters and setters: critical dimensions\">\n",
                  file=snippet_file)
            print(getters_setters_snippet, file=snippet_file, end="")
            print("    //</editor-fold>\n", file=snippet_file)
            print("}", file=snippet_file)


def cd2colmetainfo(cd):
    """Convert a critical dimension to column name and types."""
    col_name = name2jsonName(cd["name"])
    # Fix reserved names.
    if col_name == "id":
        col_name = "id_"
    elif col_name == "part_id":
        col_name = "part_id_"
    types = cd2types(cd)
    return ColumnMetaInfo(col_name=col_name, types=types)


def generate_alters(table_name, cda):
    """
    Generate ALTER SQL statements.

    The statements add critical dimensions to existing tables.
    """
    columns_meta = dict()
    columns = set()
    sql = ""
    for cd in cda:
        col_meta = cd2colmetainfo(cd)
        columns_meta[cd["id"]] = col_meta
        col_ref = cd2sqlreference(cd)
        if col_meta.col_name in columns:
            raise ValueError("In table '{}' found duplicated columns '{}'."
                             .format(table_name, col_name))
        else:
            columns.add(col_meta.col_name)
        sql += "alter table {} add column {} {}".format(
            table_name, col_meta.col_name, col_meta.types.sql_type)
        if col_ref:
            sql += ", add foreign key ({}) {}".format(col_meta.col_name,
                                                      col_ref)
        sql += ";\n"
    return columns_meta, sql


def generate_create_table(table_name, cda):
    """
    Generate CREATE TABLE SQL statements.

    The statements creates tables for new part types.
    """
    columns_meta = dict()
    sql = "create table {} (\n" \
        "\tpart_id bigint(20) not null references part (id),\n" \
        "\tkey part_id (part_id)".format(table_name)
    columns = set()
    for cd in cda:
        sql += ",\n"
        col_meta = cd2colmetainfo(cd)
        columns_meta[cd["id"]] = col_meta
        if col_meta.col_name in columns:
            raise ValueError("In table '{}' found duplicated columns '{}'."
                             .format(table_name, col_meta.col_name))
        else:
            columns.add(col_meta.col_name)
        col_ref = cd2sqlreference(cd)
        sql += "\t{} {} {}".format(col_meta.col_name,
                                   col_meta.types.sql_type, col_ref)
    sql += "\n) engine=innodb default charset=utf8;"
    return columns_meta, sql


def register_crit_dim(alter_file, input_data, table_name, pt_id, pt_cda,
                      columns_meta):
    """Generate INSERT statements to register a new critical dimension."""
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
    for cd in pt_cda:
        cd_id = cd["id"]
        col_meta = columns_meta[cd_id]
        types = col_meta.types
        seq_num = cd["sequence"]
        if seq_num is None:
            seq_num = "null"
        print(format_warn(WARN_BAD_SEQ, cd_id=cd_id, cd_name=cd["name"]),
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
        json_name = col_meta.col_name
        idx_name = name2idxName(table_name, json_name, idx_names)
        idx_names.add(idx_name)
        if types.field_type == "BOOLEAN":
            enum_id = YESNOENUM_ID
        elif types.data_type == "ENUMERATION":
            enum_id = cd["enumeration_id"]
            if enum_id is None:
                enum_id = "null"
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

        pcd = input_data.getParentCda(cd)
        if pcd is None:
            parent_id = "null"
        else:
            pcd_ptid = pcd["part_type_id"]
            cd_ptid = cd["part_type_id"]
            if pcd_ptid == cd_ptid:
                parent_id = pcd["id"]
            else:
                print(format_warn(WARN_INVALID_REF, cd_id=cd_id,
                                  cd_name=cd["name"], cd_ptid=cd_ptid,
                                  pcd_id=pcd["id"], pcd_name=pcd["name"],
                                  pcd_ptid=pcd_ptid), file=alter_file)
                continue
        length = def_sql_null(cd, "length")
        scale = def_sql_null(cd, "scale")
        if add_comma:
            sql += ",\n"
        values = "({cd_id}, {part_type_id}, {seq_num}, {data_type}, {unit}, " \
            "{tolerance}, {name}, {json_name}, {idx_name}, " \
            "{enum_id}, {null_allowed}, {null_display}, {min_val}, " \
            "{max_val}, {regex}, {parent_id}, {length}, " \
            "{scale})".format(
                cd_id=cd_id, part_type_id=pt_id, seq_num=seq_num,
                data_type=sql_str_param(data_type), unit=sql_str_param(unit),
                tolerance=tolerance, name=sql_str_param(name),
                json_name=sql_str_param(json_name),
                idx_name=sql_str_param(idx_name), enum_id=enum_id,
                null_allowed=null_allowed,
                null_display=sql_str_param(null_display), min_val=min_val,
                max_val=max_val, regex=sql_str_param(regex),
                parent_id=parent_id, length=length, scale=scale)
        sql += values
        add_comma = True
    if add_comma:
        sql += ";\n"
    else:
        sql = ""
    if registered_enums:
        sql = registered_enums + sql
    print(sql, file=alter_file)


def tsvrec2importval(pt_cda, columns_meta, headers, row):
    """
    Covert row - array of values, to a dict.

    In the dict keys are defined by elements in the 'headers' array
    and values are correspondent (with the same index) values
    in the 'row' array.
    """
    retval = list()
    cda_idx_by_fieldname = {"cd_" + cd["name_clean"]: cd for cd in pt_cda}
    for idx, header in enumerate(headers):
        cd = cda_idx_by_fieldname[header]
        cd_id = cd["id"]
        value = row[idx]
        col_meta = columns_meta[cd_id]
        if col_meta.types.data_type == "ENUMERATION":
            value = None
        retval.append(ImportValue(col_meta, value))
    return retval


def import_insert(part_id, part_number, manfr_id, part_type_id, inactive,
                  version, table_name, import_values):
    """Generate INSERT SQL statements to register a new part."""
    retval = ""
    if part_id is None:
        retval += ("insert into part(manfr_part_num, manfr_id, "
                   "part_type_id, inactive, version) "
                   "values('{}', {}, {}, {}, {});\n".format(part_number,
                                                            manfr_id,
                                                            part_type_id,
                                                            inactive,
                                                            version))
    retval += ("insert into " + table_name + "(part_id")
    for iv in import_values:
        retval += (", " + iv.col_meta.col_name)
    retval += ") values("
    if part_id is None:
        retval += "last_insert_id()"
    else:
        retval += str(part_id)
    for iv in import_values:
        retval += (", " + sql_str_param(iv.value))
    retval += ");\n"
    return retval


def import_update(part_id, table_name, import_values):
    """Generate UPDATE SQL statements to update a part."""
    retval = ""
    if import_values:
        retval += ("update " + table_name + " set ")
        for idx, iv in enumerate(import_values):
            if idx > 0:
                retval += ", "
            retval += (iv.col_meta.col_name + "=" + sql_str_param(iv.value))
        retval += "where part_id=" + part_id + ";\n"
    return retval


def change_part_type(part_id, new_part_type_id, old_table_name):
    """Delete record about part from a specific table and change its type."""
    retval = ""
    if old_table_name == "journal_bearing":
        retval += ("delete from standard_journal_bearing "
                   "where standard_part_id={part_id} "
                   "or oversized_part_id={part_id};\n".format(part_id=part_id))
    retval += ("delete from {old_table_name} where part_id={part_id};\n"
               "update part set part_type_id={new_part_type_id} "
               "where id={part_id};\n"
               .format(old_table_name=old_table_name,
                       new_part_type_id=new_part_type_id, part_id=part_id))
    return retval


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
argparser.add_argument("--in-enumerations", required=False,
                       default=os.path.join(os.getcwd(), "in",
                                            "enumerations.json"),
                       help="File in JSON format with exported data from the "
                       "table 'enumeration'.")
argparser.add_argument("--in-enum-items", required=False,
                       default=os.path.join(os.getcwd(), "in",
                                            "enum_items.json"),
                       help="File in JSON format with exported data from the "
                       "table 'enum_item'.")
argparser.add_argument("--in-extra-data", required=False,
                       default=os.path.join(os.getcwd(), "in",
                                            "extra_data.json"),
                       help="File in JSON format with extra info.")
argparser.add_argument("--in-current-state", required=False,
                       default=os.path.join(os.getcwd(), "in",
                                            "current_state.tsv"),
                       help="File in TSV format with info about current "
                            "exist parts.")
argparser.add_argument("--in-data-dir", required=False,
                       default=os.path.join(os.getcwd(), "in",
                                            "data"),
                       help="Directory with *.tsv files to import.")
argparser.add_argument("--out-dir", required=False,
                       default=os.path.join(os.getcwd(), "out"),
                       help="Output directory.")
args = argparser.parse_args()

if os.path.exists(args.out_dir):
    shutil.rmtree(args.out_dir)
os.mkdir(args.out_dir)


filename_alter = os.path.join(args.out_dir, "alter.sql")
with open(filename_alter, "w", encoding="utf-8") as alter_file:

    print("""
-- Cear database.
-- delete from bom_alt_item;
-- delete from bom;
-- delete from cartridge;
-- delete from interchange_item;
-- delete from part_turbo_type;
-- delete from interchange_header;
-- delete from product_image;
-- delete from turbo_car_model_engine_year;
-- delete from turbo;
-- delete from kit;
-- delete from turbine_wheel;
-- delete from compressor_wheel;
-- delete from bearing_housing;
-- delete from backplate;
-- delete from heatshield;
-- delete from nozzle_ring;
-- delete from gasket;
-- delete from bearing_spacer;
-- delete from standard_journal_bearing;
-- delete from journal_bearing;
-- delete from piston_ring;
-- delete from part;

alter table part add column legend_img_filename varchar(255);
alter table part_type add column legend_img_filename varchar(255);

-- drop view test_vmagmi_service_kits;
-- drop view vmagmi_service_kits;
-- drop view vpart_turbotype_kits;
-- drop view vwhere_used;

-- Change 'magento_attribute_set' (a current value is 'Backplate') for obsolete
-- part type 'Backplate / Sealplate' to avoid conflict with
-- a new part type 'Backplate'.
update part_type set magento_attribute_set='Backplate or Sealplate'
where id=14;
alter table backplate rename backplate_sealplate;
-- The same change for 'Heatshield / Shroud':
-- 'Heatshield' -> 'Heatshield or Shroud'
update part_type set magento_attribute_set='Heatshield or Shroud'
where id=15;
alter table heatshield rename heatshield_shroud;

alter table bearing_housing drop column cool_type_id;
alter table bearing_housing drop column oil_inlet;
alter table bearing_housing drop column oil_outlet;
alter table bearing_housing drop column oil;
alter table bearing_housing drop column outlet_flange_holes;

alter table bearing_housing drop column water_ports;
alter table bearing_housing drop column design_features;
alter table bearing_housing drop column bearing_type;

alter table compressor_wheel drop column inducer_oa;
alter table compressor_wheel drop column tip_height_b;
alter table compressor_wheel drop column exducer_oc;
alter table compressor_wheel drop column hub_length_d;
alter table compressor_wheel drop column bore_oe;
alter table compressor_wheel drop column trim_no_blades;
alter table compressor_wheel drop column application;

alter table gasket drop foreign key gasket_ibfk_2;
alter table gasket drop column gasket_type_id;

alter table heatshield_shroud drop column overall_diameter;
alter table heatshield_shroud drop column inside_diameter;
alter table heatshield_shroud drop column inducer_diameter;
alter table heatshield_shroud drop column notes;

alter table journal_bearing drop column outside_dim_min;
alter table journal_bearing drop column outside_dim_max;
alter table journal_bearing drop column inside_dim_min;
alter table journal_bearing drop column inside_dim_max;
alter table journal_bearing drop column width;

alter table piston_ring drop column outside_dim_min;
alter table piston_ring drop column outside_dim_max;
alter table piston_ring drop column width_min;
alter table piston_ring drop column width_max;
alter table piston_ring drop column i_gap_min;
alter table piston_ring drop column i_gap_max;

alter table turbine_wheel drop column exduce_oa;
alter table turbine_wheel drop column tip_height_b;
alter table turbine_wheel drop column inducer_oc;
alter table turbine_wheel drop column journal_od;
alter table turbine_wheel drop column stem_oe;
alter table turbine_wheel drop column trim_no_blades;
alter table turbine_wheel drop column shaft_thread_f;

alter table turbo drop foreign key turbo_ibfk_2;
alter table turbo drop column cool_type_id;
-- alter table turbo drop foreign key turbo_ibfk_3;
-- alter table turbo drop column turbo_model_id;

alter table kit drop foreign key kit_ibfk_1;
alter table kit drop column kit_type_id;

alter table bearing_spacer drop column outside_dim_min;
alter table bearing_spacer drop column outside_dim_max;
alter table bearing_spacer drop column inside_dim_min;
alter table bearing_spacer drop column inside_dim_max;

alter table backplate_sealplate drop column seal_type_id;
alter table backplate_sealplate drop column style_compressor_wheel;
alter table backplate_sealplate drop column seal_type;
alter table backplate_sealplate drop column overall_diameter;
alter table backplate_sealplate drop column compressor_wheel_diameter;
alter table backplate_sealplate drop column piston_ring_diameter;
alter table backplate_sealplate drop column compressor_housing_diameter;
alter table backplate_sealplate drop column notes;
alter table backplate_sealplate drop column secondary_diameter;
alter table backplate_sealplate drop column overall_height;

drop table gasket_type;
drop table seal_type;
-- drop table turbo_model;
drop table cool_type;
drop table part_turbo;
drop table kit_type;
        """, file=alter_file)

    input_data = InputData(args.in_part_type, args.in_part_type_metadata,
                           args.in_crit_dim_attribute, args.in_enumerations,
                           args.in_enum_items, args.in_extra_data,
                           args.in_current_state)
    create_crit_dim_tables(alter_file, input_data)
    delete_obsolete_part_types(alter_file, input_data)
    ptid2columns_meta = register_new_part_types(alter_file, input_data)
    import_data(alter_file, input_data, ptid2columns_meta)
    generate_jpa_classes(alter_file, input_data, ptid2columns_meta)
