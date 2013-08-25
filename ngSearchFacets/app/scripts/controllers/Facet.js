'use strict';

angular.module('ngSearchFacetsApp')
  .controller('FacetCtrl', function ($scope, searchService, facetNameService) {
      $scope.facetFilters = {};

      $scope.filterFacets = function(facets) {
          var results = {};

          angular.forEach(facets, function(facet, facetName) {
              if (facet.total > 0 || $scope.isFacetSelected(facetName)) {
                  facet.friendlyName = facetNameService(facetName);
                  results[facetName] = facet;
              }
          });

          return results;
      };

      $scope.filterTerms = function(facetName, facet) {
          var results = [];

          angular.forEach(facet.terms, function(term) {
              if ($scope.isFacetSelected(facetName)) {}

              if (term.count > 0 || isTermSelected(facetName, term)) {
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
          console.log("Searching");
          searchService("gasket", $scope.facetFilters).then(function(searchResults) {
              $scope.searchResults = searchResults.data;
          });
      };

      $scope.$watch('facetFilters', $scope.search, true);
  });
