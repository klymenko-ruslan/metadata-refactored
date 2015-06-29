'use strict';

angular.module('ngMetaCrudApp')
  .service('SalesNotes', function SalesNotes(Restangular) {
      var SalesNotes = this;
      
      SalesNotes.states = ["draft", "submitted", "approved", "rejected", "published"];
      
      SalesNotes.addAttachment = function(salesNote, name, file) {
        Restangular.one('other/sales_note', salesNote.id).all('image').post(file, {}, {'Content-Type': 'application/octet-stream'}).then(
            function(response) {
              // Success
              gToast.open("Added image.");
              $modalInstance.close(response);
            },
            function(response) {
              // Error
              restService.error("Could not upload image.", response);
            }
        );
      };
      
      // State Management
      
      SalesNotes.submit = function(salesNote) {
          return Restangular.one("other/salesNote", salesNote.id).post("submit")
                  .then(function() {
                      salesNote.state="submitted";
                    });
      };
      
      SalesNotes.approve = function(salesNote) {
          return Restangular.one("other/salesNote", salesNote.id).post("approve")
                  .then(function() {
                      salesNote.state="approved";
                    });
      };
      
      SalesNotes.reject = function(salesNote) {
          return Restangular.one("other/salesNote", salesNote.id).post("reject")
                  .then(function() {
                      salesNote.state="rejected";
                    });
      };
      
      SalesNotes.publish = function(salesNote) {
          return Restangular.one("other/salesNote", salesNote.id).post("publish")
                  .then(function() {
                      salesNote.state="published";
                    });
      };
      
      SalesNotes.retract = function(salesNote) {
          return Restangular.one("other/salesNote", salesNote.id).post("retract")
                  .then(function() {
                      salesNote.state="approved";
                    });
      };
      
      return this;
  });
