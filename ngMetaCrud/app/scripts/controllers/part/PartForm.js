'use strict';

angular.module('ngMetaCrudApp')
  .controller('PartFormCtrl', function ($scope, partService, $routeParams) {
        if (angular.isDefined($routeParams.partId)) {
            console.log("Editing part # " + $routeParams.partId);
            $scope.part = partService.findPart($routeParams.partId).get();
        } else {
            console.log("Creating new part");
            $scope.part = {};
        }
  });
