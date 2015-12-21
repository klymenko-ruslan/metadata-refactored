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

    $scope._merge = function () {
      var cmey2 = {
        "id": $scope.cmeyId
      };
      if ($scope.cmey.model != null && $scope.cmey.model.id) {
        cmey2["model"] = {
          "id": $scope.cmey.model.id,
        };
      }
      if ($scope.cmey.engine != null && $scope.cmey.engine.id) {
        cmey2["engine"] = {
          "id": $scope.cmey.engine.id
        };
      }
      if ($scope.cmey.year != null && $scope.cmey.year.name) {
        cmey2["year"] = {
          "name": $scope.cmey.year.name
        };
      }
      return cmey2;
    };

    $scope.save = function () {
      $log.log("To save (raw): " + angular.toJson($scope.cmey));
      var cmey2 = $scope._merge();
      $log.log("To save (normalized): " + angular.toJson(cmey2));
      if ($scope.cmeyId == null) {
        restService.createCarmodelengineyear(cmey2).then(
          function (newCmeyId) {
            $log.log("Created 'car_model_engine_year': " + newCmeyId);
            gToast.open("A new Model Engine Year has been successfully created.");
            $location.path('/application/carmodelengineyear/list');
          },
          function (errorResponse) {
            restService.error("Could not create 'car_model_engine_year'.", errorResponse);
          }
        );
      } else {
        restService.updateCarmodelengineyear(cmey2).then(
          function (newCmeyId) {
            $log.log("Updated 'car_model_engine_year': " + $scope.cmeyId);
            gToast.open("The Model Engine Year has been successfully updated.");
            $location.path('/application/carmodelengineyear/list');
          },
          function (errorResponse) {
            restService.error("Could not update 'car_model_engine_year': " + $scope.cmeyId, errorResponse);
          }
        );
      }
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
