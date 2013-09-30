'use strict';

angular.module('ngMetaCrudApp')
    .directive('partSearch', function () {
        return {
            scope: {
                actions: '@',
                onAction: '&',
                partType: '@'
            },
            restrict: 'E',
            templateUrl: '/views/component/PartSearch.html',
            controller: 'PartSearchCtrl'
        };
    });
