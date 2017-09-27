'use strict';

angular.module('ngMetaCrudApp')

.controller('PartListCtrl', ['$scope', '$log', '$uibModal', 'partTypes', 'cachedDictionaries', 'critDimsByPartTypes', 'critDimEnumVals',
  function ($scope, $log, $uibModal, partTypes, cachedDictionaries, critDimsByPartTypes, critDimEnumVals) {
    $scope.createPart = function () {
      $uibModal.open({
        'templateUrl': '/views/part/PartCreateModal.html',
        'controller': 'PartCreateModalCtrl'
      });
    };

    $scope.partTypes = partTypes;
    $scope.critDimsByPartTypes = critDimsByPartTypes;
    $scope.critDimEnumVals = critDimEnumVals;

    cachedDictionaries.load(); // pre-load cache

  }
])
.controller('PartCreateModalCtrl', ['$scope', '$uibModalInstance', '$log', '$location',
  function ($scope, $uibModalInstance, $log, $location) {
    $scope.selection = {};

    $scope.create = function () {
      $uibModalInstance.close('cancel');
      $location.path('/part/createByPartTypeId/' + $scope.selection.partType.id);
    };

    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

  }
]);
