"use strict";

angular.module("ngMetaCrudApp").controller("Mas90SyncCtrl", ["$scope", "$interval", "$log", "gToast", "ngTableParams",
    "restService", "status",
  function($scope, $interval, $log, gToast, ngTableParams, restService, status) {

    $scope.errors = "";
    $scope.phase = 0;

    $scope._updateStatus = function(newStatus) {
      if (newStatus) {
        $scope.startedOn = newStatus.startedOn;
        $scope.userId = newStatus.userId;
        $scope.userName = newStatus.userName;
        $scope.partsUpdateCurrentStep = newStatus.partsUpdateCurrentStep;
        $scope.partsUpdateTotalSteps = newStatus.partsUpdateTotalSteps;
        $scope.partsUpdateInserts = newStatus.partsUpdateInserts;
        $scope.partsUpdateUpdates = newStatus.partsUpdateUpdates;
        $scope.partsUpdateSkipped = newStatus.partsUpdateSkipped;
        if (angular.isObject(newStatus.errors)) {
          angular.forEach(newStatus.errors, function(s) {
            $scope.errors += ("\u2022 " + s + "\n");
          });
        }
        $scope.finished = newStatus.finished;
      }
    };
    $scope._updateStatus(status);
    $scope.mas90syncHistoryTableParams = new ngTableParams({
      page: 1,
      count: 25
    }, {
      getData: function($defer, params) {
        restService.findMas90SyncHistory((params.page() - 1) * params.count(), params.count()).then(
          function success(result) {
            params.total(result.total);
            $defer.resolve(result.recs);
          },
          function failure(error) {
            restService.error("Can't load history of synchronization.", error);
          }
        );
      }
    });

    $scope.onCloseStatus = function () {
      $scope.phase = 0;
    };

    $scope.startSync = function() {
      restService.startMas90Sync().then(
        function success(newStatus) {
          $scope.phase = 1;
          $scope._updateStatus(newStatus);
        },
        function failure(error) {
          restService.error("Starting of the synchronization process failed.", error);
        }
      );
    };

    $scope.$on("$destroy", function () {
      $interval.cancel(refreshTask);
    });

    // Start periodical update of the status.
    var refreshTask = $interval(function() {
      restService.statusMas90Sync().then(
        function success(newStatus2) {
          if (newStatus2.finished) {
            $scope.mas90syncHistoryTableParams.reload();
          }
          if (newStatus2.finished && $scope.phase == 1) {
            $scope._updateStatus(newStatus2);
            $scope.phase = 2; // Started synchronization finished.
          } else {
            $scope._updateStatus(newStatus2);
          }
        },
        function failure(error) {
          $log.log("Update of the sync.status failed: " + angular.toJson(error));
        }
      );
    }, 1000);

  }
]);
