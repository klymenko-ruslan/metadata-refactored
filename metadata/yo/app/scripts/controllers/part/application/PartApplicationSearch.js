'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartApplicationSearchCtrl', function ($scope, $routeParams, restService) {
    $scope.partId = $routeParams.id;
    // The part whose applications we're editing
    $scope.part = restService.findPart($scope.partId)
      .then(function (part) {
        $scope.part = part;
      }, function (errorResponse) {
        restService.error("Could not get part details", errorResponse);
      });
    $scope.applications = restService.findPartApplications($scope.partId)
      .then(function (applications) {
        $scope.applications = applications;
      }, function (errorResponse) {
        restService.error("Could not get part details", errorResponse);
      });

    $scope.save = function () {
      alert("TODO");
    }

  });
