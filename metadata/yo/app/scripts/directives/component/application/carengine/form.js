"use strict";

angular.module("ngMetaCrudApp")
  .directive("cengineForm", function() {
    return {
      scope: {
        carEngine: "=",
        carFuelTypes: "="
      },
      restrict: "E",
      replace: false,
      templateUrl: "/views/application/carengine/form.html",
      controller: ["restService", "$scope", "$log",
        function(restService, $scope, $log) {

          $scope.$on("form:created", function(event, data) {
            if (data.name === "carengineForm") {
              $scope.carengineForm = data.controller;
            }
          });

          $scope.carfueltypeFilter = "";

          if ($scope.carEngine !== null) { // edit
            $scope.titleHead = "Edit";
            $scope.carengineId = $scope.carEngine.id;
            $scope.carengine = $scope.carEngine;
          } else { // create
            $scope.titleHead = "Create";
            $scope.carengineId = null;
            $scope.carengine = {};
          }

          $scope.carfueltypes = $scope.carFuelTypes;

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

          $scope._save = function() {
            $scope._merge();
            if ($scope.carengineId == null) {
              // create
              return restService.createCarengine($scope.carengine);
            } else {
              // update
              return restService.updateCarengine($scope.carengine);
            }
          };

          $scope.$on("carengineform:save", function(event, callback) {
            var promise = $scope._save();
            callback(promise);
          });


          $scope.validateForm = function() {
            var fuelTypeId = null;
            if (angular.isObject($scope.carengine.fuelType)) {
              fuelTypeId = $scope.carengine.fuelType.id;
            }
            restService.existsCarengine($scope.carengine.engineSize, fuelTypeId).then(
              function success(exists) {
                if (exists) {
                  $scope.carengineForm.$valid = false;
                  $scope.carengineForm.$invalid = true;
                  $scope.carengineForm.$error.nonUniqueCarEngine = true;
                } else {
                  delete $scope.carengineForm.$error.nonUniqueCarEngine;
                  if (jQuery.isEmptyObject($scope.carengineForm.$error)) {
                    $scope.carengineForm.$valid = true;
                    $scope.carengineForm.$invalid = false;
                  }
                }
              },
              function failure(response) {
                restService.error("Could not validate the car engine.", response);
              }
            );
            /*
            restService.existsCarEngine($scope.cmey.model.id, $scope.cmey.engine.id,
              $scope.cmey.year.name).then(
                function success(exists) {
                  if (exists) {
                    $scope.cmeyForm.$valid = false;
                    $scope.cmeyForm.$invalid = true;
                    $scope.cmeyForm.$error.nonUniqueApp = true;
                  } else {
                    delete $scope.cmeyForm.$error.nonUniqueApp;
                    if (jQuery.isEmptyObject($scope.cmeyForm.$error)) {
                      $scope.cmeyForm.$valid = false;
                      $scope.cmeyForm.$invalid = true;
                    }
                  }
                },
                function failure(response) {
                  restService.error("Could not validate application.", response);
                }
              );
              */
          };

          $scope.$watch("carengine", function() {
            $scope.validateForm();
          }, true);

        }
      ]
    }
  })
  .directive("uniqueCarengineName", ["$log", "$q", "restService", function($log, $q, restService) {
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
