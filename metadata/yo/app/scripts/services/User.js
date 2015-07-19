'use strict';

angular.module('ngMetaCrudApp')
  .service('User', function User($location, $log, $q, Restangular) {
      var User = this; // jshint ignore:line

      User.roles = [];

      // Fetch the user's roles
      User.init = function() {
        $log.log('User.init');

        var rolesPromise = Restangular.all('security/user/myroles').getList().then(
            function(roles) {
              $log.log("Set Roles", roles);
              User.roles = roles;
            },
            function() {
//              alert("Could not fetch your account info.");
        }
        );

        var userPromise = Restangular.one('security/user/me').get().then(
            function(user) {
              User.user = user;
            },
            function() {
//              alert("Could not fetch your account info.");
        }
        );

        return $q.all([rolesPromise, userPromise]);
      };
      
      User.hasRole = function(role) {
          return _.contains(User.roles, role);
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
