'use strict';

angular.module('ngMetaCrudApp')
  .service('User', function User($log, Restangular) {
      User = this;

      User.roles = [];

      // Fetch the user's roles
      this.init = function() {
        $log.log("User.init");

        var rolesPromise = Restangular.all('security/users/roles').getList().then(
            function(roles) {
              User.roles = roles;
            },
            function() {
              alert("Could not fetch your account info.");
            }
        );
      }

  });
