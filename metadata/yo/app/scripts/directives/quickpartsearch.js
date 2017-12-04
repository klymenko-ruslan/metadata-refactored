'use strict';

angular.module('ngMetaCrudApp')
.directive('quickPartSearch', function() {
  return {
    templateUrl: '/views/component/quickpartsearch.html',
    scope: {},
    restrict: 'E',
    controller: ['$scope', '$location', function($scope, $location) {

      $scope.srchPartNumber = null;

      $scope.onSearch = function() {
        $location.search('pn', $scope.srchPartNumber);
        $location.path('/part/list');
        $scope.srchPartNumber = null;
      };

    }]
  };
});
