#!/bin/bash

./main.py && mysql -uroot -proot -e "drop database if exists metadata;create database metadata;" && (bzcat metadata_201605181617.sql.bz2 | mysql -uroot -proot metadata) && (mysql -umetaserver -pmetaserver metadata < out/alter.sql)
