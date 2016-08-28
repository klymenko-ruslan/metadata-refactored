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
          userId, $scope.search.description, $scope.search.data,
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
      "description": null,
      "data": null
    };

    $scope.applyFilter = function() {
      $scope.changelogTableParams.reload();
    };

    $scope.onOpenViewDlg = function(changelogRecord) {
      $uibModal.open({
        templateUrl: "/views/changelog/view.html",
        animation: false,
        size: "lg",
        controller: "ChangelogViewDlgCtrl",
        resolve: {
          changelogRecord: function() {
            return changelogRecord;
          }
        }
      });
    };

  }])
  .controller("ChangelogViewDlgCtrl", ["$scope", "$log", "$uibModalInstance", "changelogRecord",
    function($scope, $log, $uibModalInstance,  changelogRecord) {
      $scope.date = changelogRecord.changeDate;
      $scope.user = changelogRecord.user;
      $scope.description = changelogRecord.description;
      $scope.changes = null;
      if (changelogRecord && changelogRecord.data !== undefined && changelogRecord.data !== null) {
        var data = changelogRecord.data;
        try {
          $scope.changes = angular.fromJson(data);
        } catch (e) {
          var patched = data.replace(/(['"])?([a-zA-Z0-9_]+)(['"])?:/g, '"$2": ');
          try {
            $scope.changes = angular.fromJson(patched);
          } catch(e) {
            $log.log("Bad data of a changelog record [" + changelogRecord.id + "]: " + e);
            $scope.changes = data; // string
          }
        }
      }

      $scope.onCloseViewDlg = function() {
        $uibModalInstance.close();
      };

  }]);
