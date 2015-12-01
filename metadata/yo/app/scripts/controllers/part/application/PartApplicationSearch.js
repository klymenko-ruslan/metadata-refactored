'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartApplicationSearchCtrl', function ($log, $scope, $location, $routeParams, restService, $dialogs, gToast) {
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

    $scope.pickedApplication = null;

    $scope.save = function () {
      restService.addPartApplication($scope.partId, $scope.pickedApplication.id).then(
        function () {
          // Success
          gToast.open("Application item added to part.");
          $location.path("/part/" + $scope.partId);
        },
          function (response) {
            $dialogs.error("Could not add Application to part.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
        });
    }

    $scope.pickApplication = function (applicationId) {
      $scope.pickedApplication = restService.findApplication(applicationId).then(
        function (pickedApplication) {
          $scope.pickedApplication = pickedApplication;
        },
        function (errorResponse) {
          restService.error("Could not pick application.", errorResponse);
          $log.log("Could not pick application", errorResponse);
        });
      }

  });
