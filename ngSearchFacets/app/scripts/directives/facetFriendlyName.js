'use strict';

angular.module('ngSearchFacetsApp')
  .directive('facetFriendlyName', function (facetNameService) {
    return {
      link: function(scope, element, attrs) {
        element.text(facetNameService(scope.facetName));
      }
    };
  });
