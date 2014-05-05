#!/usr/bin/env python

import sys
import re
import sets
from difflib import SequenceMatcher

idPattern = re.compile('^"(\d+)"')

newCsv = {}
oldCsv = {}

if len(sys.argv) != 3:
  print "Usage: " + sys.argv[0] + " <new.csv> <old.csv>"
  sys.exit();

for line in open(sys.argv[1]):
  match = idPattern.match(line)  
  if (match):
    newCsv[match.group()] = line.strip()

for line in open(sys.argv[2]):
  match = idPattern.match(line)  
  if (match):
    oldCsv[match.group()] = line.strip()

newIds     = sets.Set(newCsv.keys())
oldIds     = sets.Set(oldCsv.keys())
sameIds    = newIds.intersection(oldIds)
addedIds   = newIds.difference(oldIds)
removedIds = oldIds.difference(newIds)

print "Same.len: " + str(len(sameIds))
print "Added:    " + str(addedIds)
print "Removed:  " + str(removedIds)

for sku in sameIds:
  s = SequenceMatcher(None, newCsv[sku], oldCsv[sku])

  if newCsv[sku] != oldCsv[sku]:
    print "---"
    print newCsv[sku]
    print oldCsv[sku]
    #print "DIFF RAT: " + str(s.ratio())
    #print "DIFF OPS: " + str(s.get_opcodes())
    #print "DIFF NEW: " + newCsv[sku]
    #print "DIFF OLD: " + oldCsv[sku]
