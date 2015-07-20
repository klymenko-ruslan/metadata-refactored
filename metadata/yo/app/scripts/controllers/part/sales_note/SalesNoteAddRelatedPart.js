'use strict';

angular.module('ngMetaCrudApp')
  .controller('SalesNoteAddRelatedPartCtrl', function($location, $log, $routeParams, $scope, ngTableParams, SalesNotes, Restangular, restService) {
    $scope.SalesNotes = SalesNotes;
    
    $scope.partId = $routeParams.partId;
    $scope.salesNoteId = $routeParams.salesNoteId;

    // Load the part
    $scope.part = null;
    $scope.partPromise = restService.findPart($scope.partId).then(
        function (part) {
            $scope.part = part;

            // Make sure we're using the correct part type
            $scope.partType = part.partType.name;
        },
        function (errorResponse) {
            restService.error("Could not get part details", errorResponse);
        });

    // Load the sales note
    $scope.salesNote = null;
    $scope.salesNotePromise = Restangular.one('other/salesNote', $scope.salesNoteId).get().then(
        function (salesNote) {
            $scope.salesNote = salesNote;
            return salesNote;
        },
        function (errorResponse) {
            restService.error("Could not get sales note details", errorResponse);
        });
            
    // Related Part Table
    $scope.relatedPartTableParams = new ngTableParams({
        page: 1,
        count: 10,
        sorting: {}
      }, {
        getData: function ($defer, params) {
            $scope.salesNotePromise.then(function(salesNote) {
                // TODO: Paginate, fiter, sort
                $defer.resolve(salesNote.parts);
            });
        }
      });

    $scope.relatedPartTableParams.reload();
    $scope.pickedPart = null;
    $scope.pickPart = function (partId) {
        $scope.pickedPart = restService.findPart(partId).then(
            function (pickedPart) {
                $scope.pickedPart = pickedPart;
            },
            function (errorResponse) {
                restService.error("Could not pick part.", errorResponse);
                $log.log("Could not pick part", errorResponse);
            });
    };
    
    $scope.canSave = function() {
        return $scope.pickedPart !== null;
    };
    
    $scope.save = function() {
        SalesNotes.addRelatedPart($scope.salesNote, $scope.pickedPart)
            .then(function() {
                $location.path("/part/" + $scope.salesNote.primaryPartId + "/sales_note/" + $scope.salesNoteId);
            }, function (errorResponse) {
                restService.error("Could not pick part.", errorResponse);
            });
    };
  });
