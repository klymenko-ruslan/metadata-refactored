"use strict";

angular.module("ngMetaCrudApp")
  .controller("PartListCtrl", ["$scope", "$modal", "critDimsByPartTypes", "critDimEnumVals",
    function ($scope, $modal, critDimsByPartTypes, critDimEnumVals) {
      $scope.createPart = function () {
        var modalInstance = $modal.open({
          "templateUrl": "/views/part/PartCreateModal.html",
          "controller": "PartCreateModalCtrl"
        });
      };
      $scope.critDimsByPartTypes = critDimsByPartTypes;
      $scope.critDimEnumVals = critDimEnumVals;
    }
  ]);
