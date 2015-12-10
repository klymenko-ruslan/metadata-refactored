"use strict";

angular.module("ngMetaCrudApp").directive("carmakeSearch", function ($log, restService) {
  return {
    "restrict": "E",
    "replace": true,
    "templateUrl": "/views/component/application/carmake/search.html",
    "transclude": true,
    "link": function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
      controller.transcludeActionsFn = transcludeFn;
    },
    "controller": function ($log, $q, $scope, carmakeSearchService, ngTableParams) {
      // Latest Results
      $scope.searchResults = null;
      // Applications Table
      $scope.carmakeTableParams = new ngTableParams(
        {
          "page": 1,
          "count": 10,
          "sorting": {}
        },
        {
          "getData": function ($defer, params) {
            // Update the pagination info
            $scope.search.count = params.count();
            $scope.search.page = params.page();
            $scope.search.sorting = params.sorting();
            carmakeSearchService($scope.search).then(
              function (searchResults) {
                $scope.searchResults = searchResults.data;
                // Update the total and slice the result
                $defer.resolve($scope.searchResults.hits.hits);
                params.total($scope.searchResults.hits.total);
              },
              function (errorResponse) {
                $log.log("Couldn't search for applications.");
                $defer.reject();
              }
            );
          }
        }
      );
      // Query Parameters
      $scope.search = {
        "carmake": "",
        "facets": {},
        "sort": {}
      };
      $scope.clear = function() {
        $scope.search = {
          "carmake": "",
          "facets": {},
          "sort": {}
        };
      };
      // Handle updating search results
      $scope.$watch("[search.carmake, search.facets]", function (newVal, oldVal) {
        // Debounce
        if (angular.equals(newVal, oldVal, true)) {
          return;
        }
        $scope.carmake.reload();
      }, true);
    }
  };
}).directive("carmakeSearchActions", function($log) {
  return {
    "restrict": "A",
    "require": "^carmakeSearch",
    "link": function postLink(scope, element, attrs, controller) {
      controller.transcludeActionsFn(scope, function(clone) {
        element.append(clone);
      });
    }
  };
});
