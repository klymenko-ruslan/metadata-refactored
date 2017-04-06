"use strict";

angular.module("ngMetaCrudApp")

.controller("PartListCtrl", ["$scope", "$log", "$uibModal", "partTypes", "critDimsByPartTypes", "critDimEnumVals",
  function ($scope, $log, $uibModal, partTypes, critDimsByPartTypes, critDimEnumVals) {
    $scope.createPart = function () {
      var modalInstance = $uibModal.open({
        "templateUrl": "/views/part/PartCreateModal.html",
        "controller": "PartCreateModalCtrl"
      });
    };

    $scope.partTypes = partTypes;
    $scope.critDimsByPartTypes = critDimsByPartTypes;
    $scope.critDimEnumVals = critDimEnumVals;

  }
])
.controller("PartCreateModalCtrl", ["$scope", "$uibModalInstance", "$log", "$location", "PartTypes",
  function ($scope, $uibModalInstance, $log, $location, PartTypes) {
    $scope.PartTypes = PartTypes;
    $scope.selection = {};

    $scope.create = function () {
      $uibModalInstance.close("cancel");
      $location.path("/part/createByPartTypeId/" + $scope.selection.partType.id);
    };

    $scope.cancel = function () {
      $uibModalInstance.dismiss("cancel");
    };

    $scope.refresh = function () {
      $scope.selection = {};
      PartTypes.refresh();
    };

  }
]);
