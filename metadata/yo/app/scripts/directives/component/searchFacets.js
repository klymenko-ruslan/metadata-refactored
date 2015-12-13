"use strict";

angular.module("ngMetaCrudApp")
  .directive("partSearchFacets", ["$log", "partFacets", function($log, partFacets) {
    $log.log("DEBUG: 1. searchFacets");
    return {
      scope: {
        search: "=",
        results: "="
      },
      restrict: "E",
      controller: function($scope) {
        $log.log("DEBUG: 2. searchFacets");
        $scope.Facets = partFacets;
        $scope.isSelected = function(facet) {
          return angular.isDefined($scope.search.facets[facet.name]);
        };
        $scope.select = function(facet, term) {
          $scope.search.facets[facet.name] = term;
        };
      },
      templateUrl: "/views/component/searchFacets.html"
    };
  }]);
