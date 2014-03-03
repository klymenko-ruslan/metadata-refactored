'use strict';

angular.module('ngMetaCrudApp')
    .directive('searchFacets', function () {
        return {
            restrict: 'E',
            controller:  function ($scope) {

              // Filter down to facets with totals greater than zero
              $scope.search.filterFacets = function (facets) {
                var results = {};

                angular.forEach(facets, function (facet, facetName) {
                  if (facet.total > 0) {
                    results[facetName] = facet;
                  }
                });

                return results;
              };

              // Filter down to terms with counts greater than zero.
              $scope.filterTerms = function (facetName, facet) {
                var results = [];

                angular.forEach(facet.terms, function (term) {
                  if (term.count > 0) {
                    term.facetName = facetName;
                    results.push(term);
                  }
                });

                return results;
              };

              $scope.isFacetSelected = function (facetName) {
                return facetName in $scope.search.facetFilters
                    && angular.isDefined($scope.search.facetFilters[facetName])
                    && $scope.search.facetFilters[facetName] != null;
              };

              $scope.isTermSelected = function (facetName, term) {
                return $scope.isFacetSelected(facetName)
                    && $scope.search.facetFilters[facetName] == term.term;
              };

              $scope.setFacetFilter = function (facetName, term) {
                console.log("Filtering on " + facetName + ": " + term);
                $scope.search.facetFilters[facetName] = term;
              };

              $scope.removeFacetFilter = function (facetName) {
                delete $scope.search.facetFilters[facetName];
              }
            },
            templateUrl: '/views/component/SearchFacets.html'
        };
    });
