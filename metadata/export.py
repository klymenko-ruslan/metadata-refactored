#!/usr/bin/env python3.4
# vim: set fileencoding=utf-8 :
# vim: set tabstop=8 expandtab shiftwidth=4 softtabstop=4 :

"""Utility to export 'metadata' MySql database to a JSON."""

import argparse
import json
import mysql.connector
from collections import namedtuple

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
args = parser.parse_args()

EntityMeta = namedtuple('EntityMeta', ['tablename', 'extra_columns'])


ENTITIES_DESCRIPTORS = {
    1: EntityMeta('turbo', 'turbo_model_id, cool_type_id, gasket_kit_id'),
    2: EntityMeta('cartridge', None),
    3: EntityMeta('kit', 'kit_type_id'),
    4: EntityMeta('piston_ring', 'installedDiameterA, gapBInstalledDiameter, '
                  'thicknessC, thicknessCTol, widthD, widthDUpperTol, '
                  'widthDLowerTol, material, type, weight as cd_weight, '
                  'diagram'),
    5: EntityMeta('journal_bearing', 'maxOuterDiameter, minOuterDiameter, '
                  'maxInnerDiameter, minInnerDiameter, size, length, '
                  'lengthTol, feedHoleCount, feedHoleDiameter, freePinned, '
                  'bearingsPerCartridge, oilFeed, rotation, material, '
                  'centerDiameterOd, endConfiguration, boreConfiguration, '
                  'weight as cd_weight, specialFeatures, '
                  'specialFeaturesOnOff, brgSurfaces, diagram'),
    6: EntityMeta('gasket', 'type, splitSinglePassage, shape, boltHoles, '
                  'passageA, b, c, d, e, f, g, h, thickness, material, '
                  'weight as cd_weight, diagram'),
    8: EntityMeta('fast_wearing_component', None),
    9: EntityMeta('major_component', None),
    10: EntityMeta('minor_component', None)
    11: EntityMeta('compressor_wheel', 'rotation, flatbackSuperback, '
                   'extendedTips, threadedBore, boreless, inducerDiameterA, '
                   'inducerDiameterATol, exducerDiameterB, '
                   'exducerDiameterBTol, tipLocation, tipLocationTol, '
                   'tipHeightD, tipHeightDTol, platformHeightE, '
                   'platformHeightTol, maxBoreDiameter, minBoreDiameter, '
                   'bladeCount, platformThickness, platformThicknessTol, '
                   'threadCallout, threadHand, platformDiameterF, '
                   'platformDiameterFTol, overallHeightC, noseDiameterG, '
                   'footDiameterH, bladeHeight, weight as cd_weight, diagram'),
    12: EntityMeta('turbine_wheel', 'inducerDiaA, inducerDiaATol, '
                   'exducerDiaB, exducerDiaBTol, tipHeightC, tipHeightCTol, '
                   'maxJournalDiameterD, minJournalDiameterD, '
                   'minStemDiameterE, maxStemDiameterE, stemLengthF, '
                   'stemLengthFTol, platformHeightH, bladeHeight, '
                   'threadLengthG, thread, threadHand, '
                   'pistonRingGrooveMajorDiaI, pistonRingGrooveMajorDiaITol, '
                   'pistonRingGrooveMinorDiaJ, pistonRingGrooveMinorDiaJTol, '
                   'pistonRingGrooveWidthK, pistonRingGrooveWidthKTol, '
                   'the2ndPistonRingGrooveMajorDia, '
                   'the2ndPistonRingGrooveMajorDiaTol, '
                   'the2ndPistonRingGrooveMinorDia, '
                   'the2ndPistonRingGrooveMinorDiaTol, '
                   'the2ndPistonRingGrooveWidth, '
                   'the2ndPistonRingGrooveWidthTol, rotation, bladeCount, '
                   'shroudType, journalType, extendedTips, '
                   'weight as cd_weight, diagram'),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
    11: EntityMeta('', ''),
}


def parttype2query(ptid):
    """Return SQL query to extract a part entity for specified part type."""
    em = ENTITIES_DESCRIPTORS.get(ptid)
    retval = '''
    select
    id, manfr_part_num, manfr_id, part_type_id, name,
    description, inactive, legend_img_filename, dim_length,
    dim_height, weight
    '''
    if em.extra_cols is not None:
        retval = retval + ', ' + em.extra_cols + ' '
    retval = (retval + 'from part p join ' + em.tablename +
              ' e on p.id=e.part_id where p.id=%{id}s')

PART_COLS = ('id, manfr_part_num, manfr_id, part_type_id, name, description, '
             'inactive, legend_img_filename, dim_length, dim_height, weight')

SQL_DICT = {
    # Turbo
    1:  'select ' + PART_COLS + ', turbo_model_id, cool_type_id, ' +
        'gasket_kit_id from part p join turbo t on p.id=t.part_id ' +
        'where p.id=%(id)s',
    # Cartridge
    2:  'select ' + PART_COLS + ' from part p join cartridge c '
        'on p.id=c.part_id where p.id=%(id)s',
    # Kit
    3:  'select ' + PART_COLS + ' from part p join cartridge c '
}


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


try:
    cnx = mysql.connector.connect(host=args.db_host, port=args.db_port,
                                  database=args.db_name,
                                  user=args.db_username,
                                  password=args.db_password)
    cursor = cnx.cursor(dictionary=True, buffered=True)
    # Map 'manufacturer_id' => 'manufacturer'.
    cursor.execute('select id, name, manfr_type_id, '
                   'parent_manfr_id, not_external '
                   'from manfr')
    mnfr_map = {m['id']: m for m in cursor.fetchall()}
    # Map 'part_type_id' => 'part_type'.
    cursor.execute('select id, name from part_type')
    parttype_map = {pt['id']: pt for pt in cursor.fetchall()}
    # Map 'turbo_type_id' => 'turbo_type'.
    cursor.execute('select id, name, manfr_id from turbo_type')
    turbotypes_map = {tt['id']: tt for tt in map(normalize_manfrid_in_tt,
                                                 cursor.fetchall())}
    # Map 'turbo_model_id' => 'turbo_model'.
    cursor.execute('select id, name, turbo_type_id from turbo_model')
    turbomodels_map = {tm['id']: tm for tm in map(normalize_ttid_in_tm,
                                                  cursor.fetchall())}
    # Map 'cool_type_id' => 'cool_type'.
    cursor.execute('select id, name from cool_type')
    cooltypes_map = {ct['id']: ct for ct in cursor.fetchall()}
    # Export parts.
    cursor.execute('select id, part_type_id from part order by id')
    result = []
    for rec in cursor.fetchall():
        ptid = rec['part_type_id']
        sql = SQL_DICT.get(ptid)
        cursor.execute(sql, {'id': rec['id']})
        p = cursor.fetchone()
        result.append(p)
    cursor.close()
    print(json.dumps(result, indent=2))
except mysql.connector.Error as err:
    print(err)
else:
    cnx.close()
