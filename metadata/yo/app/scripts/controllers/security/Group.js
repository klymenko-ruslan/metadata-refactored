'use strict';

angular.module('ngMetaCrudApp')
.controller('GroupCtrl', function (dialogs, $location, $log, $scope,
  $routeParams, toastr, restService) {
    $scope.newName = null;
    $scope.roleSelections = null;
    $scope.userSelections = null;
    $scope.group = null;
    $scope.users = null;
    $scope.roles = null;

    function setSelections() {
      $scope.roleSelections = {};
      $scope.userSelections = {};

      angular.forEach($scope.group.roles, function(role) {
        $scope.roleSelections[role.id] = true;
      });

      angular.forEach($scope.group.users, function(user) {
        $scope.userSelections[user.id] = true;
      });

    }

    // Fetch the roles
    restService.getRoles().then(
      function(roles) {
        $scope.roles = _.indexBy(roles, 'id');
      },
      function(response) {
        restService.error('Could not list roles.', response);
      }
    );

    // Fetch the users
    restService.getUsers().then(
      function(users) {
        $scope.users = _.indexBy(users, 'id');
      },
      function(response) {
        restService.error('Could not list users.', response);
      }
    );

    // Setup the group object for create/edit
    if ($routeParams.id === 'create') {
      $scope.group = {
        name: 'New Group',
        roles: [],
        users: []
      };
      // Setup the selection  models
      setSelections();
    } else {
      restService.getGroup($routeParams.id).then(
        function(group) {
          $scope.group = group;
          $scope.newName = group.name;
          setSelections();
        },
        function(response) {
          restService.error('Could not load group.', response);
        }
      );
    }

    $scope.save = function() {
      $scope.group.name = $scope.newName;
      $scope.group.roles = _.chain($scope.roleSelections).map(function(isSelected, roleId) {
        if (isSelected) {
          return $scope.roles[roleId];
        }
      }).compact().value();

      $scope.group.users = _.chain($scope.userSelections).map(function(isSelected, userId) {
        if (isSelected) {
          return $scope.users[userId];
        }
      }).compact().value();

      if ($routeParams.id === 'create') {
        restService.createGroup($scope.group).then(
          function() {
            toastr.success('Group created.');
            $location.path('/security/groups');
          },
          function(response) {
            restService.error('Could not create group.', response);
          }
        );
      } else {
        $scope.group.put().then(
          function() {
            toastr.success('Group updated.');
            $location.path('/security/groups');
          },
          function(response) {
            restService.error('Could not update group.', response);
          }
        );
      }
    };

    $scope.delete = function() {
      dialogs.confirm('Delete group?', 'Are you sure you want to delete the ' +
        $scope.group.name + ' group?').result
      .then(
        function() {
          // Yes
          restService.removeGroup($routeParams.id).then(
            function() {
              // Success
              toastr.success('Deleted group.');
              $location.path('/security/groups');
            },
            function(response) {
              // Error
              dialogs.error('Could delete group.',
                'Server said: <pre>' + JSON.stringify(response.data) +
                '</pre>'
              );
            }
          );
        },
        function() { /* No */ }
      );
    };

    $scope.undo = function() {
      $scope.form.$setPristine(true);
      $scope.newName = $scope.group.name;
      setSelections();
    };

    $scope.isNewGroup = function() {
      return $routeParams.id === 'create';
    };

  }
);
