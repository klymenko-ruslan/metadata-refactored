'use strict';

angular.module('ngMetaCrudApp')
  .controller('SalesNoteAddRelatedPartCtrl', ['$location', '$log',
    '$routeParams', '$scope', 'NgTableParams', 'SalesNotes',
    'restService', 'part', 'salesNote',
    function($location, $log, $routeParams, $scope, NgTableParams, SalesNotes,
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
          'part.manufacturerPartNumber': 'asc'
        }
      }, {
        dataset: salesNote.parts
      });

      $scope.pickedPart = null;
      $scope.pickPart = function(partId) {
        $scope.pickedPart = restService.findPart(partId).then(
          function(pickedPart) {
            $scope.pickedPart = pickedPart;
          },
          function(errorResponse) {
            restService.error('Could not pick part.', errorResponse);
            $log.log('Could not pick part', errorResponse);
          });
      };

      $scope.canPick = function(id) {
        var exists = _.find($scope.salesNote.parts, function(p) {
          return p.part.id === id;
        });
        return !exists;
      };

      $scope.canSave = function() {
        return $scope.pickedPart !== null;
      };

      $scope.save = function() {
        SalesNotes.addRelatedPart($scope.salesNote, $scope.pickedPart)
          .then(function() {
            $location.path('/part/' + $scope.salesNote.primaryPartId + '/sales_note/' + $scope.salesNoteId);
          }, function(errorResponse) {
            restService.error('Could not pick part.', errorResponse);
          });
      };
    }
  ]);
