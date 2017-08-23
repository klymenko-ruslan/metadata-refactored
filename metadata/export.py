#!/usr/bin/env python3.4
# vim: set fileencoding=utf-8 :
# vim: set tabstop=8 expandtab shiftwidth=4 softtabstop=4 :

"""Utility to export 'metadata' MySql database to a JSON."""

import argparse
# import json
import simplejson as json
import mysql.connector
from collections import namedtuple
from tqdm import tqdm

parser = argparse.ArgumentParser(description='Utility to export some entities'
                                 'of the \'metadata\' MySql database to '
                                 'a JSON.')
parser.add_argument('--db-host', default='localhost',
                    help='Host with a running instance of MySql server.')
parser.add_argument('--db-port', default=3306, type=int,
                    help='A port for the instntance of the MySql server.')
parser.add_argument('--db-name', default='metadata',
                    help='Database name (\'metaserver\') to export.')
parser.add_argument('--db-username', default='metaserver',
                    help='Username for the database \'metadata\'.')
parser.add_argument('--db-password', default='metaserver',
                    help='Password for the user \'metaserver\'')
parser.add_argument('--out', default='metadata_arangodb.json',
                    help='Filename for output of a result.')
args = parser.parse_args()

EntityMeta = namedtuple('EntityMeta', ['tablename', 'extra_columns'])


ENTITIES_DESCRIPTORS = {
    1: EntityMeta('turbo', 'turbo_model_id, cool_type_id, gasket_kit_id'),
    2: EntityMeta('cartridge', None),
    3: EntityMeta('kit', 'kit_type_id'),
    4: EntityMeta('piston_ring', None),
    5: EntityMeta('journal_bearing', None),
    6: EntityMeta('gasket', None),
    8: EntityMeta('fast_wearing_component', None),
    9: EntityMeta('major_component', None),
    10: EntityMeta('minor_component', None),
    11: EntityMeta('compressor_wheel', None),
    12: EntityMeta('turbine_wheel', None),
    13: EntityMeta('bearing_housing', None),
    14: EntityMeta('backplate_sealplate', None),
    15: EntityMeta('heatshield', None),
    16: EntityMeta('nozzle_ring', None),
    17: EntityMeta('o_ring', None),
    18: EntityMeta('oil_deflector', None),
    19: EntityMeta('clamp', None),
    20: EntityMeta('thrust_part', None),            # it has invalid value
    21: EntityMeta('misc_minor_component', None),   # it has invalid value
    30: EntityMeta('actuator', None),
    31: EntityMeta('compressor_cover', None),
    32: EntityMeta('plug', None),
    33: EntityMeta('turbine_housing', None),
    34: EntityMeta('backplate', None),
    35: EntityMeta('bolt_screw', None),
    36: EntityMeta('fitting', None),
    37: EntityMeta('journal_bearing_spacer', None),
    38: EntityMeta('nut', None),
    39: EntityMeta('pin', None),
    40: EntityMeta('retaining_ring', None),
    41: EntityMeta('seal_plate', None),
    42: EntityMeta('spring', None),
    43: EntityMeta('thrust_bearing', None),
    44: EntityMeta('thrust_collar', None),
    45: EntityMeta('thrust_spacer', None),
    46: EntityMeta('thrust_washer', None),
    47: EntityMeta('washer', None),
    48: EntityMeta('carbon_seal', None),
    49: EntityMeta('gasket_kit', None),
    50: EntityMeta('misc', None),
    51: EntityMeta('p', None),
    52: EntityMeta('shroud', None)
}


def normalize_crit_dim_col(colname):
    """
    Normalize column name of a critical dimension.

    Some columnt in critical diminsions can have the same name as in
    a main table of part (e.g. column 'weight' has in the table 'part'
    and in some critical dimensions). This function conver such columns
    to a form 'e.<colname> as cd_<colname>'.
    """
    if colname in ['weight']:
        colname = 'e.{colname} as cd_{colname}'.format(colname=colname)
    return colname


def parttype2query(ptid):
    """Return SQL query to extract a part entity for specified part type."""
    em = ENTITIES_DESCRIPTORS.get(ptid)
    retval = ('select p.id as id, p.manfr_part_num as manfr_part_num, '
              'p.manfr_id as manfr_id, p.part_type_id as part_type_id, '
              'p.name as name, p.description as description, '
              'p.inactive as inactive, '
              'p.legend_img_filename as legend_img_filename, '
              'p.dim_length as dim_length, p.dim_height as dim_height, '
              'p.weight as weight')
    if em.extra_columns is not None:
        retval = retval + ', ' + em.extra_columns
    crit_dims = crit_dims_map.get(ptid, None)
    if crit_dims is not None:
        retval = (retval + ', ' +
                  ', '.join(map(lambda cd:
                                normalize_crit_dim_col(cd['json_name']),
                                crit_dims)))
    retval = (retval + ' from part p left outer join ' + em.tablename +
              ' e on p.id=e.part_id where p.id=%(id)s')
    return retval


def normalize_manfrid_in_tt(tt):
    """Replace key manfr_id in a turbo_type record by manufacturer."""
    manfr = mnfr_map[tt.pop('manfr_id')]
    tt['manfr'] = manfr
    return tt


def normalize_ttid_in_tm(tm):
    """Replace key turbo_type_id in a turbo_model record by turbo_type."""
    ttid = tm.pop('turbo_type_id')
    if ttid is not None:
        tt = turbotypes_map[ttid]
    else:
        tt = None
    tm['turbo_type'] = tt
    return tm


def normalize_part(p):
    """Replace manfr_id and part_type_id by objects."""
    manfr_id = p.pop('manfr_id')
    m = mnfr_map.get(manfr_id)
    p['manufacturer'] = m
    ptid = p.pop('part_type_id')
    pt = parttypes_map.get(ptid)
    p['part_type'] = pt
    return p


def load_part_short(cur, pid):
    """
    Load a part.

    The function loads some main part attributes only.
    """
    cur.execute('select id, manfr_part_num, manfr_id, part_type_id, name '
                'from part where id=%(id)s', {'id': pid})
    p = cur.fetchone()
    normalize_part(p)
    return p


def load_bom_alternatives(cur, bomid):
    """Load BOM alternatives for the specified BOM."""
    retval = []
    cur.execute('select bom_alt_header_id, part_id from bom_alt_item '
                'where bom_id=%(bomid)s', {'bomid': bomid})
    retval = [{'bom_alt_hdr_id': row['bom_alt_header_id'],
               'part_id': row['part_id']} for row in cur.fetchall()]
    return retval


def load_boms(cur, cur1, pid):
    """Load parts's BOMs."""
    retval = []
    cur.execute('select id, child_part_id, quantity from bom '
                'where parent_part_id=%(ppid)s', {'ppid': pid})
    for bom in cur.fetchall():
        chldpid = bom.pop('child_part_id')
        chldp = load_part_short(cur, chldpid)
        bom['child'] = chldp
        alternatives = load_bom_alternatives(cur1, bom['id'])
        bom['alternatives'] = alternatives
        retval.append(bom)
    return retval


def load_interchanges(cur, pid):
    """Load interchanges for a part."""
    retval = {}
    cur.execute('select interchange_header_id from interchange_item '
                'where part_id=%(pid)s', {'pid': pid})
    row = cur.fetchone()
    if row is not None:
        hid = row['interchange_header_id']
        retval['header_id'] = hid
        cur.execute('select part_id from interchange_item '
                    'where part_id!=%(pid)s and interchange_header_id=%(hid)s',
                    {'pid': pid, 'hid': hid})
        retval['members'] = [row['part_id'] for row in cur.fetchall()]
    return retval


def load_whereused(cur, pid):
    """Load 'where used' for a part."""
    cur.execute("""
                select
                    p.id, p.manfr_part_num, pt.name, m.name, ba.distance,
                    ba.type
                from
                    part as p join manfr m on m.id = p.manfr_id
                    join part_type pt on pt.id = p.part_type_id
                    join (
                        select distinct
                            part_id, ancestor_part_id, distance, type
                        from vbom_ancestor
                        where part_id=%(pid)s and distance > 0
                    ) as ba on ba.ancestor_part_id = p.id
                order by ba.distance, ba.type, p.manfr_part_num
                """, {'pid': pid})
    return cur.fetchall()


try:
    cnx = mysql.connector.connect(host=args.db_host, port=args.db_port,
                                  database=args.db_name,
                                  user=args.db_username,
                                  password=args.db_password)
    cursor = cnx.cursor(dictionary=True, buffered=True)
    cursor2 = cnx.cursor(dictionary=True, buffered=True)
    cursor3 = cnx.cursor(dictionary=True, buffered=True)
    # Map 'manufacturer_id' => 'manufacturer'.
    print('Load manufactureres...')
    cursor.execute('select id, name, manfr_type_id, '
                   'parent_manfr_id, not_external '
                   'from manfr')
    mnfr_map = {m['id']: m for m in cursor.fetchall()}
    # Map 'part_type_id' => 'part_type'.
    print('Load part types...')
    cursor.execute('select id, name from part_type')
    parttypes_map = {pt['id']: pt for pt in cursor.fetchall()}
    # Map 'turbo_type_id' => 'turbo_type'.
    print('Load turbo types...')
    cursor.execute('select id, name, manfr_id from turbo_type')
    turbotypes_map = {tt['id']: tt for tt in map(normalize_manfrid_in_tt,
                                                 cursor.fetchall())}
    # Map 'turbo_model_id' => 'turbo_model'.
    print('Load turbo models...')
    cursor.execute('select id, name, turbo_type_id from turbo_model')
    turbomodels_map = {tm['id']: tm for tm in map(normalize_ttid_in_tm,
                                                  cursor.fetchall())}
    # Map 'cool_type_id' => 'cool_type'.
    print('Load cool types...')
    cursor.execute('select id, name from cool_type')
    cooltypes_map = {ct['id']: ct for ct in cursor.fetchall()}
    # Map 'part_type_id' => 'list of critical_dimension for the part type'.
    print('Load critical dimenstions...')
    cursor.execute('select part_type_id, json_name from crit_dim')
    crit_dims_map = {}
    for cd in cursor.fetchall():
        ptid = cd['part_type_id']
        ptcd = crit_dims_map.get(ptid, None)
        if ptcd is None:
            ptcd = []
            crit_dims_map[ptid] = ptcd
        ptcd.append(cd)
    # Export parts.
    print('Export parts...')
    cursor.execute('select id, part_type_id from part order by id')
    result = []
    for rec in tqdm(cursor.fetchall()):
        ptid = rec['part_type_id']
        sql = parttype2query(ptid)
        pid = rec['id']
        cursor.execute(sql, {'id': pid})
        p = cursor.fetchone()
        normalize_part(p)
        p['boms'] = load_boms(cursor2, cursor3, pid)
        p['interchanges'] = load_interchanges(cursor2, pid)
        p['whereused'] = load_whereused(cursor2, pid)
        result.append(p)
    cursor3.close()
    cursor2.close()
    cursor.close()
    print('Writing reslust to a file [{}]...'.format(args.out))
    with open(args.out, 'wt') as out:
        out.write(json.dumps(result, indent=2))
    print('Done.')
except mysql.connector.Error as err:
    print(err)
else:
    cnx.close()
