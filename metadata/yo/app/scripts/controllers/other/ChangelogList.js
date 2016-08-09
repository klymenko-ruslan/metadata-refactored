"use strict";

angular.module("ngMetaCrudApp")
  .controller("ChangelogListCtrl", ["$scope", "$log", "ngTableParams", "restService",
  function(
    $scope, $log, ngTableParams, restService) {

    // Notes Table
    $scope.changelogTableParams = new ngTableParams({
      page: 1,
      count: 10,
      sorting: {
        changeDate: "desc"
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
        var userId = null;
        if ($scope.search.user) {
          userId = user.id;
        }
        restService.filterChangelog($scope.search.startDate, $scope.search.finishDate,
          userId, $scope.search.description,
          sortProperty, sortOrder, offset, limit).then(
          function(result) {
            // Update the total and slice the result
            $defer.resolve(result.recs);
            params.total(result.total);
          },
          function(errorResponse) {
            restService.error("Search in the changelog failed.", errorResponse);
            $defer.reject();
          });
      }
    });

    // Query Parameters
    $scope.search = {
      "startDate": null,
      "finishDate": null,
      "user": null,
      "description": null
    };

  }
]);
