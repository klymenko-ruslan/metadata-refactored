'use strict';

angular.module('ngMetaCrudApp')
    .directive('picker', function ($parse) {
        return {
            scope: {
                "items": '=',
                "ngModel": "="
            },
            templateUrl: '/views/component/Picker.html',
            restrict: 'E'
        }
    });
