'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartApplicationSearchCtrl', function ($log, $scope, $location, $routeParams, restService, dialogs, gToast) {
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
      }
    );

    $scope.pickedApplications = [];

    $scope.save = function () {
      restService.addPartApplications($scope.partId, $scope.pickedApplications).then(
        function () {
          // Success
          gToast.open("Application(s) added to part.");
          $location.path("/part/" + $scope.partId);
        },
          function (response) {
            dialogs.error("Could not add Applications to part.",
                          "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
        }
      );
    }

    $scope.pickApplication = function (app) {
      if ($scope.pickedApplications.indexOf(app) == -1) {
        $scope.pickedApplications.push(app);
      }
    }

    $scope.unpickApplication = function (idx) {
      $scope.pickedApplications.splice(idx, 1);
    }

  });
