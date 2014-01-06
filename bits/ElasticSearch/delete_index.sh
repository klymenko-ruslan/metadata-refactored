#!/bin/bash

SERVER='http://localhost:9200'
INDEX='metadata'
TYPE='part'

echo "Deleting index..." &&
curl -X DELETE "$SERVER/$INDEX"

