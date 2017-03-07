"use strict";

angular.module("ngMetaCrudApp").controller("UsersCtrl", ["$log", "$scope", "ngTableParams", "restService",
  "authProviders", function($log, $scope, ngTableParams, restService, authProviders) {
    $scope.authProviders = _.map(authProviders.recs || [], function (ap) {
      return {id: ap.id, title: ap.name};
    });
    $scope.authProviders.unshift({ id: null, title: "Local DB" });
    $scope.authProviders.unshift({ id: -1, title: "" });
    $scope.usersTableParams = new ngTableParams({
      page: 1,
      count: 25,
      sorting: {
        name: "asc"
      },
      filter: {
        displayName: null,
        userName: null,
        email: null,
        authProvider: null
      }
    }, {
      getData: function($defer, params) {
        var sortOrder;
        var sorting = params.sorting();
        for (var sortProperty in sorting) break;
        if (sortProperty) {
          sortOrder = sorting[sortProperty];
        }
        var offset = params.count() * (params.page() - 1);
        var limit = params.count();
        var filter = params.filter();
$log.log("filter: " + angular.toJson(filter, 2));
        restService.filterUsers(filter.displayName, filter.userName, filter.email,
            filter.authProviderId, sortProperty, sortOrder, offset, limit).then(
          function(result) {
            // Update the total and slice the result
            $defer.resolve(result.recs);
            params.total(result.total);
          },
          function(errorResponse) {
            restService.error("Search in the user list failed.", errorResponse);
            $defer.reject();
          });
      }
    });
  }
]);
