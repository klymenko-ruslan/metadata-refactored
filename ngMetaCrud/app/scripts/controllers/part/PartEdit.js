'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartEditCtrl', function ($scope, partService, $routeParams) {
        $scope.part = partService.findPart($routeParams.partId).get();
  });
