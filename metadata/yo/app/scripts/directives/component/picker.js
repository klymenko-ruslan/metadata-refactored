'use strict';

angular.module('ngMetaCrudApp')
    .directive('picker', function ($log, Restangular) {
        return {
            scope: {
                "path": '@?',
                "items": '=?',
                "ngModel": '@',
                "required": '@'
            },
            templateUrl: '/views/component/Picker.html',
            restrict: 'E',
            link: function(scope, element, attrs) {
//              scope.ngModelAttr = attrs.ngModel;
            }
        }
    });
