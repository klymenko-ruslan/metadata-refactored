'use strict';

angular.module('ngMetaCrudApp')
    .directive('partSearch', function () {
        return {
            scope: true,
            restrict: 'E',
            controller: 'PartSearchCtrl',
            templateUrl: '/views/component/PartSearch.html'
        };
    });
