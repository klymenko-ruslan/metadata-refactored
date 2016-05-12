"use strict";

angular.module("ngMetaCrudApp")
  .controller("PartListCtrl", ["$scope", "$modal", "partTypes", "manufacturers", "critDimsByPartTypes",
    function ($scope, $modal, partTypes, manufacturers, critDimsByPartTypes) {
      $scope.createPart = function () {
        var modalInstance = $modal.open({
          "templateUrl": "/views/part/PartCreateModal.html",
          "controller": "PartCreateModalCtrl"
        });
      };
      $scope.partTypes = partTypes;
      $scope.manufacturers = manufacturers;
      $scope.critDimsByPartTypes = critDimsByPartTypes;
    }
  ]);
