'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartFormCtrl', function ($q, $scope, $location, $log, $routeParams, ngTableParams, restService, Restangular, PartTypes) {
        $scope.partId     = $routeParams.id;

        // Set the part type
        if ($routeParams.typeId) {
          $q.when(PartTypes.getById($routeParams.typeId)).then(function(partType) {
            $scope.part.partType = partType;
            $log.log('Got part type by ID', $routeParams.typeId, $scope.partType);
          });
        }

        $scope.part       = {};
        $scope.oldPart    = null;

        $scope.bomTableParams = new ngTableParams({
            count: 5,
            page: 1,
            total: 0
        });

        // Lookup the part or setup the create workflow
        if (angular.isDefined($scope.partId)) {

            $scope.oldPart = restService.findPart($scope.partId);
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
          if ($scope.oldPart == null) {
            Restangular.all('part').post($scope.part).then(
                function(id) {
                  $location.path('/part/' + $scope.part.partType.typeName + '/' + id + '/form');
                },
                function() {
                  alert("Could not save part.");
                })
          } else {
            $scope.part.put().then(
                function(part) {
                  $scope.part = part;
                  $scope.oldPart = Restangular.copy(part);
                },
                function() {
                  alert("Could not update part");
                }
            );
          }
        }

        $scope.bomDelete = function(index, bomItem) {
            $log.log("Deleting BOM Item", index, bomItem);
            $scope.part.bom.splice(index, 1)

        }

  });
