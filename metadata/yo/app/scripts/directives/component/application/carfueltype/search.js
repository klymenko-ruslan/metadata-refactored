"use strict";

angular.module("ngMetaCrudApp").directive("carfueltypeSearch", ["$log", "restService", "gToast", function ($log, restService, gToast) {
  return {
    "restrict": "E",
    "replace": true,
    "templateUrl": "/views/component/application/carfueltype/search.html",
    "transclude": true,
    "link": function postLink(scope, iElement, iAttrs, controller, transcludeFn) {
      controller.transcludeActionsFn = transcludeFn;
    },
    "controller": ["$log", "$q", "$scope", "dialogs", "carfueltypeSearchService", "ngTableParams", function ($log, $q, $scope, dialogs, carfueltypeSearchService, ngTableParams) {
      // Latest Results
      $scope.searchResults = null;

      // Temp storage for quantities
      $scope.modifyValues = {};

      $scope.isModifying = function(carfueltype) {
        return angular.isDefined($scope.modifyValues[carfueltype.id]);
      };

      $scope.modifyStart = function(carfueltype, form) {
        $scope._resetForm(form);
        $scope.modifyValues = {}; // close other edited form
        $scope.modifyValues[carfueltype.id] = carfueltype.name;
      };

      $scope._resetForm = function(form) {
        form.$rollbackViewValue();
        form.$setPristine();
      };

      $scope.modifyCancel = function(carfueltype, form) {
        delete $scope.modifyValues[carfueltype.id];
        $scope._resetForm(form);
      };

      $scope.modifySave = function(carfueltype, form) {
        var name = $scope.modifyValues[carfueltype.id];
        carfueltype.name = name;
        restService.updateCarfueltype(carfueltype).then(
          function() {
            // Success.
            delete $scope.modifyValues[carfueltype.id];
            $scope._resetForm(form);
            gToast.open("The car fuel type '" + name + "' has been successfully updated.");
          },
          function errorResponse(response) {
            restService.error("Car fuel type (id:" + carfueltype.id + ") '" + name + "' update failed.", response);
          }
        );
      };

      $scope.remove = function(id, name) {
        dialogs.confirm("Delete car fuel type '" + name + "'.", "Are you sure?").result.then(
          function() {
            // Yes
            restService.removeCarfueltype(id).then(
              function () {
                $scope.clear(); // reload table
                $scope.carfueltypeTableParams.reload();
                gToast.open("Car fuel type '" + name + "' has been successfully removed.");
              },
              function errorResponse(response) {
                restService.error("Car fuel type '" + name + "' remove failed.", response);
              }
            );
          }
        );
      };

      // CarFuelType Table
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
