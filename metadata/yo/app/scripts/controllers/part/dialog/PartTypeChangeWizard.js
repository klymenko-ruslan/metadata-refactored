'use strict';

angular.module('ngMetaCrudApp')
.controller('partTypeChangeWizardCtrl', ['$scope', '$log', '$uibModalInstance', 'partTypes', 'part', function($scope, $log, $uibModalInstance, partTypes, part) {

  $scope.partTypes = partTypes;
  $scope.part = part;

  var wzStep = 0;       // current step of the wizard
  var wzLastStep = 1;   // nubmer of the last step of the wizard

  $scope.wzData = {
    partType: {
      id: null
    }  // a new part type
  };

  $scope.wzData.partType.id = $scope.part.partType.id;

  $scope.stepIndicator = function() {
    return (wzStep + 1) + '/' + (wzLastStep + 1);
  };

  $scope.isBttnBackDisabled = function() {
    return wzStep === 0;
  };

  $scope.isBttnNextDisabled = function() {
    if (wzStep === 0) {
      // A 'New Part Type' has to be differ from the original part type.
      return $scope.part.partType.id === $scope.wzData.partType.id;
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
