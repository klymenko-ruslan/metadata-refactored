"use strict";

angular.module("ngMetaCrudApp").controller("CarModelFormCtrl", ["restService", "$q", "$scope", "$location", "$log",
  "$routeParams", "gToast", function(restService, $q, $scope, $location, $log, $routeParams, gToast) {

    $scope.carmakeFilter = "";

    if ($routeParams.id) { // edit
      $scope.titleHead = "Edit";
      $scope.carmodelId = $routeParams.id;
      restService.findCarmodel($scope.carmodelId).then(
        function (foundCarmodel) {
          $scope.carmodel = foundCarmodel;
        },
        function (errorResponse) {
          restService.error("Could not read car model (id:" + $scope.carmodelId + ").", response);
        }
      );
    } else { // create
      $scope.titleHead = "Create";
      $scope.carmodelId = null;
      $scope.carmodel = {};
    }

    restService.findAllCarMakesOrderedByName().then(
      function (carmakes) {
        $scope.carmakes = carmakes;
      },
      function (errorResponse) {
        restService.error("Could not load car makes.", response);
      }
    );

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

    $scope.save = function() {
      $scope._merge();
      if ($scope.carmodelId == null) {
        // create
        restService.createCarmodel($scope.carmodel).then(
          function(carmodelId) {
            $log.log("Carmodel has been successfully created: " + carmodelId);
            gToast.open("Car model '" + $scope.carmodel.name + "' has been successfully created.");
            $location.path('/application/carmodel/list');
          },
          function (errorResponse) {
            restService.error("Could not create car model.", response);
          }
        );
      } else {
        // update
        restService.updateCarmodel($scope.carmodel).then(
          function() {
            $log.log("Carmodel '" + $scope.carmodel.name + "' has been successfully updated.");
            gToast.open("Car model '" + $scope.carmodel.name + "' has been successfully updated.");
            $location.path('/application/carmodel/list');
          },
          function (errorResponse) {
            restService.error("Could not update car model '" + $scope.carmodel.name + "'.", response);
          }
        );
      }
    };

  }
]).directive("uniqueCarmodelName", ["$log", "$q", "restService", function($log, $q, restService) {
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
  };
}]);
