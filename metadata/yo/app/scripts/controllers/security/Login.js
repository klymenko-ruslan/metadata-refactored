'use strict';

angular.module('ngMetaCrudApp')
  .controller('LoginCtrl', function ($location, $scope, $routeParams, gToast, Restangular, User) {

    $scope.login = function() {
      Restangular.all('security/login').post(
        jQuery.param({
          username: $scope.email,
          password: $scope.password
        }),
        {},
        {'Content-Type': 'application/x-www-form-urlencoded'}
      ).then(
        function(loginResponse) {
          User.init().then(
            function() {
              $location.path("/part/list");
            }
          );
        },
        function() {
          gToast.open("Login failed.");
        }
      );
    }

    $scope.resetRequest = function() {
      Restangular.all('security/password/reset/request').post(
        jQuery.param({email: $scope.email}),
        {},
        {'Content-Type': 'application/x-www-form-urlencoded'}).then(
        function() {
          gToast.open("Password reset link sent.");
        },
        function() {
          gToast.open("Is your username/email correct?");
        }
      );
    }

    $scope.resetToken = function() {
      Restangular.all('security/password/reset/token/' + $routeParams.token).post(
        jQuery.param({password: $scope.password}),
        {},
        {'Content-Type': 'application/x-www-form-urlencoded'}).then(
        function() {
          $location.path("/login")
        },
        function() {
          gToast.open("Could not reset password.");
        }
      );
    }
  });
