'use strict';

angular.module('ngMetaCrudApp')
.controller('partTypeChangeWizardCtrl', ['$scope', '$uibModalInstance', 'part', function($scope, $uibModalInstance, part) {
  $scope.part = part;
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
