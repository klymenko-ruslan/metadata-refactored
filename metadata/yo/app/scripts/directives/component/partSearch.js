'use strict';

angular.module('ngMetaCrudApp')
    .directive('partSearch', function () {
        return {
            scope: {
                actions: '@',
                onAction: '&'
            },
            restrict: 'E',
            templateUrl: '/views/component/PartSearch.html',
            controller: 'PartSearchCtrl'
        };
    });
