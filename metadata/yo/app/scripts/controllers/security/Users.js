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

    });
