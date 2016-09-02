"use strict";

angular.module("ngMetaCrudApp").controller("CarModelEngineYearFormCtrl",
  ["$scope", "carEngines", "carMakes", "carModelEngineYear",
  function($scope, carEngines, carMakes, carModelEngineYear) {
    $scope.carEngines = carEngines;
    $scope.carMakes = carMakes;
    $scope.carModelEngineYear = carModelEngineYear;
  }
]);
