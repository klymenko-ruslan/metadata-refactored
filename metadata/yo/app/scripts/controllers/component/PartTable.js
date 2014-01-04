'use strict';

angular.module('ngMetaCrudApp')
    .controller('ParttableCtrl', function ($scope, ngTableParams) {
        $scope.partListTableParams = new ngTableParams({
            count: 25,
            page: 1,
            total: 0,
            counts: [10, 25, 50, 100]
        });
    });
