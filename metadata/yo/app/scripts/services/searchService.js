
"use strict";

angular.module("ngMetaCrudApp").service("searchStringNormalizer", [function() {
  return function(s) {
    return s.toLowerCase().replace(/\W+/g, '');
  }
}]).factory("partSearchService", ["$http", "partFacets", "METADATA_BASE", "searchStringNormalizer", function($http, partFacets, METADATA_BASE, searchStringNormalizer) {
  return function(partSearchParams) {
    // Basic search request body
    var searchRequest = {
      "from": partSearchParams.count * (partSearchParams.page - 1),
      "size": partSearchParams.count,
      "facets": {},
      "query": {
        "bool": {
          "must": [],
          "should": []
        }
      },
      "sort": []
    };
    // Facets
    angular.forEach(partFacets, function(facet) {
      // Facets
      searchRequest.facets[facet.name] = {
        "terms": {
          "field": facet.field,
          "size": 100
        }
      };
      // Facet terms
      var facetValue = partSearchParams.facets[facet.name];
      if (facetValue) {
        var term = {};
        term[facet.field] = facetValue;
        searchRequest.query.bool.must.push({
          "term": term
        });
      }
    });
    // Part Number
    if (partSearchParams.partNumber) {
      var partNumberShort = searchStringNormalizer(partSearchParams.partNumber);
      searchRequest.query.bool.must.push({
        "prefix": {
          "manufacturerPartNumber.short": partNumberShort
        }
      });
    }
    // Default query
    if (searchRequest.query.bool.must.length === 0 && searchRequest.query.bool.should.length === 0) {
      searchRequest.query = {
        match_all: {}
      }; // jshint ignore:line
    }
    // Sorting
    angular.forEach(partSearchParams.sorting, function(order, fieldName) {
      var sortField = {};
      sortField[fieldName] = {
        "missing": "_last",
        "ignore_unmapped": true,
        "order": order
      };
      searchRequest.sort.push(sortField);
    });
    // Call to ElasticSearch
    return $http({
      "method": "POST",
      "headers": {
        "Content-type": "text/plain"
      },
      "withCredentials": true,
      "url": METADATA_BASE + "search/part",
      "data": searchRequest
    });
  };
}]).factory('cmeySearchService', ["$http", "cmeyFacets", "METADATA_BASE", "searchStringNormalizer", function($http, cmeyFacets, METADATA_BASE, searchStringNormalizer) {
  return function(cmeySearchParams) {
    // Basic search request body
    var searchRequest = {
      "from": cmeySearchParams.count * (cmeySearchParams.page - 1),
      "size": cmeySearchParams.count,
      "facets": {},
      "query": {
        "bool": {
          "must": [],
          "should": []
        }
      },
      "sort": []
    };
    // Facets
    angular.forEach(cmeyFacets, function(facet) {
      searchRequest.facets[facet.name] = {
        "terms": {
          "field": facet.field,
          "size": 100
        }
      };
      // Facet terms
      var facetValue = cmeySearchParams.facets[facet.name];
      if (facetValue) {
        var term = {};
        term[facet.field] = facetValue;
        searchRequest.query.bool.must.push({
          'term': term
        });
      }
    });
    var cmey = cmeySearchParams.cmey;
    if (cmey) {
      var sq = "*" + searchStringNormalizer(cmey) + "*";
      searchRequest.query.bool.must.push({
        "query_string": {
          "default_field": "_all",
          "query": sq
        }
      });
    }
    // Default query
    if (searchRequest.query.bool.must.length === 0 && searchRequest.query.bool.should.length === 0) {
      searchRequest.query = {
        'match_all': {}
      }; // jshint ignore:line
    }
    // Sorting
    angular.forEach(cmeySearchParams.sorting, function(order, fieldName) {
      var sortField = {};
      sortField[fieldName] = {
        "missing": "_last",
        "ignore_unmapped": true,
        "order": order
      };
      searchRequest.sort.push(sortField);
    });
    // Call to ElasticSearch
    return $http({
      "method": "POST",
      "headers": {
        "Content-type": "text/plain"
      },
      "url": METADATA_BASE + "search/carmodelengineyear",
      "data": searchRequest
    });
  };
}]).factory('carmakeSearchService', ["$http", "METADATA_BASE", "searchStringNormalizer", function($http, METADATA_BASE, searchStringNormalizer) {
  return function(carmakeSearchParams) {
    // Basic search request body
    var searchRequest = {
      "from": carmakeSearchParams.count * (carmakeSearchParams.page - 1),
      "size": carmakeSearchParams.count,
      "facets": {},
      "query": {
        "bool": {
          "must": [],
          "should": []
        }
      },
      "sort": []
    };
    var carmake = carmakeSearchParams.carmake;
    if (carmake) {
      var sq = searchStringNormalizer(carmake);
      searchRequest.query.bool.must.push({
        "prefix": {
          "name.short": sq
        }
      });
    }
    // Default query
    if (searchRequest.query.bool.must.length === 0 && searchRequest.query.bool.should.length === 0) {
      searchRequest.query = {
        'match_all': {}
      }; // jshint ignore:line
    }
    // Sorting
    angular.forEach(carmakeSearchParams.sorting, function(order, fieldName) {
      var sortField = {};
      sortField[fieldName] = {
        "missing": "_last",
        "ignore_unmapped": true,
        "order": order
      };
      searchRequest.sort.push(sortField);
    });
    // Call to ElasticSearch
    return $http({
      "method": "POST",
      "headers": {
        "Content-type": "text/plain"
      },
      "url": METADATA_BASE + "search/carmake",
      "data": searchRequest
    });
  };
}]).factory('carfueltypeSearchService', ["$http", "METADATA_BASE", "searchStringNormalizer", function($http, METADATA_BASE, searchStringNormalizer) {
  return function(carfueltypeSearchParams) {
    // Basic search request body
    var searchRequest = {
      "from": carfueltypeSearchParams.count * (carfueltypeSearchParams.page - 1),
      "size": carfueltypeSearchParams.count,
      "facets": {},
      "query": {
        "bool": {
          "must": [],
          "should": []
        }
      },
      "sort": []
    };
    var carfueltype = carfueltypeSearchParams.carfueltype;
    if (carfueltype) {
      var sq = searchStringNormalizer(carfueltype);
      searchRequest.query.bool.must.push({
        "prefix": {
          "name.short": sq
        }
      });
    }
    // Default query
    if (searchRequest.query.bool.must.length === 0 && searchRequest.query.bool.should.length === 0) {
      searchRequest.query = {
        'match_all': {}
      }; // jshint ignore:line
    }
    // Sorting
    angular.forEach(carfueltypeSearchParams.sorting, function(order, fieldName) {
      var sortField = {};
      sortField[fieldName] = {
        "missing": "_last",
        "ignore_unmapped": true,
        "order": order
      };
      searchRequest.sort.push(sortField);
    });
    // Call to ElasticSearch
    return $http({
      "method": "POST",
      "headers": {
        "Content-type": "text/plain"
      },
      "url": METADATA_BASE + "search/carfueltype",
      "data": searchRequest
    });
  };
}]).factory('carmodelSearchService', ["$http", "carmodelFacets", "METADATA_BASE", "searchStringNormalizer", function($http, carmodelFacets, METADATA_BASE, searchStringNormalizer) {
  return function(carmodelSearchParams) {
    // Basic search request body
    var searchRequest = {
      "from": carmodelSearchParams.count * (carmodelSearchParams.page - 1),
      "size": carmodelSearchParams.count,
      "facets": {},
      "query": {
        "bool": {
          "must": [],
          "should": []
        }
      },
      "sort": []
    };
    // Facets
    angular.forEach(carmodelFacets, function(facet) {
      // Facets
      searchRequest.facets[facet.name] = {
        "terms": {
          "field": facet.field,
          "size": 100
        }
      };
      // Facet terms
      var facetValue = carmodelSearchParams.facets[facet.name];
      if (facetValue) {
        var term = {};
        term[facet.field] = facetValue;
        searchRequest.query.bool.must.push({
          "term": term
        });
      }
    });
    var carmodel = carmodelSearchParams.carmodel;
    if (carmodel) {
      var sq = searchStringNormalizer(carmodel);
      searchRequest.query.bool.must.push({
        "prefix": {
          "name.short": sq
        }
      });
    }
    // Default query
    if (searchRequest.query.bool.must.length === 0 && searchRequest.query.bool.should.length === 0) {
      searchRequest.query = {
        'match_all': {}
      }; // jshint ignore:line
    }
    // Sorting
    angular.forEach(carmodelSearchParams.sorting, function(order, fieldName) {
      var sortField = {};
      sortField[fieldName] = {
        "missing": "_last",
        "ignore_unmapped": true,
        "order": order
      };
      searchRequest.sort.push(sortField);
    });
    // Call to ElasticSearch
    return $http({
      "method": "POST",
      "headers": {
        "Content-type": "text/plain"
      },
      "url": METADATA_BASE + "search/carmodel",
      "data": searchRequest
    });
  };
}]);
