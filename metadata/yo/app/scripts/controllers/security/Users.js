"use strict";

angular.module("ngMetaCrudApp")
  .controller('UsersCtrl', ["$log", "$scope", "$routeParams",
    "ngTableParams", "utils", "users",
    function($log, $scope, $routeParams,
      ngTableParams, utils, users) {
      var localDbAuthProvider = {"name": "Local DB"};
      angular.forEach(users, function(u) {
        if (angular.isUndefined(u.authProvider)) {
          u.authProvider = localDbAuthProvider;
        }
      });
      $scope.usersTableParams = new ngTableParams({
        page: 1,
        count: 10
      }, {
        getData: utils.localPagination(users, "username")
      });
    }
  ]);
