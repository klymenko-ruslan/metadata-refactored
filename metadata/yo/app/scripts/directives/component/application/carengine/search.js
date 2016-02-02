"use strict";

angular.module("ngMetaCrudApp").directive("carengineSearch", ["$log", "restService", function ($log, restService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "/views/component/application/carengine/search.html",
      transclude: true,
      link: function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
        controller.transcludeActionsFn = transcludeFn;
      },
      controller: ["$log", "$q", "$scope", "gToast", "dialogs", "ngTableParams",
                    function ($log, $q, $scope, gToast, dialogs, ngTableParams) {
        // Latest Results
        $scope.searchResults = null;

        $scope.remove = function(id, engineSize) {
          dialogs.confirm("Delete car engine '" + engineSize + "'.", "Are you sure?").result.then(
            function() {
              // Yes
              restService.removeCarengine(id).then(
                function () {
                  $scope.clear(); // reload table
                  gToast.open("Car engine '" + name + "' has been successfully removed.");
                },
                function errorResponse(response) {
                  restService.error("Car engine remove failed.", response);
                }
              );
            }
          );
        };

        $scope.carengineTableParams = new ngTableParams(
          {
            page: 1,
            count: 10,
            sorting: {}
          },
          {
            getData: function ($defer, params) {
              // Update the pagination info
              $scope.search.count = params.count();
              $scope.search.page = params.page();
              $scope.search.sorting = params.sorting();
              var offset = params.count() * (params.page() - 1);
              var limit = params.count();
              for (var sortProperty in $scope.search.sorting) break;
              if (sortProperty) {
                var sortOrder = $scope.search.sorting[sortProperty];
              }
              restService.filterCarEngines($scope.search.carengine, $scope.search.aggregations["Fuel Type"], sortProperty, sortOrder, offset, limit).then(
                function (filtered) {
                  $scope.searchResults = filtered;
                  // Update the total and slice the result
                  $defer.resolve($scope.searchResults.hits.hits);
                  params.total($scope.searchResults.hits.total);
                },
                function (errorResponse) {
                  $log.log("Couldn't search for 'carengine'.");
                  $defer.reject();
                }
              );
            }
          }
        );
        // Query Parameters
        $scope.search = {
          carengine: "",
          aggregations: {},
          sort: {}
        };
        $scope.clear = function() {
          $scope.search = {
            carengine: "",
            aggregations: {},
            sort: {}
          };
        };
        // Handle updating search results
        $scope.$watch("[search.carengine, search.aggregations]",
          function (newVal, oldVal) {
            // Debounce
            if (angular.equals(newVal, oldVal, true)) {
              return;
            }
            $scope.carengineTableParams.reload();
          },
          true
        );
      }]
    };
  }]
).directive("carengineSearchActions", ["$log", function($log) {
  return {
    restrict: "A",
    require: "^carengineSearch",
    link: function postLink(scope, element, attrs, controller) {
      controller.transcludeActionsFn(scope, function(clone) {
        element.append(clone);
      });
    }
  };
}]);
