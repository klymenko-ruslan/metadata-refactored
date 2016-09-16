"use strict";

angular.module("ngMetaCrudApp").controller("PartFormCtrl",
  ["$scope", "$location", "$log", "restService", "Restangular", "part", "partType", "manufacturers",
  function($scope, $location, $log, restService, Restangular, part, partType, manufacturers) {

    $scope.manufacturers = manufacturers;

    $scope.mpns = []; // manufacturer parts numbers

    function newPn(idx, val) {
      return {
        id: "pn" + idx,
        val: val
      };
    }

    // Setup the create/update workflow
    if (part !== null) {
      $scope.partId = part.id;
      $scope.part = part;
      $scope.mpns.push(newPn(0, part.manufacturerPartNumber));
      part.manufacturerPartNumber = null;
      $scope.oldPart = Restangular.copy(part);
    } else {
      $scope.partId = null;
      $scope.part = {};
      $scope.oldPart = null;
      $scope.mpns.push(newPn(0, null));
    }

    // Set the part type
    if (partType !== null) {
      $scope.part.partType = partType;
    }

    $scope.addPn = function() {
      var newId = $scope.mpns.length;
      $scope.mpns.push(newPn(newId, null));
    };

    $scope.delPn = function(idx) {
      $scope.mpns.splice(idx, 1);
    };

    $scope.revert = function() {
      $scope.part = Restangular.copy($scope.oldPart);
      $scope.partForm.$setPristine(true);
      $scope.$broadcast("revert");
    };

    $scope.$watch("part.manufacturer", function(newVal, oldVal) {
      // Fire validation in 'Manufacturer P/N' fields.
      _.each($scope.mpns, function(o) {
        var model = $scope.partForm[o.id];
        if (angular.isObject(model)) {
          model.$validate();
        }
      });
    });

    $scope.save = function() {
      var url = "part";
      var partNumbers = _.map($scope.mpns, function(o) { return o.val; });
      if (!angular.isObject($scope.oldPart)) {
        restService.createPart($scope.part, partNumbers).then(
          function(response) {
            if (response.results.length === 1) {
              var id = response.results[0].partId;
              $location.path("/part/" + id);
            } else {
              $location.path("/part/list");
            }
          },
          function(response) {
            restService.error("Could not save part(s).", response);
          });
      } else {
        part.manufacturerPartNumber = $scope.mpns[0].val;
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
        if ($scope.part.manufacturer === undefined) {
          def.resolve();
        } else {
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
        }
        return def.promise;
      };
    }
  };
}]);
