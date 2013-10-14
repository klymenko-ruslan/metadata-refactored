'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartFormCtrl', function ($scope, $location, $routeParams, ngTableParams, restService, Restangular) {
        $scope.partId   = $routeParams.id;
        $scope.partType = $routeParams.type;
        $scope.part     = {};
        $scope.oldPart  = null;

        $scope.bomTableParams = new ngTableParams({
            count: 5,
            page: 1,
            total: 0
        });

        $scope.manufacturers = restService.listManufacturers();

        $scope.$watch('part.partType.typeName', function(name) {

            // Make sure we're using the correct part type
            if (name != null) {
                $scope.partType = name;
            }
        });

        // Lookup the part or setup the create workflow
        if (angular.isDefined($scope.partId)) {

            $scope.oldPart = restService.findPart($scope.partId, {fields: 'bom'});
            $scope.oldPart.then(function(part) {
                    console.log("Part data loaded.");

                    // Save the part
                    $scope.part = part;
                    $scope.oldPart = Restangular.copy(part);
                }, function(response) {
                    console.error("Could not get part list from the server.");
                });
        }

        $scope.sync = function() {
          restService.sync($scope.partId);
        }

        $scope.revert = function() {
            $scope.part = Restangular.copy($scope.oldPart);
            $scope.$broadcast("revert");
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
