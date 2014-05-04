'use strict';

angular.module('ngMetaCrudApp')
  .controller('UserCtrl', function ($dialogs, $location, $log, $scope, $routeParams, gToast, restService, Restangular) {

      $scope.showResetPassword = false;

      // Setup the user object for create/edit
      if ($routeParams.id == 'create') {
        $scope.user = {
          name: '',
          enabled: true,
          groups: []
        }

      } else {
        var userPromise = Restangular.one('security/user', $routeParams.id).get().then(
            function(user) {
              $scope.originalUser = user;
              $scope.user = Restangular.copy(user);
            },
            function(response) {
              restService.error("Could not get user.", response);
            });
      }

      $scope.save = function() {
        if ($routeParams.id == 'create') {

          // Create
          Restangular.all('security/user/').post($scope.user).then(
              function() {
                gToast.open("Created user.")
                $location.path('/security/users/');
              },
              function(response) {
                restService.error("Could not create user.", response);
              }
          );
        } else {

          // Update
          $scope.user.put().then(
              function() {
                gToast.open("Updated user.");
              },
              function(response) {
                restService.error("Could not update user.", response);
              }
          );
        }
      };

      $scope.delete = function() {
        $dialogs.confirm(
                "Delete user?",
                "Are you sure you want to delete the user for " + $scope.user.name + "?").result.then(
            function() {
              // Yes
              Restangular.one("security/user", $routeParams.id).remove().then(
                  function() {
                    // Success
                    gToast.open("Deleted user.");
                    $location.path('/security/users/');
                  },
                  function(response) {
                    // Error
                    $dialogs.error(
                        "Could delete user.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
                  });
            },
            function() {
              // No
            });
      };

      $scope.undo = function() {
        $scope.form.$setPristine(true);
        $scope.user = Restangular.copy($scope.originalUser);
      };

      $scope.toggleResetPassword = function() {
        $scope.showResetPassword = !$scope.showResetPassword;
        delete $scope.user['password'];
      }

    });
