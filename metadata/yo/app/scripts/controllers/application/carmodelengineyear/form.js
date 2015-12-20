"use strict";

angular.module("ngMetaCrudApp").controller("CarModelEngineYearFormCtrl", ["restService", "$q", "$scope", "$location",
  "$log", "$routeParams", "gToast", function(restService, $q, $scope, $location, $log, $routeParams, gToast) {

    $scope.cmeyId = null;
    $scope.carmodels = null;
    $scope.carYearExists = null;
    $scope.origCmey = null;
    $scope.cmey = {};

    restService.findAllCarEnginesOrderedByName().then(
      function (cengines) {
        $scope.carengines = Array();
        angular.forEach(cengines, function(ce) {
          var name = ce.engineSize;
          var fuelType = ce.fuelType;
          if (fuelType != null) {
            name += (", " + fuelType.name);
          }
          $scope.carengines.push({"id": ce.id, "name": name});
        });
        restService.findAllCarMakesOrderedByName().then(
          function (carmakes) {
            $scope.carmakes = carmakes;
            if ($routeParams.id) { // edit
              $scope.titleHead = "Edit";
              $scope.cmeyId = $routeParams.id;
              restService.findCarmodelengineyear($scope.cmeyId).then(
                function (foundCmey) {
                  $scope.origCmey = foundCmey;
$log.log("foundCmey: " + angular.toJson(foundCmey));
                  angular.copy($scope.origCmey, $scope.cmey);
$log.log("cmey: " + angular.toJson($scope.cmey));
                  $scope.onChangeMake();
                  $scope.carYearExists = true;
                },
                function (errorResponse) {
                  restService.error("Could not read car model engine year (id:" + $scope.cmeyId + ").", errorResponse);
                }
              );
            } else { // create
              $scope.titleHead = "Create";
              $scope.cmeyId = null;
              $scope.cmey = {};
              $scope.carYearExists = true;
            }
          },
          function (errorResponse) {
            restService.error("Could not load car makes.", errorResponse);
          }
        )
      },
      function (errorResponse) {
        restService.error("Could not load car engines.", errorResponse);
      }
    );

    $scope.revert = function () {
      if ($scope.origCmey != null) {
        angular.copy($scope.origCmey, $scope.cmey);
        $scope.onChangeMake();
      }
    };

    $scope.save = function () {
      $log.log("To save: " + angular.toJson($scope.cmey));
    };

    $scope.remove = function () {
      alert("TODO");
    };

    $scope.onChangeMake = function () {
      $scope.carmodels = null;
      var model = $scope.cmey.model;
      if (!model) return;
      var make = model.make;
      if (!make) return;
      var makeId = make.id;
      if (!makeId) return;
      restService.findCarModelsOfMake(makeId).then(
        function (carmodels) {
          $scope.carmodels = carmodels;
        },
        function (errorResponse) {
          restService.error("Could not load car models for car make: " + makeId, errorResponse);
        }
      );
    };

    $scope.onClearModel = function (form) {
      $scope.cmey.model.id = null;
      form.$setDirty();
    };

    $scope.onClearEngine = function(form) {
      $scope.cmey.engine.id = null;
      form.$setDirty();
    };

    $scope.onChangeYear = function () {
      var year = $scope.cmey.year;
      if (!year) return;
      var year_name = year.name;
      if (!year_name) return;
      restService.findCarYearByName(year_name).then(
        function (caryear) {
          $scope.carYearExists = caryear != null;
        },
        function (errorResponse) {
          $log.log("Cat't validate the 'car year'. Error: " + angular.toJson(errorResponse));
        }
      );
    };

  }
]);
