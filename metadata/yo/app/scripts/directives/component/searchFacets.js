'use strict';

angular.module('ngMetaCrudApp')
    .directive('searchFacets', function () {
        return {
            restrict: 'E',
            templateUrl: '/views/component/SearchFacets.html'
        };
    });
