'use strict';

angular.module('ngMetaCrudApp')
    .directive('partTable', function () {
      return {
        scope: {
          parts: '=',
          key: '@'
        },
        restrict: 'E',
        replace: false,
        templateUrl: '/views/component/partTable.html',
        controller: function($scope) {
          $scope.getParts = function() {

            // If we don't have a key, just return the parts list
            if (!$scope.key) {
              return $scope.parts;
            }

            // We do have a key, extract the parts
            return _.map($scope.parts, function(row) {
              return row[$scope.key];
            });
          }
        }
      };
    });
