"use strict";

angular.module("ngMetaCrudApp")
  .controller("AddTurboTypeDialogCtrl", function ($scope, $modalInstance, $log, $location, data, gToast, Restangular, restService) {

    var partId = data.partId;
    $scope.filter = "";
    $scope.selection = {};
    $scope.turboTypes = [];

    $scope.add = function () {
      // Get the turbo type (and inject the manufacturer)
      var turboType = $scope.selection.turboType;
      turboType.manufacturer = $scope.selection.manufacturer;
      restService.addTurboTypeToPart(partId, turboType.id).then(
        function() {
          gToast.open("Turbo type added.");
          $modalInstance.close(turboType);
        },
        function(response) {
          restService.error("Could not add turbo type.", response);
        });
    }

    $scope.cancel = function () {
      $modalInstance.dismiss("cancel");
    }

    $scope.$watch("selection.manufacturer.id", function(manufacturerId) {
      if (manufacturerId) {
        restService.listTurboTypesForManufacturerId(manufacturerId).then(
          function (turboTypes) {
            $scope.turboTypes = turboTypes;
          },
          function (response) {
            restService.error("Could not get turbo types for manufacturer.", response);
          });
      } else {
        $scope.turboTypes = [];
      }
    });

  });
