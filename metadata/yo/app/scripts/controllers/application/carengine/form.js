"use strict";

angular.module("ngMetaCrudApp").controller("CarEngineFormCtrl", ["restService", "$scope", "$location", "$log", "toastr",
  "carEngine", "carFuelTypes", function(restService, $scope, $location, $log, toastr, carEngine, carFuelTypes) {

    $scope.carFuelTypes = carFuelTypes;

    $scope.carEngine = carEngine;
    $scope.carengineId = null;
    if (carEngine != null) {
      $scope.carengineId = carEngine.id;
    }

    $scope.$on("form:created", function(event, data) {
      if (data.name === "carengineForm") {
        $scope.carengineForm = data.controller;
      }
    });

    $scope.save = function() {
      $scope.$broadcast("carengineform:save", function(promise) {
        if ($scope.carengineId === null) {
          promise.then(
            function(carEngine) {
              $log.log("Carengine has been successfully created: " + carEngine.id);
              toastr.success("Car engine [" + carEngine.id + "] - '" + carEngine.engineSize + "' has been successfully created.");
              $location.path('/application/carengine/list');
            },
            function (errorResponse) {
              restService.error("Could not create car engine.", response);
            }
          );
        } else {
          promise.then(
            function() {
              $log.log("Car engine [" + $scope.carEngine.id + "] - '" + $scope.carEngine.engineSize + "' has been successfully updated.");
              toastr.success("Car engine [" + $scope.carEngine.id + "] - '" + $scope.carEngine.engineSize + "' has been successfully updated.");
              $location.path('/application/carengine/list');
            },
            function (errorResponse) {
              restService.error("Could not update car engine '" + $scope.carengine.engineSize + "'.", response);
            }
          );
        }
      });
    };
  }
]);
