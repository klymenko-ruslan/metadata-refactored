"use strict";

angular.module("ngMetaCrudApp").controller("CarMakeFormCtrl", ["restService", "$q", "$scope", "$location", "$log",
  "$routeParams", "gToast", function(restService, $q, $scope, $location, $log, $routeParams, gToast) {

    $scope.carmake = {};

    $scope.save = function() {
      restService.createCarmake($scope.carmake).then(
        function(carmakeId) {
          $log.log("Carmake has been successfully created: " + carmakeId);
          gToast.open("Carmake '" + $scope.carmake.name + "' has been successfully created.");
          $location.path('/application/carmake/list');
        },
        function (errorResponse) {
          restService.error("Could not create carmake.", response);
        }
      );
    };

  }
]).directive("uniqueCarmakeName", ["$log", "$q", "restService", function($log, $q, restService) {
  // Validator for uniqueness of the carmake name.
  return {
    require: "ngModel",
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.nonUniqueName = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        restService.findCarmakeByName(viewValue).then(
          function(foundCarmake) {
            if (foundCarmake === undefined) {
              def.resolve();
            } else {
              def.reject();
            }
          },
          function (errorResponse) {
            $log.log("Couldn't validate name of the carmake: " + viewValue);
            def.reject();
          }
        );
        return def.promise;
      };
    }
  };
}]);
