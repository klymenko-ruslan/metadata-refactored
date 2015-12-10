'use strict';

angular.module('ngMetaCrudApp')
    .directive('cmeySummary', function () {
      return {
        scope: {
          item: '='
        },
        restrict: 'E',
        replace: false,
        templateUrl: '/views/component/application/carmodelengineyear/summary.html'
      };
    });
