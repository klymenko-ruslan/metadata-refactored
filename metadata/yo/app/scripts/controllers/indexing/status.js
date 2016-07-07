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

    $scope._updateStatus = function(status) {

      $scope.phase = status.phase;

      if (status.phase == 3) {
        $interval.cancel($scope.refreshTask);
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

      $scope.toIndex.parts = status.indexParts;
      $scope.toIndex.applications = status.indexApplications;
      $scope.toIndex.salesNotes = status.indexSalesNotes;

      $scope.userId = status.userId;
      $scope.userName = status.userName;

    };

    this._resetToIndex = function() {
      $scope.toIndex.parts = true;
      $scope.toIndex.applications = true;
      $scope.toIndex.salesNotes = true;
    };

    this.startIndexing = function() {
      restService.startIndexing($scope.toIndex).then(
        function success(newStatus) {
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
      $scope.phase = 0;
    };

    $scope._updateStatus(status);

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
