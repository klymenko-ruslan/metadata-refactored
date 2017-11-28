'use strict';

angular.module('ngMetaCrudApp')
  .controller('LoginCtrl', ['$injector', '$location', '$scope', '$routeParams', 'toastr', 'restService', 'User', '$uibModal',
    function ($injector, $location, $scope, $routeParams, toastr, restService, User, $uibModal) {

    $scope.login = function() {
      return restService.login($scope.username, $scope.password).then(
        function(/*loginResponse*/) {
          // We obtain the service 'User' manually because it has
          // code inside itself to initialize (User.init()).
          // If we inject it in the controller declaration above then
          // that code will be called before user's successful login
          // and consequently the User initialization will fail because
          // user's permissions can be loaded only after login.
          var User = $injector.get('User');
          User.init().then(
            function success() {
              $location.path('/part/list');
            }
          );
        },
        function(/*response*/) {
          toastr.error('Login failed.');
        }
      );
    };

    $scope.onOpenPasswordResetConfirmDlg = function() {
      $uibModal.open({
        templateUrl: '/views/dialog/PasswordResetConfirmDlg.html',
        animation: false,
        size: 'lg',
        controller: 'PasswordResetConfirmDlgCtrl',
        resolve: {
          username: function() {
            return $scope.username;
          }
        }
      });
    };

    $scope.resetToken = function() {
      restService.resetToken($routeParams.token, $scope.password).then(
        function() {
          $location.path('/login');
        },
        function() {
          toastr.success('Could not reset password.');
        }
      );
    };

  }])
  .controller('PasswordResetConfirmDlgCtrl',['$scope', '$uibModalInstance',
    'toastr', 'restService', 'username',
    function($scope, $uibModalInstance, toastr, restService,username) {
    $scope.username = username;
    $scope.onConfirmPasswordResetConfirmDlg = function() {
      restService.resetPassword(username)
        .then(
          function success() {
            toastr.success('Password reset link sent.');
          },
          function failure() {
            toastr.error('Is your username correct?');
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
