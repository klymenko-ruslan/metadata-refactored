'use strict';

angular.module('ngSearchFacetsApp')
  .controller('FacetsCtrl', function ($scope, searchService) {
      $scope.facetFilters = {};

      // Filter down to facets with totals greater than zero
      $scope.filterFacets = function(facets) {
          var results = {};

          angular.forEach(facets, function(facet, facetName) {
              if (facet.total > 0) {
                  results[facetName] = facet;
              }
          });

          return results;
      };

      // Filter down to terms with counts greater than zero.
      $scope.filterTerms = function(facetName, facet) {
          var results = [];

          angular.forEach(facet.terms, function(term) {
              if ($scope.isFacetSelected(facetName)) {}

              if (term.count > 0) {
                  term.facetName = facetName;
                  results.push(term);
              }
          });

          return results;
      };

      $scope.isFacetSelected = function(facetName) {
          return facetName in $scope.facetFilters
              && angular.isDefined($scope.facetFilters[facetName])
              && $scope.facetFilters[facetName] != null;
      };

      $scope.isTermSelected = function(facetName, term) {
          return $scope.isFacetSelected(facetName)
              && $scope.facetFilters[facetName] == term.term;
      };

      $scope.setFacetFilter = function(facetName, term) {
          $scope.facetFilters[facetName] = term;
      };

      $scope.removeFacetFilter = function(facetName) {
          delete $scope.facetFilters[facetName];
      }

      $scope.search = function() {
          var queryString = null;
          var searchRequest = {
              from: 0,
              size: 50,
              facets: {
                  "part_type": {
                      terms: {
                          field: "part_type",
                          order: "term"
                      }
                  },
                  "manufacturer_name.text": {
                      terms: {
                          field: "manufacturer_name.text",
                          order: "term"
                      }
                  },
                  "kit_type_name.text": {
                      terms: {
                          field: "kit_type_name.text",
                          order: "term"
                      }
                  },
                  "gasket_type_name.text": {
                      terms: {
                          field: "gasket_type_name.text",
                          order: "term"
                      }
                  }
              }
          };

          // Build the query
          searchRequest.query = {
              "bool": {
                  "must":[]
              }
          };

          // Facet Terms
          if (angular.isObject($scope.facetFilters) && !jQuery.isEmptyObject($scope.facetFilters)) {
              angular.forEach($scope.facetFilters, function(facetValue, facetName) {
                  var term = {};
                  term[facetName] = facetValue;

                  searchRequest.query.bool.must.push({"term":term});
              });
          }

          // Query string
          if (angular.isString(queryString) && queryString.length > 0) {
              searchRequest.query.bool.must.push({
                  query: queryString,
                  fields: [
                      "manufacturer_part_number.autocomplete",
                      "manufacturer_part_number.text",
                      "manufacturer_name.autocomplete",
                      "manufacturer_name.text",
                      "part_type"
                  ]
              });
          }

          // Default query
          if (searchRequest.query.bool.must.length == 0) {
              searchRequest.query = {match_all: {}};
          }

          searchService(searchRequest).then(function(searchResults) {
              $scope.searchRequest = searchRequest;
              $scope.ngModel = searchResults.data;
          });
      };

      $scope.$watch('facetFilters', $scope.search, true);
  });
