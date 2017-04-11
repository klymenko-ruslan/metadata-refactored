"use strict";

angular.module("ngMetaCrudApp").directive("cmeySearch", ["$log", "restService", function ($log, restService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "/views/component/application/carmodelengineyear/search.html",
      transclude: true,
      link: function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
        controller.transcludeActionsFn = transcludeFn;
      },
      controller: ["$log", "$q", "dialogs", "toastr", "$scope", "ngTableParams",
                    function ($log, $q, dialogs, toastr, $scope, ngTableParams) {

        $scope.fltrCmey = {
          cmey: null,
          year: null,
          make: null,
          model: null,
          engine: null,
          fueltype: null
        };
        // Latest Results
        $scope.cmeySearchResults = null;
        // Applications Table
        $scope.cmeyTableParams = new ngTableParams(
          {
            page: 1,
            count: 10,
            sorting: {}
          },
          {
            getData: function ($defer, params) {
              // Update the pagination info
              var offset = params.count() * (params.page() - 1);
              var limit = params.count();
              var sortProperty, sortOrder;
              for (sortProperty in params.sorting()) break;
              if (sortProperty) {
                sortOrder = params.sorting()[sortProperty];
              }
              restService.filterCarModelEngineYears($scope.fltrCmey.cmey,
                  $scope.fltrCmey.year, $scope.fltrCmey.make,
                  $scope.fltrCmey.model, $scope.fltrCmey.engine,
                  $scope.fltrCmey.fueltype,
                  sortProperty, sortOrder, offset, limit).then(
                function (filtered) {
                  $scope.cmeySearchResults = filtered;
                  // Update the total and slice the result
                  $defer.resolve($scope.cmeySearchResults.hits.hits);
                  params.total($scope.cmeySearchResults.hits.total);
                },
                function (errorResponse) {
                  $log.log("Couldn't search for 'carmodelengineyear'.");
                  $defer.reject();
                }
              );
            }
          }
        );

        $scope.clear = function() {
          $scope.fltrCmey.cmey = null;
          $scope.fltrCmey.year = null;
          $scope.fltrCmey.make = null;
          $scope.fltrCmey.model = null;
          $scope.fltrCmey.engine = null;
          $scope.fltrCmey.fueltype = null;
        };

        // Handle updating search results
        $scope.$watch("[fltrCmey]",
          function (newVal, oldVal) {
            // Debounce
            if (angular.equals(newVal, oldVal, true)) {
              return;
            }
            $scope.cmeyTableParams.reload();
          },
          true
        );

        $scope.remove = function (id) {
          dialogs.confirm("Delete Model Engine Year.", "Are you sure?").result.then(
            function() {
              // Yes
              restService.removeCarmodelengineyear(id).then(
                function () {
                  $scope.clear(); // reload table
                  toastr.success("Car Model Engine Year has been successfully removed.");
                },
                function errorResponse(response) {
                  restService.error("Car Model Engine Year remove failed.", response);
                }
              );
            }
          );
        };

      }]
    };
  }]
).directive("cmeySearchActions", ["$log", function($log) {
  return {
    restrict: "A",
    require: "^cmeySearch",
    link: function postLink(scope, element, attrs, controller) {
      controller.transcludeActionsFn(scope, function(clone) {
        element.append(clone);
      });
    }
  };
}]);
