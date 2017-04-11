'use strict';

angular.module('ngMetaCrudApp')
  .controller('MyAccountCtrl', function (dialogs, $log, $location, $scope, toastr, Restangular, restService) {
      $scope.user;

      var userPromise = Restangular.one("security/user/me").get().then(
          function(user) {
            $scope.user = user;
          },
          function(response) {
            restService.error("Could not load my account.", response);
          }
      );


      $scope.save = function() {
        var savePromise = Restangular.all('security/user/me').post($scope.user).then(
            function() {
              toastr.success("Updated my account.");
              $location.path('/');
            },
            function(response) {
              restService.error("Could not update my account.", response);
            });
      };
    });
