'use strict';

angular.module('ngMetaCrudApp')
  .controller('ApplicationDetailCtrl', function ($scope, $routeParams, restService) {
    $scope.applicationId = $routeParams.id;
    $scope.part = restService.findApplication($scope.applicationId)
      .then(function (application) {
        $scope.application = application;
      }, function (errorResponse) {
        restService.error("Could not get application details", errorResponse);
      });
  });
