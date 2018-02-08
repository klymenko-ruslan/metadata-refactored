'use strict';

angular.module('ngMetaCrudApp')
  .service('SalesNotes', ['$location', '$log', /*'$uibModalInstance',*/ 'Restangular', 'restService', 'toastr', 'User',
    function SalesNotes($location, $log, /*$uibModalInstance,*/ Restangular, restService, toastr, User) {

      this.states = ['draft', 'submitted', 'approved', 'rejected', 'published'];

      this.addRelatedParts = function(salesNote, partIds) {
        return Restangular.one('other/salesNote/' + salesNote.id).post('parts', partIds);
      };

      this.removeRelatedPart = function(salesNoteId, partId) {
        return Restangular.one('other/salesNote/' + salesNoteId + '/part', partId).remove();
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
