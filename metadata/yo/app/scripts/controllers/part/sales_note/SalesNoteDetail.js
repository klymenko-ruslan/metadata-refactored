'use strict';

angular.module('ngMetaCrudApp')
	.controller('SalesNoteDetailCtrl', function($routeParams, $scope, ngTableParams, Restangular, SalesNotes, restService) {
    $scope.partId = $routeParams.partId;
    $scope.salesNoteId = $routeParams.salesNoteId;
    
    $scope.SalesNotes = SalesNotes;

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
    $scope.editedSalesNote = {};
    $scope.salesNotePromise = Restangular.one('other/salesNote', $scope.salesNoteId).get().then(
        function (salesNote) {
            $scope.salesNote = salesNote;
            return salesNote;
        },
        function (errorResponse) {
            restService.error("Could not get sales note details", errorResponse);
        });
        
    // Attachment Table
    $scope.attachmentTableParams = new ngTableParams({
        page: 1,
        count: 10,
        sorting: {}
      }, {
        getData: function ($defer, params) {
            $scope.salesNotePromise.then(function(salesNote) {
                // TODO: Paginate, fiter, sort
                $defer.resolve(salesNote.attachments);
            });
        }
      });

    $scope.attachmentTableParams.reload();
    
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
    
    // Editing flag
    var editing = false;
    
    $scope.isEditing = function() {
        return editing;
    };
    
    $scope.edit = function() {
        editing = true;
        
        $scope.salesNotePromise.then(function(salesNote) {
            $scope.editedSalesNote.comment = salesNote.comment;
        });
    };
    
    $scope.cancel = function() {
        editing = false;
    };
    
    $scope.save = function() {
        Restangular.one("other/salesNote")
            .post($scope.salesNoteId, {"comment": $scope.editedSalesNote.comment})
            .then(function() {
                $scope.salesNote.comment = $scope.editedSalesNote.comment;
                editing = false;
            },
            function (errorResponse) {
                restService.error("Could not get sales note details", errorResponse);
            });
    };
});
