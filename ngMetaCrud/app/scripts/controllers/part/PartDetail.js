'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartDetailCtrl', function ($scope, $location, $routeParams, partService) {
        $scope.partId = $routeParams.id;
        $scope.partType = $routeParams.type;

        $scope.part = partService.findPart($scope.partId);

        $scope.part.then(function(part) {

                console.log("Loaded part: " + part.id);

            // Make sure we're using the correct part type
            $scope.partType = part.partType.typeName;
        }, function(response) {
            alert("Could not get part data from the server.");
        });

    });
