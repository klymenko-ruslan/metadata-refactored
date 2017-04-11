"use strict";

angular.module("ngMetaCrudApp")
  .directive("cmodelForm", function() {
    return {
      scope: {
        carMakeId: "=",
        carModel: "=",
        carMakes: "="
      },
      restrict: "E",
      replace: false,
      templateUrl: "/views/application/carmodel/form.html",
      controller: ["restService", "$scope", "$location", "$log",
        function(restService, $scope, $location, $log) {

          $scope.carmakeFilter = "";

          if ($scope.carModel === null) { // edit
            $scope.titleHead = "Create";
            $scope.carmodelId = null;
            $scope.carmodel = {};
          } else { // create
            $scope.titleHead = "Edit";
            $scope.carmodel = $scope.carModel;
            $scope.carmodelId = $scope.carModel.id;
          }

          $scope.carmakes = $scope.carMakes;

          if ($scope.carMakeId !== null && $scope.carMakeId !== undefined && $scope.carMakes) {
            var pos = _.findIndex($scope.carMakes, function(a){return a.id == $scope.carMakeId;});
            if (pos > 0) {
              $scope.carmodel.make = $scope.carMakes[pos];
            }
          }

          $scope._merge = function() {
            var carmakeId = $scope.carmodel.make.id;
            var carmakes = $scope.carmakes;
            var n = carmakes.length;
            var makeName = null;
            for (var i = 0; i < n; i++) {
              if (carmakes[i].id == carmakeId) {
                makeName = carmakes[i].name;
              }
            }
            $scope.carmodel.make.name = makeName;
          };

          $scope._save = function() {
            $scope._merge();
            if ($scope.carmodelId == null) {
              // create
              return restService.createCarmodel($scope.carmodel);
            } else {
              // update
              return restService.updateCarmodel($scope.carmodel);
            }
          };

          $scope.$on("carmodelform:save", function(event, callback) {
            var promise = $scope._save();
            callback(promise);
          });

        }
      ]
    }
  })
  .directive("uniqueCarmodelRec", ["$log", "$q", "restService", function($log, $q, restService) {
    // Validator for uniqueness of the carmodel name.
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
    }
  }]);
