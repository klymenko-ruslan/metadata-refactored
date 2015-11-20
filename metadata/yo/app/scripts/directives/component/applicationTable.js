'use strict';

angular.module('ngMetaCrudApp')
    .directive('applicationTable', function ($log) {
      return {
        scope: {
          parts: '=',
          key: '@'
        },
        restrict: 'E',
        replace: false,
        transclude: true,
        templateUrl: '/views/component/applicationTable.html',
        controller: function($scope) {
          $scope.getApplication = function(item) {
            // If we don't have a key, just return the applications list
            if (!$scope.key) {
              return item;
            }
            // We do have a key, extract the part
            return item[$scope.key];
          }
        }
      };
    });
