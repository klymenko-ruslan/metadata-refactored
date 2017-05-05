'use strict';

angular.module('ngMetaCrudApp')
  .service('SalesNotes', ['$location', '$log', '$uibModalInstance', 'Restangular', 'restService', 'toastr', 'User',
    function SalesNotes($location, $log, $uibModalInstance, Restangular, restService, toastr, User) {

      this.states = ['draft', 'submitted', 'approved', 'rejected', 'published'];

      this.addAttachment = function(salesNote, name, file) {
        Restangular.one('other/sales_note', salesNote.id).all('image').post(file, {}, {
          'Content-Type': 'application/octet-stream'
        }).then(
          function(response) {
            // Success
            toastr.success('Added image.');
            $uibModalInstance.close(response);
          },
          function(response) {
            // Error
            restService.error('Could not upload image.', response);
          }
        );
      };

      this.addRelatedPart = function(salesNote, part) {
        return Restangular.one('other/salesNote/' + salesNote.id + '/part')
          .post(part.id, null)
          .then(function() {
            // TODO: Add the related part? Right now it's reloaded.
          }, function(errorResponse) {
            $log.log('Could not add related part', errorResponse);
          });
      };

      this.removeRelatedPart = function(salesNote, partId, tableParams) {
        return Restangular.one('other/salesNote/' + salesNote.id + '/part', partId)
          .remove()
          .then(function() {
            var idx = _.findIndex(salesNote.parts, function(salesNotePart) {
              return salesNotePart.part.id === partId;
            });
            if (idx !== -1) {
              salesNote.parts.splice(idx, 1);
              tableParams.reload();
            }
          }, function(errorResponse) {
            $log.log('Could not remove related part', errorResponse);
          });
      };

      this.canSubmit = function(salesNote) {
        return _.isObject(salesNote) && (salesNote.state === 'draft' || salesNote.state === 'rejected');
      };

      this.canPublish = function(salesNote) {
        return _.isObject(salesNote) && salesNote.state === 'approved';
      };

      this.canApprove = function(salesNote) {
        return _.isObject(salesNote) && salesNote.state === 'submitted';
      };

      this.canReject = function(salesNote) {
        return _.isObject(salesNote) && (salesNote.state === 'submitted' || salesNote.state === 'approved');
      };

      this.canRetract = function(salesNote) {
        return _.isObject(salesNote) && salesNote.state === 'published';
      };

      this.canEdit = function(salesNote) {
        if (!_.isObject(salesNote)) {
          return false;
        }

        if (salesNote.state === 'published' && User.hasRole('ROLE_SALES_NOTE_PUBLISH')) {
          return true;
        }

        if ((salesNote.state === 'approved' || salesNote.state === 'submitted') && User.hasRole('ROLE_SALES_NOTE_APPROVE')) {
          return true;
        }

        return (salesNote.state === 'draft' || salesNote.state === 'rejected') && User.hasRole('ROLE_SALES_NOTE_SUBMIT');
      };

      // State Management
      this.submit = function(salesNote) {
        return Restangular.one('other/salesNote', salesNote.id).post('submit')
          .then(function() {
            salesNote.state = 'submitted';
          });
      };

      this.approve = function(salesNote) {
        return Restangular.one('other/salesNote', salesNote.id).post('approve')
          .then(function() {
            salesNote.state = 'approved';
          });
      };

      this.reject = function(salesNote) {
        return Restangular.one('other/salesNote', salesNote.id).post('reject').then(
          function success() {
            salesNote.state = 'rejected';
          });
      };

      this.publish = function(salesNote) {
        return Restangular.one('other/salesNote', salesNote.id).post('publish')
          .then(function() {
            salesNote.state = 'published';
          });
      };

      this.retract = function(salesNote) {
        return Restangular.one('other/salesNote', salesNote.id).post('retract')
          .then(function() {
            salesNote.state = 'approved';
          });
      };

      return this;
    }
  ]);
