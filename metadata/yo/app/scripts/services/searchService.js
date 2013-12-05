'use strict';

angular.module('ngMetaCrudApp')
    .factory('searchService', function ($http, $log) {
      return function (search) {
//        console.log("Searching for `" + search.queryString + "`, facets: " + JSON.stringify(search.facetFilters));

        var searchRequest = {
          from: search.count * (search.page - 1),
          size: search.count,
          facets: {
            "Kit Type": {
              terms: {
                field: "kitType.name",
                order: "term",
                size: 25
              }
            },
            "Gasket Type": {
              terms: {
                field: "gasketType.name",
                order: "term",
                size: 25
              }
            },
            "Seal Type": {
              terms: {
                field: "sealType.name",
                order: "term",
                size: 25
              }
            }
          }
        };

        // Build the query
        searchRequest.query = {
          "bool": {
            "must": []
          }
        };

        // Part type
        if (search.partType) {
//          $log.log("Part Type: " + search.partType.name);
          searchRequest.query.bool.must.push({
            term: {
              'part.partType.id': search.partType.id
            }
          });
        }

        // Manufacturer
        if (search.manufacturer) {
//          $log.log("Manufacturer: " + search.manufacturer.name);
          searchRequest.query.bool.must.push({
            term: {
              'part.manufacturer.id': search.manufacturer.id
            }
          });
        }

        // Facet Terms
        if (angular.isObject(search.facetFilters) && !jQuery.isEmptyObject(search.facetFilters)) {
          angular.forEach(search.facetFilters, function (facetValue, facetName) {
            var term = {};
            term[facetName] = facetValue;

            searchRequest.query.bool.must.push({"term": term});
          });
        }

        // Query string
        if (angular.isString(search.queryString) && search.queryString.length > 0) {
          searchRequest.query.bool.must.push({
            query_string: {
              query: search.queryString,
              fields: [
                "_id^1000",
                "part_type^1000",
                "manufacturer_part_number.autocomplete",
                "manufacturer_part_number.text",
                "manufacturer.name.autocomplete",
                "manufacturer.name.text",
                "name"
              ]
            }
          });
        }

        // Default query
        if (searchRequest.query.bool.must.length == 0) {
          searchRequest.query = {match_all: {}};
        }

        // Sorting
        searchRequest.sort = [];
        if (search.sorting) {
          angular.forEach(search.sorting, function(order, field) {

            // Build the sort field
            var sortField = {};
            sortField[field] = {
              "missing": "_last",
              "ignore_unmapped": true,
              "order": order
            }

            searchRequest.sort.push(sortField);
          });
        }

        // Call to ElasticSearch
        return $http({
          method: 'POST',
          headers: {
            "Content-type": "text/plain"
          },
//          url: "http://localhost:9200/metadata/_search",
          url: "http://metadata.turbointernational.com:9200/metadata/_search",
          data: searchRequest
        });
      };
    });
