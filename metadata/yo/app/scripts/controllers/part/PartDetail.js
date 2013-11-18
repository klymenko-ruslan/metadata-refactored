'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartDetailCtrl', function ($scope, $log, $location, $routeParams, ngTableParams, restService) {
      $scope.partId = $routeParams.id;
      $scope.partType = $routeParams.type;

      $scope.part = restService.findPart($scope.partId, {fields: 'bom,interchange.parts'});


      $scope.interchangeTableParams = new ngTableParams({
        page: 1,
        count: 10,
        counts: [10, 25, 50, 100]
      });

      $scope.bomTableParams = new ngTableParams({
        page: 1,
        count: 10,
        counts: [10, 25, 50, 100]
      });

      $scope.part.then(function(part) {
        console.log("Loaded part: " + part.id);

        // Make sure we're using the correct part type
        $scope.partType = part.partType.typeName;
      }, function(response) {
        console.error("Could not get part data from server.");
      });

      $scope.rowClick = function(partType, partId) {
        $location.path("/part/" + partType + "/" + partId);
      };

    });
