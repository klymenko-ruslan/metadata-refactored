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
      controller: ["$log", "$q", "$scope", "toastr", "dialogs", "NgTableParams",
                    function ($log, $q, $scope, toastr, dialogs, NgTableParams) {

        $scope.fltrCarengine = {
          carengine: null,
          fueltype: null
        };
        // Latest Results
        $scope.carengineSearchResults = null;

        $scope.remove = function(id, engineSize) {
          dialogs.confirm("Delete car engine '" + engineSize + "'.", "Are you sure?").result.then(
            function() {
              // Yes
              restService.removeCarengine(id).then(
                function () {
                  $scope.clear(); // reload table
                  toastr.success("Car engine '" + name + "' has been successfully removed.");
                },
                function errorResponse(response) {
                  restService.error("Car engine remove failed.", response);
                }
              );
            }
          );
        };

        $scope.carengineTableParams = new NgTableParams(
          {
            page: 1,
            count: 10,
            sorting: {}
          },
          {
            getData: function (params) {
              var offset = params.count() * (params.page() - 1);
              var limit = params.count();
              var sortProperty, sortOrder;
              for (sortProperty in params.sorting()) break;
              if (sortProperty) {
                sortOrder = params.sorting()[sortProperty];
              }
              restService.filterCarEngines($scope.fltrCarengine.carengine, $scope.fltrCarengine.fueltype,
                  sortProperty, sortOrder, offset, limit).then(
                function (filtered) {
                  $scope.carengineSearchResults = filtered;
                  // Update the total and slice the result
                  params.total($scope.carengineSearchResults.hits.total);
                  return $scope.carengineSearchResults.hits.hits;
                },
                function (errorResponse) {
                  $log.log("Couldn't search for 'carengine'.");
                }
              );
            }
          }
        );

        $scope.clear = function() {
          $scope.fltrCarengine.carengine = null;
          $scope.fltrCarengine.fueltype = null;
        };

        // Handle updating search results
        $scope.$watch("[fltrCarengine]",
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
