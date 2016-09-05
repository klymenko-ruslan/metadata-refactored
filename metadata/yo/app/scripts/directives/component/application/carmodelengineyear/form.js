"use strict";

angular.module("ngMetaCrudApp")
  .directive("cmeyForm", function() {
    return {
      scope: {
        carEngines: "=",
        carMakes: "=",
        carModelEngineYear: "="
      },
      restrict: "E",
      replace: false,
      templateUrl: "/views/application/carmodelengineyear/form.html",
      controller: ["restService", "$q", "$scope", "$location", "$parse", "$log", "$routeParams", "gToast",
        "$uibModal",
        function(restService, $q, $scope, $location, $parse, $log, $routeParams, gToast, $uibModal) {

          var makeIdGetter = $parse("cmey.model.make.id");

          $scope.onChangeMake = function() {
            //$scope.carmodels = [];
            var makeId = makeIdGetter($scope);
            if (makeId === undefined || makeId === null) {
              return;
            }
            restService.findCarModelsOfMake(makeId).then(
              function(carmodels) {
                $scope.carmodels = carmodels;
              },
              function(errorResponse) {
                restService.error("Could not load car models for car make: " + makeId, errorResponse);
              }
            );
          };

          $scope.carYearExists = null;
          $scope.cmey = {}; // Caveat. Don't use null to init because binding will not work.
          $scope.origCmey = null;

          $scope.carengines = Array();
          angular.forEach($scope.carEngines, function(ce) {
            var name = ce.engineSize;
            var fuelType = ce.fuelType;
            if (angular.isObject(fuelType)) {
              name += (", " + fuelType.name);
            }
            $scope.carengines.push({
              "id": ce.id,
              "name": name
            });
          });

          $scope.carmakes = $scope.carMakes;
          $scope.carmodels = [];
          $scope.cmeyId = $routeParams.id;

          if ($scope.cmeyId === undefined) { // create
            $scope.titleHead = "Create";
            $scope.cmey = {
              model: {
                id: null,
                make: {
                  id: null
                }
              },
              engine: {
                id: null
              },
              year: {
                name: null
              }
            };
            $scope.carYearExists = false;
          } else { // edit
            $scope.titleHead = "Edit";
            $scope.origCmey = $scope.carModelEngineYear;
            angular.copy($scope.origCmey, $scope.cmey);
            $scope.onChangeMake();
            $scope.carYearExists = true;
          }

          $scope._revert = function() {
            if (angular.isObject($scope.origCmey)) {
              angular.copy($scope.origCmey, $scope.cmey);
              $scope.onChangeMake();
            }
          };

          $scope._merge = function() {
            var cmey2 = {};
            if ($scope.cmeyId !== null && $scope.cmeyId !== undefined) {
              cmey2.id = $scope.cmeyId;
            }
            if (angular.isObject($scope.cmey.model) && $scope.cmey.model.id) {
              cmey2.model = {
                "id": $scope.cmey.model.id,
              };
            }
            if (angular.isObject($scope.cmey.engine) && $scope.cmey.engine.id) {
              cmey2.engine = {
                "id": $scope.cmey.engine.id
              };
            }
            if (angular.isObject($scope.cmey.year) && $scope.cmey.year.name) {
              cmey2.year = {
                "name": $scope.cmey.year.name
              };
            }
            return cmey2;
          };

          $scope._save = function() {
            //$log.log("To save (raw): " + angular.toJson($scope.cmey));
            var cmey2 = $scope._merge();
            if (_.isEmpty(cmey2)) {
              // Ignore.
              gToast.open("Nothing to save. Ignored.");
              return;
            }
            $log.log("To save (normalized): " + angular.toJson(cmey2));
            if ($scope.cmeyId === undefined) {
              return restService.createCarmodelengineyear(cmey2);
            } else {
              return restService.updateCarmodelengineyear(cmey2);
            }
          };

          $scope.$on("cmeyform:save", function(event, callback) {
            var promise = $scope._save();
            callback(promise);
          });

          $scope.$on("cmeyform:revert", function() {
            $scope._revert();
          });

          $scope.onClearMM = function(form) {
            $scope.cmey.model.make.id = null;
            $scope.cmey.model.id = null;
            form.$setDirty();
          };

          $scope.onClearEngine = function(form) {
            $scope.cmey.engine.id = null;
            form.$setDirty();
          };

          $scope.onClearYear = function(form) {
            $scope.cmey.year.id = null;
            $scope.cmey.year.name = null;
          };

          $scope.onChangeYear = function() {
            var year = $scope.cmey.year;
            if (!year) return;
            var year_name = year.name;
            if (!year_name) return;
            restService.findCarYearByName(year_name).then(
              function(caryear) {
                $scope.carYearExists = angular.isObject(caryear);
              },
              function(errorResponse) {
                $log.log("Cat't validate the 'car year'. Error: " + angular.toJson(errorResponse));
              }
            );
          };

          $scope.quickCreateCarMake = function() {
            $uibModal.open({
              templateUrl: "/views/application/carmodelengineyear/createCarMakeDlg.html",
              animation: false,
              size: "lg" ,
              controller: "createCarMakeDlgCtrl",
              resolve: {
                addCarMakeCallback: function() {
                  return function(newCarMake) {
                    if (_.isArray($scope.carmakes)) {
                      var pos = _.sortedIndex($scope.carmakes, newCarMake, "name");
                      $scope.carmakes.splice(pos, 0, newCarMake);
                      $scope.cmey.model.make = newCarMake;
                    }
                  }
                }
              }
            });
          };

          $scope.quickCreateCarModel = function() {
            alert("TODO: quickCreateCarModel");
          };

          $scope.quickCreateCarEngine = function() {
            alert("TODO: quickCreateCarEngine");
          };


        }
      ]

    };
  })
  .controller("createCarMakeDlgCtrl",["$scope", "$log", "gToast", "$uibModalInstance", "addCarMakeCallback",
    function($scope, $log, gToast, $uibModalInstance, addCarMakeCallback) {

    $scope.$on("form:created", function(event, data) {
      if (data.name === "carmakeForm") {
        $scope.carmakeForm = data.controller;
      }
    });

    $scope.save = function() {
      $scope.$broadcast("carmakeform:save", function(promise) {
        promise.then(
          function(carMake) {
            $log.log("Carmake has been successfully created: " + carMake.id);
            gToast.open("Carmake [" + carMake.id + "] has been successfully created.");
            addCarMakeCallback(carMake);
            $scope.close ();
          },
          function (errorResponse) {
            $scope.close ();
            restService.error("Could not create carmake.", response);
          }
        );
      });
    };

    $scope.close = function() {
      $uibModalInstance.close();
    };

  }]);
