'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartFormCtrl', function ($scope, restService, $routeParams, ngTableParams, $location) {
        $scope.partId   = $routeParams.id;
        $scope.partType = $routeParams.type;
        $scope.part     = null;
        $scope.oldPart  = null;

        $scope.bomTableParams = new ngTableParams({
            count: 5,
            page: 1,
            total: 0
        });

        // Lookup the part or setup the create workflow
        if (angular.isDefined($scope.partId)) {

            $scope.part = restService.findPart($scope.partId, {fields: 'bom'});
            $scope.part.then(function(part) {
                    console.log("Part data loaded.");

                    // Save the part
                    $scope.part = part;

                    // Save a copy for reverting
                    $scope.oldPart = {};
                    angular.copy($scope.part, $scope.oldPart);

                    // Make sure we're using the correct part type
                    $scope.partType = part.partType.typeName;
                }, function(response) {
                    console.error("Could not get manufacturer list from the server.");
                });
        } else {
            $scope.part = {};
        }

        $scope.isChanged = function() {
            return !angular.equals($scope.part, $scope.oldPart);
        }

        $scope.revert = function() {
            angular.copy($scope.oldPart, $scope.part);
        }

        $scope.save = function() {
            $scope.part.put();
        }

        $scope.bomDelete = function(index, bomItem) {
            console.log("Deleting BOM Item", index, bomItem);
            $scope.part.bom.splice(index, 1)
            console.log($scope.part.bom);

        }

  });
