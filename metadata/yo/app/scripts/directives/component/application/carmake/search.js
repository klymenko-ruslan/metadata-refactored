"use strict";

angular.module("ngMetaCrudApp").directive("carmakeSearch", ["$log", "restService", "toastr", function ($log, restService, toastr) {
  return {
    "restrict": "E",
    "replace": true,
    "templateUrl": "/views/component/application/carmake/search.html",
    "transclude": true,
    "link": function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
      controller.transcludeActionsFn = transcludeFn;
    },
    "controller": ["$log", "$q", "$scope", "dialogs", "NgTableParams", function ($log, $q, $scope, dialogs, NgTableParams) {
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
            toastr.success("The car make '" + name + "' has been successfully updated.");
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
                toastr.success("Car make '" + name + "' has been successfully removed.");
              },
              function errorResponse(response) {
                restService.error("Car make '" + name + "' remove failed.", response);
              }
            );
          }
        );
      };

      // CarMake Table
      $scope.carmakeTableParams = new NgTableParams(
        {
          "page": 1,
          "count": 10,
          "sorting": {}
        },
        {
          "getData": function (params) {
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
            return restService.filterCarMakes($scope.search.carmake, sortProperty, sortOrder, offset, limit).then(
              function (filtered) {
                $scope.searchResults = filtered;
                // Update the total and slice the result
                params.total($scope.searchResults.hits.total);
                return $scope.searchResults.hits.hits;
              },
              function (errorResponse) {
                $log.log("Couldn't search for 'carmake'.");
              }
            );
          }
        }
      );
      // Query Parameters
      $scope.search = {
        "carmake": "",
        "aggregations": {},
        "sort": {}
      };
      $scope.clear = function() {
        $scope.search = {
          "carmake": "",
          "aggregations": {},
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
