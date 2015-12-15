"use strict";

angular.module("ngMetaCrudApp").directive("carmakeSearch", ["$log", "restService", "gToast", function ($log, restService, gToast) {
  return {
    "restrict": "E",
    "replace": true,
    "templateUrl": "/views/component/application/carmake/search.html",
    "transclude": true,
    "link": function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
      controller.transcludeActionsFn = transcludeFn;
    },
    "controller": ["$log", "$q", "$scope", "dialogs", "carmakeSearchService", "ngTableParams", function ($log, $q, $scope, dialogs, carmakeSearchService, ngTableParams) {
      // Latest Results
      $scope.searchResults = null;

      // Temp storage for quantities
      $scope.modifyValues = {};

      $scope.isModifying = function(carmake) {
        return angular.isDefined($scope.modifyValues[carmake.id]);
      };

      $scope.modifyStart = function(carmake, form) {
        $scope._resetForm(form);
        $scope.modifyValues = {}; // close other edited form
        $scope.modifyValues[carmake.id] = carmake.name;
      };

      $scope._resetForm = function(form) {
        form.$rollbackViewValue();
        form.$setPristine();
      };

      $scope.modifyCancel = function(carmake, form) {
        delete $scope.modifyValues[carmake.id];
        $scope._resetForm(form);
      };

      $scope.modifySave = function(carmake, form) {
        var name = $scope.modifyValues[carmake.id];
        carmake.name = name;
        restService.updateCarmake(carmake).then(
          function() {
            // Success.
            delete $scope.modifyValues[carmake.id];
            $scope._resetForm(form);
            gToast.open("The car make '" + name + "' has been successfully updated.");
          },
          function errorResponse(response) {
            restService.error("Car make (id:" + carmake.id + ") '" + name + "' update failed.", response);
          }
        );
      };

      $scope.remove = function(id, name) {
        dialogs.confirm("Delete car make '" + name + "'.", "Are you sure?").result.then(
          function() {
            // Yes
            restService.removeCarmake(id).then(
              function () {
                $scope.clear(); // reload table
                gToast.open("Car make '" + name + "' has been successfully removed.");
              },
              function errorResponse(response) {
                restService.error("Car make '" + name + "' remove failed.", response);
              }
            );
          }
        );
      };

      // CarMake Table
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
                $log.log("Couldn't search for 'carmake'.");
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
      $scope.$watch(
        "[search.carmake, search.facets]",
        function (newVal, oldVal) {
          // Debounce
          if (angular.equals(newVal, oldVal, true)) {
            return;
          }
          $scope.carmakeTableParams.reload();
        },
        true
      );
    }]
  };
}]
).directive("carmakeSearchActions", ["$log", function($log) {
  return {
    "restrict": "A",
    "require": "^carmakeSearch",
    "link": function postLink(scope, element, attrs, controller) {
      controller.transcludeActionsFn(scope, function(clone) {
        element.append(clone);
      });
    }
  };
}]);
