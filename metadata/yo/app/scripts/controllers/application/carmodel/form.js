"use strict";

angular.module("ngMetaCrudApp").controller("CarModelFormCtrl", ["restService", "$scope", "$location", "$log",
  "toastr", "carModel", "carMakes",
  function(restService, $scope, $location, $log, toastr, carModel, carMakes) {

    $scope.carMakes = carMakes;

    $scope.carModel = carModel;
    $scope.carmodelId = null;
    if (carModel != null) {
      $scope.carmodelId = carModel.id;
    }

    $scope.$on("form:created", function(event, data) {
      if (data.name === "carmodelForm") {
        $scope.carmodelForm = data.controller;
      }
    });

    $scope.save = function() {
      $scope.$broadcast("carmodelform:save", function(promise) {
        if ($scope.carmodelId === null) {
          promise.then(
            function(carModel) {
              $log.log("Carmodel has been successfully created: " + $scope.carmodelId);
              toastr.success("Car model [" + carModel.id + "] - '" + carModel.name + "' has been successfully created.");
              $location.path("/application/carmodel/list");
            },
            function (errorResponse) {
              restService.error("Could not create car model.", response);
            }
          );
        } else {
          promise.then(
            function() {
              $log.log("Carmodel [" + $scope.carModel.id + "] - '" + $scope.carModel.name + "' has been successfully updated.");
              toastr.success("Car model [" + $scope.carModel.id + "] '" + $scope.carModel.name + "' has been successfully updated.");
              $location.path("/application/carmodel/list");
            },
            function (errorResponse) {
              restService.error("Could not update car model '" + $scope.carModel.name + "'.", response);
            }
          );
        }
      });
    };

  }
]);
