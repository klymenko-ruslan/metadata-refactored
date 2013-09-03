'use strict';

angular.module('ngSearchFacetsApp')
  .directive('searchFacets', function () {
    return {
      restrict: 'E',
      templateUrl: '/views/SearchFacets.html',
      controller: 'SearchCtrl'
    };
  });
