'use strict';

angular.module('ngMetaCrudApp')
  .controller('MainCtrl', ['$scope', '$log', '$location', 'User',
    function($scope, $log, $location, User) {

      // The only goal of this controlle is export of a functions
      // which can be accessible from any view (*.html).

      $scope.hasRole = function(role) {
        return User.hasRole(role);
      };

    }
  ]);
