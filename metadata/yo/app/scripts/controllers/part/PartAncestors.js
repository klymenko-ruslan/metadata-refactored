'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartAncestorsCtrl', function ($routeParams, $scope, restService, Restangular) {
    $scope.partId = $routeParams.id;
    $scope.partType = $routeParams.type;

    $scope.part = null;
    $scope.partPromise = restService.findPart($scope.partId).then(
      function (part) {
        $scope.part = part;

        // Make sure we're using the correct part type
        $scope.partType = part.partType.typeName;
      },
      function (errorResponse) {
        $log.log("Could not get part details", errorResponse);
        alert("Could not get part details");
      });

    Restangular.setParentless(false);
    $scope.ancestorsPromise = Restangular.one('part', $scope.partId).all('ancestors').getList().then(
      function(ancestors) {
        $scope.ancestors = ancestors;
      },
      function(response) {
        restService.error("Could not get ancestors.", response);
      }
    );
  });
