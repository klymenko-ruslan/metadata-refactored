"use strict";

angular.module("ngMetaCrudApp")
  .controller("LoginCtrl", ["$location", "$scope", "$routeParams", "toastr", "restService", "User", "$uibModal",
    function ($location, $scope, $routeParams, toastr, restService, User, $uibModal) {

    $scope.login = function() {
      restService.login($scope.username, $scope.password).then(
        function(loginResponse) {
          User.init().then(
            function() {
              $location.path("/part/list");
            }
          );
        },
        function() {
          toastr.error("Login failed.");
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
      restService.resetToken($routeParams.token).then(
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
      restService.resetPassword(username).then(
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
