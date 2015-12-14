"use strict";

angular.module("ngMetaCrudApp").controller("CarMakeFormCtrl", ["$q", "$scope", "$location", "$log", "$routeParams",
  "restService",
  function($q, $scope,
    $location, $log, $routeParams, ngTableParams,
    restService) {
    // Setup the create/update workflow
    if ($routeParams.id) {
      $scope.carmakeId = $routeParams.id;
      $scope.carmake = {};
//      $scope.oldPartPromise = restService.findPart($scope.partId).then(
//        function(part) {
//          console.log("Part data loaded.");
//
//          // Save the part
//          $scope.part = part;
//          $scope.oldPart = Restangular.copy(part);
//        },
//        function(response) {
//          restService.error("Could not get part data from the server.", response);
//        });
    } else {
      $scope.carmakeId = null;
      $scope.carmake = {};

    }

    $scope.revert = function() {
      $scope.carmake = angular.copy($scope.oldCarmake);
      $scope.carmakeForm.$setPristine(true);
      $scope.$broadcast("revert");
    }

    $scope.save = function() {
      if ($scope.carmakeId) {
        // TODO
        $log.log("TODO");
      } else {
        $scope.carmakeId = restService.createCarmake($scope.carmake);
        $log.log("Created carmake: " + $scope.carmakeId);
      }

//      if ($scope.oldCarmake == null) {
//        Restangular.all('application/carmake').post($scope.carmake).then(
//          function(id) {
//            $location.path('/applciation/camake/list');
//          },
//          function(response) {
//            restService.error("Could not save 'carmake'.", response);
//          })
//      } else {
//        $scope.carmake.put().then(
//          function(carmake) {
//            $location.path('/application/carmake/list');
//          },
//          function(response) {
//            restService.error("Could not update 'carmake'.", response);
//          }
//        );
//      }
    }

  }
]).directive("uniqueCarmakeName", ["$log", "$q", "restService", function($log, $q, restService) {
  // Validator for uniqueness of the carmake name.
  return {
    require: "ngModel",
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.carmakeNameUniqueValidator = function(modelValue, viewValue) {
$log.log("form: " + angular.toJson($scope.carmakeForm));
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        restService.findCarmakeByName(viewValue).then(
          function(foundCarmake) {
            if (foundCarmake === undefined) {
              def.resolve();
            } else {
              def.reject();
            }
          },
          function (errorResponse) {
            $log.log("Couldn't validate name of the carmake: " + viewValue);
            def.reject();
          }
        );
        return def.promise;
      };
    }
  };
}]);
