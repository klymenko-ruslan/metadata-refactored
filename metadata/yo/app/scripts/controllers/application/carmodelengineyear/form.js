"use strict";

angular.module("ngMetaCrudApp").controller("CarModelEngineYearFormCtrl", ["restService", "$q", "$scope", "$location",
  "$log", "$routeParams", "gToast", function(restService, $q, $scope, $location, $log, $routeParams, gToast) {

    $scope.carmodels = null;

    $scope.onChangeMake = function () {
      var makeId = $scope.cmey.model.make.id;
      restService.findCarModelsOfMake(makeId).then(
        function (carmodels) {
          $scope.carmodels = carmodels;
        },
        function (errorResponse) {
          restService.error("Could not load car models for car make: " + makeId, errorResponse);
        }
      );
    };

    restService.findAllCarMakesOrderedByName().then(
      function (carmakes) {
        $scope.carmakes = carmakes;
        if ($routeParams.id) { // edit
          $scope.titleHead = "Edit";
          $scope.cmeyId = $routeParams.id;
          restService.findCarmodelengineyear($scope.cmeyId).then(
            function (foundCmey) {
              $scope.cmey = foundCmey;
              $scope.onChangeMake();
            },
            function (errorResponse) {
              restService.error("Could not read car model engine year (id:" + $scope.cmeyId + ").", errorResponse);
            }
          );
        } else { // create
          $scope.titleHead = "Create";
          $scope.cmeyId = null;
          $scope.cmey = {};
        }
      },
      function (errorResponse) {
        restService.error("Could not load car makes.", errorResponse);
      }
    );

    $scope.save = function () {
      alert("TODO");
    };

    $scope.remove = function () {
      alert("TODO");
    };

  }
]);
