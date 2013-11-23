'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartCreateModalCtrl', function($scope, $modalInstance, $log, $location, PartTypes) {
      $scope.PartTypes = PartTypes;
      $scope.selection = {};

      $scope.create = function() {
        $modalInstance.close('cancel');
        $location.path('/part/createByPartTypeId/' + $scope.selection.partType.id);
      }

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      }

      $scope.refresh = function() {
        $scope.selection = {};
        PartTypes.refresh();
      }
  });
