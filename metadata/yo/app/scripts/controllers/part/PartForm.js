"use strict";

angular.module("ngMetaCrudApp").controller("PartFormCtrl", ["$q", "$scope", "$location",
  "$log", "$routeParams", "ngTableParams", "restService", "Restangular", "PartTypes",
  function($q, $scope, $location, $log, $routeParams,
    ngTableParams, restService, Restangular, PartTypes) {

    // Setup the create/update workflow
    if ($routeParams.id) {
      $scope.partId = $routeParams.id;
      $scope.oldPartPromise = restService.findPart($scope.partId).then(
        function(part) {
          // Save the part
          $scope.part = part;
          $scope.oldPart = Restangular.copy(part);
        },
        function(response) {
          restService.error("Could not get part data from the server.", response);
        });
    } else {
      $scope.partId = null;
      $scope.part = {};
    }

    // Set the part type
    if ($routeParams.typeId) {
      $scope.part.partType = PartTypes.getById($routeParams.typeId);
      $log.log("Got part type by ID", $routeParams.typeId, $scope.part.partType);
    }

    $scope.revert = function() {
      $scope.part = Restangular.copy($scope.oldPart);
      $scope.partForm.$setPristine(true);
      $scope.$broadcast("revert");
    };

    $scope.save = function() {
      var url = "part";
      if (!angular.isObject($scope.oldPart)) {
        $log.log("Part: " + angular.toJson($scope.part));
        restService.createPart($scope.part).then(
          function(id) {
            $location.path("/part/" + id);
          },
          function(response) {
            restService.error("Could not save part.", response);
          });
      } else {
        restService.updatePart($scope.part).then(
          function(part) {
            $location.path("/part/" + $scope.part.id);
          },
          function(response) {
            restService.error("Could not update part", response);
          }
        );
      }
    };

  }
]).directive("uniquePartNumber", ["$log", "$q", "restService", function($log, $q, restService) {
  // Validator for uniqueness of the part number.
  return {
    require: "ngModel",
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.nonUniquePartNumber = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        restService.findPartByNumber($scope.part.manufacturer.id, viewValue).then(
          function(foundPart) {
            if (!angular.isObject(foundPart) || foundPart.id == $scope.partId) {
              def.resolve();
            } else {
              def.reject();
            }
          },
          function(errorResponse) {
            $log.log("Couldn't validate part number: " + viewValue);
            def.reject();
          }
        );
        return def.promise;
      };
    }
  };
}]);
