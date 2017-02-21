"use strict";

angular.module("ngMetaCrudApp")
  .controller("PartApplicationSearchCtrl", ["$scope", "$log", "$location", "$routeParams", "restService", "dialogs",
    "gToast", "LinkSource", "services",
    function($scope, $log, $location, $routeParams, restService, dialogs, gToast, LinkSource, services) {

      $scope.partId = $routeParams.id;

      $scope.requiredSource = LinkSource.isSourceRequiredForApplication(services);

      // The part whose applications we're editing
      $scope.part = restService.findPart($scope.partId)
        .then(function(part) {
          $scope.part = part;
        }, function(errorResponse) {
          restService.error("Could not get part details", errorResponse);
        });
      $scope.applications = restService.findPartApplications($scope.partId)
        .then(function(applications) {
          $scope.applications = applications;
        }, function(errorResponse) {
          restService.error("Could not get part details", errorResponse);
        });

      $scope.pickedApplications = [];

      function cbSave(srcIds, ratings, description) {
        restService.addPartApplications($scope.partId, $scope.pickedApplications, srcIds, ratings, description).then(
          function() {
            // Success
            gToast.open("Application(s) added to part.");
            $location.path("/part/" + $scope.partId);
          },
          function(response) {
            dialogs.error("Could not add Applications to part.",
              "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
          }
        );
      };

      $scope.save = function() {
        if ($scope.pickedApplications.length) {
          LinkSource.link(cbSave, $scope.requiredSource, null);
        }
      };

      $scope.pickApplication = function(app) {
        if ($scope.pickedApplications.indexOf(app) == -1) {
          for (var i = 0; i < $scope.applications.length; i++) {
            var val = $scope.applications[i];
            if (val.carModelEngineYear.id == app.id) {
              gToast.open("The item already exists.");
              return;
            }
          }
          $scope.pickedApplications.push(app);
        }
      };

      $scope.unpickApplication = function(idx) {
        $scope.pickedApplications.splice(idx, 1);
      };

    }
  ]);
