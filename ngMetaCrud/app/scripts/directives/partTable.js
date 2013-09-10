'use strict';

angular.module('ngMetaCrudApp')
    .directive('partList', function () {
        return {
            restrict: 'E',
            templateUrl: '/views/part/PartTable.html',
            controller: 'PartSearchCtrl'
        };
    });
