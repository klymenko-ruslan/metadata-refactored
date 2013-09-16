'use strict';

angular.module('ngMetaCrudApp')
    .directive('picker', function (Restangular) {
        return {
            scope: {
                "path": '@',
                "ngModel": '=',
                "required": '@'
            },
            templateUrl: '/views/component/Picker.html',
            restrict: 'E',
            link: function(scope, element, attrs) {
                scope.ngModel = attrs.ngModel;
                scope.items = Restangular.all(attrs.path).getList().then(function (items) {
                    console.log("Loaded " + items.length + " items from " + attrs.path);
                    scope.items = items;
                }, function(response) {
                    console.error("Failed to load items from " + scope.path);
                });
            }
        }
    });
