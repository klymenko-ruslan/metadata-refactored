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
                }
              }
            },
            "typeName": {
              "type": "string",
              "analyzer": "keyword",
              "store": "yes"
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
              "type": "string",
              "store": "yes"
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
