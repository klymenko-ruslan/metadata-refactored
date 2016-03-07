"use strict";

angular.module("ngMetaCrudApp")
  .directive("turboTypes", ["$log", "utils", "ngTableParams", function($log, utils, ngTableParams) {
    return {
      restrict: 'E',
      scope: {
        turboTypes: "=ttypes"
      },
      templateUrl: '/views/component/turbo_types.html',
      controller: ["$scope", "$parse", function($scope, $parse) {
        $scope.turboTypesTableParams = new ngTableParams({
          page: 1,
          count: 10
        }, {
          getData: utils.localPagination($scope.turboTypes, "manufacturer.name")
        });
      }]
    };
  }]);
