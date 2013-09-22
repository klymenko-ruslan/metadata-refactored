'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartInterchangeSearchCtrl', function ($scope, $location, $routeParams, restService) {
        $scope.partId = $routeParams.id;
        $scope.partType = $routeParams.type;
  });
