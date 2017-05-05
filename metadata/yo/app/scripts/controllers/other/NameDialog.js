'use strict';

angular.module('ngMetaCrudApp')

.controller('NameDialogCtrl', ['data', '$scope', '$uibModalInstance', function (data, $scope, $uibModalInstance) {

  $scope.data = data;
  $scope.name = data.name;

  $scope.respond = function(name) {
    $uibModalInstance.close(name);
  };

  $scope.cancel = function () {
    $uibModalInstance.dismiss('cancel');
  };

}]);
