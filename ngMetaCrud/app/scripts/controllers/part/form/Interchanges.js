'use strict';

angular.module('ngMetaCrudApp')
  .controller('InterchangesCtrl', function ($scope, $resource) {
        angular.copy($scope.part.interchange, $scope.oldInterchange);

        // Methods
        $scope.isChanged = function() {
            return angular.equals($scope.part.interchange, $scope.oldInterchange)
        };

        $scope.undo = function() {
            angular.copy($scope.oldInterchange, $scope.part.interchange);
            $scope.interchangePartId = null;
        };

        $scope.clear = function() {
            $scope.part.interchange = null;
            $scope.oldInterchange = null;
            $scope.interchangePartId = null;
        };

        $scope.$on('PartTable.click', function(event, part) {
            if (part != null) {
                $scope.interchange = {id : part.id} = $scope.result != null ? $scope.result.interchange_id : null;
                $scope.interchangePartId = $scope.result._id;
            }
        });
  });
