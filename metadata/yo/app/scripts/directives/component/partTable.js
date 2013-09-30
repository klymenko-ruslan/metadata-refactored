'use strict';

angular.module('ngMetaCrudApp')
    .directive('partTable', function () {
        return {
            scope: true,
            restrict: 'E',
            templateUrl: '/views/component/PartTable.html',
            controller: 'PartTableCtrl'
        };
    });
