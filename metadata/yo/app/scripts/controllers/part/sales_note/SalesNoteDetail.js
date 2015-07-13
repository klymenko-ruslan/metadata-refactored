'use strict';

angular.module('ngMetaCrudApp')
	.controller('SalesNoteDetailCtrl', function($routeParams, $scope, ngTableParams, Restangular, restService) {
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
});
