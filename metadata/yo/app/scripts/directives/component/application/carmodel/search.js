"use strict";

angular.module("ngMetaCrudApp").directive("carmodelSearch", ["$log", "restService", function ($log, restService) {
  return {
    "restrict": "E",
    "replace": true,
    "templateUrl": "/views/component/application/carmodel/search.html",
      "transclude": true,
      "link": function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
        controller.transcludeActionsFn = transcludeFn;
      },
      "controller": ["$log", "$q", "$scope", "toastr", "dialogs", "ngTableParams",
                    function ($log, $q, $scope, toastr, dialogs, ngTableParams) {
        $scope.fltrCarmodel = {
          carmodel: null,
          make: null
        };
        // Latest Results
        $scope.carmodelSearchResults = null;

        $scope.remove = function(id, name) {
          dialogs.confirm("Delete car model '" + name + "'.", "Are you sure?").result.then(
            function() {
              // Yes
              restService.removeCarmodel(id).then(
                function () {
                  $scope.clear(); // reload table
                  toastr.success("Car model '" + name + "' has been successfully removed.");
                },
                function errorResponse(response) {
                  restService.error("Car model remove failed.", response);
                }
              );
            }
          );
        };

        // Car Model Table
        $scope.carmodelTableParams = new ngTableParams(
          {
            "page": 1,
            "count": 10,
            "sorting": {}
          },
          {
            "getData": function ($defer, params) {
              var offset = params.count() * (params.page() - 1);
              var limit = params.count();
              var sortProperty, sortOrder;
              for (sortProperty in params.sorting()) break;
              if (sortProperty) {
                sortOrder = params.sorting()[sortProperty];
              }
              restService.filterCarModels($scope.fltrCarmodel.carmodel, $scope.fltrCarmodel.make,
                  sortProperty, sortOrder, offset, limit).then(
                function (filtered) {
                  $scope.carmodelSearchResults = filtered;
                  // Update the total and slice the result
                  $defer.resolve($scope.carmodelSearchResults.hits.hits);
                  params.total($scope.carmodelSearchResults.hits.total);
                },
                function (errorResponse) {
                  $log.log("Couldn't search for 'carmodel'.");
                  $defer.reject();
                }
              );
            }
          }
        );
        $scope.clear = function() {
          $scope.fltrCarmodel.carmodel = null;
          $scope.fltrCarmodel.make = null;
        };
        // Handle updating search results
        $scope.$watch("[fltrCarmodel]",
          function (newVal, oldVal) {
            // Debounce
            if (angular.equals(newVal, oldVal, true)) {
              return;
            }
            $scope.carmodelTableParams.reload();
          },
          true
        );
      }]
    };
  }]
).directive("carmodelSearchActions", ["$log", function($log) {
  return {
    "restrict": "A",
    "require": "^carmodelSearch",
    "link": function postLink(scope, element, attrs, controller) {
      controller.transcludeActionsFn(scope, function(clone) {
        element.append(clone);
      });
    }
  };
}]);
