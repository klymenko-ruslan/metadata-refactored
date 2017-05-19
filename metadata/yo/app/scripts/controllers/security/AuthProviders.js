'use strict';

angular.module('ngMetaCrudApp').controller('AuthProvidersCtrl', ['$scope', '$log', 'restService', 'dialogs', 'toastr',
  'NgTableParams', 'Restangular',
  function($scope, $log, restService, dialogs, toastr, NgTableParams, Restangular) {

    $scope.modifyingRow = null;
    $scope.refRow = null;

    // Authentication Providers Table
    $scope.authProvidersTableParams = new NgTableParams({
      'page': 1,
      'count': 10,
      'sorting': {
        'id': 'asc'
      }
    }, {
      'getData': function(params) {
        // Update the pagination info
        var offset = params.count() * (params.page() - 1);
        var limit = params.count();
        for (var sortProperty in params.sorting()) {
            break;
        }
        var sortOrder;
        if (sortProperty) {
          sortOrder = params.sorting()[sortProperty];
        }
        return restService.getAllAuthProviders(sortProperty, sortOrder, offset, limit).then(
          function(authProviders) {
            params.total(authProviders.length);
            $scope.modifyingRow = null;
            return authProviders;
          },
          function(/*errorResponse*/) {
            $log.log('Couldn\'t load all authentication providers.');
          }
        );
      }
    });

    $scope._resetForm = function(form) {
      form.$rollbackViewValue();
      form.$setPristine();
    };

    $scope.isModifying = function(id) {
      return angular.isObject($scope.modifyingRow) && (angular.isUndefined(id) || $scope.modifyingRow.id === id);
    };

    $scope.modifyStart = function(row, form) {
      $scope._resetForm(form);
      $scope.refRow = row;
      $scope.modifyingRow = Restangular.copy($scope.refRow);
    };

    $scope.undo = function(form) {
      $scope._resetForm(form);
      $scope.modifyingRow = Restangular.copy($scope.refRow);
    };

    $scope.modifyCancel = function(row, form) {
      $scope.modifyingRow = null;
      $scope.refRow = null;
      $scope._resetForm(form);
    };

    $scope.modifySave = function(form) {
      restService.updateAuthProviderLDAP($scope.modifyingRow).then(
        function success() {
          // Copy updated values.
          $scope.refRow.name = $scope.modifyingRow.name;
          $scope.refRow.host = $scope.modifyingRow.host;
          $scope.refRow.port = $scope.modifyingRow.port;
          $scope.refRow.domain = $scope.modifyingRow.domain;
          $scope.refRow.protocol = $scope.modifyingRow.protocol;

          $scope.modifyingRow = null;
          $scope.refRow = null;
          $scope._resetForm(form);
          toastr.success('The LDAP authenticationa provider "' + name +
              '" has been successfully updated.');
        },
        function failure(errorResponse) {
          restService.error('Update of the LDAP authentication povider (id:' +
              $scope.modifyingRow.id + ') "' +
              $scope.modifyingRow.name + '" failed.', errorResponse);
        }
      );
    };

    $scope.remove = function(rec) {
      dialogs.confirm('Delete authentication provider [' + rec.id + '] "' + rec.name + '".', 'Are you sure?').result.then(
        function() {
          restService.removeAuthenticationProvider(rec.id).then(
            function() {
              $scope.authProvidersTableParams.reload();
              toastr.success('Authentication provider [' + rec.id + '] "' + name + '" has been successfully removed.');
            },
            function errorResponse(response) {
              restService.error('Removal of the authentication provider [' + rec.id + '] "' + name + '" failed.', response);
            }
          );
        }
      );
    };

  }
]).controller('AuthProviderFormCtrl', ['$scope', '$log', '$location', 'toastr',
  'restService',
  function($scope, $log, $location, toastr, restService) {

    $scope.authp = {
      name: null,
      host: null,
      port: 389,
      domain: null,
      typ: 'LDAP',
      protocol: 'LDAP'
    };

    $scope.save = function() {
      restService.createAuthProviderLDAP($scope.authp).then(
        function(/*id*/) {
          toastr.success('Authentication provider "' + $scope.authp.name + '" has been successfully created.');
          $location.path('/security/auth_providers');
        },
        function(errorResponse) {
          restService.error('Could not create authentication provider.', errorResponse);
        }
      );
    };

    $scope.back = function() {
      $location.path('/security/auth_providers');
    };

  }
]).directive('hostName', ['$log', 'VALID_IP_ADDRESS_REGEX', 'VALID_HOSTNAME_REGEX',
  function($log, VALID_IP_ADDRESS_REGEX, VALID_HOSTNAME_REGEX) {
  // Validator of a hostname (or IP).
  return {
    require: 'ngModel',
    link: function($scope, elm, attr, ctrl) {
      ctrl.$validators.hostName = function(modelValue, viewValue) {
        if (ctrl.$isEmpty(modelValue)) {
          // consider empty models to be valid
          return true;
        } else {
          return VALID_IP_ADDRESS_REGEX.test(viewValue) || VALID_HOSTNAME_REGEX.test(viewValue);
        }
      };
    }
  };
}]).directive('domainName', ['$log', 'VALID_HOSTNAME_REGEX', function($log, VALID_HOSTNAME_REGEX) {
  // Validator of a hostname (or IP).
  return {
    require: 'ngModel',
    link: function($scope, elm, attr, ctrl) {
      ctrl.$validators.domainName = function(modelValue, viewValue) {
        if (ctrl.$isEmpty(modelValue)) {
          // consider empty models to be valid
          return true;
        } else {
          return VALID_HOSTNAME_REGEX.test(viewValue);
        }
      };
    }
  };
}]).directive('uniqueAuthLdapName', ['$log', '$q', 'restService', function($log, $q, restService) {
  // Validator for uniqueness of a LDAP provider.
  return {
    require: 'ngModel',
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.uniqueAuthLdapName = function(modelValue, viewValue) {
        if (ctrl.$isEmpty(modelValue) /*|| modelValue ===  viewValue*/) {
          return $q.when();
        }
        var def = $q.defer();
        restService.findAuthProviderLdapByName(viewValue).then(
          function success(authProvider) {
            if (authProvider === undefined) {
              def.resolve();
            } else {
              var id = $scope.$eval('modifyingRow.id');
              if (authProvider.id === id) {
                def.resolve();
              } else {
                def.reject();
              }
            }
          },
          function failure(/*errorResponse*/) {
            $log.log('Couldn\'t validate name of the LDAP ' +
                'authentication provider: ' + viewValue);
            def.reject();
          }
        );
        return def.promise;
      };
    }
  };
}]);
