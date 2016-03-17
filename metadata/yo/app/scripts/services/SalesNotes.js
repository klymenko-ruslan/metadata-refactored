"use strict";

angular.module("ngMetaCrudApp")
  .service("SalesNotes", ["$location", "$log", "Restangular", "User",
    function SalesNotes($location, $log, Restangular, User) {
      var SalesNotes = this;

      SalesNotes.states = ["draft", "submitted", "approved", "rejected", "published"];

      SalesNotes.addAttachment = function(salesNote, name, file) {
        Restangular.one("other/sales_note", salesNote.id).all("image").post(file, {}, {
          "Content-Type": "application/octet-stream"
        }).then(
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

      SalesNotes.addRelatedPart = function(salesNote, part) {
        return Restangular.one("other/salesNote/" + salesNote.id + "/part")
          .post(part.id, null)
          .then(function() {
            // TODO: Add the related part? Right now it's reloaded.
          }, function(errorResponse) {
            $log.log("Could not add related part", errorResponse);
          });
      };

      SalesNotes.removeRelatedPart = function(salesNote, partId, tableParams) {
        return Restangular.one("other/salesNote/" + salesNote.id + "/part", partId)
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
            $log.log("Could not remove related part", errorResponse);
          });
      };

      SalesNotes.canSubmit = function(salesNote) {
        return _.isObject(salesNote) && (salesNote.state === "draft" || salesNote.state === "rejected");
      };

      SalesNotes.canPublish = function(salesNote) {
        return _.isObject(salesNote) && salesNote.state === "approved";
      };

      SalesNotes.canApprove = function(salesNote) {
        return _.isObject(salesNote) && salesNote.state === "submitted";
      };

      SalesNotes.canReject = function(salesNote) {
        return _.isObject(salesNote) && (salesNote.state === "submitted" || salesNote.state === "approved");
      };

      SalesNotes.canRetract = function(salesNote) {
        return _.isObject(salesNote) && salesNote.state === "published";
      };

      SalesNotes.canEdit = function(salesNote) {
        if (!_.isObject(salesNote)) {
          return false;
        }

        if (salesNote.state == "published" && User.hasRole("ROLE_SALES_NOTE_PUBLISH")) {
          return true;
        }

        if ((salesNote.state == "approved" || salesNote.state == "submitted") && User.hasRole("ROLE_SALES_NOTE_APPROVE")) {
          return true;
        }

        return (salesNote.state == "draft" || salesNote.state == "rejected") && User.hasRole("ROLE_SALES_NOTE_SUBMIT");
      };

      // State Management
      SalesNotes.submit = function(salesNote) {
        return Restangular.one("other/salesNote", salesNote.id).post("submit")
          .then(function() {
            salesNote.state = "submitted";
          });
      };

      SalesNotes.approve = function(salesNote) {
        return Restangular.one("other/salesNote", salesNote.id).post("approve")
          .then(function() {
            salesNote.state = "approved";
          });
      };

      SalesNotes.reject = function(salesNote) {
        return Restangular.one("other/salesNote", salesNote.id).post("reject")
          .then(function() {
            salesNote.state = "rejected";
          });
      };

      SalesNotes.publish = function(salesNote) {
        return Restangular.one("other/salesNote", salesNote.id).post("publish")
          .then(function() {
            salesNote.state = "published";
          });
      };

      SalesNotes.retract = function(salesNote) {
        return Restangular.one("other/salesNote", salesNote.id).post("retract")
          .then(function() {
            salesNote.state = "approved";
          });
      };

      return this;
    }
  ]);
