'use strict';

angular.module('ngMetaCrudApp')
.config(['ngTableFilterConfigProvider', function(ngTableFilterConfigProvider) {
    ngTableFilterConfigProvider.setConfig({
        aliasUrls: {
          'clearbttn': 'filters/clearbutton.html'
        }
    });
}])
.controller('UsersCtrl', ['$log', '$scope', 'NgTableParams', 'restService',
    'authProviders',
  function($log, $scope, NgTableParams, restService, authProviders) {
    $scope.authProviders = _.map(authProviders.recs || [], function (ap) {
      return {id: ap.id, title: ap.name};
    });
    $scope.authProviders.unshift({ id: -2, title: 'Local DB' });
    $scope.enabledOpts = [
      {id: true, title: 'yes'},
      {id: false, title: 'no'}
    ];

    $scope.usersTableParams = new NgTableParams({
      page: 1,
      count: 25,
      sorting: {
        name: 'asc'
      },
      filter: {
        displayName: null,
        userName: null,
        email: null,
        enabled: null,
        authProviderId: -1
      }
    }, {
      getData: function(params) {
        var sortOrder;
        var sorting = params.sorting();
        for (var sortProperty in sorting) {
            break;
        }
        if (sortProperty) {
          sortOrder = sorting[sortProperty];
        }
        var offset = params.count() * (params.page() - 1);
        var limit = params.count();
        var filter = params.filter();
        // normalize auth provider value
        var fltrAuthProviderId = filter.authProviderId;
        if (fltrAuthProviderId === '' || fltrAuthProviderId === null ||
                fltrAuthProviderId === undefined) {
            fltrAuthProviderId = -1;
        } else if (fltrAuthProviderId === -2) {
            fltrAuthProviderId = null;
        }
        return restService.filterUsers(filter.displayName, filter.userName,
          filter.email, fltrAuthProviderId, filter.enabled, sortProperty,
          sortOrder, offset, limit).then(
          function(result) {
            // Update the total and slice the result
            params.total(result.total);
            return result.recs;
          },
          function(errorResponse) {
            restService.error('Search in the user list failed.', errorResponse);
          });
      }
    });

    $scope.clearFilter = function () {
      var filter = $scope.usersTableParams.filter();
      filter.displayName = null;
      filter.userName = null;
      filter.email = null;
      filter.enabled = null;
      filter.authProviderId = -1;
    };

  }
]);
