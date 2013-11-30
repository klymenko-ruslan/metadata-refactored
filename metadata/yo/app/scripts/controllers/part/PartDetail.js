'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartDetailCtrl', function ($scope, $log, $q, $location, $routeParams, ngTableParams, restService) {
      $scope.partId = $routeParams.id;
      $scope.partType = $routeParams.type;

      $scope.part = null;
      $scope.partPromise = restService.findPart($scope.partId).then(
          function(part) {
            $scope.part = part;

            // Make sure we're using the correct part type
            $scope.partType = part.partType.typeName;

            // Reload the table
            $scope.interchangeTableParams.reload();
            $scope.bomTableParams.reload();
          },
          function(errorResponse) {
            $log.log("Could not get part details", errorResponse);
            alert("Could not get part details");
          });


      $scope.interchangeTableParams = new ngTableParams({
        page: 1,
        count: 10
      }, {
        getData: function($defer, params) {
          if (!angular.isObject($scope.part) || !angular.isObject($scope.part.interchange)) {
            $defer.reject();
            return;
          };

          // Update the total and slice the result
          $defer.resolve($scope.part.interchange.parts.slice((params.page() - 1) * params.count(), params.page() * params.count()));
          params.total($scope.part.interchange.parts.length);
        }
      });

      $scope.bomTableParams = new ngTableParams({
        page: 1,
        count: 10
      }, {
        getData: function($defer, params) {
          if (!angular.isObject($scope.part)) {
            $defer.reject();
            return;
          };

          // Update the total and slice the result
          $defer.resolve($scope.part.bom.slice((params.page() - 1) * params.count(), params.page() * params.count()));
          params.total($scope.part.bom.length);
        }
      });

      $scope.rowClick = function(partType, partId) {
        $location.path("/part/" + partType + "/" + partId);
      };

    });
