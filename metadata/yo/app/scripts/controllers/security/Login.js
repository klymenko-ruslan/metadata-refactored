"use strict";

angular.module("ngMetaCrudApp")
  .controller("LoginCtrl", ["$location", "$scope", "$routeParams", "toastr", "Restangular", "User", "$uibModal",
    function ($location, $scope, $routeParams, toastr, Restangular, User, $uibModal) {

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
          toastr.success("Login failed.");
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
          toastr.success("Could not reset password.");
        }
      );
    };

  }])
  .controller("PasswordResetConfirmDlgCtrl",["$scope", "$uibModalInstance", "Restangular", "toastr", "username",
    function($scope, $uibModalInstance, Restangular, toastr, username) {
    $scope.username = username;
    $scope.onConfirmPasswordResetConfirmDlg = function() {
      Restangular.all('security/password/reset/request')
        .post(jQuery.param({username: username}), {}, {'Content-Type': 'application/x-www-form-urlencoded'})
        .then(
          function success() {
            toastr.success("Password reset link sent.");
          },
          function failure() {
            toastr.error("Is your username correct?");
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
