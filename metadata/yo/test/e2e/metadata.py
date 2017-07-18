#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# vim: set fileencoding=utf-8 :
# vim: tabstop=8 expandtab shiftwidth=4 softtabstop=4

"""
Build and runs the 'metadata' webapp for e2e testing.

The webapp for e2e testing should be run with a special profile (config).
This script builds the webapp, do cleanup and run the webapp with
profile 'e2e'.

The script can be run from any current working dir. It has a code to
calculate real pathes to targets. The calculation is done relative to
a location of this script, so don't move this script in other dirs.
"""

import argparse
import os
import re
import shutil
import subprocess
import sys

parser = argparse.ArgumentParser(description='Build and runs the \'metadata\' '
                                 'webapp for e2e testing.')
parser.add_argument('--skip-build', action='store_true',
                    help='Skip building of the application. It is expected '
                    'that application has already been built.')
parser.add_argument('--java-extra-opts', default='',
                    help='Extra options to run \'java\'.')
parser.add_argument('--files-storage-dir', default='/tmp/metadata',
                    help='A path to a directory where file storage for'
                    'testing \'metadata\' instance is located.')
args = parser.parse_args()


def check_jdk():
    """
    Check presence of Java in the system and its version.

    If this check fails then script is finished.
    Returns full filename to the checked 'java' executable.
    """
    javafname = shutil.which('java')
    if javafname is None:
        print('Can\'t find in PATH a java '
              '(https://java.com) executable: java', file=sys.stderr)
        sys.exit(1)
    javaversionout = subprocess.getoutput('java -version')
    javaversionout0 = javaversionout.splitlines()[0]
    javaversionmatch = re.search(r'^java version "([\d\._]+)"$',
                                 javaversionout0)
    if javaversionmatch is None:
        print('Can\'t parse java\'s output to find out its version:\n{}'
              .format(javaversionout0), file=sys.stderr)
        sys.exit(1)
    javaversionstr = javaversionmatch.group(1)
    javaversionarr = javaversionstr.split('.')
    if int(javaversionarr[1]) < 8:
        print('Found \'java\' ({location}) has incompatible '
              'version \'{version}\'. It is expected version \'1.8.x\' or '
              'above.'
              .format(location=javafname, version=javaversionstr),
              file=sys.stderr)
        sys.exit(1)
    return javafname


def check_maven():
    """
    Check presence of Apache Maven in the system and its version.

    If this check fails then script is finished.
    Returns full filename to the checked Apache Maven executable.
    """
    mvnfname = shutil.which('mvn')
    if mvnfname is None:
        print('Can\'t find in PATH an Apache Maven '
              '(https://maven.apache.org/) executable: mvn', file=sys.stderr)
        sys.exit(1)
    mvnversionout = subprocess.getoutput('mvn --version')
    mvnversionout0 = mvnversionout.splitlines()[0]
    mvnversionmatch = re.search(r'^Apache Maven ([\d\.]+) .+$',
                                mvnversionout0)
    if mvnversionmatch is None:
        print('Can\'t parse mvn\'s output to find out its version:\n{}'
              .format(mvnversionout0), file=sys.stderr)
        sys.exit(1)
    mvnversionstr = mvnversionmatch.group(1)
    mvnversionarr = mvnversionstr.split('.')
    if int(mvnversionarr[0]) < 3 or int(mvnversionarr[1]) < 3:
        print('Found Apache Maven ({location}) has incompatible '
              'version \'{version}\'. It is expected version \'3.3.3\' or '
              'above.'
              .format(location=mvnfname, version=mvnversionstr),
              file=sys.stderr)
        sys.exit(1)
    return mvnfname


def buildapp():
    """
    Biuld the 'metadata' webapp.

    Before build the sobroutine do some checks of prerequisites and
    exit (with error message) if they are not met.
    """
    mvnfname = check_maven()
    pomfilename = os.path.join(scriptdir, '..', '..', '..', 'pom.xml')
    if not os.path.exists(pomfilename):
        print('Can\'t find a POM for the \'metadata\' webapp: {}'
              .format(pomfilename), file=sys.stderr)
        sys.exit(1)
    cmdmvn = ('{mvn} -f {pom} -DskipTests -Dyo.test.skip=true '
              '-DbuildNumber=e2e clean package'
              .format(mvn=mvnfname, pom=pomfilename))
    print('Building of the \'metadata\' webapp.\n')
    retcode = subprocess.call(cmdmvn, shell=True)
    if retcode != 0:
        print('Building of the \'metadata\' webapp failed with return code: {}'
              .format(retcode), file=sys.stderr)
        sys.exit(1)

scriptdir = os.path.dirname(os.path.realpath(__file__))

if not args.skip_build:
    buildapp()

print('Checking of prerequisites.')
javafname = check_jdk()

print('Starting the built webapp.')
jarfilename = os.path.join(scriptdir, '..', '..', '..', 'target',
                           'metadata-e2e.jar').format(scriptdir)
filesStorageDir = args.files_storage_dir
productImagesDir = os.path.join(filesStorageDir, 'product_images')
originalImagesDir = os.path.join(productImagesDir, 'originals')
resizedImagesDir = os.path.join(productImagesDir, 'resized')

otherDir = os.path.join(filesStorageDir, 'other')
attachmentsSalesnotesDir = os.path.join(otherDir, 'salesNote',
                                        'attachments')
changelogSourcesDir = os.path.join(otherDir, 'changelog', 'sources',
                                   'attachments')
changelogSourceLnkDscrAttchDir = os.path.join(otherDir, 'changelog',
                                              'sources', 'link',
                                              'description', 'attachments')
cmdjava = ('{java} '
           '-Dimages.originals=file:{product_images_originals} '
           '-Dimages.resized=file:{product_images_resized} '
           '-Dattachments.salesNote.dir=file:{sales_notes_dir} '
           '-Dchangelog.sources.dir=file:{changelog_sources_dir} '
           '-Dchangelog.source.link.description.attachments.dir=file:{cldscr} '
           '{extraopts} '
           '-jar {metadatajar} --spring.profiles.active=e2e').format(
               java=javafname,
               product_images_originals=originalImagesDir,
               product_images_resized=resizedImagesDir,
               sales_notes_dir=attachmentsSalesnotesDir,
               changelog_sources_dir=changelogSourcesDir,
               cldscr=changelogSourceLnkDscrAttchDir,
               metadatajar=jarfilename,
               extraopts=args.java_extra_opts
           )
subprocess.call(cmdjava, shell=True)
