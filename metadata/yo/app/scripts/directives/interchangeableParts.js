"use strict";

angular.module("ngMetaCrudApp")
  .directive("interchangeableParts", function($log, Restangular) {
    return {
      scope: {
        partId: "="
      },
      replace: true,
      templateUrl: '/views/component/interchangeable_parts.html',
      restrict: 'E',
      controller: function($scope, restService, ngTableParams) {

        restService.findInterchange(interchangeId).then(
        function(interchange) {
        $scope.interchangeablePartsTableParams = new ngTableParams({
          page: 1,
          count: 10
        }, {
          getData: function($defer, params) {

            if (!angular.isObject($scope.bom)) {
              $defer.reject();
              return;
            }

            $scope.bom = _.sortBy($scope.bom, 'id');

            // Update the total and slice the result
            $defer.resolve($scope.bom.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            params.total($scope.bom.length);
          }
        });

      }
    };
  });
