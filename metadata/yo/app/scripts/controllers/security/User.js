"use strict";

angular.module("ngMetaCrudApp")
  .controller("UserCtrl", ["dialogs", "$location", "$log", "$scope",
    "$routeParams", "ngTableParams", "gToast", "restService", "Restangular",
    "authProviders",
    function(dialogs, $location, $log,
      $scope, $routeParams, ngTableParams, gToast, restService, Restangular, authProviders) {

      $scope.mode = null;

      $scope.isMemberOpts = [
        {id: null, title: ""},
        {id: true, title: "yes"},
        {id: false, title: "no"}
      ];

      $scope.isMember = {};

      $scope.onChangeIsMember = function(g) {
        var isMember = $scope.isMember[g.id];
        var userId = $scope.originalUser.id;
        restService.setUserMembershit(userId, g.id, isMember).then(
          function success() {
            gToast.open("The user membership has been updated.");
          },
          function failure(response) {
            restService.error("The user membership update failed.", response);
          }
        );
      };

      var authProviderLocalDB = {
        id: -1,
        name: "Local DB"
      };
      $scope.showResetPassword = false;
      $scope.authProviders = authProviders.recs || [];
      $scope.authProviders.unshift(authProviderLocalDB);

      // Setup the user object for create/edit
      if ($routeParams.id == "create") {
        $scope.mode = "create";
        $scope.user = {
          name: '',
          enabled: true,
          groups: [],
          authProvider: authProviderLocalDB
        };
      } else {
        $scope.mode = "edit";
        Restangular.one("security/user", $routeParams.id).get().then(
          function(user) {
            $scope.originalUser = user;
            if (!angular.isObject(user.authProvider)) { // null or undefined
              user.authProvider = authProviderLocalDB;
            }
            $scope.user = Restangular.copy(user);
          },
          function(response) {
            restService.error("Could not get user.", response);
          });
      }

      if ($scope.mode == "edit") {
        $scope.userGroupsTableParams = new ngTableParams(
          {
            page: 1,
            count: 25,
            sorting: {
              "name": "asc"
            }
          },
          {
            getData: function ($defer, params) {
              // Update the pagination info
              var offset = params.count() * (params.page() - 1);
              var limit = params.count();
              var sortProperty, sortOrder;
              for (sortProperty in params.sorting()) break;
              if (sortProperty) {
                sortOrder = params.sorting()[sortProperty];
              }
              var userId = $scope.originalUser ? $scope.originalUser.id : null;
              var filter = params.filter();
              restService.filterUserGroups(userId, filter.fltrName, filter.fltrRole, filter.fltrIsMember, sortProperty, sortOrder, offset, limit).then(
                function (result) {
                  $scope.isMember = {};
                  _.each(result.recs, function(g) {
                    $scope.isMember[g.id] = g.isMember;
                  });
                  $defer.resolve(result.recs);
                  params.total(result.total);
                },
                function (errorResponse) {
                  $log.log("Couldn't load users groups.");
                  $defer.reject();
                }
              );
            }
          }
        );
      }

      $scope.save = function() {
        if ($routeParams.id == "create") {
          // Create
          Restangular.all("security/user").post($scope.user).then(
            function(user) {
              gToast.open("Created user.");
              $location.path("/security/user/" + user.id);
            },
            function(response) {
              restService.error("Could not create user.", response);
            }
          );
        } else {
          // Update
          $scope.user.put().then(
            function() {
              gToast.open("Updated user.");
            },
            function(response) {
              restService.error("Could not update user.", response);
            }
          );
        }
      };

      $scope.delete = function() {
        dialogs.confirm(
          "Delete user?",
          "Are you sure you want to delete the user for " + $scope.user.name + "?").result.then(
          function() {
            // Yes
            Restangular.one("security/user", $routeParams.id).remove().then(
              function() {
                // Success
                gToast.open("Deleted user.");
                $location.path('/security/users/');
              },
              function(response) {
                // Error
                dialogs.error(
                  "Could delete user.", "Server said: <pre>" + JSON.stringify(response.data) + "</pre>");
              });
          },
          function() {
            // No
          });
      };

      $scope.undo = function() {
        $scope.form.$setPristine(true);
        $scope.user = Restangular.copy($scope.originalUser);
      };

      $scope.toggleResetPassword = function() {
        $scope.showResetPassword = !$scope.showResetPassword;
        delete $scope.user.password;
      };

      $scope.back = function() {
        $location.path("/security/users");
      }

    }
  ]);
