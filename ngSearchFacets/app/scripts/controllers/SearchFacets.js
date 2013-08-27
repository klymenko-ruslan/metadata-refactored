'use strict';

angular.module('ngSearchFacetsApp')
  .controller('SearchFacetsCtrl', function ($scope) {

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
          console.log("Filtering on " + facetName + ": " + term);
          $scope.facetFilters[facetName] = term;
      };

      $scope.removeFacetFilter = function(facetName) {
          delete $scope.facetFilters[facetName];
      }
  });
