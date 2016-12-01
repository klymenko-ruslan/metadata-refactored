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
    $scope.status = {
      phase: 0
    };

    $scope.closed = false;

    $scope._updateStatus = function(status) {

      if ($scope.closed) {
        // Prevent displaying of the progress dialog if it was closed by an user.
        return;
      }

      status = status.data; // $http

      $scope.status = status;

      if ($scope.status.phase != 0) {
        $scope.options.indexBoms = status.indexBoms;
      }

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
          restService.httpServiceError("Starting of the BOM rebuilding process failed.",
            response);
        }
      );
    };

    this.onCloseStatus = function() {
      this._resetOptions();
      $scope.closed = true;
      $scope.status.phase = 0;
    };

    this._resetOptions();
    $scope._updateStatus(status);

    $scope.refreshTask = $interval(function() {
      restService.getBomRebuildingStatus().then(
        function success(newStatus) {
          $scope._updateStatus(newStatus);
        },
        function failure(response) {
          restService.httpServiceError("Update of a status of the BOM rebuilding process failed.",
            response);
        }
      );
    }, 1000);

    $scope.$on("$destroy", function() {
      $interval.cancel($scope.refreshTask);
    });

  }
]);
