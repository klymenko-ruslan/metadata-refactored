'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartDetailCtrl', function ($scope, $location, $routeParams, ngTableParams, restService) {
        $scope.partId = $routeParams.id;
        $scope.partType = $routeParams.type;

        $scope.part = restService.findPart($scope.partId, {fields: 'bom,interchange.parts'});


      $scope.interchangeTableParams = new ngTableParams({
        count: 5,
        page: 1,
        total: 0
      });


      $scope.bomTableParams = new ngTableParams({
        count: 5,
        page: 1,
        total: 0
      });

        $scope.part.then(function(part) {

                console.log("Loaded part: " + part.id);

            // Make sure we're using the correct part type
            $scope.partType = part.partType.typeName;
        }, function(response) {
            console.error("Could not get part data from server.");
        });

    });
