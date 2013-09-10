'use strict';

angular.module('ngMetaCrudApp')
    .controller('PartListCtrl', function ($scope, ngTableParams) {
        $scope.partListTableParams = new ngTableParams({
            count: 20,
            page: 1,
            total: 100,
            counts: []
        });

        $scope.clicked = function (part) {
            console.log("Clicked part: ", JSON.stringify(part));
            $scope.$broadcast("PartList.click", part);
        }
    });
