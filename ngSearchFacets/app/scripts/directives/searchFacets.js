'use strict';

angular.module('ngSearchFacetsApp')
  .directive('searchFacets', function () {
    return {
      scope: {
          'ngModel': '='
      },
      require: 'ngModel',
      restrict: 'E',
      controller: 'FacetsCtrl',
      templateUrl: '/views/Facets.html'
    };
  });
