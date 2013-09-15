'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartFormCtrl', function ($scope, partService, $routeParams) {
        $scope.partId   = $routeParams.id;
        $scope.partType = $routeParams.type;
        $scope.part     = null;
        $scope.oldPart  = null;

        // Lookup the part or setup the create workflow
        if (angular.isDefined($scope.partId)) {
            console.log("Editing part # " + $scope.partId);

            partService.findPart($scope.partId)
                .then(function(part) {
                    console.log("Loaded part: " + JSON.stringify(part));

                    // Save the part
                    $scope.part = part;

                    // Save a copy for reverting
                    $scope.oldPart = {};
                    angular.copy($scope.part, $scope.oldPart);

                    // Make sure we're using the correct part type
                    $scope.partType = part.partType.typeName;

                }, function(response) {
                    console.error("Could not get part data from the server.");
                    // TODO: Display error
                });
        } else {
            console.log("Creating new part");
            $scope.part = {};
        }

        $scope.revert = function() {
            console.log("Current: " + JSON.stringify($scope.part));
            console.log("Old: " + JSON.stringify($scope.oldPart));
            angular.copy($scope.oldPart, $scope.part);
        }

        $scope.save = function() {

        }

        $scope.disable = function() {

        }

        $scope.enable = function() {

        }


  });
