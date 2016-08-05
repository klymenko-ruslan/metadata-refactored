"use strict";

angular.module("ngMetaCrudApp")
.controller("IndexingCtrl", ["$log", "$scope", "$timeout", "$interval",
  "restService", "status",
  function($log, $scope, $timeout, $interval, restService, status) {

    $scope.toIndex = {};

    /*
     * Phases:
     *  0 - display screen with button "Start"
     *  1 - show progress dialog with estimating of a job size
     *  2 - show progress dialog with indexing in progress
     *  3 - the indexing finished
     */
    $scope.phase = 0;
    $scope.errorMessage = null;

    $scope.partsIndexed = null;
    $scope.partsIndexingFailures = null;
    $scope.partsIndexingTotalSteps = null;
    $scope.partsIndexingCurrentStep = null;

    $scope.applicationsIndexed = null;
    $scope.applicationsIndexingFailures = null;
    $scope.applicationsIndexingTotalSteps = null;
    $scope.applicationsIndexingCurrentStep = null;

    $scope.salesNotesIndexed = null;
    $scope.salesNotesIndexingFailures = null;
    $scope.salesNotesIndexingTotalSteps = null;
    $scope.salesNotesIndexingCurrentStep = null;

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
        $scope.toIndex.parts = status.indexParts;
        $scope.toIndex.applications = status.indexApplications;
        $scope.toIndex.salesNotes = status.indexSalesNotes;
        $scope.toIndex.recreateIndex = status.recreateIndex;
      }

      $scope.errorMessage = status.errorMessage;

      $scope.partsIndexed = status.partsIndexed;
      $scope.partsIndexingFailures = status.partsIndexingFailures;
      $scope.partsIndexingTotalSteps = status.partsIndexingTotalSteps;
      $scope.partsIndexingCurrentStep = status.partsIndexingCurrentStep;

      $scope.applicationsIndexed = status.applicationsIndexed;
      $scope.applicationsIndexingFailures = status.applicationsIndexingFailures;
      $scope.applicationsIndexingTotalSteps = status.applicationsIndexingTotalSteps;
      $scope.applicationsIndexingCurrentStep = status.applicationsIndexingCurrentStep;

      $scope.salesNotesIndexed = status.salesNotesIndexed;
      $scope.salesNotesIndexingFailures = status.salesNotesIndexingFailures;
      $scope.salesNotesIndexingTotalSteps = status.salesNotesIndexingTotalSteps;
      $scope.salesNotesIndexingCurrentStep = status.salesNotesIndexingCurrentStep;

      $scope.userId = status.userId;
      $scope.userName = status.userName;

      $scope.startedOn = status.startedOn;
      $scope.finishedOn = status.finishedOn;

    };

    this._resetToIndex = function() {
      $scope.toIndex.parts = true;
      $scope.toIndex.applications = true;
      $scope.toIndex.salesNotes = true;
      $scope.toIndex.recreateIndex = true;
    };

    this.startIndexing = function() {
      restService.startIndexing($scope.toIndex).then(
        function success(newStatus) {
          $scope.closed = false;
          $scope._updateStatus(newStatus);
        },
        function failure(response) {
          restService.error("Starting of the indexing process failed.",
            response);
        }
      );
    };

    this.onCloseStatus = function() {
      this._resetToIndex();
      $scope.closed = true;
      $scope.phase = 0;
    };

    this._resetToIndex();
    $scope._updateStatus(status);

    $scope.$watch("toIndex.recreateIndex", function(newVal, oldVal) {
      if (newVal) {
        $scope.toIndex.parts = true;
        $scope.toIndex.applications = true;
        $scope.toIndex.salesNotes = true;
      }
    });

    $scope.refreshTask = $interval(function() {
      restService.getIndexingStatus().then(
        function success(newStatus) {
          $scope._updateStatus(newStatus);
        },
        function failure(response) {
          restService.error("Update of a status of the indexing process failed.",
            response);
        }
      );
    }, 1000);

    $scope.$on("$destroy", function() {
      $interval.cancel($scope.refreshTask);
    });

  }
]);
