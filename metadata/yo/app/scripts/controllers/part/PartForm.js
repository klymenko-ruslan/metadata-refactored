"use strict";

angular.module("ngMetaCrudApp").controller("PartFormCtrl",
  ["$scope", "$location", "$log", "restService", "Restangular", "part", "partType", "manufacturers",
  function($scope, $location, $log, restService, Restangular, part, partType, manufacturers) {

    $scope.manufacturers = manufacturers;
    $scope.turboTypes = [];
    $scope.turboModels = [];

    $scope.mpns = []; // manufacturer parts numbers

    $scope.filters = {
      turboType: "",
      turboModel: ""
    };

    function newPn(idx, val) {
      return {
        id: "pn" + idx,
        val: val
      };
    }

    $scope.onChangeManufacturer = function() {
      if ($scope.part.partType.magentoAttributeSet == "Turbo") {
        var mnfrId = $scope.part.manufacturer.id;
        restService.listTurboTypesForManufacturerId(mnfrId).then(
          function success(turboTypes) {
            $scope.turboTypes.splice(0, $scope.turboTypes.length);
            _.each(turboTypes, function(tt) {
              $scope.turboTypes.push(tt);
            });
          },
          function failure(response) {
            restService.error("Loading of turbo types for the manufacturer [" + mnfrId + "] - " +
                              $scope.part.manufacturer.name + " failed.", response);
          }
        );
      }
    };

    $scope.onChangeTurboType = function() {
      var ttId = $scope.part.turboModel.turboType.id;
      restService.listTurboModelsForTurboTypeId(ttId).then(
        function success(turboModels) {
          $scope.turboModels.splice(0, $scope.turboModels.length);
          _.each(turboModels, function(tm) {
            $scope.turboModels.push(tm);
          });
        },
        function failure(response) {
          restService.error("Loading of turbo models for the turbo type [" + ttId + "] - " +
                            $scope.part.turboModel.turboType.name + " failed.", response);
        }
      );
    };

    // Setup the create/update workflow
    if (part !== null) {
      $scope.partId = part.id;
      $scope.part = part;
      $scope.mpns.push(newPn(0, part.manufacturerPartNumber));
      part.manufacturerPartNumber = null;
      $scope.oldPart = Restangular.copy(part);
      $scope.onChangeManufacturer();
      $scope.onChangeTurboType();
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
        var element = $scope.partForm[o.id];
        if (angular.isObject(element)) {
          element.$validate();
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

    $scope.createTurboType = function() {
      $log.log("createTurboType");
    };

    $scope.createTurboModel = function() {
      $log.log("createTurboModel");
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

//     .controller('TurboModelPickerCtrl', function ($log, $scope, restService, ngTableParams, Restangular) {
//
//         // List of turbo types and models (set in $watch)
//         $scope.turboTypes = null;
//         $scope.turboModels = null;
//
//         /**
//          * Keep the turbo types updated with the part's manufacturer
//          */
//         var turboTypesPromise = null;
//         $scope.$watch('part.manufacturer.id', function (newMfrId, oldMfrId) {
// //        $log.log("TurboModelPicker.$watch part.manufacturer.id", newMfrId, oldMfrId);
//
//             if (!angular.isObject($scope.part)) return;    // NOP if the part is null
//             if ($scope.manufacturerId == newMfrId) return; // NOP if we're already displaying this manufacturer's turbo types
//
//             // Update the manufacturer ID for the turbo types list
//             $scope.manufacturerId = newMfrId;
//
//             // TODO: Cancel any previous fetch we were doing
//
//             $log.log("Fetching new turbo types for manufacturer", $scope.manufacturerId);
//
//             // Fetch the new types
//             $scope.turboTypes = null;
//             $scope.turboModels = null;
//
//             // Use the turbo type and model from the part if the manufacturer is the same
//             var partTurboTypeMfrId = $scope.$eval('part.turboModel.turboType.manufacturer.id');
//             if ($scope.manufacturerId == partTurboTypeMfrId) {
//                 $scope.turboTypeId = $scope.$eval('part.turboModel.turboType.id');
//                 $scope.turboModelId = $scope.$eval('part.turboModel.id');
// //          $log.log("TurboModelPicker using part values type/model", $scope.turboTypeId, $scope.turboModelId);
//             } else {
//                 $scope.turboTypeId = null;
//                 $scope.turboModelId = null;
//             }
// //        $log.log("TurboModelPicker.turboTypeId", $scope.turboTypeId);
//
//             // Clear and fetch the turbo types
//             $scope.turboTypes = null;
//
//             Restangular.setParentless(false);
//             turboTypesPromise = Restangular.all("other/turboType").one('manufacturer', $scope.manufacturerId)
//                 .getList()
//                 .then(function (response) {
//
//                     $scope.turboTypes = response;
//                 }, function (errorResponse) {
//
//                     restService.error("Could not fetch turbo types", errorResponse);
//                 });
//         });
//
//         // Fetch the new turbo models when the turbo type changes
//         var turboModelsPromise = null;
//         $scope.$watch('turboTypeId', function (newTurboTypeId, oldTurboTypeId) {
// //        $log.log("TurboModelPicker.$watch turboTypeId", newTurboTypeId, oldTurboTypeId);
//
//             // Clear and fetch the turbo models
//             $scope.turboModels = null;
//
//             // Fetch the turbo models if we have a type
//             if ($scope.turboTypeId != null) {
//                 turboModelsPromise = Restangular.all("other/turboModel")
//                     .getList({"turboTypeId": $scope.turboTypeId})
//                     .then(function (response) {
//
//                         $scope.turboModels = response;
//                     }, function (errorResponse) {
//
//                         restService.error("Could not fetch turbo models", errorResponse);
//                     });
//             }
//         });
//
//         // Watch for changes in the part's model/type (i.e. reverting) and propagate them to the picker
//         $scope.$watch('{modelId:part.turboModel.id, typeId: part.turboModel.turboType.id}', function (newValue, oldValue) {
// //        $log.log("TurboModelPicker.$watch (part.turboModel.id, part.turboModel.turboType.id}", newValue);
//             $scope.turboModelId = newValue.modelId;
//             $scope.turboTypeId = newValue.typeId;
//         }, true);
//
//
//         // Reset the turbo TYPE filter when the list changes
//         $scope.$watch('turboTypes', function () {
//             $scope.turboTypeFilter = "";
//         });
//
//         // Reset the turbo MODEL filter when the list changes
//         $scope.$watch('turboModels', function () {
//             $scope.turboModelFilter = "";
//         });
//
//
//         $scope.setPartTurboModel = function () {
//             var turboModel = _.find($scope.turboModels, function (turboModel) {
//                 return $scope.turboModelId == turboModel.id;
//             });
//
//             $scope.part.turboModel = Restangular.copy(turboModel)
// //        $log.log('setPartTurboModel', $scope.part.turboModel);
//         };
//     });
