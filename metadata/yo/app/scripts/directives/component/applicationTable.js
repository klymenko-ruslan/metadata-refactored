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
        templateUrl: '/views/component/applicationTable.html'//,
        controller: function($scope) {
          // Applications.
          restService.findApplications($scope.partId).then(
            function (applications) {
                $scope.applications = applications;
            },
            function (errorResponse) {
                $log.log("Could not get part's applications", errorResponse);
                restService.error("Could not get part's applications", errorResponse);
            }
          );
        }
      };
    });
