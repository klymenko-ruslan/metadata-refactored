"use strict";

angular.module("mockApp", ["angucomplete-alt"]);

angular.module("mockApp")
.controller("autocompleteCtrl", ["$scope", "$log", "mockData",
    function($scope, $log, mockData) {
      $scope.filteredParts = mockData.filteredParts;

      $scope.selectedTurboModel = function($item) {
        $log.log("selectedTurboModel[$item]: " + angular.toJson($item));
      };

      $scope.inputChanged = function($item) {
        $log.log("inputChanged[selectedTurboModel]: " + $scope.selectedTurboModel);
        $log.log("inputChanged[$item]: " + angular.toJson($item));
        $log.log("inputChanged[typeof $item]: " + typeof($item));
      };
    }]);
