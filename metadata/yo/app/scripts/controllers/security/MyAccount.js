"use strict";

angular.module("ngMetaCrudApp")
  .controller("MyAccountCtrl", function (dialogs, $log, $location, $scope, toastr, restService) {
      $scope.user;

      var userPromise = restService.getMe().then(
          function(user) {
            $scope.user = user;
          },
          function(response) {
            restService.error("Could not load my account.", response);
          }
      );

      $scope.save = function() {
        var savePromise = restService.saveMe($scope.user).then(
            function() {
              toastr.success("Updated my account.");
              $location.path("/");
            },
            function(response) {
              restService.error("Could not update my account.", response);
            });
      };
    });
