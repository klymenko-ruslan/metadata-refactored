"use strict";

angular.module("ngMetaCrudApp")
  .controller("LoginCtrl", ["$location", "$scope", "$routeParams", "gToast", "Restangular", "User", "$uibModal",
    function ($location, $scope, $routeParams, gToast, Restangular, User, $uibModal) {

    $scope.login = function() {
      Restangular.all('security/login').post(
        jQuery.param({
          username: $scope.username,
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
    };

    $scope.onOpenPasswordResetConfirmDlg = function() {
      $uibModal.open({
        templateUrl: "/views/dialog/PasswordResetConfirmDlg.html",
        animation: false,
        size: "lg",
        controller: "PasswordResetConfirmDlgCtrl",
        resolve: {
          username: function() {
            return $scope.username;
          }
        }
      });
    };

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
    };

  }])
  .controller("PasswordResetConfirmDlgCtrl",["$scope", "$uibModalInstance", "Restangular", "gToast", "username",
    function($scope, $uibModalInstance, Restangular, gToast, username) {
    $scope.username = username;
    $scope.onConfirmPasswordResetConfirmDlg = function() {
      Restangular.all('security/password/reset/request')
        .post(jQuery.param({username: username}), {}, {'Content-Type': 'application/x-www-form-urlencoded'})
        .then(
          function success() {
            gToast.open("Password reset link sent.");
          },
          function failure() {
            gToast.open("Is your username correct?");
          }
        )
        .finally(function always() {
          $uibModalInstance.close();
        });
    };

    $scope.onClosePasswordResetConfirmDlg = function() {
      $uibModalInstance.close();
    };

  }]);
