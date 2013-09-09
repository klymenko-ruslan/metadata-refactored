'use strict';

angular.module('ngMetaCrudApp')
    .directive('facetFriendlyName', function (facetNameService) {
        return {
            link: function (scope, element, attrs) {
                element.text(facetNameService(scope.facetName));
            }
        };
    });
