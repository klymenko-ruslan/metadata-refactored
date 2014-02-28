'use strict';

angular.module('ngMetaCrudApp')
  .service('User', function User($log, Restangular) {
      var User = this;

      User.roles = [];

      // Fetch the user's roles
      User.init = function() {
        $log.log("User.init");

        var rolesPromise = Restangular.all('security/user/myroles').getList().then(
            function(roles) {
              User.roles = roles;
            },
            function() {
              alert("Could not fetch your account info.");
            }
        );
      }

  });
