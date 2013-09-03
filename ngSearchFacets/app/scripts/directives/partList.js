'use strict';

angular.module('ngSearchFacetsApp')
  .directive('partList', function () {
    return {
        restrict: 'E',
        templateUrl: '/views/PartList.html',
        controller: 'SearchCtrl'
    };
  });
