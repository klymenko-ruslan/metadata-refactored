'use strict';

angular.module('ngMetaCrudApp')
  .controller('SalesNoteAddRelatedPartCtrl', ['$location', '$log', 'toastr',
    '$routeParams', '$scope', 'NgTableParams', 'SalesNotes',
    'restService', 'part', 'salesNote',
    function($location, $log, toastr, $routeParams, $scope, NgTableParams, SalesNotes,
      restService, part, salesNote) {
      $scope.SalesNotes = SalesNotes;
      $scope.partId = $routeParams.partId;
      $scope.salesNoteId = $routeParams.salesNoteId;
      $scope.part = part;
      $scope.salesNote = salesNote;

      // Related Part Table
      $scope.relatedPartTableParams = new NgTableParams({
        page: 1,
        count: 10,
        sorting: {
          'manufacturerPartNumber': 'asc'
        }
      }, {
        dataset: salesNote.parts
      });

      var pickedParts = [];
      var pickedPartIds = {};
      var existingRelatedPartIds = null;

      function updateRelatedPartIds() {
        existingRelatedPartIds = {};
        _.each(salesNote.parts, function(p) {
          existingRelatedPartIds[p.part.id] = true;
        });
      }

      updateRelatedPartIds();

      // Picked parts table.
      $scope.pickedPartsTableParams = new NgTableParams({
        page: 1,
        count: 5,
      }, {
        counts: [5, 10, 15],
        dataset: pickedParts
      });

      $scope.pickPart = function(partId) {
        $scope.pickedPart = restService.findPart(partId).then(
          function(pickedPart) {
            pickedParts.push(pickedPart);
            pickedPartIds[pickedPart.id] = true;
            $scope.pickedPartsTableParams.settings({dataset: pickedParts});
          },
          function(errorResponse) {
            restService.error('Could not pick part.', errorResponse);
            $log.log('Could not pick part', errorResponse);
          });
      };

      $scope.unpickPart = function(partId) {
        var idx = _.findIndex(pickedParts, function(p) {
          return p.id === partId;
        });
        pickedParts.splice(idx, 1);
        delete pickedPartIds[partId];
        $scope.pickedPartsTableParams.settings({dataset: pickedParts});
      };

      $scope.unpickAll = function() {
        _.each(pickedParts, function(pp) {
          delete pickedPartIds[pp.id];
        });
        pickedParts.splice(0, pickedParts.length);
        $scope.pickedPartsTableParams.settings({dataset: pickedParts});
      };

      $scope.isBttnUnpickAllDisabled = function() {
        return pickedParts.length === 0;
      };

      $scope.canPick = function(id) {
        var exists = existingRelatedPartIds[id] || pickedPartIds[id];
        return !exists;
      };

      $scope.canSave = function() {
        return pickedParts.length > 0;
      };

      $scope.save = function() {
        var partIds = _.map(pickedParts, function(p) { return p.id; });
        SalesNotes.addRelatedParts($scope.salesNote, partIds).then(
          function success(/*updateSalesNote*/) {
            updateRelatedPartIds();
            toastr.success('The picked part(s) have been successfully added to the sales note.');
            $location.path('/part/' + $scope.salesNote.primaryPartId + '/sales_note/' + $scope.salesNoteId);
          },
          function failure(errorResponse) {
            restService.error('Storing of the picked part(s) failed.', errorResponse);
          });
      };

    }
  ]);
