'use strict';

angular.module('ngMetaCrudApp')
  .controller('MainCtrl', function ($log, $location, User) {

    // Initialize the user
    User.init().then(
      function() {
        $log.log('User initialized.');
      },
      function() {
        $log.log('User init failed.');
        $location.path('/');
      });

    });
