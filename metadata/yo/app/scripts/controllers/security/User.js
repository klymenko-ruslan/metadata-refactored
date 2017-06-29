'use strict';

angular.module('ngMetaCrudApp')

.controller('UserCtrl', ['dialogs', '$location', '$log', '$scope',
  '$routeParams', 'NgTableParams', 'toastr', 'restService', 'Restangular',
  'authProviders',
  function(dialogs, $location, $log,
    $scope, $routeParams, NgTableParams, toastr, restService, Restangular, authProviders) {

    $scope.mode = null;
    $scope.userId = null;
    $scope.originalUser = null;

    $scope.isMemberOpts = [
      {id: true, title: 'yes'},
      {id: false, title: 'no'}
    ];

    $scope.isMember = {};

    $scope.onChangeIsMember = function(g) {
      var isMember = $scope.isMember[g.id];
      restService.setUserMembershit($scope.userId, g.id, isMember).then(
        function success() {
          toastr.success('The user membership has been updated.');
        },
        function failure(response) {
          restService.error('The user membership update failed.', response);
        }
      );
    };

    var authProviderLocalDB = {
      id: -1,
      name: 'Local DB'
    };
    $scope.showResetPassword = false;
    $scope.authProviders = authProviders.recs || [];
    $scope.authProviders.unshift(authProviderLocalDB);

    // Setup the user object for create/edit
    if ($routeParams.id === 'create') {
      $scope.mode = 'create';
      $scope.user = {
        name: '',
        enabled: true,
        groups: [],
        authProvider: authProviderLocalDB
      };
    } else {
      $scope.mode = 'edit';
      $scope.userId = $routeParams.id;
      restService.getUser($scope.userId).then(
        function(user) {
          $scope.originalUser = user;
          if (!angular.isObject(user.authProvider)) { // null or undefined
            user.authProvider = authProviderLocalDB;
          }
          $scope.user = Restangular.copy(user);
        },
        function(response) {
          restService.error('Could not get user.', response);
        });
    }

    if ($scope.mode === 'edit') {
      $scope.userGroupsTableParams = new NgTableParams(
        {
          page: 1,
          count: 25,
          sorting: {
            'name': 'asc'
          }
        },
        {
          getData: function (params) {
            // Update the pagination info
            var offset = params.count() * (params.page() - 1);
            var limit = params.count();
            var sortProperty, sortOrder;
            for (sortProperty in params.sorting()) {
                break;
            }
            if (sortProperty) {
              sortOrder = params.sorting()[sortProperty];
            }
            var filter = params.filter();
            return restService.filterUserGroups($scope.userId, filter.fltrName, filter.fltrRole, filter.fltrIsMember, sortProperty, sortOrder, offset, limit).then(
              function (result) {
                $scope.isMember = {};
                _.each(result.recs, function(g) {
                  $scope.isMember[g.id] = g.isMember;
                });
                params.total(result.total);
                return result.recs;
              },
              function (/*errorResponse*/) {
                $log.log('Couldn\'t load users groups.');
              }
            );
          }
        }
      );
    }

    $scope.save = function() {
      if ($routeParams.id === 'create') {
        // Create
        restService.updateUser($scope.user).then(
          function(user) {
            toastr.success('Created user.');
            $location.path('/security/user/' + user.id);
          },
          function(response) {
            restService.error('Could not create user.', response);
          }
        );
      } else {
        // Update
        $scope.user.put().then(
          function() {
            toastr.success('Updated user.');
          },
          function(response) {
            restService.error('Could not update user.', response);
          }
        );
      }
    };

    $scope.delete = function() {
      dialogs.confirm(
        'Delete user?',
        'Are you sure you want to delete the user for ' + $scope.user.name + '?').result.then(
        function() {
          // Yes
          restService.removeUser($routeParams.id).then(
            function() {
              // Success
              toastr.success('Deleted user.');
              $location.path('/security/users/');
            },
            function(response) {
              // Error
              dialogs.error(
                'Could delete user.', 'Server said: <pre>' + JSON.stringify(response.data) + '</pre>');
            });
        },
        function() {
          // No
        });
    };

    $scope.undo = function(form) {
      form.$setPristine(true);
      $scope.user = Restangular.copy($scope.originalUser);
    };

    $scope.toggleResetPassword = function() {
      $scope.showResetPassword = !$scope.showResetPassword;
      delete $scope.user.password;
    };

    $scope.back = function() {
      $location.path('/security/users');
    };

  }
])
.directive('uniqueUserUsername', ['$log', '$q', 'restService', function($log, $q, restService) {
  // A validator for uniqueness of the username.
  return {
    require: 'ngModel',
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.uniqueUserUsername = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        if ($scope.user.username === undefined) {
          def.resolve();
        } else {
          restService.isUserUsernameUnique($scope.user.id, viewValue).then(
            function(unique) {
              if (unique) {
                def.resolve();
              } else {
                def.reject();
              }
            },
            function(/*errorResponse*/) {
              $log.log('Couldn\'t validate user\'s username: ' + viewValue);
              def.reject();
            }
          );
        }
        return def.promise;
      };
    }
  };
}])
.directive('uniqueUserEmail', ['$log', '$q', 'restService', function($log, $q, restService) {
  // A validator for uniqueness of the username.
  return {
    require: 'ngModel',
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.uniqueUserEmail = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        if ($scope.user.email === undefined) {
          def.resolve();
        } else {
          restService.isUserEmailUnique($scope.user.id, viewValue).then(
            function(unique) {
              if (unique) {
                def.resolve();
              } else {
                def.reject();
              }
            },
            function(/*errorResponse*/) {
              $log.log('Couldn\'t validate user\'s username: ' + viewValue);
              def.reject();
            }
          );
        }
        return def.promise;
      };
    }
  };
}]);
