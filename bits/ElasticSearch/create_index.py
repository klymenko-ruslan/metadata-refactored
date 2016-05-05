#!/usr/bin/env python3
# -*- coding: UTF-8 -*-

""" Utility. """

import argparse
from elasticsearch import Elasticsearch

INDEX_NAME = "metadata"


def idx_carmodelengineyear():
    """ Build a definition for the type 'carmodelengineyear'. """
    idx = None


def build_index_body():
    """ Build an index definition. """
    body = {
        "mappings": {
            # ================================================================
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
                            },
                            "typeName": {
                                "type": "string",
                                "analyzer": "keyword",
                                "store": "yes",
                                "fields": {
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
                    },
                    "coolType": {
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
                    "gasketType": {
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
                    "kitType": {
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
                    "sealType": {
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
                    }
                }
            },
            # ================================================================
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
argparser.add_argument("-H", "--host", required=False, default="localhost",
                       help="Host where ElasticSearch service runs.")
argparser.add_argument("-p", "--port", required=False, default=9200,
                       help="Port on the host where ElasticSearch service "
                       "runs.")
args = argparser.parse_args()

es = Elasticsearch([{"host": args.host, "port": args.port}])

if es.indices.exists(INDEX_NAME):
    print("Found index '{0:s}'. Deleting...".format(INDEX_NAME))
    es.indices.delete(INDEX_NAME)

index_body = build_index_body()
print("Creating a new index '{0:s}'.".format(INDEX_NAME))
es.indices.create(index=INDEX_NAME, body=build_index_body())
