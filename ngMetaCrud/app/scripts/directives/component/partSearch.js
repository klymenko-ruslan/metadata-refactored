'use strict';

angular.module('ngMetaCrudApp')
    .directive('partSearch', function () {
        return {
            restrict: 'E',
            controller: 'PartSearchCtrl',
            templateUrl: '/views/component/PartSearch.html'
        };
    });
