"use strict";

angular.module("ngMetaCrudApp").controller("CarEngineFormCtrl", ["restService", "$q", "$scope", "$location", "$log",
  "$routeParams", "gToast", function(restService, $q, $scope, $location, $log, $routeParams, gToast) {

    $scope.carfueltypeFilter = "";

    if ($routeParams.id) { // edit
      $scope.titleHead = "Edit";
      $scope.carengineId = $routeParams.id;
      restService.findCarengine($scope.carengineId).then(
        function (foundCarengine) {
          $scope.carengine = foundCarengine;
        },
        function (errorResponse) {
          restService.error("Could not read car engine (id:" + $scope.carengineId + ").", response);
        }
      );
    } else { // create
      $scope.titleHead = "Create";
      $scope.carengineId = null;
      $scope.carengine = {};
    }

    restService.findAllCarFuelTypesOrderedByName().then(
      function (carfueltypes) {
        $scope.carfueltypes = carfueltypes;
      },
      function (errorResponse) {
        restService.error("Could not load car makes.", response);
      }
    );

    $scope._merge = function() {
      var carfueltypeId = $scope.carengine.fuelType.id;
      var carfueltypes = $scope.carfueltypes;
      var n = carfueltypes.length;
      var fueltypeName = null;
      for (var i = 0; i < n; i++) {
        if (carfueltypes[i].id == carfueltypeId) {
          fueltypeName = carfueltypes[i].name;
        }
      }
      $scope.carengine.fuelType.name = fueltypeName;
    };

    $scope.save = function() {
      $scope._merge();
      if ($scope.carengineId == null) {
        // create
        restService.createCarengine($scope.carengine).then(
          function(carengineId) {
            $log.log("Carengine has been successfully created: " + carengineId);
            gToast.open("Car engine '" + $scope.carengine.engineSize + "' has been successfully created.");
            $location.path('/application/carengine/list');
          },
          function (errorResponse) {
            restService.error("Could not create car engine.", response);
          }
        );
      } else {
        // update
        restService.updateCarengine($scope.carengine).then(
          function() {
            $log.log("Car engine '" + $scope.carengine.engineSize + "' has been successfully updated.");
            gToast.open("Car engine '" + $scope.carengine.engineSize + "' has been successfully updated.");
            $location.path('/application/carengine/list');
          },
          function (errorResponse) {
            restService.error("Could not update car engine '" + $scope.carengine.engineSize + "'.", response);
          }
        );
      }
    };

  }
]).directive("uniqueCarengineName", ["$log", "$q", "restService", function($log, $q, restService) {
  // Validator for uniqueness of the carengine name.
  return {
    require: "ngModel",
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.nonUniqueName = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        def.resolve();
        /*restService.findCarmodelByName(viewValue).then(
          function(foundCarmodel) {
            if (foundCarmodel === undefined) {
              def.resolve();
            } else {
              def.reject();
            }
          },
          function (errorResponse) {
            $log.log("Couldn't validate name of the car model: " + viewValue);
            def.reject();
          }
        );*/
        return def.promise;
      };
    }
  };
}]);
