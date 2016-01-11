"use strict";

angular.module("ngMetaCrudApp")
  .directive("turboTypes", ["ngTableParams", function(ngTableParams) {
    return {
      restrict: 'E',
      scope: {
        turboTypes: "=ttypes"
      },
      templateUrl: '/views/component/turbo_types.html',
      controller: function($scope) {
        $scope.turboTypesTableParams = new ngTableParams({
          page: 1,
          count: 10
        }, {
          getData: function($defer, params) {
            params.total($scope.turboTypes.length);
            var recs = $scope.turboTypes.slice((params.page() - 1) * params.count(), params.page() * params.count());
            $defer.resolve(recs);
          }
        });
      }
    };
  }]);
