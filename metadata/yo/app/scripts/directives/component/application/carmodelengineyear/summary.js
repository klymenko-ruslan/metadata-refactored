'use strict';

angular.module('ngMetaCrudApp')
    .directive('carmodelengineyearSummary', function () {
      return {
        scope: {
          item: '='
        },
        restrict: 'E',
        replace: false,
        templateUrl: '/views/component/application/carmodelengineyear/summary.html'
      };
    });
