"use strict";

angular.module("ngMetaCrudApp")
  .controller("SalesNoteDetailCtrl", ["$log", "$routeParams", "$parse", "dialogs", "$scope",
    "ngTableParams", "$location", "toastr", "SalesNotes", "utils", "restService", "part", "salesNote",
    function($log, $routeParams, $parse, dialogs, $scope, ngTableParams, $location, toastr, SalesNotes,
        utils, restService, part, salesNote) {
      $scope.part = part;
      $scope.salesNote = salesNote;
      $scope.salesNoteId = $routeParams.salesNoteId;
      $scope.SalesNotes = SalesNotes;
      $scope.editedSalesNote = {};

      $scope.isPrimaryRelatedPart = function(relatedPart) {
        return salesNote.primaryPartId === relatedPart.part.id;
      };

      // Attachment Table
      $scope.attachmentTableParams = new ngTableParams({
        page: 1,
        count: 10,
        sorting: {}
      }, {
        getData: utils.localPagination(salesNote.attachments, "createDate")
      });

      // Related Part Table
      $scope.relatedPartTableParams = new ngTableParams({
        page: 1,
        count: 10,
        sorting: {}
      }, {
        getData: utils.localPagination(salesNote.parts, "part.manufacturerPartNumber")
      });

      // Editing flag
      var editing = false;

      $scope.isEditing = function() {
        return editing;
      };

      $scope.edit = function() {
        editing = true;
        $scope.editedSalesNote.comment = salesNote.comment;
      };

      $scope.cancel = function() {
        editing = false;
      };

      $scope.save = function() {
        restService.updateSalesNote($scope.salesNoteId, $scope.editedSalesNote.comment).then(
            function success() {
              $scope.salesNote.comment = $scope.editedSalesNote.comment;
              editing = false;
            },
            function failure(errorResponse) {
              restService.error("Could not get sales note details", errorResponse);
            });
      };

      $scope.remove = function() {
        dialogs.confirm("Delete sales note [" + $scope.salesNoteId + "].", "Are you sure?").result.then(
          function() {
            restService.removeSalesNote($scope.salesNoteId).then(
              function() {
                $location.path("/part/" + $scope.part.id + "/sales_notes");
                toastr.success("Sales note [" + $scope.salesNoteId + "] has been successfully removed.");
              },
              function errorResponse(response) {
                restService.error("Removal of the sales note [" + $scope.salesNoteId + "] failed.", response);
              }
            );
          }
        );
      };

    }
  ]);
