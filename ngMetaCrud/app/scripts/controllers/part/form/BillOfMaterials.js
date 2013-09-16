'use strict';

angular.module('ngMetaCrudApp')
    .controller('BillOfMaterialsCtrl', function ($scope, ngTableParams) {
        $scope.bomTableParams = new ngTableParams({
            count: 5,
            page: 1,
            total: 0
        });
    });
