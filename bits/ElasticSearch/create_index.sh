#!/bin/bash

SERVER='http://localhost:9200'
INDEX='metadata'
TYPE='part'

echo "Creating index..." &&
curl -X PUT "$SERVER/$INDEX" &&

echo "Closing index to set configuration..." &&
curl -X POST "$SERVER/$INDEX/_close" &&

echo "Setting configuration..." &&
curl -X PUT "$SERVER/$INDEX/_settings" --data @metadata_index_settings.json && 

echo "Setting mapping..." &&
curl -X PUT "$SERVER/$INDEX/$TYPE/_mapping" --data @metadata_index_settings.json &&

echo "Opening index for use..." &&
curl -X POST "$SERVER/$INDEX/_open"
