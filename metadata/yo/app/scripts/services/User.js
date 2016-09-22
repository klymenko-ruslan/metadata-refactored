'use strict';

angular.module('ngMetaCrudApp')
  .service('User', function User($location, $log, $q, Restangular) {
      var User = this; // jshint ignore:line

      User.roles = [];

      // Fetch the user's roles
      User.init = function() {
        $log.log('User.init');

        return Restangular.one('security/user/me').get().then(
            function(user) {
                $log.log("User Retrieved", user);
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
              $log.log("Set Roles", User.roles);
            },
            function() {
//              alert("Could not fetch your account info.");
        });
      };

      User.hasRole = function(role) {
          return _.indexOf(User.roles, role, true) !== -1;
      }

      User.logout = function() {
        return Restangular.all("security/logout").post().then(
          function() {
            User.user = null;
            User.roles = [];
            $location.path("/login")
          }
        );
      }
    });
