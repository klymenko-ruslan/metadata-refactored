'use strict';

angular.module('ngMetaCrudApp')
  .controller('SalesNoteCreateCtrl', function($scope, $routeParams, $log) {
    $scope.partId = $routeParams.id;
    $log.log('Inside NewNoteCtrl');
    $scope.part = null;
  });
