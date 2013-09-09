'use strict';

angular.module('ngMetaCrudApp')
    .directive('partList', function () {
        return {
            restrict: 'E',
            templateUrl: '/views/PartList.html',
            controller: 'SearchCtrl'
        };
    });
