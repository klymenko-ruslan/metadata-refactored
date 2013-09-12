'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartDetailCtrl', function ($scope, $location, $routeParams, partService) {
        $scope.part = partService.findPart($routeParams.partId);

        $scope.part.then(function(part) {
            console.log("Loaded.");
        });

    });
