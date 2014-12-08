'use strict';

angular.module('ngMetaCrudApp')
  .controller('NameDialogCtrl', function (data, $scope, $modalInstance) {
    $scope.data = data;
    $scope.name = data.name;

    $scope.respond = function(name) {
      $modalInstance.close(name);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
