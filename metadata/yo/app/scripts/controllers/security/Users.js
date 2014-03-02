'use strict';

angular.module('ngMetaCrudApp')
    .controller('UsersCtrl', function ($log, $scope, $routeParams, Restangular, $dialogs, gToast) {

      // Load the users
      Restangular.all('security/user').getList().then(
          function(users) {
            $scope.users = users;
          },
          function() {
            $dialogs.error("Could not load user data", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
          });

      // User rename model
      $scope.names = {};

      $scope.rename = function(user) {
        $scope.names[user.id] = user.name;
      };

      $scope.cancelRename = function(user) {
        delete $scope.names[user.id];
      };

      $scope.isRenaming = function(user) {
        return angular.isDefined($scope.names[user.id]);
      };

      $scope.save = function(user) {
        user.name = $scope.names[user.id];
        user.put().then(
            function() {
              // Success
              gToast.open("Renamed user.");
              delete $scope.names[user.id];
            },
            function(response) {
              // Error
              $dialogs.error(
                  "Could rename user.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
            });
      };


    });
