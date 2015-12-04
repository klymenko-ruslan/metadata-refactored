'use strict';

angular.module('ngMetaCrudApp')
    .factory('partSearchService', function ($http, $log, Facets, METADATA_BASE) {
        return function (partSearchParams) {
//        console.log('Searching for `' + search.partNumber + '`, facets: ' + JSON.stringify(search.facets));

          // Basic search request body
          var searchRequest = {
            from: partSearchParams.count * (partSearchParams.page - 1),
            size: partSearchParams.count,
            facets: {},
            query: {
              bool: {
                must: [],
                should: []
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
                size: 100
              }
            };

            // Facet terms
            var facetValue = partSearchParams.facets[facet.name];
            if (facetValue) {
              var term = {};
              term[facet.field] = facetValue;

              searchRequest.query.bool.must.push({'term': term});
            }
          });

          // Part Number
          if (partSearchParams.partNumber) {
            var partNumber = partSearchParams.partNumber.toLowerCase();
            var partNumberShort = partNumber.replace(/\W+/g, '');
//            searchRequest.query.bool.must.push({
//                prefix: {'manufacturerPartNumber.short': partNumberShort}
//              });

            searchRequest.query.bool.must.push({
                prefix: {'manufacturerPartNumber': partNumberShort}
              });

//              searchRequest.query.bool.should.push({
//                prefix: {"manufacturerPartNumber.full": partNumber.toLowerCase()}
//              });
          }

          // Default query
          if (searchRequest.query.bool.must.length === 0 && searchRequest.query.bool.should.length === 0) {
            searchRequest.query = {match_all: {}}; // jshint ignore:line
          }

          // Sorting
          angular.forEach(partSearchParams.sorting, function (order, fieldName) {
              var sortField = {};
              sortField[fieldName] = {
                  'missing': '_last',
                  'ignore_unmapped': true,
                  'order': order
                };

              searchRequest.sort.push(sortField);
            });

        // Call to ElasticSearch
          return $http({
            method: 'POST',
            headers: {
              'Content-type': 'text/plain'
            },
            withCredentials: true,
            url: METADATA_BASE + 'search/part',
            //url: '/metadata/search/part',
            data: searchRequest
          });
        };
      })
     .factory('applicationSearchService', function ($http, $log, partApplicationFacets, METADATA_BASE) {
        return function (applicationSearchParams) {
//        console.log('Searching for `' + search.partNumber + '`, facets: ' + JSON.stringify(search.facets));

          // Basic search request body
          var searchRequest = {
            from: applicationSearchParams.count * (applicationSearchParams.page - 1),
            size: applicationSearchParams.count,
            facets: {},
            query: {
              bool: {
                must: [],
                should: []
              }
            },
            sort: []
          };

          // Facets
          angular.forEach(partApplicationFacets, function(facet) {

            // Facets
            searchRequest.facets[facet.name] = {
              terms: {
                field: facet.field,
                size: 100
              }
            };

            // Facet terms
            var facetValue = applicationSearchParams.facets[facet.name];
            if (facetValue) {
              var term = {};
              term[facet.field] = facetValue;
              searchRequest.query.bool.must.push({'term': term});
            }
          });

          // Application
          if (applicationSearchParams.application) {
            var application = applicationSearchParams.application.toLowerCase();
            //var applicationShort = partNumber.replace(/\W+/g, '');
            searchRequest.query.bool.must.push({
                query_string: {
                  'default_field': '_all',
                  'query': '*' + application + '*'
                }
              });
          }

          // Default query
          if (searchRequest.query.bool.must.length === 0 && searchRequest.query.bool.should.length === 0) {
            searchRequest.query = {'match_all': {}}; // jshint ignore:line
          }

          // Sorting
          angular.forEach(applicationSearchParams.sorting, function (order, fieldName) {
              var sortField = {};
              sortField[fieldName] = {
                  'missing': '_last',
                  'ignore_unmapped': true,
                  'order': order
                };

              searchRequest.sort.push(sortField);
            });

        // Call to ElasticSearch
          return $http({
            method: 'POST',
            headers: {
              'Content-type': 'text/plain'
            },
            url: METADATA_BASE + 'search/application',
            // url: '/metadata/search/application',
            data: searchRequest
          });
        };
      });
