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
      controller: ["$log", "$q", "dialogs", "gToast", "$scope", "cmeySearchService", "ngTableParams",
                    function ($log, $q, dialogs, gToast, $scope, cmeySearchService, ngTableParams) {
        // Latest Results
        $scope.searchResults = null;
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
              $scope.search.count = params.count();
              $scope.search.page = params.page();
              $scope.search.sorting = params.sorting();
              cmeySearchService($scope.search).then(
                function (searchResults) {
                  $scope.searchResults = searchResults.data;
                  // Update the total and slice the result
                  $defer.resolve($scope.searchResults.hits.hits);
                  params.total($scope.searchResults.hits.total);
                },
                function (errorResponse) {
                  $log.log("Couldn't search for 'carmodelengineyear'.");
                  $defer.reject();
                }
              );
            }
          }
        );
        // Query Parameters
        $scope.search = {
          cmey: "",
          facets: {},
          sort: {}
        };
        $scope.clear = function() {
          $scope.search = {
            cmey: "",
            facets: {},
            sort: {}
          };
        };
        // Handle updating search results
        $scope.$watch("[search.cmey, search.facets]",
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
                  gToast.open("Car Model Engine Year has been successfully removed.");
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
