'use strict';

angular.module('ngMetaCrudApp')
  .service('User', ['$location', '$log', '$q', 'restService', function User($location, $log, $q, restService) {
    var User = this; // jshint ignore:line
    User.roles = [];
    // Fetch the user's roles
    User.init = function() {
      return restService.getMe().then(
        function(user) {
          User.user = user;
          var roles = _.chain(user.groups)
            .map(function(group) {
              return group.roles;
            })
            .flatten()
            .map(function(role) {
              return role.name;
            })
            .value();
          User.roles = _.sortBy(roles);
          //$log.log('User roles: ' + angular.toJson(User.roles, 2));
        },
        function() {
          $log.error('Could not fetch your account info.');
        }
      );
    };

    User.init();

    User.hasRole = function(role) {
      return _.indexOf(User.roles, role, true) !== -1;
    };

    User.logout = function() {
      return restService.logout().then(
        function() {
          //User.user = null;
          //User.roles = [];
          // Don't use code $location.path('/'); below as it could
          // lead to recursion in loading.
          window.location.href = '/';
        }
      );
    };
  }]);
