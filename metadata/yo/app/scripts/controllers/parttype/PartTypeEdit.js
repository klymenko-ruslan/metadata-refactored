"use strict";

angular.module("ngMetaCrudApp")
  .controller("PartTypeEditCtrl", ["$scope", "partType",
    function ($scope, partType) {
      $scope.partType = partType;
    }
  ]);
