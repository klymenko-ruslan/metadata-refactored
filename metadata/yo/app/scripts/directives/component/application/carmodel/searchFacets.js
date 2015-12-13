"use strict";

angular.module("ngMetaCrudApp")
  .directive("carmodelSearchFacets", ["$log", "carmodelFacets", function($log, carmodelFacets) {
    return {
      "scope": {
        "search": "=",
        "results": "="
      },
      "restrict": "E",
      "controller": function($scope) {
        $scope.Facets = carmodelFacets;
        $scope.isSelected = function(facet) {
          return angular.isDefined($scope.search.facets[facet.name]);
        };
        $scope.select = function(facet, term) {
          $scope.search.facets[facet.name] = term;
        };
      },
      "templateUrl": "/views/component/searchFacets.html"
    };
  }]);
