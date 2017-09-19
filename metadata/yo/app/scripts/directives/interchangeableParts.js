'use strict';

angular.module('ngMetaCrudApp')
  .directive('interchangeableParts', ['$log', 'restService', 'NgTableParams',
    function($log, restService, NgTableParams) {
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
              count: 10,
              sorting: {'manufacturer.name': 'asc'}
            }, {
              dataset: $scope.part.interchange.parts
            });
          });
        }]
      };
    }
  ]);
