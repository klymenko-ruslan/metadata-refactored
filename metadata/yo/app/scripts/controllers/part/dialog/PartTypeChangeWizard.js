'use strict';

angular.module('ngMetaCrudApp')
.controller('partTypeChangeWizardCtrl', ['$scope', '$log', '$uibModalInstance', 'restService', 'partTypes', 'part', function($scope, $log, $uibModalInstance, restService, partTypes, part) {

  $scope.part = part;
  $scope.partTypes = _.filter(partTypes, function(pt) {
    return pt.id !== part.partType.id;
  });

  var wzStep = 0;       // current step of the wizard
  var wzLastStep = 3;   // nubmer of the last step of the wizard

  $scope.wzData = {
    partType: {
      id: null
    },  // a new part type
    turbo: {
      tm: null,
      tt: null
    },
    kitType: null,
    options: {
      clearBoms: false,
      clearInterchanges: true,
      copyCritDims: true
    }
  };

  // We use a special object to store a reference on the form(s)
  // in order to avoid the issue with inheritance of scopes
  // (without '.' a form controller is mapped on a parent scope).
  $scope.forms ={
    extraAttrsForm: null
  };

  //$scope.wzData.partType.id = $scope.part.partType.id;
  //$scope.wzData.partType.id = 30; // Actuator

  $scope.turboTypes = null;     // []
  $scope.turboModels = null;    // []

  $scope.filters = {
    turboType: '',
    turboModel: ''
  };


  $scope.kitTypes = null;       // []

  $scope.onChangePartType = function() {
    // Reset some wizard data which is sensetive to change the part type.
    $scope.wzData.turbo.tm = null;
    $scope.wzData.turbo.tt = null;
    $scope.wzData.kitType = null;
    if ($scope.wzData.partType.value === 'turbo' && !$scope.turboTypes) { // singleton
      restService.listTurboTypesForManufacturerId(part.manufacturer.id).then(
        function success(partTypes) {
          $scope.turboTypes = partTypes;
        },
        function failure(errorResponse) {
          restService.error('Loading of turbo types failed.', errorResponse);
        }
      );
    } else if ($scope.wzData.partType.value === 'kit' && !$scope.kitTypes) {
      restService.listKitTypes().then(
        function success(kitTypes) {
          $scope.kitTypes = kitTypes;
        },
        function failure(errorResponse) {
          restService.error('Loading of kit types failed.', errorResponse);
        }
      );
    }
  };

  $scope.onChangeTurboType = function() {
    var ttId = $scope.wzData.turbo.tt.id;
    if (ttId !== undefined) {
      restService.listTurboModelsForTurboTypeId(ttId).then(
        function success(turboModels) {
          /*
          $scope.turboModels.splice(0, $scope.turboModels.length);
          _.each(turboModels, function(tm) {
            $scope.turboModels.push(tm);
          });
          */
          $scope.turboModels = turboModels;
        },
        function failure(response) {
          restService.error('Loading of turbo models for the turbo type [' + ttId + ']' + ' failed.', response);
        }
      );
    }
  };

  $scope.getWzStep = function() {
    return wzStep;
  };

  $scope.stepIndicator = function() {
    return (wzStep + 1) + '/' + (wzLastStep + 1);
  };


  $scope.isRequiredExtraAttributes = function() {
    var pv = $scope.wzData.partType.value;
    return pv === 'turbo' || pv === 'kit';
  };

  $scope.isBttnBackDisabled = function() {
    return wzStep === 0;
  };

  $scope.isBttnNextDisabled = function() {
    if (wzStep === 0) {
      // A 'New Part Type' has to be differ from the original part type.
      return $scope.wzData.partType.id === null || $scope.part.partType.id === $scope.wzData.partType.id;
    } else if (wzStep === 1) {
        return $scope.forms.extraAttrsForm && $scope.forms.extraAttrsForm.$invalid;

    } else if (wzStep === wzLastStep) {
      return true;
    }
    return false;
  };

  $scope.isBttnDoItDisabled = function() {
    return true;
  };

  $scope.isBttnNextVisible = function() {
    return wzStep < wzLastStep;
  };

  $scope.isBttnDoItVisible = function() {
    return wzStep === wzLastStep;
  };

  $scope.onCancel = function() {
    $uibModalInstance.close();
  };

  $scope.onBack = function() {
    wzStep--;
  };

  $scope.onNext = function() {
    wzStep++;
  };

  $scope.onDoIt = function() {
    $scope.onCancel(); // TODO
  };

  /*
    var mergeDialog = dialogs.create('/views/dialog/PartTypeChangeWizard.html', 'mergeInterchangeablesCtrl',
      {
        'mergeChoice': MERGE_OPTIONS.PICKED_ALL_TO_PART
      }, {
        'size': 'lg',
        'keyboard': true,
        'backdrop': false
      }
    );

  mergeDialog.result.then(
    function(bttn) {
    },
    function cancel() {
    }
  );

  */

}]);
