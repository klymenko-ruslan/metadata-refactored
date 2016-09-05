"use strict";

angular.module("ngMetaCrudApp").controller("CarModelEngineYearFormCtrl",
  ["$scope", "$log", "$location", "gToast", "restService",  "carEngines", "carMakes", "carModelEngineYear",
  function($scope, $log, $location, gToast, restService, carEngines, carMakes, carModelEngineYear) {

$log.log("Controller, cmeyForm: " + angular.toJson($scope.cmeyForm));

    $scope.setCmeyForm = function(cmeyFormScope) {
      $log.log("Controller: cmeyFormScope=" + angular.toJson(cmeyFormScope, 2));
    }

    $scope.carEngines = carEngines;
    $scope.carMakes = carMakes;
    $scope.carModelEngineYear = carModelEngineYear;
    $scope.cmeyId = null;
    if (carModelEngineYear != null) {
      $scope.cmeyId = carModelEngineYear.id;
    }

    $scope.$on("form:created", function(event, data) {
      if (data.name === "cmeyForm") {
        $scope.cmeyForm = data.controller;
      }
    });

    $scope.onClickViewCmey = function() {
      $location.path("application/carmodelengineyear/" + $scope.cmeyId);
    };

    $scope.save = function() {
      $scope.$broadcast("cmeyform:save", function(promise) {
        if ($scope.cmeyId === null) {
          promise.then(
            function(newCmeyId) {
              $log.log("Created 'car_model_engine_year': " + newCmeyId);
              gToast.open("A new Model Engine Year has been successfully created.");
              $location.path('/application/carmodelengineyear/list');
            },
            function(errorResponse) {
              restService.error("Could not create 'car_model_engine_year'.", errorResponse);
            }
          );
        } else {
          promise.then(
            function(newCmeyId) {
              $log.log("Updated 'car_model_engine_year': " + $scope.cmeyId);
              gToast.open("The Model Engine Year has been successfully updated.");
              $location.path('/application/carmodelengineyear/list');
            },
            function(errorResponse) {
              restService.error("Could not update 'car_model_engine_year': " + $scope.cmeyId, errorResponse);
            }
          );
        }
      });
    };

    $scope.revert = function() {
      $scope.$broadcast("cmeyform:revert");
    };

  }

]);
