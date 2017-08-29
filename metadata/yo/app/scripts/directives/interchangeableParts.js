'use strict';

angular.module('ngMetaCrudApp')
  .directive('interchangeableParts', ['$log', 'utils', 'restService', 'NgTableParams',
    function($log, utils, restService, NgTableParams) {
      return {
        restrict: 'E',
        scope: {
          part: '='
        },
        templateUrl: '/views/component/interchangeable_parts.html',
        controller: ['$scope', function($scope) {
          $scope.$watch('part', function(newVal, oldVal) {
            if (!angular.isObject(newVal) && angular.equals(newVal, oldVal)) {
              return;
            }
            $scope.interchangeablePartsTableParams = new NgTableParams({
              page: 1,
              count: 10
            }, {
              getData: utils.localPagination($scope.part.interchange.parts, 'manufacturer.name')
            });
          });
        }]
      };
    }
  ]);
