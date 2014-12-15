'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartAncestorsCtrl', function ($routeParams, $scope, restService, Restangular) {
    $scope.partId = $routeParams.id;

    $scope.part = null;
    $scope.partPromise = restService.findPart($scope.partId).then(
      function (part) {
        $scope.part = part;

        // Make sure we're using the correct part type
        $scope.partType = part.partType.name;
      },
      function (errorResponse) {
        $log.log("Could not get part details", errorResponse);
        restService.error("Could not get part details", errorResponse);
      });
      
    $scope.ancestors = null;
    $scope.loadAncestors = function() {
      if (!angular.isArray($scope.ancestors)) {
        Restangular.setParentless(false);
        
        return Restangular.one('part', $scope.partId).all('ancestors').getList().then(
          function(ancestors) {
            $scope.ancestors = ancestors;
            $scope.apply();
          },
          function(response) {
            restService.error("Could not get ancestors.", response);
          }
        );
      }
    };
    
    restService.getBomAncestryRebuildingCompletePromise().then($scope.loadAncestors);
  });
