'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartFormCtrl', function ($scope, restService, $routeParams, $location) {
        $scope.partId   = $routeParams.id;
        $scope.partType = $routeParams.type;
        $scope.part     = null;
        $scope.oldPart  = null;

        // Lookup the part or setup the create workflow
        if (angular.isDefined($scope.partId)) {
            console.log("Editing part #" + $scope.partId);

            restService.findPart($scope.partId)
                .then(function(part) {
                    console.log("Part data loaded.");

                    // Save the part
                    $scope.part = part;
//                    $scope.manufacturer.id = part.manufacturer.id;

                    // Save a copy for reverting
                    $scope.oldPart = {};
                    angular.copy($scope.part, $scope.oldPart);

                    // Make sure we're using the correct part type
                    $scope.partType = part.partType.typeName;
                }, function(response) {
                    console.error("Could not get manufacturer list from the server.");
                    // TODO: Display error
                });
        } else {
            console.log("Creating new part");
            $scope.part = {};
        }

        $scope.clearInterchange = function() {
            $scope.part.interchange = null;
        }

        $scope.revertInterchange = function() {
            $scope.part.interchange = {};
            angular.copy($scope.oldPart.interchange, $scope.part.interchange);
        };

        $scope.revert = function() {
            angular.copy($scope.oldPart, $scope.part);
        }

        $scope.save = function() {
            $scope.part.put();
        }

        $scope.disable = function() {

        }

        $scope.enable = function() {

        }

  });
