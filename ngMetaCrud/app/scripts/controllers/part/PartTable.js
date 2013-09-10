'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartTableCtrl', function ($scope, ngTableParams) {
        $scope.partListTableParams = new ngTableParams({
            count: 20,
            page: 1,
            total: 100,
            counts: []
        });

        $scope.clicked = function (part) {
            console.log("Clicked part: ", JSON.stringify(part));
            $scope.$broadcast("PartTable.click", part);
            $scope.go("/part/1");
        }
    });
