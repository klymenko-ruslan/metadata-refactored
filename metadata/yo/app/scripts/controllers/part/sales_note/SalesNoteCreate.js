'use strict';

angular.module('ngMetaCrudApp')
  .controller('SalesNoteCreateCtrl', ['$location', '$scope', '$routeParams', 'Restangular', 'restService',
      function($location, $scope, $routeParams, Restangular, restService) {
    $scope.partId = $routeParams.id;
    
    $scope.salesNote = {
        primaryPartId: $scope.partId,
        comment:       "Enter your notes here"
    };
    
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
        
    $scope.saveAndEdit = function() {
        $scope.savePromise = Restangular.all('other/salesNote').post($scope.salesNote).then(
                function (salesNoteResponse) {
                    $location.path("/part/" + $scope.partId + "/sales_note/" + salesNoteResponse.id);
                    return salesNoteResponse;
                },
                function (errorResponse) {
                  restService.error("Couldn't save for sales note.", errorResponse);
                });
    };
  }]);


          