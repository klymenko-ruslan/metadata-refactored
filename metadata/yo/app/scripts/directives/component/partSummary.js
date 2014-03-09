'use strict';

angular.module('ngMetaCrudApp')
    .directive('partSummary', function () {
      return {
        scope: {
          part: '='
        },
        restrict: 'E',
        replace: false,
        templateUrl: '/views/component/partSummary.html'
      };
    });
