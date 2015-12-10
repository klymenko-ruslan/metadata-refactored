"use strict";

angular.module("ngMetaCrudApp").directive("carfueltypeSearch", ["$log", "restService", function ($log, restService) {
  return {
    "restrict": "E",
    "replace": true,
    "templateUrl": "/views/component/application/carfueltype/search.html",
    "transclude": true,
    "link": function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
      controller.transcludeActionsFn = transcludeFn;
    },
    "controller": ["$log", "$q", "$scope", "carfueltypeSearchService", "ngTableParams", function ($log, $q, $scope, carfueltypeSearchService, ngTableParams) {
      // Latest Results
      $scope.searchResults = null;
      // Applications Table
      $scope.carfueltypeTableParams = new ngTableParams(
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
            carfueltypeSearchService($scope.search).then(
              function (searchResults) {
                $scope.searchResults = searchResults.data;
                // Update the total and slice the result
                $defer.resolve($scope.searchResults.hits.hits);
                params.total($scope.searchResults.hits.total);
              },
              function (errorResponse) {
                $log.log("Couldn't search for 'carfueltype'.");
                $defer.reject();
              }
            );
          }
        }
      );
      // Query Parameters
      $scope.search = {
        "carfueltype": "",
        "facets": {},
        "sort": {}
      };
      $scope.clear = function() {
        $scope.search = {
          "carfueltype": "",
          "facets": {},
          "sort": {}
        };
      };
      // Handle updating search results
      $scope.$watch(
        "[search.carfueltype, search.facets]",
        function (newVal, oldVal) {
          // Debounce
          if (angular.equals(newVal, oldVal, true)) {
            return;
          }
          $scope.carfueltypeTableParams.reload();
        },
        true
      );
    }]
  };
}]
).directive("carfueltypeSearchActions", ["$log", function($log) {
  return {
    "restrict": "A",
    "require": "^carfueltypeSearch",
    "link": function postLink(scope, element, attrs, controller) {
      controller.transcludeActionsFn(scope, function(clone) {
        element.append(clone);
      });
    }
  };
}]);
