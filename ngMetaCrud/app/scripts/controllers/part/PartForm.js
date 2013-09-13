'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartFormCtrl', function ($scope, partService, $routeParams) {
        $scope.partId   = $routeParams.id;
        $scope.partType = $routeParams.type;
        
        if (angular.isDefined($scope.partId)) {
            console.log("Editing part # " + $scope.partId);

            $scope.part = partService.findPart($scope.partId);

            $scope.part.then(function(part) {

                console.log("Loaded part: " + $scope.partId);

                // Make sure we're using the correct part type
                $scope.partType = part.partType.typeName;
            }, function(response) {
                alert("Could not get part data from the server.");
            });
        } else {
            console.log("Creating new part");
            $scope.part = {};
        }


  });
