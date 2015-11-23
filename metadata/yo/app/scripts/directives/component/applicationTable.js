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
        }
      };
    });
