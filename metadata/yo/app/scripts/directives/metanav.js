'use strict';

angular.module('ngMetaCrudApp')
    .directive('metanav', function () {
        return {
            transclude: true,
            templateUrl: '/views/component/Metanav.html',
            restrict: 'E'
        };
    });
