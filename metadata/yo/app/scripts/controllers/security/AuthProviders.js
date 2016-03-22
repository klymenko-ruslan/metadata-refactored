"use strict";

angular.module("ngMetaCrudApp").controller("AuthProvidersCtrl", ["$scope", "$log", "restService", "dialogs",
  "ngTableParams", function ($scope, $log, restService, dialogs, ngTableParams) {

  $scope.modifyingRow = null;

  // Authentication Providers Table
  $scope.authProvidersTableParams = new ngTableParams(
    {
      "page": 1,
      "count": 10,
      "sorting": {
        "id": "asc"
      }
    },
    {
      "getData": function ($defer, params) {
        // Update the pagination info
        var offset = params.count() * (params.page() - 1);
        var limit = params.count();
        for (var sortProperty in params.sorting()) break;
        if (sortProperty) {
          var sortOrder = params.sorting()[sortProperty];
        }
        restService.getAllAuthProviders(sortProperty, sortOrder, offset, limit).then(
          function (authProviders) {
            params.total(authProviders.length);
            $scope.modifyingRow = null;
            $defer.resolve(authProviders);
          },
          function (errorResponse) {
            $log.log("Couldn't load all authentication providers.");
            $defer.reject();
          }
        );
      }
    }
  );

  $scope.isModifying = function(id) {
    return angular.isObject($scope.modifyingRow) && (angular.isUndefined(id) || $scope.modifyingRow.id == id);
  }

  $scope._resetForm = function(form) {
    form.$rollbackViewValue();
    form.$setPristine();
  };

  $scope.modifyStart = function(row, form) {
    $scope._resetForm(form);
    $scope.modifyingRow = row;
  };

  $scope.modifyCancel = function(row, form) {
    $scope.modifyingRow = null;
    $scope._resetForm(form);
  };

  $scope.modifySave = function(form) {
    $log.log("TODO: modifySave");
  /*
    var name = $scope.modifyValues[carfueltype.id];
    carfueltype.name = name;
    restService.updateCarfueltype(carfueltype).then(
      function() {
        // Success.
        delete $scope.modifyValues[carfueltype.id];
        $scope._resetForm(form);
        gToast.open("The car fuel type '" + name + "' has been successfully updated.");
      },
      function errorResponse(response) {
        restService.error("Car fuel type (id:" + carfueltype.id + ") '" + name + "' update failed.", response);
      }
    );
  */
  };

  $scope.remove = function(rec) {
    dialogs.confirm("Delete authentication provider '" + rec.name + "'.", "Are you sure?").result.then(
      function() {
        // Yes
        $log.log("TODO: remove");
        /*
        restService.removeCarfueltype(id).then(
          function () {
            $scope.clear(); // reload table
            $scope.carfueltypeTableParams.reload();
            gToast.open("Car fuel type '" + name + "' has been successfully removed.");
          },
          function errorResponse(response) {
            restService.error("Car fuel type '" + name + "' remove failed.", response);
          }
        );
        */
      }
    );
  };

}]);

angular.module("ngMetaCrudApp").controller("AuthProviderFormCtrl", ["$scope", "$log", "$location", "gToast",
  "restService", function ($scope, $log, $location, gToast, restService) {

  $scope.authp = {
    name: null,
    host: null,
    port: 389,
    typ: "LDAP"
  }

  $scope.save = function() {
    restService.createAuthProviderLDAP($scope.authp).then(
        function(id) {
          gToast.open("Authentication provider '" + $scope.authp.name + "' has been successfully created.");
          $location.path('/security/auth_providers');
        },
        function (errorResponse) {
          restService.error("Could not create authentication provider.", errorResponse);
        }
      );
  }

  $scope.back = function() {
    $location.path("/security/auth_providers");
  }

}]);
