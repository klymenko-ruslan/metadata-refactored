"use strict";

angular.module("ngMetaCrudApp")
  .controller("ChangelogListCtrl", ["$scope", "$log", "ngTableParams", "$uibModal", "restService", "users",
    "DATE_FORMAT", function(
    $scope, $log, ngTableParams, $uibModal, restService, users, DATE_FORMAT) {

    $scope.dateFormat = DATE_FORMAT;

    $scope.users = users;

    $scope.opened = {
      startDate: false,
      finishDate: false
    };
    $scope.openStartDateCalendar = function() {
      $scope.opened.startDate = true;
    };

    $scope.openFinishDateCalendar = function() {
      $scope.opened.finishDate = true;
    };

    $scope.datePickerOptions = {
      dateDisabled: false,
      startingDay: 1
    };

    // Notes Table
    $scope.changelogTableParams = new ngTableParams({
      page: 1,
      count: 25,
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
        if (angular.isObject($scope.search.user)) {
          userId = $scope.search.user.id;
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

    $scope.applyFilter = function() {
$log.log("Search: " + angular.toJson($scope.search));
      $scope.changelogTableParams.reload();
    };

  }
]);
