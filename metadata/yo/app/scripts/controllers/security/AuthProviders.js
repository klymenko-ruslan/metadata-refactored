"use strict";

angular.module("ngMetaCrudApp").controller("AuthProvidersCtrl", ["$scope", "$log", "restService", "dialogs", "gToast",
  "ngTableParams", "Restangular",
  function($scope, $log, restService, dialogs, gToast, ngTableParams, Restangular) {

    $scope.modifyingRow = null;
    $scope.refRow = null;

    // Authentication Providers Table
    $scope.authProvidersTableParams = new ngTableParams({
      "page": 1,
      "count": 10,
      "sorting": {
        "id": "asc"
      }
    }, {
      "getData": function($defer, params) {
        // Update the pagination info
        var offset = params.count() * (params.page() - 1);
        var limit = params.count();
        for (var sortProperty in params.sorting()) break;
        var sortOrder;
        if (sortProperty) {
          sortOrder = params.sorting()[sortProperty];
        }
        restService.getAllAuthProviders(sortProperty, sortOrder, offset, limit).then(
          function(authProviders) {
            params.total(authProviders.length);
            $scope.modifyingRow = null;
            $defer.resolve(authProviders);
          },
          function(errorResponse) {
            $log.log("Couldn't load all authentication providers.");
            $defer.reject();
          }
        );
      }
    });

    $scope.isModifying = function(id) {
      return angular.isObject($scope.modifyingRow) && (angular.isUndefined(id) || $scope.modifyingRow.id == id);
    };

    $scope._resetForm = function(form) {
      form.$rollbackViewValue();
      form.$setPristine();
    };

    $scope.modifyStart = function(row, form) {
      $scope._resetForm(form);
      $scope.refRow = row;
      $scope.modifyingRow = Restangular.copy(row);
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
          $scope.refRow.protocol = $scope.modifyingRow.protocol;

          $scope.modifyingRow = null;
          $scope.refRow = null;
          $scope._resetForm(form);
          gToast.open("The LDAP authenticationa provider '" + name + "' has been successfully updated.");
        },
        function failure(errorResponse) {
          restService.error("Update of the LDAP authentication povider (id:" + $scope.modifyingRow.id + ") '"
            + $scope.modifyingRow.name + "' failed.", errorResponse);
        }
      );
    };

    $scope.remove = function(rec) {
      dialogs.confirm("Delete authentication provider [" + rec.id + "] '" + rec.name + "'.", "Are you sure?").result.then(
        function() {
          restService.removeAuthenticationProvider(rec.id).then(
            function() {
              $scope.authProvidersTableParams.reload();
              gToast.open("Authentication provider [" + rec.id + "] '" + name + "' has been successfully removed.");
            },
            function errorResponse(response) {
              restService.error("Removal of the authentication provider [" + rec.id + "] '" + name + "' failed.", response);
            }
          );
        }
      );
    };

  }
]).controller("AuthProviderFormCtrl", ["$scope", "$log", "$location", "gToast",
  "restService",
  function($scope, $log, $location, gToast, restService) {

    $scope.authp = {
      name: null,
      host: null,
      port: 389,
      typ: "LDAP",
      protocol: "LDAP"
    };

    $scope.save = function() {
      restService.createAuthProviderLDAP($scope.authp).then(
        function(id) {
          gToast.open("Authentication provider '" + $scope.authp.name + "' has been successfully created.");
          $location.path('/security/auth_providers');
        },
        function(errorResponse) {
          restService.error("Could not create authentication provider.", errorResponse);
        }
      );
    };

    $scope.back = function() {
      $location.path("/security/auth_providers");
    };

  }
]).directive("hostName", ["$log", function($log) {
  // Validator of a hostname (or IP).
  return {
    require: "ngModel",
    link: function($scope, elm, attr, ctrl) {
      var validIpAddressRegex = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/;
      var validHostnameRegex = /^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\-]*[A-Za-z0-9])$/;
      ctrl.$validators.hostName = function(modelValue, viewValue) {
        if (ctrl.$isEmpty(modelValue)) {
          // consider empty models to be valid
          return true;
        } else {
          return validHostnameRegex.test(viewValue) || validIpAddressRegex.test(viewValue);
        }
      };
    }
  };
}]).directive("uniqueAuthLdapName", ["$log", "$q", "restService", function($log, $q, restService) {
  // Validator for uniqueness of a LDAP provider.
  return {
    require: "ngModel",
    link: function($scope, elm, attr, ctrl) {
      ctrl.$asyncValidators.uniqueAuthLdapName = function(modelValue, viewValue) {
        var def = $q.defer();
        if (ctrl.$isEmpty(modelValue)) {
          return $q.when();
        }
        restService.findAuthProviderLdapByName(viewValue).then(
          function(authProvider) {
            if (authProvider === undefined) {
              def.resolve();
            } else {
              def.reject();
            }
          },
          function(errorResponse) {
            $log.log("Couldn't validate name of the LDAP authentication provider: " + viewValue);
            def.reject();
          }
        );
        return def.promise;
      };
    }
  };
}]);
