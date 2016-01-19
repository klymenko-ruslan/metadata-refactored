"use strict";

angular.module("ngMetaCrudApp").controller("Mas90SyncCtrl", ["$scope", "$interval", "$log", "gToast", "ngTableParams",
    "restService", "status",
  function($scope, $interval, $log, gToast, ngTableParams, restService, status) {

    $scope.errors = "";
    /*
     * Phases:
     *  0. Pre-request.
     *  1. Synchronization request in a progress.
     *  2. Synchronization request finished.
     */
    $scope.phase = null;

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
        if ($scope.phase == null) {
          $scope.phase = newStatus.finished ? 0 : 1;
        } else if ($scope.phase == 0 && $scope.finished && !newStatus.finished) {
          $scope.phase = 1;
        } else if ($scope.phase == 1 && !$scope.finished && newStatus.finished) {
          $scope.phase = 2;
          $scope.mas90syncHistoryTableParams.reload();
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
      $scope.phase = 0;
      $scope.errors = "";
      restService.startMas90Sync().then(
        function success(newStatus) {
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
          $scope._updateStatus(newStatus2);
        },
        function failure(error) {
          $log.log("Update of the sync.status failed: " + angular.toJson(error));
        }
      );
    }, 1000);

  }
]);
