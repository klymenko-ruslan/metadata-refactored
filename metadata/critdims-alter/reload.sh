#!/bin/bash

# DUMP_DB="metadata_201607151347.sql.bz2"
DUMP_DB="metadata_prod_201608121138.sql.bz2"

rm -rf out/*
mysql -uroot -proot -e "drop database if exists metadata;create database metadata;"
bzcat ${DUMP_DB} | mysql -uroot -proot metadata
./prepare.sh
./main.py
mysql -umetaserver -pmetaserver metadata < out/alter.sql
