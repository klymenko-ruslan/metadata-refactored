'use strict';

angular.module('ngMetaCrudApp')
  .controller('GroupCtrl', function ($dialogs, $location, $log, $scope, $routeParams, gToast, restService, Restangular) {

      $scope.newName,
      $scope.roleSelections,
      $scope.group,
      $scope.roles = null;

      function setRoleSelections() {
        $scope.roleSelections = {};

        angular.forEach($scope.group.roles, function(role) {
          $scope.roleSelections[role.id] = true;
        });
      }

      // Fetch the roles
      var rolePromise = Restangular.all('security/group/roles').getList().then(
          function(roles) {
            $scope.roles = _.indexBy(roles, 'id');
          },
          function(response) {
            restService.error(response);
          });

      // Setup the group object for create/edit
      if ($routeParams.id == 'create') {
        $scope.group = {
          name: 'New Group',
          roles: [],
          users: []
        }

        // Setup the roles model
        setRoleSelections();
      } else {
        var groupPromise = Restangular.one('security/group', $routeParams.id).get().then(
            function(group) {
              $scope.group = group;
              $scope.newName = group.name;
              setRoleSelections();
            },
            function(response) {
              restService.error(response);
            });
      }

      $scope.save = function() {
        $scope.group.roles = _.chain($scope.roleSelections).map(function(isSelected, roleId) {
          if (isSelected) {
            return $scope.roles[roleId];
          }
        }).compact().value();

        if ($routeParams.id == 'create') {
          Restangular.all('security/group').post($scope.group).then(
              function() {
                gToast.open("Group created.");
                $location.path('/security/groups');
              },
              function(response) {
                restService.error(response);
              });
        } else {
          $scope.group.put().then(
              function() {
                gToast.open("Group updated.");
                $location.path('/security/groups');
              },
              function(response) {
                restService.error(response);
              });
        }
      };

      $scope.delete = function() {
        $dialogs.confirm(
                "Delete group?",
                "Are you sure you want to delete the " + $scope.group.name + " group?").result.then(
            function() {
              // Yes
              Restangular.one("security/group", $routeParams.id).remove().then(
                  function() {
                    // Success
                    gToast.open("Deleted group.");
                  },
                  function(response) {
                    // Error
                    $dialogs.error(
                        "Could delete group.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
                  });
            },
            function() {
              // No
            });
      };

      $scope.undo = function() {
        $scope.form.$setPristine(true);
        $scope.newName = $scope.group.name;
        setRoleSelections();
      };

    });
