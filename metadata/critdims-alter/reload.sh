#!/bin/bash

mysql -uroot -proot -e "drop database if exists metadata;create database metadata;" && (bzcat metadata_201605181617.sql.bz2 | mysql -uroot -proot metadata) && (./main.py | mysql -umetaserver -pmetaserver metadata)
