'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartFormCtrl', function ($q, $scope, $location, $log, $routeParams, ngTableParams, restService, Restangular, PartTypes) {

      // Setup the create/update workflow
      if ($routeParams.id) {
        $scope.partId     = $routeParams.id;

        $scope.oldPartPromise = restService.findPart($scope.partId).then(
            function(part) {
              console.log("Part data loaded.");

              // Save the part
              $scope.part = part;
              $scope.oldPart = Restangular.copy(part);

              // Reload the table
              $scope.bomTableParams.reload();
            },
            function(response) {
              alert("Could not get part data from the server.");
            });
      } else {
        $scope.partId = null;
        $scope.part = {};
      }

      // Set the part type
      if ($routeParams.typeId) {
        $q.when(PartTypes.getById($routeParams.typeId)).then(function(partType) {
          $scope.part.partType = partType;
          $log.log('Got part type by ID', $routeParams.typeId, $scope.partType);
        });
      }

      $scope.bomTableParams = new ngTableParams({
        page: 1,
        count: 10
      }, {
        getData: function($defer, params) {
          if (!angular.isObject($scope.part)) {
            $defer.reject();
            return;
          };;

          // Update the total and slice the result
          $defer.resolve($scope.part.bom.slice((params.page() - 1) * params.count(), params.page() * params.count()));
          params.total($scope.part.bom.length);
        }
      });

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
