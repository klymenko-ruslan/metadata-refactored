'use strict';

angular.module('ngMetaCrudApp')

.controller('PartListCtrl', ['$scope', '$log', '$location', '$uibModal', 'partTypes', 'cachedDictionaries', 'critDimsByPartTypes', 'critDimEnumVals',
  function ($scope, $log, $location, $uibModal, partTypes, cachedDictionaries, critDimsByPartTypes, critDimEnumVals) {
    $scope.createPart = function () {
      $uibModal.open({
        'templateUrl': '/views/part/PartCreateModal.html',
        'controller': 'PartCreateModalCtrl',
        'resolve': {
          'partTypes': function() {
            return partTypes;
          }
        }
      });
    };

    $scope.partTypes = partTypes;
    $scope.critDimsByPartTypes = critDimsByPartTypes;
    $scope.critDimEnumVals = critDimEnumVals;

    cachedDictionaries.load(); // pre-load cache

  }
])
.controller('PartCreateModalCtrl', ['$scope', '$uibModalInstance', '$log', '$location', 'partTypes',
  function ($scope, $uibModalInstance, $log, $location, partTypes) {

    $scope.selection = null;
    $scope.partTypes = partTypes;

    $scope.onPartTypeChanged = function() {
      $scope.selection = null;
    };

    $scope.onPartTypeSelected = function($item) {
      if ($item !== undefined) {
        $scope.selection = $item.originalObject;
      }
    };

    $scope.create = function () {
      $uibModalInstance.close('cancel');
      $location.path('/part/createByPartTypeId/' + $scope.selection.id);
    };

    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

  }
]);
