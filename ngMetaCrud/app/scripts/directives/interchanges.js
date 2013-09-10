'use strict';

angular.module('ngMetaCrudApp')
  .directive('interchanges', function () {
        return {
            scope: {
                interchangeId: '@',
                path: '@'
            },
            restrict: 'E',
            transclude: true,
            templateUrl:'/partials/Interchanges.html',
            controller: 'InterchangesCtrl',
            link: function(scope, element, attrs, controller) {
                var interchangeId = attrs.interchangeId;

                if (interchangeId === null || interchangeId.length < 1) {
                    interchangeId = null;
                }

                scope.interchangeId = interchangeId;
                scope.interchangeNewId = interchangeId;
            }
        };
    });
