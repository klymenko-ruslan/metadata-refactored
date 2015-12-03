'use strict';

angular.module('ngMetaCrudApp')
  .directive('subnav', function() {
    return {
      restrict: 'E',
      transclude: true,
      replace: false,
      templateUrl: '/views/component/Subnav.html',
      scope: {
        item: '@'
      },
      controller: function($log, $scope) {
        $log.log('1. Scope: ' + angular.toJson($scope));
      }
    }
  });
