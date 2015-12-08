#!/bin/bash

PORT=9200
SERVER="http://localhost:$PORT"
INDEX="metadata"

#echo
#echo
#echo "Deleting index..." && curl -X DELETE "$SERVER/$INDEX"

echo
echo
echo "Setting mapping..." && curl -XPUT "$SERVER/$INDEX/?pretty=1" -d '
{
  "mappings": {
    "part": {
      "properties": {
        "id": {
          "type": "long",
          "store": "yes"
        },
        "name": {
          "type": "string",
          "store": "yes"
        },
        "manufacturerPartNumber": {
          "type": "multi_field",
          "store": "yes",
          "fields": {
            "full": {
              "type": "string",
              "tokenizer": "lowercase",
              "analyzer": "keyword"
            },
            "short": {
              "type": "string",
              "analyzer": "part_number_short"
            }
          }
        },
        "partType.id": {
          "type": "long",
          "store": "yes",
          "analyzer": "keyword"
        },
        "partType.name": {
          "store": "yes",
          "type": "string",
          "analyzer": "keyword"
        },
        "partType.typeName": {
          "type": "string",
          "analyzer": "keyword",
          "store": "yes"
        },
        "manufacturer.id": {
          "type": "long",
          "store": "yes",
          "analyzer": "keyword"
        },
        "manufacturer.name": {
          "store": "yes",
          "type": "string",
          "analyzer": "keyword"
        },
        "coolType.id": {
          "type": "long",
          "store": "yes",
          "analyzer": "keyword"
        },
        "coolType.name": {
          "store": "yes",
          "type": "string",
          "analyzer": "keyword"
        },
        "gasketType.id": {
          "type": "long",
          "store": "yes",
          "analyzer": "keyword"
        },
        "gasketType.name": {
          "store": "yes",
          "type": "string",
          "analyzer": "keyword"
        },
        "kitType.id": {
          "type": "long",
          "store": "yes",
          "analyzer": "keyword"
        },
        "kitType.name": {
          "store": "yes",
          "type": "string",
          "analyzer": "keyword"
        },
        "sealType.id": {
          "type": "long",
          "store": "yes",
          "analyzer": "keyword"
        },
        "sealType.name": {
          "store": "yes",
          "type": "string",
          "analyzer": "keyword"
        },
        "turboModel.id": {
          "type": "long",
          "store": "yes",
          "analyzer": "keyword"
        },
        "turboModel.name": {
          "store": "yes",
          "type": "string",
          "analyzer": "keyword"
        },
        "turboModel.turboType.id": {
          "type": "long",
          "store": "yes",
          "analyzer": "keyword"
        },
        "turboModel.turboType.name": {
          "store": "yes",
          "type": "string",
          "analyzer": "keyword"
        }
      }
    }
  },
  "settings": {
    "analysis": {
      "char_filter": {
        "nonword": {
          "type": "pattern_replace",
          "pattern": "\\W+",
          "replacement": ""
        }
      },
      "analyzer": {
        "part_number_short": {
          "type": "custom",
          "tokenizer": "keyword",
          "filter": [
            "lowercase",
            "autocomplete"
          ],
          "char_filter": [
            "nonword"
          ]
        },
        "autocomplete": {
          "type": "custom",
          "tokenizer": "keyword",
          "filter": [
            "lowercase",
            "autocomplete"
          ]
        }
      },
      "filter": {
        "autocomplete": {
          "type": "nGram",
          "min_gram": 2,
          "max_gram": 20
        }
      }
    }
  }
}'

echo
echo
