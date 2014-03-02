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

      // Group rename model
      $scope.names = {};

      $scope.rename = function(group) {
        $scope.names[group.id] = group.name;
      };

      $scope.cancelRename = function(group) {
        delete $scope.names[group.id];
      };

      $scope.isRenaming = function(group) {
        return angular.isDefined($scope.names[group.id]);
      };

      $scope.save = function(group) {
        group.name = $scope.names[group.id];
        group.put().then(
            function() {
              // Success
              gToast.open("Renamed group.");
              delete $scope.names[group.id];
            },
            function(response) {
              // Error
              $dialogs.error(
                  "Could rename group.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
            });
      };

    });
