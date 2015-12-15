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
      "controller": ["$log", "$q", "$scope", "dialogs", "carmodelSearchService", "ngTableParams",
                    function ($log, $q, $scope, dialogs, carmodelSearchService, ngTableParams) {
        // Latest Results
        $scope.searchResults = null;

        $scope.remove = function(id, name) {
          dialogs.confirm("Delete car model '" + name + "'.", "Are you sure?").result.then(
            function() {
              // Yes
              restService.removeCarmodel(id).then(
                function () {
                  $scope.clear(); // reload table
                  gToast.open("Car model '" + name + "' has been successfully removed.");
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
              // Update the pagination info
              $scope.search.count = params.count();
              $scope.search.page = params.page();
              $scope.search.sorting = params.sorting();
              carmodelSearchService($scope.search).then(
                function (searchResults) {
                  $scope.searchResults = searchResults.data;
                  // Update the total and slice the result
                  $defer.resolve($scope.searchResults.hits.hits);
                  params.total($scope.searchResults.hits.total);
                },
                function (errorResponse) {
                  $log.log("Couldn't search for 'carmodel'.");
                  $defer.reject();
                }
              );
            }
          }
        );
        // Query Parameters
        $scope.search = {
          "carmodel": "",
          "facets": {},
          "sort": {}
        };
        $scope.clear = function() {
          $scope.search = {
            "carmodel": "",
            "facets": {},
            "sort": {}
          };
        };
        // Handle updating search results
        $scope.$watch("[search.carmodel, search.facets]",
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
