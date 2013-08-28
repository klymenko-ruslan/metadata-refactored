'use strict';

angular.module('ngSearchFacetsApp')
  .directive('search', function () {
    return {
        restrict: 'E',
        controller: 'SearchCtrl',
        link: function() {
        }
    };
  });
