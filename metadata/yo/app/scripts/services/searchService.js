'use strict';

angular.module('ngMetaCrudApp')
    .factory('searchService', function ($http, $log, Facets) {
        return function (partSearchParams) {
//        console.log("Searching for `" + search.partNumber + "`, facets: " + JSON.stringify(search.facets));

          // Basic search request body
          var searchRequest = {
            from: partSearchParams.count * (partSearchParams.page - 1),
            size: partSearchParams.count,
            facets: {},
            query: {
              bool: {
                must: []
              }
            },
            sort: []
          };

          // Facets
          angular.forEach(Facets, function(facet) {

            // Facets
            searchRequest.facets[facet.name] = {
              terms: {
                field: facet.field,
                order: 'term',
                size: 100
              }
            }

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
            searchRequest.query.bool.must.push({
              prefix: {'manufacturerPartNumber.text': partSearchParams.partNumber}
            });
          }

          // Default query
          if (searchRequest.query.bool.must.length == 0) {
              searchRequest.query = {match_all: {}};
          }

          // Sorting
          angular.forEach(partSearchParams.sorting, function (order, fieldName) {
              var sortField = {};
              sortField[fieldName] = {
                  "missing": "_last",
                  "ignore_unmapped": true,
                  "order": order
              }

              searchRequest.sort.push(sortField);
          });

        // Call to ElasticSearch
        return $http({
          method: 'POST',
          headers: {
            "Content-type": "text/plain"
          },
          url: "/search",
          data: searchRequest
        });
      };
    });
