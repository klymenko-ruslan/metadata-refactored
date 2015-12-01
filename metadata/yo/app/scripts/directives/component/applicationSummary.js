'use strict';

angular.module('ngMetaCrudApp')
    .directive('applicationSummary', function () {
      return {
        scope: {
          application: '='
        },
        restrict: 'E',
        replace: false,
        templateUrl: '/views/component/applicationSummary.html'
      };
    });
