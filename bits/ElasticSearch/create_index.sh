#!/bin/bash

SERVER='http://localhost:9300'
INDEX='metadata'
TYPE='part'

echo "Creating index..." &&
wget --tries 1 -qO /dev/null --method PUT "$SERVER/$INDEX"

echo "Closing index to set configuration..." &&
wget --tries 1 -qO /dev/null --method POST "$SERVER/$INDEX/_close"

echo "Setting configuration..." &&
wget --tries 1 -qO /dev/null --method PUT "$SERVER/$INDEX/_settings" --header "Content-Type: application/json" --body-file @metadata_index_settings.json

echo "Setting mapping..." &&
wget --tries 1 -qO /dev/null --method PUT "$SERVER/$INDEX/$TYPE/_mapping" --header "Content-Type: application/json" --body-file @metadata_index_mapping.json

echo "Opening index for use..." &&
wget --tries 1 -qO /dev/null --method POST "$SERVER/$INDEX/_open"

