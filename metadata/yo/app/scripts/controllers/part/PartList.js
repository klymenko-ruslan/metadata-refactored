"use strict";

angular.module("ngMetaCrudApp")
  .controller("PartListCtrl", ["$scope", "$modal", "partTypes", "manufacturers", "critDimsByPartTypes", "critDimEnumVals",
    function ($scope, $modal, partTypes, manufacturers, critDimsByPartTypes, critDimEnumVals) {
      $scope.createPart = function () {
        var modalInstance = $modal.open({
          "templateUrl": "/views/part/PartCreateModal.html",
          "controller": "PartCreateModalCtrl"
        });
      };
      $scope.partTypes = partTypes;
      $scope.manufacturers = manufacturers;
      $scope.critDimsByPartTypes = critDimsByPartTypes;
      $scope.critDimEnumVals = critDimEnumVals;
    }
  ]);
