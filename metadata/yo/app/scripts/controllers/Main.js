"use strict";

angular.module("ngMetaCrudApp")
  .controller('MainCtrl', ["$scope", "$log", "$location", "User",
    function($scope, $log, $location, User) {
      //Initialize the user
      User.init().then(
        function() {
          $log.log('User initialized.');
        },
        function() {
          $log.log('User init failed.');
          $location.path('/');
        }
      );

      $scope.hasRole = function(role) {
        return _.contains(User.roles, role);
      };
    }
  ]);
