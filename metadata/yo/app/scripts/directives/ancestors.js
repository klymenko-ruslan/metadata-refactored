"use strict";

angular.module("ngMetaCrudApp")
  .directive("ancestors", ["ngTableParams", function(ngTableParams) {
    return {
      restrict: 'E',
      templateUrl: '/views/component/ancestors.html',
      controller: function($scope) {
        $scope.ancestorsTableParams = new ngTableParams({
          page: 1,
          count: 10
        }, {
          getData: function($defer, params) {
            params.total($scope.ancestors.length);
            var recs = $scope.ancestors.slice((params.page() - 1) * params.count(), params.page() * params.count());
            $defer.resolve(recs);
          }
        });
      }
    };
  }]);
