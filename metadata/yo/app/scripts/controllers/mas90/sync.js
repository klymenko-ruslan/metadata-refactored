"use strict";

angular.module("ngMetaCrudApp").controller("Mas90SyncCtrl", ["$scope", "$interval", "$log", "gToast", "ngTableParams",
    "restService", "status",
  function($scope, $interval, $log, gToast, ngTableParams, restService, status) {
    $scope._updateStatus = function(newStatus) {
      $log.log("newStatus: " + angular.toJson(newStatus));
      if (newStatus) {
        $scope.startedOn = newStatus.startedOn;
        $scope.userId = newStatus.userId;
        $scope.userName = newStatus.userName;
        $scope.phase = newStatus.phase;
        $scope.partsUpdateCurrentStep = newStatus.partsUpdateCurrentStep;
        $scope.partsUpdateTotalSteps = newStatus.partsUpdateTotalSteps;
        $scope.partsUpdateInserts = newStatus.partsUpdateInserts;
        $scope.partsUpdateUpdates = newStatus.partsUpdateUpdates;
        $scope.bomUpdateTotalSteps = newStatus.bomUpdateTotalSteps;
        $scope.bomUpdateCurrentStep = newStatus.bomUpdateCurrentStep;
        $scope.bomUpdateInserts = newStatus.bomUpdateInserts;
        $scope.bomUpdateUpdates = newStatus.bomUpdateUpdates;
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

    var refreshTask = $interval(function() {
      restService.statusMas90Sync().then(
        function success(newStatus2) {
          $scope._updateStatus(newStatus2);
          if (newStatus2.finished) {
            $scope.mas90syncHistoryTableParams.reload();
          }
        },
        function failure(error) {
          $log.log("Update of the sync.status failed: " + angular.toJson(error));
        }
      );
    }, 1000);

    $scope.$on("$destroy", function () {
      $interval.cancel(refreshTask);
    });

    $scope.startSync = function() {
      restService.startMas90Sync().then(
        function success(newStatus) {
          $scope._updateStatus(newStatus);
        },
        function failure(error) {
          restService.error("Starting of the synchronization process failed.", error);
        }
      );
    };

  }
]);
