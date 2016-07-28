#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

""" Utility. """

import argparse
import sys

try:
    # sudo pip3 install elasticsearch
    from elasticsearch import Elasticsearch
except ImportError as e:
    print("Required python module 'elasticsearch' not found.",
          file=sys.stderr)
    print("Use following command to install:", file=sys.stderr)
    print("\t $ sudo pip3 install elasticsearch", file=sys.stderr)
    sys.exit(1)
try:
    # sudo apt-get install python3-mysql.connector
    import mysql.connector
except ImportError as e:
    print("Required python module 'mysql.connector' not found.",
          file=sys.stderr)
    print("Use following command to install:", file=sys.stderr)
    print("\t $ sudo pip3 install python3-mysql.connector", file=sys.stderr)
    sys.exit(1)


INDEX_NAME = "metadata"


def idx_carmodelengineyear():
    """ Build a definition for the type 'carmodelengineyear'. """
    idx = None


def build_index_definition():
    """ Build an index definition. """
    body = {
        "mappings": {
            "part": {
                "properties": {
                    "id": {
                        "type": "long",
                        "store": "yes"
                    },
                    "name": {
                        "type": "multi_field",
                        "fields": {
                            "full": {
                                "type": "string",
                                "tokenizer": "lowercase",
                                "analyzer": "keyword",
                                "store": "yes"
                            },
                            "short": {
                                "type": "string",
                                "analyzer": "normalized_short",
                                "store": "yes"
                            },
                            "lower_case_sort": {
                                "type": "string",
                                "analyzer": "case_insensitive_sort",
                                "store": "yes"
                            }
                        }
                    },
                    "description": {
                        "type": "multi_field",
                        "fields": {
                            "full": {
                                "type": "string",
                                "tokenizer": "lowercase",
                                "analyzer": "keyword",
                                "store": "yes"
                            },
                            "short": {
                                "type": "string",
                                "analyzer": "normalized_short",
                                "store": "yes"
                            },
                            "lower_case_sort": {
                                "type": "string",
                                "analyzer": "case_insensitive_sort",
                                "store": "yes"
                            }
                        }
                    },
                    "inactive": {
                        "type": "boolean",
                        "store": "yes"
                    },
                    "turboModel": {
                        "properties": {
                            "id": {
                                "type": "long",
                                "store": "yes",
                                "analyzer": "keyword"
                            },
                            "name": {
                                "type": "multi_field",
                                "fields": {
                                    "full": {
                                        "type": "string",
                                        "tokenizer": "lowercase",
                                        "analyzer": "keyword",
                                        "store": "yes"
                                    },
                                    "short": {
                                        "type": "string",
                                        "analyzer": "normalized_short",
                                        "store": "yes"
                                    },
                                    "lower_case_sort": {
                                        "type": "string",
                                        "analyzer": "case_insensitive_sort",
                                        "store": "yes"
                                    }
                                }
                            },
                            "turboType": {
                                "properties": {
                                    "id": {
                                        "type": "long",
                                        "store": "yes",
                                        "analyzer": "keyword"
                                    },
                                    "name": {
                                        "type": "multi_field",
                                        "fields": {
                                            "full": {
                                                "type": "string",
                                                "tokenizer": "lowercase",
                                                "analyzer": "keyword",
                                                "store": "yes"
                                            },
                                            "short": {
                                                "type": "string",
                                                "analyzer": "normalized_short",
                                                "store": "yes"
                                            },
                                            "lower_case_sort": {
                                                "type": "string",
                                                "analyzer":
                                                    "case_insensitive_sort",
                                                "store": "yes"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "manufacturerPartNumber": {
                        "type": "multi_field",
                        "fields": {
                            "full": {
                                "type": "string",
                                "tokenizer": "lowercase",
                                "analyzer": "keyword",
                                "store": "yes"
                            },
                            "short": {
                                "type": "string",
                                "analyzer": "normalized_short",
                                "store": "yes"
                            },
                            "lower_case_sort": {
                                "type": "string",
                                "analyzer": "case_insensitive_sort",
                                "store": "yes"
                            }
                        }
                    },
                    "partType": {
                        "properties": {
                            "id": {
                                "type": "long",
                                "store": "yes",
                                "analyzer": "keyword"
                            },
                            "name": {
                                "type": "multi_field",
                                "fields": {
                                    "full": {
                                        "type": "string",
                                        "tokenizer": "lowercase",
                                        "analyzer": "keyword",
                                        "store": "yes"
                                    },
                                    "short": {
                                        "type": "string",
                                        "analyzer": "normalized_short",
                                        "store": "yes"
                                    },
                                    "lower_case_sort": {
                                        "type": "string",
                                        "analyzer": "case_insensitive_sort",
                                        "store": "yes"
                                    }
                                }
                            }
                        }
                    },
                    "manufacturer": {
                        "properties": {
                            "id": {
                                "type": "long",
                                "store": "yes",
                                "analyzer": "keyword"
                            },
                            "name": {
                                "type": "multi_field",
                                "fields": {
                                    "full": {
                                        "type": "string",
                                        "tokenizer": "lowercase",
                                        "analyzer": "keyword",
                                        "store": "yes"
                                    },
                                    "short": {
                                        "type": "string",
                                        "analyzer": "normalized_short",
                                        "store": "yes"
                                    },
                                    "lower_case_sort": {
                                        "type": "string",
                                        "analyzer": "case_insensitive_sort",
                                        "store": "yes"
                                    }
                                }
                            }
                        }
                    }
                }
            },
            "carmodelengineyear": {
                "properties": {
                    "id": {
                        "type": "long",
                        "store": "yes"
                    },
                    "year": {
                        "properties": {
                            "name": {
                                "type": "multi_field",
                                "fields": {
                                    "full": {
                                        "type": "string",
                                        "tokenizer": "lowercase",
                                        "analyzer": "keyword",
                                        "store": "yes"
                                    },
                                    "short": {
                                        "type": "string",
                                        "analyzer": "normalized_short",
                                        "store": "yes"
                                    },
                                    "lower_case_sort": {
                                        "type": "string",
                                        "analyzer": "case_insensitive_sort",
                                        "store": "yes"
                                    }
                                }
                            }
                        }
                    },
                    "model": {
                        "properties": {
                            "name": {
                                "type": "multi_field",
                                "fields": {
                                    "full": {
                                        "type": "string",
                                        "tokenizer": "lowercase",
                                        "analyzer": "keyword",
                                        "store": "yes"
                                    },
                                    "short": {
                                        "type": "string",
                                        "analyzer": "normalized_short",
                                        "store": "yes"
                                    },
                                    "lower_case_sort": {
                                        "type": "string",
                                        "analyzer": "case_insensitive_sort",
                                        "store": "yes"
                                    }
                                }
                            },
                            "make": {
                                "properties": {
                                    "name": {
                                        "type": "multi_field",
                                        "fields": {
                                            "full": {
                                                "type": "string",
                                                "tokenizer": "lowercase",
                                                "analyzer": "keyword",
                                                "store": "yes"
                                            },
                                            "short": {
                                                "type": "string",
                                                "analyzer": "normalized_short",
                                                "store": "yes"
                                            },
                                            "lower_case_sort": {
                                                "type": "string",
                                                "analyzer":
                                                    "case_insensitive_sort",
                                                "store": "yes"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "engine": {
                        "properties": {
                            "engineSize": {
                                "type": "multi_field",
                                "fields": {
                                    "full": {
                                        "type": "string",
                                        "tokenizer": "lowercase",
                                        "analyzer": "keyword",
                                        "store": "yes"
                                    },
                                    "short": {
                                        "type": "string",
                                        "analyzer": "normalized_short",
                                        "store": "yes"
                                    },
                                    "lower_case_sort": {
                                        "type": "string",
                                        "analyzer": "case_insensitive_sort",
                                        "store": "yes"
                                    }
                                }
                            },
                            "fuelType": {
                                "properties": {
                                    "name": {
                                        "type": "multi_field",
                                        "fields": {
                                            "full": {
                                                "type": "string",
                                                "tokenizer": "lowercase",
                                                "analyzer": "keyword",
                                                "store": "yes"
                                            },
                                            "short": {
                                                "type": "string",
                                                "analyzer": "normalized_short",
                                                "store": "yes"
                                            },
                                            "lower_case_sort": {
                                                "type": "string",
                                                "analyzer":
                                                    "case_insensitive_sort",
                                                "store": "yes"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            "carmake": {
                "properties": {
                    "id": {
                        "type": "long",
                        "store": "yes"
                    },
                    "name": {
                        "type": "multi_field",
                        "fields": {
                            "full": {
                                "type": "string",
                                "tokenizer": "lowercase",
                                "analyzer": "keyword",
                                "store": "yes"
                            },
                            "short": {
                                "type": "string",
                                "analyzer": "normalized_short",
                                "store": "yes"
                            },
                            "lower_case_sort": {
                                "type": "string",
                                "analyzer": "case_insensitive_sort",
                                "store": "yes"
                            }
                        }
                    }
                }
            },
            "carfueltype": {
                "properties": {
                    "id": {
                        "type": "long",
                        "store": "yes"
                    },
                    "name": {
                        "type": "multi_field",
                        "fields": {
                            "full": {
                                "type": "string",
                                "tokenizer": "lowercase",
                                "analyzer": "keyword",
                                "store": "yes"
                            },
                            "short": {
                                "type": "string",
                                "analyzer": "normalized_short",
                                "store": "yes"
                            },
                            "lower_case_sort": {
                                "type": "string",
                                "analyzer": "case_insensitive_sort",
                                "store": "yes"
                            }
                        }
                    }
                }
            },
            "carmodel": {
                "properties": {
                    "id": {
                        "type": "long",
                        "store": "yes"
                    },
                    "name": {
                        "type": "multi_field",
                        "fields": {
                            "full": {
                                "type": "string",
                                "tokenizer": "lowercase",
                                "analyzer": "keyword",
                                "store": "yes"
                            },
                            "short": {
                                "type": "string",
                                "analyzer": "normalized_short",
                                "store": "yes"
                            },
                            "lower_case_sort": {
                                "type": "string",
                                "analyzer": "case_insensitive_sort",
                                "store": "yes"
                            }
                        }
                    },
                    "make": {
                        "properties": {
                            "name": {
                                "type": "multi_field",
                                "fields": {
                                    "full": {
                                        "type": "string",
                                        "tokenizer": "lowercase",
                                        "analyzer": "keyword",
                                        "store": "yes"
                                    },
                                    "short": {
                                        "type": "string",
                                        "analyzer": "normalized_short",
                                        "store": "yes"
                                    },
                                    "lower_case_sort": {
                                        "type": "string",
                                        "analyzer": "case_insensitive_sort",
                                        "store": "yes"
                                    }
                                }
                            }
                        }
                    }
                }
            },
            "carengine": {
                "properties": {
                    "id": {
                        "type": "long",
                        "store": "yes"
                    },
                    "engineSize": {
                        "type": "multi_field",
                        "fields": {
                            "full": {
                                "type": "string",
                                "tokenizer": "lowercase",
                                "analyzer": "keyword",
                                "store": "yes"
                            },
                            "short": {
                                "type": "string",
                                "analyzer": "normalized_short",
                                "store": "yes"
                            },
                            "lower_case_sort": {
                                "type": "string",
                                "analyzer": "case_insensitive_sort",
                                "store": "yes"
                            }
                        }
                    },
                    "fuelType": {
                        "properties": {
                            "name": {
                                "type": "multi_field",
                                "fields": {
                                    "full": {
                                        "type": "string",
                                        "tokenizer": "lowercase",
                                        "analyzer": "keyword",
                                        "store": "yes"
                                    },
                                    "short": {
                                        "type": "string",
                                        "analyzer": "normalized_short",
                                        "store": "yes"
                                    },
                                    "lower_case_sort": {
                                        "type": "string",
                                        "analyzer":
                                            "case_insensitive_sort",
                                        "store": "yes"
                                    }
                                }
                            }
                        }
                    }
                }
            },
            "salesnotepart": {
                "properties": {
                    "createDate": {
                        "type": "string",
                        "analyzer": "keyword",
                        "store": "yes"
                    },
                    "primary": {
                        "type": "boolean",
                        "store": "yes"
                    },
                    "primaryPartId": {
                        "type": "long",
                        "store": "yes"
                    },
                    "pk": {
                        "properties": {
                            "salesNote": {
                                "properties": {
                                    "id": {
                                        "type": "long",
                                        "store": "yes"
                                    },
                                    "state": {
                                        "type": "string",
                                        "analyzer": "keyword",
                                        "store": "yes"
                                    },
                                    "comment": {
                                        "type": "multi_field",
                                        "fields": {
                                            "full": {
                                                "type": "string",
                                                "tokenizer": "lowercase",
                                                "analyzer": "keyword",
                                                "store": "yes"
                                            },
                                            "short": {
                                                "type": "string",
                                                "analyzer": "normalized_short",
                                                "store": "yes"
                                            },
                                            "lower_case_sort": {
                                                "type": "string",
                                                "analyzer":
                                                    "case_insensitive_sort",
                                                "store": "yes"
                                            }
                                        }
                                    }
                                }
                            },
                            "part": {
                                "properties": {
                                    "id": {
                                        "type": "long",
                                        "store": "yes"
                                    },
                                    "manufacturerPartNumber": {
                                        "type": "multi_field",
                                        "fields": {
                                            "full": {
                                                "type": "string",
                                                "tokenizer": "lowercase",
                                                "analyzer": "keyword",
                                                "store": "yes"
                                            },
                                            "short": {
                                                "type": "string",
                                                "analyzer": "normalized_short",
                                                "store": "yes"
                                            },
                                            "lower_case_sort": {
                                                "type": "string",
                                                "analyzer":
                                                    "case_insensitive_sort",
                                                "store": "yes"
                                            }
                                        }
                                    }
                                }
                            }
                        }
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
                    "normalized_short": {
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
                    },
                    "case_insensitive_sort": {
                        "tokenizer": "keyword",
                        "filter": [
                            "lowercase"
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
    }
    return body

argparser = argparse.ArgumentParser(description="Utility to (re)create index "
                                    "for the 'metadata' webapp in "
                                    "an instance of ElasticSearch service.")
argparser.add_argument("--es-host", required=False, default="localhost",
                       help="Host where ElasticSearch service runs.")
argparser.add_argument("--es-port", required=False, default=9200,
                       help="Port on the host where ElasticSearch service "
                       "runs.")
argparser.add_argument("--db-name", required=False, default="metadata",
                       help="Database name.")
argparser.add_argument("--db-host", required=False, default="localhost",
                       help="Host where MySql server runs.")
argparser.add_argument("--db-port", required=False, default=3306,
                       help="Port on the host where MySql server "
                       "runs.")
argparser.add_argument("--db-user", required=False, default="metaserver",
                       help="Username to connect to the database.")
argparser.add_argument("--db-password", required=False, default="metaserver",
                       help="Password to connect to the database.")
args = argparser.parse_args()

es = Elasticsearch([{"host": args.es_host, "port": args.es_port}])

index_definition = build_index_definition()

# Add to the index mapping critical dimensions defined in a database.
cnx = mysql.connector.connect(user=args.db_user, password=args.db_password,
                              host=args.db_host, database=args.db_name)
try:
    cursor = cnx.cursor()
    try:
        cursor.execute("select idx_name, data_type "
                       "from crit_dim "
                       "order by part_type_id, seq_num")
        index_properties = index_definition["mappings"]["part"]["properties"]
        for (idx_name, data_type) in cursor:
            if data_type == "DECIMAL":
                idx_type = "double"
            elif data_type == "INTEGER":
                idx_type = "long"
            elif data_type == "TEXT":
                idx_type = "string"
            elif data_type == "ENUMERATION":
                # Enumeration has a special index structure in oder
                # to store an enumeration item ID and its textual
                # representation. We need in the textual representation
                # in order to have a possibility to show enumeration value
                # in a WEB UI (e.g. in tables).

                # Index to store enumeration item ID.
                index_properties[idx_name] = {
                    "type": "long",
                    "store": "yes"
                }

                # Index to store enumeration item LABEL.
                # Caveat. The suffix "Label" below is hardcoded in the
                # Java
                # (see method CriticalDimensionService.JsonIdxNameTransformer
                #  #transform(Object)),
                # in the JavaSript code (see PartSearch.js)
                # and in the HTML (see PartSearch.html).
                # So if you changes this suffix you also must reflect the
                # rename in those files too.
                idx_name_label = idx_name + "Label"
                index_properties[idx_name_label] = {
                    "type": "multi_field",
                    "fields": {
                        "text": {
                            "type": "string",
                            "tokenizer": "lowercase",
                            "analyzer": "keyword",
                            "store": "yes"
                        },
                        "lower_case_sort": {
                            "type": "string",
                            "analyzer": "case_insensitive_sort",
                            "store": "yes"
                        }
                    }
                }
                continue
            else:
                print("Unknown data type: {}".format(data_type),
                      file=sys.stderr)
            index_definition["mappings"]["part"]["properties"][idx_name] = {
                "type": idx_type,
                "store": "yes"
            }
    finally:
        cursor.close()
finally:
    cnx.close()

# Delete existing index (if any) and create a new one.
if es.indices.exists(INDEX_NAME):
    print("Found index '{0:s}'. Deleting...".format(INDEX_NAME))
    es.indices.delete(INDEX_NAME)

print("Creating a new index '{0:s}'.".format(INDEX_NAME))

es.indices.create(index=INDEX_NAME, body=index_definition)
