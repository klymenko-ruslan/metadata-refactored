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
                      "analyzer": "case_insensitive_sort",
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
                      "analyzer": "case_insensitive_sort",
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
                      "analyzer": "case_insensitive_sort",
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
                  "analyzer": "case_insensitive_sort",
                  "store": "yes"
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
}'

echo
echo
