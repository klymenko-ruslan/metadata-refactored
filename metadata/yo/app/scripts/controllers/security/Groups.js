'use strict';

angular.module('ngMetaCrudApp')
    .controller('GroupsCtrl', function ($log, $scope, $routeParams, Restangular, $dialogs, gToast) {

      // Load the groups
      Restangular.all('security/group').getList().then(
          function(groups) {
            $scope.groups = groups;
          },
          function() {
            $dialogs.error("Could not load group data", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
          });

    });
