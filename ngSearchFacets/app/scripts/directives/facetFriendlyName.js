'use strict';

angular.module('ngSearchFacetsApp')
  .directive('facetFriendlyName', function (facetNameService) {
    return {
//        scope: {
//            'facetName': '@'
//        },
      link: function postLink(scope, element, attrs) {
        element.text(facetNameService(scope.facetName));
      }
    };
  });
