"use strict";

angular.module("ngMetaCrudApp").factory("partSearchService", function ($http, $log, Facets, METADATA_BASE) {
  return function (partSearchParams) {
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
    angular.forEach(Facets, function(facet) {
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
        searchRequest.query.bool.must.push({"term": term});
      }
    });
    // Part Number
    if (partSearchParams.partNumber) {
      var partNumber = partSearchParams.partNumber.toLowerCase();
      var partNumberShort = partNumber.replace(/\W+/g, '');
      searchRequest.query.bool.must.push({
        "prefix": {"manufacturerPartNumber.short": partNumberShort}
      });
    }
    // Default query
    if (searchRequest.query.bool.must.length === 0 && searchRequest.query.bool.should.length === 0) {
      searchRequest.query = {match_all: {}}; // jshint ignore:line
    }
    // Sorting
    angular.forEach(partSearchParams.sorting, function (order, fieldName) {
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
}).factory('cmeySearchService', function ($http, $log, cmeyFacets, METADATA_BASE) {
  return function (cmeySearchParams) {
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
        searchRequest.query.bool.must.push({'term': term});
      }
    });
    var cmey = cmeySearchParams.cmey;
    if (cmey) {
      searchRequest.query.bool.must.push({
        "query_string": {
          "default_field": "_all",
          "query": "*" + cmey.toLowerCase() + "*"
        }
      });
    }
    // Default query
    if (searchRequest.query.bool.must.length === 0 && searchRequest.query.bool.should.length === 0) {
      searchRequest.query = {'match_all': {}}; // jshint ignore:line
    }
    // Sorting
    angular.forEach(cmeySearchParams.sorting, function (order, fieldName) {
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
}).factory('carmakeSearchService', function ($http, $log, METADATA_BASE) {
  return function (carmakeSearchParams) {
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
      searchRequest.query.bool.must.push({
        "query_string": {
          "default_field": "_all",
          "query": "*" + carmake.toLowerCase() + "*"
        }
      });
    }
    // Default query
    if (searchRequest.query.bool.must.length === 0 && searchRequest.query.bool.should.length === 0) {
      searchRequest.query = {'match_all': {}}; // jshint ignore:line
    }
    // Sorting
    angular.forEach(carmakeSearchParams.sorting, function (order, fieldName) {
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
});
