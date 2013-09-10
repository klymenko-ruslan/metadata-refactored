'use strict';

angular.module('ngMetaCrudApp')
  .controller('InterchangesCtrl', function ($scope, $resource) {

        // Methods
        $scope.isChanged = function() {
            return $scope.interchangeId !== $scope.interchangeNewId;
        };

        $scope.undo = function() {
            $scope.interchangeNewId = $scope.interchangeId;
            $scope.interchangePartId = null;
            $scope.result = null;
        };

        $scope.clear = function() {
            $scope.interchangeNewId = null;
            $scope.interchangePartId = null;
            $scope.result = null;
        };

        $scope.$watch('result', function() {
            if ($scope.result != null) {
                $scope.interchangeNewId = $scope.result != null ? $scope.result.interchange_id : null;
                $scope.interchangePartId = $scope.result._id;
            }
        })
  });
