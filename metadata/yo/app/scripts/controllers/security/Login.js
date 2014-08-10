'use strict';

angular.module('ngMetaCrudApp')
  .controller('LoginCtrl', function ($location, $scope, $routeParams, gToast, Restangular, User) {

    $scope.login = function() {
      Restangular.all('security/login').post(
        jQuery.param({
          j_username:    $scope.email,
          j_password: $scope.password
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
      Restangular.one('security/password/reset/request').get({email: $scope.email}).then(
        function() {
          gToast.open("Password reset link sent.");
        },
        function() {
          gToast.open("Is your email address correct?");
        }
      );
    }

    $scope.resetToken = function() {
      Restangular.one('security/password/reset/token/' + $routeParams.token).get({password: $scope.password}).then(
        function(loginResponse) {
          User.init().then(
            function() {
              $location.path("/login")
            }
          );
        },
        function() {
          gToast.open("Could not reset password.");
        }
      );
    }
  });
