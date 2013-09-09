'use strict';

angular.module('ngMetaCrudApp')
    .directive('search', function () {
        return {
            restrict: 'E',
            controller: 'SearchCtrl',
            link: function () {
            }
        };
    });
