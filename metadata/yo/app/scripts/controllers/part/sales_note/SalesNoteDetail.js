'use strict';

angular.module('ngMetaCrudApp')
  .controller('SalesNoteDetailCtrl', ['$log', '$routeParams', '$parse', 'dialogs', '$scope',
    'NgTableParams', '$location', 'toastr', 'SalesNotes', 'restService', 'part', 'salesNote',
    function($log, $routeParams, $parse, dialogs, $scope, NgTableParams, $location, toastr, SalesNotes,
        restService, part, salesNote) {
      $scope.part = part;
      $scope.salesNote = salesNote;
      $scope.salesNoteId = $routeParams.salesNoteId;
      $scope.SalesNotes = SalesNotes;
      $scope.editedSalesNote = {};

      var file = null;
      var formData = new FormData();
      var attachments = [];

      $scope.isUploadBttnDisabled = function () {
        return !formData.has('file') || $scope.isEditing();
      };

      $scope.changedAttachment = function(files) {
        file = files[0];
        formData.append('file', files[0]);
      };

      function _updateAttachmentsTable(updatedAttachments) {
        attachments.splice(0, attachments.length);
        _.each(updatedAttachments, function (e) {
          attachments.push(e);
        });
        $scope.attachmentsTableParams.settings({dataset: attachments});
        formData = new FormData();
      }

      // Attachment Table
      $scope.attachmentsTableParams = new NgTableParams({
        page: 1,
        count: 10,
        sorting: {'createDate': 'asc'}
      });

      // Related Part Table
      $scope.relatedPartTableParams = new NgTableParams({
        page: 1,
        count: 10,
        sorting: {'part.manufacturerPartNumber': 'asc'}
      });

      function _updateSalesNote(updatedSalesNote) {
        $scope.salesNote = updatedSalesNote;
        _updateAttachmentsTable($scope.salesNote.attachments);
        attachments = $scope.salesNote.attachments || [];
        $scope.attachmentsTableParams.settings({dataset: attachments});
        $scope.relatedPartTableParams.settings({dataset: $scope.salesNote.parts});
      }

      _updateSalesNote(salesNote);

      $scope.uploadAttachment = function() {
        restService.uploadAttachmentForSalesNote($scope.salesNoteId, file.name, file).then(
          function success(updatedSalesNote) {
            _updateSalesNote(updatedSalesNote);
            toastr.success('File uploaded.');
            formData.delete('file');
          },
          function failure(response) {
            restService.error('Can not upload the attachment.', response);
          }
        );
      };

      $scope.removeAttachment = function(attachmentId) {
        dialogs.confirm('Confirmation', 'Are you sure?\nDo you want to remove this attachment?')
          .result.then(
            function yes() {
              restService.removeAttachmentForSalesNote($scope.salesNoteId, attachmentId).then(
                function success(updatedSalesNote) {
                  _updateSalesNote(updatedSalesNote);
                  toastr.success('The attachment has been successfully removed.');
                },
                function failure(response) {
                  restService.error('Can not remove the attachment.', response);
                }
              );
              toastr.success('The attachment has been successfully removed.');
            }
          );
      };

      $scope.isPrimaryRelatedPart = function(relatedPart) {
        return $scope.salesNote.primaryPartId === relatedPart.part.id;
      };


      $scope.removeRelatedPart = function(partId) {
        dialogs.confirm('Confirmation', 'Are you sure?\nDo you want to remove this related part?')
          .result.then(
            function yes() {
              SalesNotes.removeRelatedPart($scope.salesNoteId, partId).then(
                function success(updatedSalesNote) {
                  _updateSalesNote(updatedSalesNote);
                  toastr.success('The related part has been successfully removed.');
                },
                function failure(response) {
                  restService.error('Can not remove the related part.', response);
                }
              );
            },
            function no() {
              // ignore
            }
          );
      };

      // Editing flag
      var editing = false;

      $scope.isEditing = function() {
        return editing;
      };

      $scope.edit = function() {
        editing = true;
        $scope.editedSalesNote.comment = $scope.salesNote.comment;
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
              restService.error('Could not get sales note details', errorResponse);
            });
      };

      $scope.remove = function() {
        dialogs.confirm('Delete sales note [' + $scope.salesNoteId + '].', 'Are you sure?').result.then(
          function() {
            restService.removeSalesNote($scope.salesNoteId).then(
              function() {
                $location.path('/part/' + $scope.part.id + '/sales_notes');
                toastr.success('Sales note [' + $scope.salesNoteId + '] has been successfully removed.');
              },
              function errorResponse(response) {
                restService.error('Removal of the sales note [' + $scope.salesNoteId + '] failed.', response);
              }
            );
          }
        );
      };

    }
  ]);
