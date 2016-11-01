"use strict";

angular.module("ngMetaCrudApp")
.controller("BomRebuildCtrl", ["$log", "$scope", "$timeout", "$interval",
  "restService", "status",
  function($log, $scope, $timeout, $interval, restService, status) {

    $scope.options = {};

    /*
     * Phases:
     *  0 - display screen with button "Start"
     *  1 - show progress dialog with estimating of a job size
     *  2 - show progress dialog with indexing in progress
     *  3 - the indexing finished
     */
    $scope.phase = 0;
    $scope.errorMessage = null;

    $scope.bomsIndexed = null;
    $scope.bomsIndexingFailures = null;
    $scope.bomsIndexingTotalSteps = null;
    $scope.bomsIndexingCurrentStep = null;

    $scope.userId = null;
    $scope.userName = null;
    $scope.startedOn = null;
    $scope.finishedOn = null;
    $scope.closed = false;

    $scope._updateStatus = function(status) {

      if ($scope.closed) {
        // Prevent displaying of the progress dialog if it was closed by an user.
        return;
      }

      $scope.phase = status.phase;

      if ($scope.phase != 0) {
        $scope.options.indexBoms = status.indexBoms;
      }

      $scope.errorMessage = status.errorMessage;

      $scope.bomsIndexed = status.bomsIndexed;
      $scope.bomsIndexingFailures = status.bomsIndexingFailures;
      $scope.bomsIndexingTotalSteps = status.bomsIndexingTotalSteps;
      $scope.bomsIndexingCurrentStep = status.bomsIndexingCurrentStep;

      $scope.userId = status.userId;
      $scope.userName = status.userName;

      $scope.startedOn = status.startedOn;
      $scope.finishedOn = status.finishedOn;

    };

    this._resetOptions = function() {
      $scope.options.indexBoms = true;
    };

    this.startRebuild = function() {
      restService.startBomRebuilding($scope.options).then(
        function success(newStatus) {
          $scope.closed = false;
          $scope._updateStatus(newStatus);
        },
        function failure(response) {
          restService.error("Starting of the BOM rebuilding process failed.",
            response);
        }
      );
    };

    this.onCloseStatus = function() {
      this._resetOptions();
      $scope.closed = true;
      $scope.phase = 0;
    };

    this._resetOptions();
    $scope._updateStatus(status);

    $scope.refreshTask = $interval(function() {
      restService.getBomRebuildingStatus().then(
        function success(newStatus) {
          $scope._updateStatus(newStatus);
        },
        function failure(response) {
          restService.error("Update of a status of the BOM rebuilding process failed.",
            response);
        }
      );
    }, 2000);

    $scope.$on("$destroy", function() {
      $interval.cancel($scope.refreshTask);
    });

  }
]);
