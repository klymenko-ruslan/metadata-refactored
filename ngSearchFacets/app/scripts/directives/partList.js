'use strict';

angular.module('ngSearchFacetsApp')
  .directive('partList', function () {
    return {
      scope: {
          'ngModel': '='
      },
      restrict: 'E',
      templateUrl: '/views/PartList.html',
      controller: 'PartListCtrl',
      link: function(scope, element, attrs) {
      }
    };
  });
